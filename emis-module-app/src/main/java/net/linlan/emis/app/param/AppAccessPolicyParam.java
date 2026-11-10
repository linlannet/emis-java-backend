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

import net.linlan.emis.app.constant.AppPolicySubjectType;
import net.linlan.utils.param.BaseParam;

/**
 *
 * AppAccessPolicy数据域:应用访问授权策略查询参数实体类
 * @author Linlan
 *
 *
 */
@Data
public class AppAccessPolicyParam extends BaseParam {

    /**
     * 应用id
     */
    private String               appId;

    /**
     * 授权主体
     */
    private String               subjectName;

    /**
     * 授权主体ID
     */
    private String               subjectId;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    private AppPolicySubjectType subjectType;

    /**
     * 应用名称
     */
    private String               appName;

}
