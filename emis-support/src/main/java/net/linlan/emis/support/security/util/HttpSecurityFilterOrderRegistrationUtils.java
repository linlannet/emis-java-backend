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
package net.linlan.emis.support.security.util;

import javax.servlet.Filter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 过滤器注册工具类
 */
public class HttpSecurityFilterOrderRegistrationUtils {

    /** 在当前过滤器前添加过滤器
     * @param http      安全入口
     * @param filter    过滤器
     * @param beforeFilter  前置过滤器
     */
    public static void putFilterBefore(HttpSecurity http, Filter filter,
                                       Class<? extends Filter> beforeFilter) {
        http.addFilterBefore(filter, beforeFilter);
    }

    /** 在当前过滤器后添加过滤器
     * @param http      安全入口
     * @param filter    过滤器
     * @param afterFilter  后置过滤器
     */
    public static void putFilterAfter(HttpSecurity http, Filter filter,
                                      Class<? extends Filter> afterFilter) {
        http.addFilterAfter(filter, afterFilter);
    }
}
