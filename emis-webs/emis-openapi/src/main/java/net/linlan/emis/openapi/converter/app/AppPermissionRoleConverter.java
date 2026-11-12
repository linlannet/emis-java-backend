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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.app.dto.AppPermissionRoleDto;
import net.linlan.emis.app.entity.AppPermissionRole;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionRoleCreateParam;
import net.linlan.emis.openapi.pojo.request.app.PermissionRoleUpdateParam;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionRoleListResult;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionRoleResult;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.Page.Pagination;

/**
 * 角色映射
 *
 * @author Linlan
 * Created on 2020/8/14 23:12
 */
@Component
public class AppPermissionRoleConverter {

    /**
     * 角色实体转换为角色分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    public Page<AppPermissionRoleListResult> entityConvertToRolePaginationResult(
        com.github.pagehelper.Page<AppPermissionRoleDto> page, long total, int current,
        int totalPages) {
        Page<AppPermissionRoleListResult> result = new Page<>();
        if (page != null && !CollectionUtils.isEmpty(page.getResult())) {
            List<AppPermissionRoleListResult> list = new ArrayList<>();
            for (AppPermissionRole role : page.getResult()) {
                list.add(entityConvertToRolePaginationResult(role));
            }
            //@formatter:off
            result.setPagination(Pagination.builder()
                    .total(total)
                    .totalPages(totalPages)
                    .current(current)
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 实体转换为角色分页结果
     *
     * @param data {@link AppPermissionRole}
     * @return {@link AppPermissionRoleListResult}
     */
    public AppPermissionRoleListResult entityConvertToRolePaginationResult(
        AppPermissionRole data) {
        if (data == null) {
            return null;
        }
        AppPermissionRoleListResult result = new AppPermissionRoleListResult();
        result.setId(data.getId());
        result.setName(data.getName());
        result.setAppId(data.getAppId());
        result.setCode(data.getCode());
        result.setEnabled(data.getIsEnabled());
        result.setRemark(data.getDescription());
        return result;
    }

    /**
     * 角色创建参数转为角色实体
     *
     * @param param {@link AppPermissionRoleCreateParam}
     * @return {@link AppPermissionRole}
     */
    public AppPermissionRole roleCreateParamConvertToEntity(AppPermissionRoleCreateParam param) {
        if (param == null) {
            return null;
        }
        AppPermissionRole entity = new AppPermissionRole();
        entity.setName(param.getName());
        entity.setCode(param.getCode());
        entity.setDescription(param.getRemark());
        entity.setIsEnabled(param.getEnabled());
        entity.setAppId(param.getAppId());
        return entity;
    }

    /**
     * 角色修改参数转为角色实体
     *
     * @param param {@link PermissionRoleUpdateParam}
     * @return {@link AppPermissionRole}
     */
    public AppPermissionRole roleUpdateParamConvertToEntity(PermissionRoleUpdateParam param) {
        if (param == null) {
            return null;
        }
        AppPermissionRole entity = new AppPermissionRole();
        entity.setId(param.getId());
        entity.setName(param.getName());
        entity.setCode(param.getCode());
        entity.setDescription(param.getRemark());
        return entity;
    }

    /**
     * 实体转换为角色详情结果
     *
     * @param data {@link AppPermissionRole}
     * @return {@link AppPermissionRoleResult}
     */
    public AppPermissionRoleResult entityConvertToRoleDetailResult(AppPermissionRole data) {
        if (data == null) {
            return null;
        }
        AppPermissionRoleResult result = new AppPermissionRoleResult();
        result.setId(data.getId());
        result.setAppId(data.getAppId());
        result.setName(data.getName());
        result.setCode(data.getCode());
        result.setEnabled(data.getIsEnabled());
        result.setRemark(data.getDescription());
        return result;
    }

}
