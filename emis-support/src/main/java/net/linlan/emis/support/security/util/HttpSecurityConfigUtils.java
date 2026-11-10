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

import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.linlan.emis.support.userdetails.UserDetailsService;
import net.linlan.utils.SpringContextUtils;

/**
 * 安全配置工具类，通过上下文获取Bean对象，放入http中
 */
public class HttpSecurityConfigUtils {

    public static UserDetailsService getUserDetailsService(HttpSecurity http) {
        UserDetailsService userDetailsService = SpringContextUtils
            .getBean(UserDetailsService.class);
        http.setSharedObject(UserDetailsService.class, userDetailsService);
        return userDetailsService;
    }

    public static PasswordEncoder getPasswordEncoder(HttpSecurity http) {
        PasswordEncoder passwordEncoder = SpringContextUtils.getBean(PasswordEncoder.class);
        http.setSharedObject(PasswordEncoder.class, passwordEncoder);
        return passwordEncoder;
    }

    public static <T> T getOptionalType(HttpSecurity httpSecurity, ResolvableType type) {
        Map<String, T> beansMap = (Map<String, T>) BeanFactoryUtils.beansOfTypeIncludingAncestors(
            httpSecurity.getSharedObject(ApplicationContext.class), type.getClass());
        return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
    }

}
