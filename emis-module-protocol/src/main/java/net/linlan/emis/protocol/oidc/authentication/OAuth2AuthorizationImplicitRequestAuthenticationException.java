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

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * OAuth2 简化模式授权请求身份验证异常
 *
 * @author Linlan
 * Created on 2023/6/26 22:54
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationImplicitRequestAuthenticationException extends
                                                                       OAuth2AuthenticationException {
    private final OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken;

    public OAuth2AuthorizationImplicitRequestAuthenticationException(OAuth2Error error,
                                                                     @Nullable OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken) {
        super(error);
        this.authorizationImplicitRequestAuthenticationToken = authorizationImplicitRequestAuthenticationToken;
    }

    public OAuth2AuthorizationImplicitRequestAuthenticationException(OAuth2Error error,
                                                                     Throwable cause,
                                                                     @Nullable OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken) {
        super(error, cause);
        this.authorizationImplicitRequestAuthenticationToken = authorizationImplicitRequestAuthenticationToken;
    }

    @Nullable
    public OAuth2AuthorizationImplicitRequestAuthenticationToken getAuthorizationImplicitRequestAuthenticationToken() {
        return this.authorizationImplicitRequestAuthenticationToken;
    }

}
