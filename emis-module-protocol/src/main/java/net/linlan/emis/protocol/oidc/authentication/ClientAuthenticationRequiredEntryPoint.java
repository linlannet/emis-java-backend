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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;

import net.linlan.emis.protocol.oidc.constant.OidcProtocolConstants;
import net.linlan.emis.support.security.web.AbstractAuthenticationEntryPoint;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.ACCESS_DENIED;

/**
 * 需要客户端身份验证
 *
 * @author Linlan
 * Created on 2023/12/9 18:18
 */
public class ClientAuthenticationRequiredEntryPoint extends AbstractAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException,
                                                                ServletException {
        super.commence(request, response, authException);
        //@formatter:off
        OAuth2Error error=new OAuth2Error(ACCESS_DENIED,authException.getMessage(), OidcProtocolConstants.OIDC_ERROR_URI);
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        OAuth2Error responseError=new OAuth2Error(error.getErrorCode(),error.getDescription(), OidcProtocolConstants.OIDC_ERROR_URI);
        errorHttpResponseConverter.write(responseError, null, httpResponse);
        //@formatter:on
    }

    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

    public ClientAuthenticationRequiredEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }

}
