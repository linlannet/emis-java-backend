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

import io.swagger.v3.oas.annotations.Parameter;
import net.linlan.emis.app.constant.AppPolicyEffect;
import net.linlan.emis.app.constant.AppPolicyObjectType;
import net.linlan.emis.app.constant.AppPolicySubjectType;
import net.linlan.emis.app.entity.AppPermissionPolicy;

/**
 *
 * AppPermissionPolicy数据域:应用权限策略Dto对象
 * @author Linlan
 *
 *
 */
@Data
public class AppPermissionPolicyDto {
    /**
     * 授权主体名称
     */
    @Parameter(description = "授权主体名称")
    private String               subjectName;

    /**
     * 权限客体名菜
     */
    @Parameter(description = "授权客体名称")
    private String               objectName;

    /**
     * ID
     */
    @Parameter(description = "id")
    private String               id;

    /**
     * 授权主体id
     */
    @Parameter(description = "授权主体id")
    private String               subjectId;

    /**
     * 权限主体类型（用户、角色、分组、组织机构）
     */
    @Parameter(description = "授权主体类型")
    private AppPolicySubjectType subjectType;

    /**
     * 权限客体ID
     */
    @Parameter(description = "授权客体id")
    private String               objectId;

    /**
     * 权限客体类型（权限、角色）
     */
    @Parameter(description = "授权客体类型")
    private AppPolicyObjectType  objectType;

    /**
     * 授权作用
     */
    @Parameter(description = "授权作用")
    private AppPolicyEffect      effect;
}
