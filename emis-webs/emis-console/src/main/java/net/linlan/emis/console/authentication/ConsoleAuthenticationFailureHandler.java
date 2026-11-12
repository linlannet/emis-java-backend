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

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.support.manage.enums.SecretType;
import net.linlan.emis.support.security.exception.SecretInvalidException;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.constant.EmisConstants.CAPTCHA_CODE_SESSION;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.*;

/**
 * 认证失败
 *
 * @author Linlan
 * Created on  2020/9/2 22:11
 */
public class ConsoleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {
        logger.info(
            "----------------------------ConsoleAuthenticationFailureHandler------------------------------");
        //@formatter:off
        ResponseResult<String> builder = new ResponseResult(EX000101.getKey(), Objects.toString(exception.getMessage(),EX000101.getValue()));
        if (exception instanceof SecretInvalidException){
            builder = ResponseResult.error(EX900005.getKey());
        }
        //@formatter:on
        request.getSession().removeAttribute(SecretType.LOGIN.getPath());
        request.getSession().removeAttribute(CAPTCHA_CODE_SESSION);
        HttpResponseUtils.flushResponseJson(response, HttpStatus.BAD_REQUEST.value(),
            builder.build());
    }
}
