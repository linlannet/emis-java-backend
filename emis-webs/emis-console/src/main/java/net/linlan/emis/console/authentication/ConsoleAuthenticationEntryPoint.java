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
package net.linlan.emis.console.authentication;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.support.security.web.AbstractAuthenticationEntryPoint;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import net.linlan.utils.http.HttpResponseUtils;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * 认证入口点
 *
 * @author Linlan
 * Created on  2020/9/2 22:11
 */
public class ConsoleAuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Commences an authentication scheme.
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws ServletException,
                                                                IOException {

        logger.info(
            "----------------------------ConsoleAuthenticationEntryPoint------------------------------");
        logger.info("未登录，或登录过期");
        super.commence(request, response, authException);
        ResponseResult<Object> result = new ResponseResult(String.valueOf(UNAUTHORIZED.value()),
            Objects.toString(authException.getMessage(), UNAUTHORIZED.getReasonPhrase()));
        HttpResponseUtils.flushResponseJson(response, UNAUTHORIZED.value(), result);
    }

    public ConsoleAuthenticationEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }

}
