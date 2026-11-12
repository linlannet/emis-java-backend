/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.pojo.request.app;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springdoc.api.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询权限列表入参
 *
 * @author Linlan
 * Created on 2020/8/11 23:08
 */
@Data
@Schema(description = "查询权限列表入参")
@ParameterObject
public class AppPermissionListQuery implements Serializable {

    /**
     * 资源名称
     */
    @Parameter(description = "权限名称")
    private String  name;

    /**
     * 所属资源
     */
    @NotNull(message = "请选择权限所属资源")
    @Parameter(description = "所属资源")
    private Long    resourceId;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean enabled;

}
