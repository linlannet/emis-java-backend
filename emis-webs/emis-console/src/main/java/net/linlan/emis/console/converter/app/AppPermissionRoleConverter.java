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
package net.linlan.emis.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.linlan.emis.app.dto.AppPermissionRoleDto;
import net.linlan.emis.app.entity.AppPermissionRole;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleListResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionRoleCreateParam;
import net.linlan.emis.console.pojo.update.app.PermissionRoleUpdateParam;
import net.linlan.emis.support.repository.domain.Page;

/**
 * 角色映射
 *
 * @author Linlan
 * Created on  2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface AppPermissionRoleConverter {

    /**
     * 角色实体转换为角色分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<AppPermissionRoleListResult> entityConvertToRolePaginationResult(com.github.pagehelper.Page<AppPermissionRoleDto> page) {
        Page<AppPermissionRoleListResult> result = new Page<>();
        if (!page.isEmpty()) {
            List<AppPermissionRoleListResult> list = new ArrayList<>();
            for (AppPermissionRole user : page) {
                list.add(entityConvertToRolePaginationResult(user));
            }
            //@formatter:off
            result.setPagination(Page.Pagination.builder()
                    .total(page.getTotal())
                    .totalPages(page.getPages())
                    .current(page.getPageNum())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 角色实体转换为角色分页结果
     *
     * @param page {@link AppPermissionRole}
     * @return {@link AppPermissionRoleListResult}
     */
    AppPermissionRoleListResult entityConvertToRolePaginationResult(AppPermissionRole page);

    /**
     * 角色创建参数转换为角色实体
     *
     * @param param {@link AppPermissionRoleCreateParam}
     * @return {@link AppPermissionRole}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", expression = "java(Boolean.TRUE)")
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppPermissionRole roleCreateParamConvertToEntity(AppPermissionRoleCreateParam param);

    /**
     * 角色更新参数转换为角色实体类
     *
     * @param param {@link PermissionRoleUpdateParam} 更新参数
     * @return {@link AppPermissionRole} 角色实体
     */
    @Mapping(target = "appId", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppPermissionRole roleUpdateParamConvertToEntity(PermissionRoleUpdateParam param);

    /**
     * 实体转系统详情结果
     *
     * @param role {@link AppPermissionRole}
     * @return {@link AppPermissionRoleResult}
     */
    AppPermissionRoleResult entityConvertToRoleDetailResult(AppPermissionRole role);
}
