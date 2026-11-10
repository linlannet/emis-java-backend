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
package net.linlan.emis.support.security.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import static net.linlan.utils.http.HttpContextUtils.acceptIncludeTextHtml;

public class AbstractAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 日志
     */
    private final Logger            logger = LoggerFactory.getLogger(this.getClass());
    protected final UserAgentParser userAgentParser;

    public AbstractAuthenticationEntryPoint(UserAgentParser userAgentParser) {
        this.userAgentParser = userAgentParser;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException,
                                                                ServletException {
        logger.info("--------------support--------------------------");
        //判断请求
        boolean isTextHtml = acceptIncludeTextHtml(request);
        //JSON
        if (!isTextHtml) {

        }
        // HTML
        else {
            //跳转前端SESSION过期路由

        }
    }

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
