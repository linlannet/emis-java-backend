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
package net.linlan.emis.protocol.jwt.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import net.linlan.emis.protocol.jwt.endpoint.http.converter.JwtErrorHttpMessageConverter;
import net.linlan.emis.protocol.jwt.exception.JwtAuthenticationException;
import net.linlan.emis.protocol.jwt.exception.JwtError;
import net.linlan.emis.protocol.jwt.exception.JwtErrorCodes;
import net.linlan.utils.http.HttpContextUtils;

/**
 *
 * @author Linlan
 * Created on 2023/9/4 13:03
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException,
                                                                           ServletException {
        if (exception instanceof JwtAuthenticationException) {
            ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
            JwtError error = ((JwtAuthenticationException) exception).getError();
            boolean isHtmlRequest = HttpContextUtils.isHtmlRequest(request);
            //JSON
            if (!isHtmlRequest) {
                if (error.getErrorCode().equals(JwtErrorCodes.SERVER_ERROR)) {
                    httpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (error.getErrorCode().equals(JwtErrorCodes.INVALID_REQUEST)) {
                    httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
                }
                errorHttpResponseConverter.write(error, null, httpResponse);
                return;
            }
            //Html
            response.sendError(HttpStatus.BAD_REQUEST.value(), error.toString());
        }
    }

    /**
     * 错误响应处理器
     */
    private final HttpMessageConverter<JwtError> errorHttpResponseConverter = new JwtErrorHttpMessageConverter();

}
