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

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;

import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.jwt.authentication.JwtLoginAuthenticationToken;
import net.linlan.emis.protocol.jwt.constant.JwtProtocolConstants;
import net.linlan.emis.protocol.jwt.exception.JwtAuthenticationException;
import net.linlan.emis.protocol.jwt.exception.JwtError;
import net.linlan.emis.protocol.jwt.exception.JwtErrorCodes;
import net.linlan.emis.protocol.jwt.model.JwtProtocolConfig;
import net.linlan.utils.http.HttpRequestUtils;
import static org.springframework.http.HttpMethod.GET;

/**
 * @author Linlan
 * Created on 2023/7/8 00:14
 */
public final class JwtRequestAuthenticationTokenConverter implements AuthenticationConverter {
    private static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken(
        "anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    @Override
    public Authentication convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = GET.name().equals(request.getMethod())
            ? HttpRequestUtils.getQueryParameters(request)
            : HttpRequestUtils.getFormParameters(request);
        String targetUrl = parameters.getFirst(JwtProtocolConstants.TARGET_URL);

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) {
            principal = ANONYMOUS_AUTHENTICATION;
        }
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        if (MapUtils.isEmpty(applicationContext.getConfig())
            | !applicationContext.getConfig().containsKey(JwtProtocolConfig.class.getName())) {
            JwtError error = new JwtError(JwtErrorCodes.SERVER_ERROR, null,
                JwtProtocolConstants.JWT_ERROR_URI);
            throw new JwtAuthenticationException(error);
        }
        JwtProtocolConfig config = (JwtProtocolConfig) applicationContext.getConfig()
            .get(JwtProtocolConfig.class.getName());
        return new JwtLoginAuthenticationToken(principal, targetUrl, config);
    }

}
