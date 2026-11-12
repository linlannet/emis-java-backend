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
package net.linlan.emis.openapi.service.app.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.openapi.pojo.request.app.AppPermissionActionCreateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionListQuery;
import net.linlan.emis.openapi.pojo.request.app.ResourceActionUpdateParam;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionActionGetResult;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionActionListResult;
import net.linlan.emis.openapi.service.app.AppPermissionActionService;

/**
 * <p>
 * 资源权限 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
@Service
@RequiredArgsConstructor
public class AppPermissionActionServiceImpl implements AppPermissionActionService {

    /**
     * 获取权限列表
     *
     * @param query {@link AppPermissionListQuery}
     * @return {@link AppPermissionActionListResult}
     */
    @Override
    public List<AppPermissionActionListResult> getPermissionActionList(AppPermissionListQuery query) {
        return null;
    }

    /**
     * 获取权限详情
     *
     * @param id {@link String}
     * @return {@link AppPermissionActionGetResult}
     */
    @Override
    public AppPermissionActionGetResult getPermissionAction(String id) {
        return null;
    }

    /**
     * 删除权限
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean deletePermissionAction(String id) {
        return null;
    }

    /**
     * 创建权限
     *
     * @param param {@link AppPermissionActionCreateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean createPermissionAction(AppPermissionActionCreateParam param) {
        return null;
    }

    /**
     * 更新权限
     *
     * @param param {@link ResourceActionUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updatePermissionAction(ResourceActionUpdateParam param) {
        return null;
    }
}
