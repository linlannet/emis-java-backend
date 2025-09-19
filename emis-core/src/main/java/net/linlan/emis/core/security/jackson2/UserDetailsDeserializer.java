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
package net.linlan.emis.core.security.jackson2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.MissingNode;

import net.linlan.emis.support.constant.EmisConstants;
import net.linlan.emis.support.userdetails.Application;
import net.linlan.emis.support.userdetails.Group;
import net.linlan.emis.support.userdetails.Organization;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.useragent.UserAgent;
import static net.linlan.emis.support.constant.EmisConstants.DEFAULT_ADMIN_USERNAME;
import static net.linlan.frame.comm.constant.AdminType.*;

/**
 * UserDetailsDeserializer
 *
 * @author Linlan
 */
public class UserDetailsDeserializer extends JsonDeserializer<UserDetails> {

    @Override
    public UserDetails deserialize(JsonParser jp,
                                   DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        //ID
        String id = readJsonNode(jsonNode, "id").asText();
        //用户名
        String username = readJsonNode(jsonNode, "username").asText();
        //密码
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        String password = passwordNode.asText("");
        //登录时间
        String loginTime = readJsonNode(jsonNode, "loginTime").asText(null);
        // 状态相关
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
        boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
        boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
        boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();
        String authType = readJsonNode(jsonNode, "authType").asText("username_password");
        //用户类型
        String userType = readJsonNode(jsonNode, "userType").asText("user");
        if (StringUtils.isBlank(userType)) {
            //根据用户名规则判断
            if (username.contains(DEFAULT_ADMIN_USERNAME)) {
                userType = ADMIN.getType();
            } else {
                userType = USER.getType();
            }
        }
        //权限
        //        Set<SimpleGrantedAuthority> authorities = jsonNode.has("authorities")
        //            ? mapper.readValue(mapper.treeAsTokens(jsonNode.get("authorities")),
        //                new TypeReference<Set<SimpleGrantedAuthority>>() {
        //                })
        //            : Collections.emptySet();
        // 20250817 修改 authorities 的反序列化方式
        Set<SimpleGrantedAuthority> authorities = Collections.emptySet();
        if (jsonNode.has("authorities") && Objects.nonNull(jsonNode.get("authorities"))) {
            // 创建配置了自定义反序列化器的 ObjectMapper
            ObjectMapper authorityMapper = new ObjectMapper();
            SimpleModule authorityModule = new SimpleModule();
            authorityModule.addDeserializer(SimpleGrantedAuthority.class,
                new SimpleGrantedAuthorityDeserializer());
            authorityMapper.registerModule(authorityModule);

            authorities = parseAuthorities(authorityMapper, jsonNode);
        }
        //机构，显式转换（避免自动类型推断）
        List<Organization> organizations = jsonNode.has("organizations")
            ? mapper.readValue(mapper.treeAsTokens(jsonNode.get("organizations")),
                new TypeReference<List<Organization>>() {
                })
            : Collections.emptyList();
        //用户组
        List<Group> groups = jsonNode.has("groups") ? mapper.readValue(
            mapper.treeAsTokens(jsonNode.get("groups")), new TypeReference<List<Group>>() {
            }) : Collections.emptyList();
        //用户关联应用
        List<Application> applications = jsonNode.has("applications")
            ? mapper.readValue(mapper.treeAsTokens(jsonNode.get("applications")),
                new TypeReference<List<Application>>() {
                })
            : Collections.emptyList();

        // 封装值
        UserDetails result = new UserDetails(id, username, password, userType, enabled,
            accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, organizations,
            groups, applications);
        //认证类型
        result.setAuthType(authType);
        //活动地点
        GeoLocation geoLocation = jsonNode.has("geoLocation")
            ? mapper.readValue(mapper.treeAsTokens(jsonNode.get("geoLocation")), GeoLocation.class)
            : new GeoLocation();
        //userAgent
        UserAgent userAgent = jsonNode.has("userAgent")
            ? mapper.readValue(mapper.treeAsTokens(jsonNode.get("userAgent")), UserAgent.class)
            : new UserAgent();
        //IP地址、设备相关
        result.setGeoLocation(geoLocation);
        result.setUserAgent(userAgent);
        //登录时间
        result.setLoginTime(StringUtils.isNoneBlank(loginTime)
            ? LocalDateTime.parse(loginTime, EmisConstants.DEFAULT_DATE_TIME_FORMATTER)
            : null);
        if (passwordNode.asText(null) == null) {
            result.eraseCredentials();
        }
        return result;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

    private Set<SimpleGrantedAuthority> parseAuthorities(ObjectMapper mapper,
                                                         JsonNode jsonNode) throws IOException {

        if (!jsonNode.has("authorities")) {
            return Collections.emptySet();
        }

        JsonNode authoritiesNode = jsonNode.get("authorities");
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        if (authoritiesNode.isArray()) {
            for (JsonNode authNode : authoritiesNode) {
                if (authNode.isObject()) {
                    // 处理对象格式 {"authority": "admin"}
                    JsonNode authorityField = authNode.get("authority");
                    if (authorityField != null && authorityField.isTextual()) {
                        authorities.add(new SimpleGrantedAuthority(authorityField.asText()));
                    }
                } else if (authNode.isTextual()) {
                    // 处理简单字符串格式 "admin"
                    authorities.add(new SimpleGrantedAuthority(authNode.asText()));
                }
            }
        }

        return authorities;
    }

}
