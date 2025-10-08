/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.manage.client;

import org.springframework.util.Assert;

import lombok.Getter;

import net.linlan.emis.manage.IdentitySourceConfig;

/**
 * 身份源客户端抽象类
 *
 * @author Linlan
 * 
 */
@Getter
public abstract class AbstractIdentitySourceClient<T extends IdentitySourceConfig>
                                                  implements IdentitySourceClient {

    public static String ACCESS_KEY = "ACCESS_KEY";

    protected AbstractIdentitySourceClient(T config) {
        Assert.notNull(config, "IdentityProviderConfig Not Null");
        this.config = config;
    }

    /**
     * 配置
     */
    private final T config;
}
