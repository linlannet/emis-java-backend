/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.pojo.query.app;

import org.springdoc.api.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AppAccountCreateParam 应用账户查询入参
 *
 * @author Linlan
 * Created on 2022/5/24 22:13
 */
@Data
@Schema(description = "应用账户查询入参")
@ParameterObject
public class AppAccountQuery {

    /**
     * appId
     */
    @Parameter(description = "appId")
    private String appId;
    /**
     * 用户ID
     */
    @Parameter(description = "用户ID")
    private String userId;
    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String username;
    /**
     * 账户名称
     */
    @Parameter(description = "账户名称")
    private String account;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String appName;
}
