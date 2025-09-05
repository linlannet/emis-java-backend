/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.exception;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;

import com.alibaba.fastjson2.JSON;

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.emis.support.exception.enums.ExceptionStatus;

/**
 * 错误信息属性支撑
 *
 * @author Linlan
 */
public class SupportErrorAttributes extends DefaultErrorAttributes {
    public static final String ERRORS     = "errors";
    public static final String ERROR      = "error";
    public static final String EXCEPTION  = "exception";
    public static final String REQUEST_ID = "requestId";
    public static final String MESSAGE    = "message";
    public static final String PATH       = "path";
    public static final String STATUS     = "status";
    public static final String TRACE      = "trace";
    public static final String SUCCESS    = "success";

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request,
                                                  ErrorAttributeOptions options) {
        Throwable throwable = getError(request);
        Map<String, Object> attributes = super.getErrorAttributes(request, options);
        //业务异常
        if (throwable instanceof SupportException) {
            attributes.put(MESSAGE, throwable.getMessage());
            attributes.put(STATUS, ((SupportException) throwable).getErrorCode());
        }
        //绑定异常
        if (throwable instanceof BindException) {
            BindingResult bindingResult = ((BindException) throwable).getBindingResult();
            StringBuilder buffer = new StringBuilder();
            //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            for (FieldError error : bindingResult.getFieldErrors()) {
                buffer.append(error.getDefaultMessage()).append(",");
            }
            attributes.put(MESSAGE, buffer.substring(0, buffer.length() - 1));
            attributes.put(STATUS, SupportException.BIND_EXCEPTION);
        }
        //约束违反异常
        if (throwable instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) throwable)
                .getConstraintViolations();
            StringBuffer errorMsg = new StringBuffer();
            constraintViolations.forEach(ex -> errorMsg.append(ex.getMessage()).append(","));
            attributes.put(MESSAGE, errorMsg.substring(0, errorMsg.length() - 1));
            attributes.put(STATUS, SupportException.CONSTRAINT_VIOLATION_EXCEPTION);
        }
        //数据访问异常
        if (throwable instanceof DataAccessException) {
            attributes.put(MESSAGE, ExceptionStatus.EX900009.getValue());
            attributes.put(STATUS, ExceptionStatus.EX900009.getKey());
        }
        //是否具有 errors
        if (attributes.containsKey(ERRORS)) {
            attributes.put(ERRORS, JSON.toJSONString((attributes.get(ERRORS))));
        }
        //请求ID
        attributes.put(REQUEST_ID, RequestIdUtils.get());
        attributes.put(SUCCESS, false);
        return attributes;
    }

}
