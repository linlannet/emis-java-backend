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
package net.linlan.emis.protocol.jwt;

import org.springframework.lang.Nullable;

import net.linlan.emis.manage.vo.LoginAccount;
import net.linlan.emis.protocol.jwt.client.JwtRegisteredClient;
import net.linlan.emis.support.userdetails.UserDetails;

/**
 *
 * @author Linlan
 * Created on 2023/7/8 00:23
 */
public interface JwtAuthorizationService {

    void save(JwtAuthentication token);

    void remove(JwtAuthentication authorization);

    @Nullable
    JwtAuthentication findById(String id);

    @Nullable
    JwtAuthentication findByToken(String token);

    /**
     * 用户是否具有登录权限
     *
     * @param client {@link JwtRegisteredClient}
     * @param user {@link UserDetails}
     * @return {@link Boolean}
     */
    Boolean hasLoginPermission(JwtRegisteredClient client, UserDetails user);

    /**
     * 根据客户端&用户ID查询默认应用账户
     *
     * @param client {@link String} 客户端
     * @param  user {@link UserDetails} 用户
     * @return {@link String}
     */
    LoginAccount getDefaultLoginAccount(JwtRegisteredClient client, UserDetails user);
}
