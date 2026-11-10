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
package net.linlan.emis.support.security.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;

import lombok.Data;

/**
 * 认证提供者
 */
@Data
public class CustomAuthenticationProvider implements AuthenticationProvider {

    public static final CustomAuthenticationProvider USERNAME_PASSWORD;

    /**
     * 认证提供者类型
     */
    private String                                   type;
    /**
     * 认证提供者名称
     */
    private String                                   name;

    static {
        USERNAME_PASSWORD = new CustomAuthenticationProvider("username_password", "用户名密码认证");
    }

    public CustomAuthenticationProvider(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication
            .getDetails();

        // 使用扩展属性
        System.out.println("Login from IP: {} " + details.getGeoLocation().getIp());
        System.out.println("UserAgent: {} " + details.getUserAgent().getBrowser());

        // ...认证逻辑...
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
