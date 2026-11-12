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

import net.linlan.emis.app.dto.AppPermissionResourceDto;
import net.linlan.emis.app.entity.AppPermissionAction;
import net.linlan.emis.app.entity.AppPermissionResource;
import net.linlan.emis.core.constant.PermissionActionType;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionResourceCreateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionResourceUpdateParam;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionResourceGetResult;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionResourceListResult;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.Page.Pagination;

/**
 * 资源映射
 *
 * @author Linlan
 * Created on 2020/8/14 22:45
 */
@Component
public class AppPermissionResourceConverter {

    /**
     * 资源创建参数转实体类
     *
     * @param param {@link AppPermissionResourceCreateParam}
     * @return {@link AppPermissionResource}
     */
    public AppPermissionResource resourceCreateParamConvertToEntity(
        AppPermissionResourceCreateParam param) {
        if (param == null) {
            return null;
        }
        AppPermissionResource entity = new AppPermissionResource();
        entity.setCode(param.getCode());
        entity.setName(param.getName());
        entity.setDescription(param.getDesc());
        entity.setIsEnabled(param.getEnabled());
        entity.setAppId(param.getAppId() == null ? null : String.valueOf(param.getAppId()));
        return entity;
    }

    /**
     * 资源修改参数转实体类
     *
     * @param param {@link AppPermissionResourceUpdateParam}
     * @return {@link AppPermissionResource}
     */
    public AppPermissionResource resourceUpdateParamConvertToEntity(
        AppPermissionResourceUpdateParam param) {
        if (param == null) {
            return null;
        }
        AppPermissionResource entity = new AppPermissionResource();
        entity.setId(param.getId());
        entity.setName(param.getName());
        entity.setDescription(param.getDesc());
        return entity;
    }

    /**
     * 资源转换为资源列表结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    public Page<AppPermissionResourceListResult> entityConvertToResourceListResult(
        com.github.pagehelper.Page<AppPermissionResourceDto> page, long total, int current,
        int totalPages) {
        Page<AppPermissionResourceListResult> result = new Page<>();
        if (page != null && !CollectionUtils.isEmpty(page.getResult())) {
            List<AppPermissionResourceListResult> list = new ArrayList<>();
            for (AppPermissionResource resource : page.getResult()) {
                list.add(entityConvertToResourceListResult(resource));
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
     * 实体转换为资源列表结果
     *
     * @param data {@link AppPermissionResource}
     * @return {@link AppPermissionResourceListResult}
     */
    public AppPermissionResourceListResult entityConvertToResourceListResult(
        AppPermissionResource data) {
        if (data == null) {
            return null;
        }
        AppPermissionResourceListResult result = new AppPermissionResourceListResult();
        result.setId(data.getId());
        result.setName(data.getName());
        result.setCode(data.getCode());
        result.setAppId(data.getAppId());
        result.setDesc(data.getDescription());
        return result;
    }

    /**
     * 实体转获取详情返回
     *
     * @param resource {@link AppPermissionResource}
     * @param actions  {@link List}
     * @return {@link AppPermissionResourceGetResult}
     */
    public AppPermissionResourceGetResult entityConvertToResourceGetResult(
        AppPermissionResource resource, List<AppPermissionAction> actions) {
        if (resource == null) {
            return null;
        }
        AppPermissionResourceGetResult result = new AppPermissionResourceGetResult();
        result.setName(resource.getName());
        result.setCode(resource.getCode());
        result.setDesc(resource.getDescription());
        result.setAppId(resource.getAppId() == null ? null : Long.valueOf(resource.getAppId()));
        List<AppPermissionResourceGetResult.AppPermissionsAction> actionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(actions)) {
            for (AppPermissionAction action : actions) {
                AppPermissionResourceGetResult.AppPermissionsAction item = new AppPermissionResourceGetResult.AppPermissionsAction();
                item.setId(action.getId());
                item.setType(PermissionActionType.getType(action.getType()));
                item.setValue(action.getContent());
                item.setName(action.getName());
                actionList.add(item);
            }
        }
        result.setActions(actionList);
        return result;
    }

}
