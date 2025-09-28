/*
 * emis-core - core核心模块
 * Copyright © 2020-2025 Linlan (open@linlan.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.linlan.emis.core.security.session;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.MapSession;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.util.Assert;

import lombok.Getter;

import net.linlan.emis.support.constant.EmisConstants;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetails;
import net.linlan.emis.support.userdetails.UserDetails;

/**
 * ClusterSessionRegistryImpl
 *
 * @author Linlan
 */
@SuppressWarnings("unchecked")
public class ClusterSessionRegistryImpl<T extends org.springframework.session.Session>
                                       extends SpringSessionBackedSessionRegistry<T> {

    /**
     * remove session
     *
     * @param sessionId {@link String}
     */
    @Override
    public void removeSessionInformation(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    /**
     * get all principals
     *
     * @return    结果列表
     */
    @Override
    public List<Object> getAllPrincipals() {
        if (sessionRepository instanceof RedisIndexedSessionRepository) {
            //names
            Set<String> names = getAllPrincipalNames();
            //session infos
            List<String> sessionIds = new ArrayList<>();
            //根据用户名查询session，包括过期
            for (String s : names) {
                sessionIds.addAll(getSessionIds(s));
            }
            // 获取session详情，不包含已过期的
            List<LocalSession> localSessions = getSessionList(sessionIds);
            return new ArrayList<>(localSessions);
        }
        return super.getAllPrincipals();
    }

    /**
     * 根据sessionId 获取session详情
     *
     * @param sessionIds {@link List}
     * @return    结果列表
     */
    @NonNull
    public List<LocalSession> getSessionList(List<String> sessionIds) {
        List<LocalSession> list = new ArrayList<>();
        Map<String, MapSession> sessions;
        sessions = getSessions(sessionIds);
        for (MapSession session : sessions.values()) {
            // session 为空，或者 session过期，跳过
            if (Objects.isNull(session) || session.isExpired()) {
                continue;
            }
            try {
                //转换为security context
                SecurityContext securityContext = session.getAttribute(SPRING_SECURITY_CONTEXT);
                //转为实体
                Authentication authentication = securityContext.getAuthentication();
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principal;
                    LocalSession localSession = getSession(session, userDetails, authentication);
                    list.add(localSession);
                }
            } catch (NullPointerException ignored) {
            }
        }
        //处理
        return list;
    }

    private static LocalSession getSession(MapSession session, UserDetails userDetails,
                                           Authentication authentication) {
        //        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication
            .getDetails();
        LocalSession localSession = new LocalSession(userDetails.getUserId(),
            userDetails.getUsername());
        //last request
        Instant instant = session.getLastAccessedTime();
        ZoneId zoneId = ZoneId.systemDefault();
        //最后请求时间
        LocalDateTime lastRequestTime = instant.atZone(zoneId).toLocalDateTime();
        localSession.setLastRequestTime(lastRequestTime);
        localSession.setAuthenticationTime(details.getAuthenticationTime());
        //        localSession.setAuthenticationTime(LocalDateTime.now());
        //登录时间
        localSession.setAuthenticationProvider(details.getAuthenticationProvider().getType());
        //        localSession.setAuthenticationProvider(CustomAuthenticationProvider.USERNAME_PASSWORD.getType());
        //用户类型
        localSession.setUserType(userDetails.getUserType());
        //地理位置
        localSession.setGeoLocation(details.getGeoLocation());
        //用户代理
        localSession.setUserAgent(details.getUserAgent());
        //会话ID
        localSession.setSessionId(session.getId());
        return localSession;
    }

    /**
     * 根据用户名获取session列表
     *
     * @param username 用户名
     * @return    结果列表
     */
    public List<Object> getSessionList(String username) {
        Assert.notNull(username, "用户名不能为空");
        //names
        Boolean hasKey = sessionRedisOperations().hasKey(getPrincipalKey(username));
        if (Boolean.FALSE.equals(hasKey)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(
            getSessionList(getSessionIds(username).stream().collect(Collectors.toList())));
    }

    private Map<String, MapSession> getSessions(List<String> sessionIds) {
        Map<String, MapSession> result = new HashMap<>(16);
        List<Object> pipelineResult = sessionRedisOperations()
            .executePipelined((RedisCallback<Object>) connection -> {
                connection.openPipeline();
                for (Object sessionId : sessionIds) {
                    String key = getSessionKeyPrefix() + sessionId;
                    connection.hashCommands().hGetAll(key.getBytes());
                }
                return null;
            });
        for (int i = 0; i < pipelineResult.size(); i++) {
            Object obj = pipelineResult.get(i);
            String id = sessionIds.get(i);
            if (obj instanceof Map && ((Map<Object, Object>) obj).size() > 0) {
                MapSession session = loadSession(id, (Map<Object, Object>) obj);
                result.put(session.getId(), session);
            }
        }
        return result;
    }

    private MapSession loadSession(String id, Map<Object, Object> objects) {
        MapSession loaded = new MapSession(id);
        for (Map.Entry<Object, Object> entry : objects.entrySet()) {
            String key = (String) entry.getKey();
            if (CREATION_TIME_KEY.equals(key)) {
                loaded.setCreationTime(Instant.ofEpochMilli((long) entry.getValue()));
            } else if (MAX_INACTIVE_INTERVAL_KEY.equals(key)) {
                loaded.setMaxInactiveInterval(Duration.ofSeconds((int) entry.getValue()));
            } else if (LAST_ACCESSED_TIME_KEY.equals(key)) {
                loaded.setLastAccessedTime(Instant.ofEpochMilli((long) entry.getValue()));
            } else if (key.startsWith(ATTRIBUTE_PREFIX)) {
                loaded.setAttribute(key.substring(ATTRIBUTE_PREFIX.length()), entry.getValue());
            }
        }
        return loaded;
    }

    /**
     * 获取主体 key
     *
     * @param principalName {@link String}
     * @return {@link String}
     */
    String getPrincipalKey(String principalName) {
        return getPrincipalKeyPrefix() + principalName;
    }

    /**
     * 获取主体 key
     *
     * @return {@link String}
     */
    String getPrincipalKeyPrefix() {
        return getNamespace() + EmisConstants.COLON + "index" + EmisConstants.COLON
               + FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME + EmisConstants.COLON;
    }

    /**
     * 获取session key
     *
     * @return {@link String}
     */
    String getSessionKeyPrefix() {
        return getNamespace() + EmisConstants.COLON + "sessions:";
    }

    /**
     * 获取所有主体名称
     *
     * @return {@link  Set}
     */
    Set<String> getAllPrincipalNames() {
        Set<String> keys = new HashSet<>();
        ((RedisIndexedSessionRepository) sessionRepository).getSessionRedisOperations()
            .execute((RedisCallback<Object>) connection -> {
                Cursor<byte[]> cursor = connection.keyCommands().scan(
                    ScanOptions.scanOptions().match(getPrincipalKey("*")).count(1000).build());
                while (cursor.hasNext()) {
                    String key = new String(cursor.next()).replaceAll(getPrincipalKeyPrefix(), "");
                    keys.add(key);
                }
                return keys;
            });
        return keys;
    }

    /**
     * 获取Session Ids
     *
     * @return {@link  Set}
     */
    Set<String> getSessionIds(String indexValue) {
        String principalKey = getPrincipalKey(indexValue);
        Set<Object> members = sessionRedisOperations().boundSetOps(principalKey).members();
        if (CollectionUtils.isNotEmpty(members)) {
            return members.stream().map(Object::toString).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    private RedisOperations<Object, Object> sessionRedisOperations() {
        return ((RedisIndexedSessionRepository) sessionRepository).getSessionRedisOperations();
    }

    /**
     * The key in the hash representing {@link org.springframework.session.Session#getCreationTime()}.
     */
    static final String                               CREATION_TIME_KEY         = "creationTime";

    /**
     * The key in the hash representing {@link org.springframework.session.Session#getLastAccessedTime()}.
     */
    static final String                               LAST_ACCESSED_TIME_KEY    = "lastAccessedTime";

    /**
     * The key in the hash representing {@link org.springframework.session.Session#getMaxInactiveInterval()}.
     */
    static final String                               MAX_INACTIVE_INTERVAL_KEY = "maxInactiveInterval";

    /**
     * The prefix of the key in the hash used for session attributes. For example, if the
     * session contained an attribute named {@code attributeName}, then there would be an
     * entry in the hash named {@code sessionAttr:attributeName} that mapped to its value.
     */
    static final String                               ATTRIBUTE_PREFIX          = "sessionAttr:";

    private static final String                       SPRING_SECURITY_CONTEXT   = "SPRING_SECURITY_CONTEXT";

    /**
     * 获取命名空间
     */
    @Getter
    private final String                              namespace;

    /**
     * FindByIndexNameSessionRepository
     */
    private final FindByIndexNameSessionRepository<?> sessionRepository;

    public ClusterSessionRegistryImpl(FindByIndexNameSessionRepository<T> sessionRepository,
                                      String namespace) {
        super(sessionRepository);
        this.sessionRepository = sessionRepository;
        this.namespace = namespace;
    }
}
