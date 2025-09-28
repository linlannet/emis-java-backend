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

import javax.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import net.linlan.emis.app.constant.AppPolicyEffect;
import net.linlan.emis.app.constant.AppPolicyObjectType;
import net.linlan.emis.app.constant.AppPolicySubjectType;
import net.linlan.utils.param.BaseParam;

/**
 *
 * AppPermissionPolicy数据域:应用权限策略查询参数实体类
 * @author Linlan
 *
 *
 */
@Data
public class AppPermissionPolicyParam extends BaseParam {

    /**
     * 所属应用
     */
    @NotNull(message = "资源所属应用不能为空")
    @Parameter(description = "所属应用")
    private String               appId;

    /**
     * 授权主体Id
     */
    @Parameter(description = "授权主体Id")
    private String               subjectId;

    /**
     * 权限主体类型（用户、角色、分组、组织机构）
     */
    @NotNull(message = "授权主体类型不能为空")
    @Parameter(description = "授权主体类型")
    private AppPolicySubjectType subjectType;

    /**
     * 授权客体Id
     */
    @Parameter(description = "授权客体Id")
    private String               objectId;

    /**
     * 权限客体类型（权限、角色）
     */
    @NotNull(message = "授权客体类型不能为空")
    @Parameter(description = "授权客体类型")
    private AppPolicyObjectType  objectType;

    /**
     * 规则效果
     */
    @Parameter(description = "规则效果")
    private AppPolicyEffect      effect;

}
