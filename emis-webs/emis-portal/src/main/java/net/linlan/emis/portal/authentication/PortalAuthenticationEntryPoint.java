/*
 * emis-portal - portal门户平台
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
package net.linlan.emis.portal.authentication;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.utils.http.HttpResponseUtils;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import static net.linlan.utils.http.HttpContextUtils.isHtmlRequest;

/**
 * 认证入口点
 *
 * @author Linlan
 * Created on  2020/9/2 22:11
 */
@SuppressWarnings("DuplicatedCode")
public class PortalAuthenticationEntryPoint // extends AbstractAuthenticationEntryPoint {
                                           implements AuthenticationEntryPoint {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Commences an authentication scheme.
     * <p>
     * <code>ExceptionTranslationFilter</code> will populate the <code>HttpSession</code>
     * attribute named
     * <code>AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY</code>
     * with the requested target URL before calling this method.
     * <p>
     * Implementations should modify the headers on the <code>ServletResponse</code> as
     * necessary to commence the authentication process.
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException,
                                                                ServletException {
        //        super.commence(request, response, authException);
        //判断请求
        boolean isTextHtml = isHtmlRequest(request);
        //JSON
        if (!isTextHtml) {
            ResponseResult<Object> result = new ResponseResult(String.valueOf(UNAUTHORIZED.value()),
                Objects.toString(authException.getMessage(), UNAUTHORIZED.getReasonPhrase()));
            HttpResponseUtils.flushResponseJson(response, UNAUTHORIZED.value(), result);
        }
        // HTML
        else {
            //跳转前端SESSION过期路由
            response.sendRedirect(PropertiesContextHelper.getPortalLoginUrl());
        }
    }

    //    public PortalAuthenticationEntryPoint(UserAgentParser userAgentParser) {
    //        super(userAgentParser);
    //    }

}
