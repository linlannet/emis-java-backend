/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.oidc.authentication;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.util.Assert;

/**
 * @author Linlan
 * Created on 2023/6/29 21:56
 */
public class AccessTokenAuthenticationManagerResolver implements
                                                      AuthenticationManagerResolver<HttpServletRequest> {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Resolve an {@link AuthenticationManager} from a provided context
     *
     * @param request {@link HttpServletRequest}
     * @return the {@link AuthenticationManager} to use
     */
    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        AuthorizationServerContext context = AuthorizationServerContextHolder.getContext();
        AuthorizationServerSettings settings = context.getAuthorizationServerSettings();
        String tokenFormat = settings
            .getSetting(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT);
        //根据令牌格式调用不同的解析实现
        if (OAuth2TokenFormat.SELF_CONTAINED.getValue().equals(tokenFormat)) {
            return jwtAuthenticationProvider::authenticate;
        }
        if (OAuth2TokenFormat.REFERENCE.getValue().equals(tokenFormat)) {
            return opaqueTokenAuthenticationProvider::authenticate;
        }
        return null;
    }

    /**
     * JWT 身份验证提供
     */
    private final JwtAuthenticationProvider         jwtAuthenticationProvider;

    /**
     * 不透明令牌身份验证提供
     */
    private final OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider;

    public AccessTokenAuthenticationManagerResolver(JwtAuthenticationProvider jwtAuthenticationProvider,
                                                    OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider) {
        Assert.notNull(jwtAuthenticationProvider, "jwtAuthenticationProvider cannot be null");
        Assert.notNull(opaqueTokenAuthenticationProvider,
            "opaqueTokenAuthenticationProvider cannot be null");
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.opaqueTokenAuthenticationProvider = opaqueTokenAuthenticationProvider;
    }

}
