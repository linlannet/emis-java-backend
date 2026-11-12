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
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.support.manage.enums.SecretType;
import net.linlan.emis.support.security.exception.SecretInvalidException;
import net.linlan.utils.SpringContextUtils;
import net.linlan.utils.http.HttpResponseUtils;
import static javax.servlet.RequestDispatcher.*;

import static org.springframework.boot.web.servlet.support.ErrorPageFilter.ERROR_REQUEST_URI;

import static net.linlan.emis.support.constant.EmisConstants.CAPTCHA_CODE_SESSION;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.EX000101;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.EX000105;
import static net.linlan.utils.http.HttpContextUtils.isHtmlRequest;

/**
 * 认证失败
 * <p>
 * 返回JSON前端处理
 *
 * @author Linlan
 * Created on  2020/9/2 22:11
 */
public class PortalAuthenticationFailureHandler implements
                                                org.springframework.security.web.authentication.AuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(PortalAuthenticationFailureHandler.class);

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws ServletException,
                                                                           IOException {
        boolean isHtmlRequest = isHtmlRequest(request);
        if (!isHtmlRequest) {
            //@formatter:off
            ResponseResult<String> builder = new ResponseResult(EX000101.getKey(),(Objects.toString(exception.getMessage(),EX000101.getValue())));
            if (exception instanceof SecretInvalidException){
                builder = new ResponseResult(EX000105.getKey(), Objects.toString(exception.getMessage(),EX000105.getValue()));
            }
            ResponseResult<String> result = builder.build();
            request.getSession().removeAttribute(SecretType.LOGIN.getPath());
            request.getSession().removeAttribute(CAPTCHA_CODE_SESSION);
            request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
            HttpResponseUtils.flushResponseJson(response, HttpStatus.BAD_REQUEST.value(), result);
            //@formatter:on
            return;
        }
        forwardToErrorPage(getErrorPath(), request, response, exception);
    }

    private void forwardToErrorPage(String path, HttpServletRequest request,
                                    HttpServletResponse response,
                                    Throwable ex) throws ServletException, IOException {
        if (logger.isErrorEnabled()) {
            String message = "Forwarding to error page from request " + getDescription(request)
                             + " due to exception [" + ex.getMessage() + "]";
            logger.error(message, ex);
        }

        request.setAttribute(ERROR_STATUS_CODE, HttpStatus.BAD_REQUEST.value());
        request.setAttribute(ERROR_MESSAGE, ex.getMessage());
        request.setAttribute(ERROR_REQUEST_URI, request.getRequestURI());
        request.setAttribute(ERROR_EXCEPTION, ex);
        request.setAttribute(ERROR_EXCEPTION_TYPE, ex.getClass());
        response.reset();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        request.getRequestDispatcher(path).forward(request, response);
        request.removeAttribute(ERROR_EXCEPTION);
        request.removeAttribute(ERROR_EXCEPTION_TYPE);
    }

    protected String getDescription(HttpServletRequest request) {
        String pathInfo = (request.getPathInfo() != null) ? request.getPathInfo() : "";
        return "[" + request.getServletPath() + pathInfo + "]";
    }

    private String getErrorPath() {
        return SpringContextUtils.getBean(ServerProperties.class).getError().getPath();
    }

}
