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
package net.linlan.emis.openapi.converter.app;

import org.springframework.stereotype.Component;

import net.linlan.emis.app.entity.AppPermissionAction;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionsActionParam;

/**
 * 权限映射
 *
 * @author Linlan
 * Created on 2020/8/14 22:45
 */
@Component
public class AppPermissionActionConverter {

    public AppPermissionAction toEntity(AppPermissionsActionParam dto) {
        if (dto == null) {
            return null;
        }
        AppPermissionAction entity = new AppPermissionAction();
        entity.setType(dto.getType() == null ? null : dto.getType().getKey());
        entity.setContent(dto.getValue());
        entity.setName(dto.getName());
        return entity;
    }

    public AppPermissionsActionParam toDTO(AppPermissionAction entity) {
        if (entity == null) {
            return null;
        }
        AppPermissionsActionParam dto = new AppPermissionsActionParam();
        dto.setType(net.linlan.emis.core.constant.PermissionActionType.getType(entity.getType()));
        dto.setValue(entity.getContent());
        dto.setName(entity.getName());
        return dto;
    }

}
