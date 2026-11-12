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

import org.springframework.util.Assert;

import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.manage.vo.LoginAccount;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.jwt.client.JwtRegisteredClient;
import net.linlan.emis.support.userdetails.UserDetails;

/**
 *
 * @author Linlan
 * Created on 2024/1/7 21:47
 */
public abstract class AbstractJwtAuthorizationService implements JwtAuthorizationService {
    private final ApplicationServiceLoader applicationServiceLoader;

    protected AbstractJwtAuthorizationService(ApplicationServiceLoader applicationServiceLoader) {
        Assert.notNull(applicationServiceLoader, "applicationServiceLoader must not be null");
        this.applicationServiceLoader = applicationServiceLoader;
    }

    /**
     * 用户是否具有登录权限
     *
     * @param client {@link JwtRegisteredClient}
     * @param user   {@link UserDetails}
     * @return {@link Boolean}
     */
    @Override
    public Boolean hasLoginPermission(JwtRegisteredClient client, UserDetails user) {
        return null;
    }

    /**
     * 根据客户端&用户ID查询默认应用账户
     *
     * @param client  {@link JwtRegisteredClient} 客户端
     * @param user {@link UserDetails} 用户
     * @return {@link String}
     */
    @Override
    public LoginAccount getDefaultLoginAccount(JwtRegisteredClient client, UserDetails user) {
        JwtApplicationService applicationService = (JwtApplicationService) applicationServiceLoader
            .getApplicationServiceByAppCode(client.getCode());
        AppAccount appAccount = applicationService.getDefaultAppAccount(client.getId(),
            user.getId());
        return LoginAccount.builder().appId(appAccount.getAppId()).userId(appAccount.getUserId())
            .username(appAccount.getAccount()).password(appAccount.getPassword())
            .isDefault(appAccount.getIsDefault()).build();
    }
}
