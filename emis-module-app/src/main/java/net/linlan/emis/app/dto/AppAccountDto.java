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
package net.linlan.emis.app.dto;

import lombok.Data;

import net.linlan.emis.app.constant.AppProtocol;
import net.linlan.emis.app.constant.AppType;
import net.linlan.emis.app.entity.AppAccount;

/**
 *
 * AppAccount数据域:应用账户Dto对象
 * @author Linlan
 *
 *
 */
@Data
public class AppAccountDto extends AppAccount {
    /**
     * 用户名称
     */
    private String      username;

    /**
     * 应用名称
     */
    private String      appName;

    /**
     * 模板
     */
    private String      appTemplate;

    /**
     * 协议
     */
    private AppProtocol appProtocol;

    /**
     * 应用类型
     */
    private AppType     appType;
}
