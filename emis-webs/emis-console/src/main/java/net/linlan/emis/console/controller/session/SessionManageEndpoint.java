/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.controller.session;

// import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.experimental.Accessors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.core.security.session.ClusterSessionRegistryImpl;
import net.linlan.emis.core.security.session.LocalSession;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.useragent.UserAgent;
import net.linlan.utils.SpringContextUtils;
import net.linlan.utils.http.HttpResponseUtils;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.USERNAME;

import static net.linlan.emis.core.constant.SessionConstants.SESSION_GROUP_NAME;
import static net.linlan.emis.core.constant.SessionConstants.SESSION_PATH;
import static net.linlan.emis.support.constant.EmisConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 会话管理
 *
 * @author Linlan
 * Created on  2021/9/8 21:39
 */
@Tag(name = SESSION_GROUP_NAME + "-" + "会话管理")
@RestController
@RequestMapping(value = SESSION_PATH)
public class SessionManageEndpoint {
    /**
     * list
     *
     * @param req  {@link HttpServletRequest}
     * @return {@link ResponseResult}
     */
    @Operation(summary = "在线会话")
    @GetMapping("/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<OnlineSession>> list(HttpServletRequest req) {
        List<OnlineSession> result = new ArrayList<>();
        List<Object> principals;
        SessionRegistry registry = SpringContextUtils.getBean(SessionRegistry.class);
        if (registry instanceof ClusterSessionRegistryImpl) {
            //根据用户查询
            if (StringUtils.isNoneBlank(req.getParameter(USERNAME))) {
                principals = ((ClusterSessionRegistryImpl<?>) (registry))
                    .getSessionList(req.getParameter(USERNAME));
            } else {
                principals = registry.getAllPrincipals();
            }
            //封装数据
            principals.forEach(principal -> {
                if (principal instanceof LocalSession) {
                    //过滤掉当前用户的会话
                    if (!((LocalSession) principal).getSessionId()
                        .equals(req.getSession(false).getId())) {
                        //@formatter:off
                        OnlineSession user = OnlineUserConverter.INSTANCE.sessionDetailsToOnlineSession(((LocalSession) principal));
                        String userType = req.getParameter("userType");
                        if (StringUtils.isEmpty(userType) || user.getUserType().equals(userType)) {
                            result.add(user);
                        }
                        //@formatter:on
                    }
                }
            });

        }
        return ResponseResult.ok(result);
    }

    /**
     * remove
     *
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Operation(summary = "下线会话")
    @Audit(type = EventType.DOWN_LINE_SESSION)
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    @DeleteMapping("/remove")
    public ResponseResult<Boolean> remove(HttpServletRequest req, HttpServletResponse resp) {
        String sessionIds = req.getParameter("sessionIds");
        //session id blank
        if (!StringUtils.isNoneBlank(sessionIds)) {
            HttpResponseUtils.flushResponse(resp,
                JSON.toJSONString(ResponseResult.error("会话ID不存在!")));
        }
        SessionRegistry registry = SpringContextUtils.getBean(SessionRegistry.class);
        String[] ids = sessionIds.split(",");
        Arrays.stream(ids).forEach((i) -> {
            //如果sessionId等于当前操作用户sessionId不操作
            if (!req.getSession(false).getId().equals(i)) {
                AuditContext.setTarget(
                    Target.builder().id(i).name("下线会话").type(TargetType.SESSION).build());
                registry.removeSessionInformation(i);
            }
        });
        //返回
        return ResponseResult.ok();
    }

    /**
     * 在线用户
     *
     * @author Linlan
     * Created on  2021/9/8 21:42
     */
    @Data
    @Accessors(chain = true)
    public static class OnlineSession implements Serializable {

        //@Serial
        private static final long serialVersionUID = 8227098865368453321L;
        /**
         * 用户ID
         */
        private String            id;
        /**
         * 用户名
         */
        private String            username;

        /**
         * 活动地点
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
        private GeoLocation       geoLocation;

        /**
         * 用户代理
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
        private UserAgent         userAgent;

        /**
         * 认证类型
         */
        private String            authType;

        /**
         * 用户类型
         */
        private String            userType;

        /**
         * 登录时间
         */
        @JSONField(format = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
        private LocalDateTime     loginTime;

        /**
         * 最后请求时间
         */
        @JSONField(format = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
        private LocalDateTime     lastRequest;

        /**
         * session ID
         */
        private String            sessionId;

    }

    @Mapper
    interface OnlineUserConverter {
        OnlineUserConverter INSTANCE = Mappers.getMapper(OnlineUserConverter.class);

        /**
         * 系统用户转在线会话
         *
         * @param session {@link LocalSession}
         * @return {@link OnlineSession}
         */
        default OnlineSession sessionDetailsToOnlineSession(LocalSession session) {
            if (session == null) {
                return null;
            }

            OnlineSession onlineSession = new OnlineSession();
            //ID
            onlineSession.setId(session.getUserId());
            //用户名
            onlineSession.setUsername(session.getUsername());
            //session id
            onlineSession.setSessionId(session.getSessionId());
            //地理位置
            onlineSession.setGeoLocation(session.getGeoLocation());
            //用户代理
            onlineSession.setUserAgent(session.getUserAgent());
            //认证类型
            onlineSession.setAuthType(session.getAuthenticationProvider());
            //用户类型
            onlineSession.setUserType(session.getUserType());
            //登录时间
            onlineSession.setLoginTime(session.getAuthenticationTime());
            //最后请求时间
            onlineSession.setLastRequest(session.getLastRequestTime());
            return onlineSession;
        }
    }
}
