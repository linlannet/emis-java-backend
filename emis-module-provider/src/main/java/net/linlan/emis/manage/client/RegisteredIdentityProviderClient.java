/*
 * emis-module-provider - provider身份提供商
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

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 已注册的身份提供程序客户端
 *
 * @author Linlan
 */
@Data
@Builder
public class RegisteredIdentityProviderClient<T extends IdentityProviderConfig>
                                             implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * code
     */
    private String code;

    /**
     * name
     */
    private String name;

    /**
     * 配置
     */
    private T      config;
}
