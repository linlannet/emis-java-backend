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
package net.linlan.emis.protocol.jwt.endpoint.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AuthenticationConverter;

import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.jwt.authentication.JwtLogoutAuthenticationToken;
import net.linlan.emis.protocol.jwt.constant.JwtProtocolConstants;
import net.linlan.emis.protocol.jwt.endpoint.JwtAuthenticationEndpointUtils;
import net.linlan.emis.protocol.jwt.exception.JwtError;
import net.linlan.emis.protocol.jwt.model.JwtProtocolConfig;

/**
 *
 * @author Linlan
 * Created on 2023/9/4 16:01
 */
public final class JwtLogoutAuthenticationConverter implements AuthenticationConverter {
    private static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken(
        "anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    @Override
    public Authentication convert(HttpServletRequest request) {

        if (request.getParameterValues(JwtProtocolConstants.POST_LOGOUT_REDIRECT_URI).length != 1) {
            JwtAuthenticationEndpointUtils.throwError(new JwtError(OAuth2ErrorCodes.INVALID_REQUEST,
                "JWT Logout Request Parameter: " + JwtProtocolConstants.POST_LOGOUT_REDIRECT_URI));
        }
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        JwtProtocolConfig config = (JwtProtocolConfig) context.getConfig()
            .get(JwtProtocolConfig.class.getName());
        String postLogoutRedirectUri = request
            .getParameter(JwtProtocolConstants.POST_LOGOUT_REDIRECT_URI);
        String sessionId = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionId = session.getId();
        }

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) {
            principal = ANONYMOUS_AUTHENTICATION;
        }
        return new JwtLogoutAuthenticationToken(principal, config, postLogoutRedirectUri,
            sessionId);
    }

}
