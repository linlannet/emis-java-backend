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
package net.linlan.emis.openapi.authorization;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.emis.openapi.constant.OpenApiStatus;
import net.linlan.emis.support.security.web.AbstractAuthenticationEntryPoint;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import net.linlan.utils.http.HttpResponseUtils;

/**
 *
 * @author Linlan
 * Created on 2023/6/25 21:55
 */
public final class AccessTokenAuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {

    /**
     * Collect error details from the provided parameters and format according to RFC
     * 6750, specifically {@code error}, {@code error_description}, {@code error_uri}, and
     * {@code scope}.
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(javax.servlet.http.HttpServletRequest request,
                         javax.servlet.http.HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws ServletException,
                                                                IOException {
        super.commence(request, httpServletResponse, authException);
        Response response = new Response();
        response.setCode(OpenApiStatus.INVALID_ACCESS_TOKEN.getKey());
        response.setMsg(OpenApiStatus.INVALID_ACCESS_TOKEN.getValue());
        HttpResponseUtils.flushResponseJson(httpServletResponse, HttpStatus.OK.value(),
            objectMapper, response);
        //        OpenApiResponse<Object> result = new OpenApiResponse(
        //            OpenApiStatus.INVALID_ACCESS_TOKEN.getCode(),
        //            OpenApiStatus.INVALID_ACCESS_TOKEN.getDesc());
        //        HttpResponseUtils.flushResponseJson(httpServletResponse, HttpStatus.OK.value(), result);
    }

    public AccessTokenAuthenticationEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Data
    public static class Response {

        /**
         * code
         */
        @JsonProperty(value = "code")
        @Schema(name = "code")
        private String code;

        /**
         * msg
         */
        @JsonProperty(value = "msg")
        @Schema(name = "msg")
        private String msg;
    }

}
