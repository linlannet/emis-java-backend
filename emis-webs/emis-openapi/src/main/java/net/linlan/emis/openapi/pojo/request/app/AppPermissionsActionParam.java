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

// import java.io.Serial;
import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

import net.linlan.emis.core.constant.PermissionActionType;

/**
 * AppPermissionsActionParam
 *
 * @author Linlan
 * Created on  2022/9/1 00:18
 */
@Data
@Valid
public class AppPermissionsActionParam implements Serializable {

    //@Serial
    private static final long    serialVersionUID = -6391182747252245592L;

    /**
     * 权限类型
     */
    @NotNull(message = "权限类型")
    private PermissionActionType type;
    /**
     * 权限值
     */
    @NotEmpty(message = "权限值")
    private String               value;
    /**
     * 权限描述
     */
    @NotEmpty(message = "权限描述")
    private String               name;
}
