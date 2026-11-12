/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.exception.handler;

import javax.validation.ConstraintViolationException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.openapi.constant.OpenApiStatus;
import net.linlan.emis.openapi.exception.OpenApiException;

/**
 * 全局异常处理
 *
 * @author Linlan
 * Created on 2020/8/20 21:55
 */
@AllArgsConstructor
@RestControllerAdvice
public class OpenApiGlobalExceptionHandler {
    /**
     * Exception
     *
     * @return {@link ResponseResult}
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult exception(Exception e) {
        return new ResponseResult(OpenApiStatus.INTERNAL_SERVER_ERROR.getKey(), e.getMessage());
    }

    /**
     * ConstraintViolationException
     *
     * @return {@link ModelAndView}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseResult constraintViolationException(ConstraintViolationException e) {
        return new ResponseResult(OpenApiStatus.INVALID_PARAMETER.getKey(), e.getMessage());
    }

    /**
     * OpenApiException
     *
     * @return {@link ResponseResult}
     */
    @ExceptionHandler(value = OpenApiException.class)
    public ResponseResult exception(OpenApiException e) {
        return new ResponseResult(e.getCode(), e.getMessage());
    }
}
