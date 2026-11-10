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
package net.linlan.emis.manage.endpoint;

import java.io.Serializable;

import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.Data;

/**
 *
 * @author Linlan
 *
 */
@Data
public class EndpointMatcher implements Serializable {

    /**
     * 请求匹配器
     */
    private RequestMatcher requestMatcher;

    /**
     * access
     */
    private Boolean        access;

    public EndpointMatcher(RequestMatcher requestMatcher, Boolean access) {
        this.requestMatcher = requestMatcher;
        this.access = access;
    }

    public EndpointMatcher() {
    }
}
