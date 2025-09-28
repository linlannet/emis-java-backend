/*
 * emis-module-app - app应用管理
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
package net.linlan.emis.app.param;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * AppAccount数据域:应用账户查询参数实体类
 * @author Linlan
 *
 *
 */
@Data
public class AppAccountParam extends BaseParam {

    /**
     * appId
     */
    private String appId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 账户名称
     */
    private String account;

    /**
     * 应用名称
     */
    private String appName;
}
