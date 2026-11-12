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
package net.linlan.emis.openapi.service.app;

import java.util.List;

import net.linlan.emis.openapi.pojo.request.app.AppPermissionActionCreateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionListQuery;
import net.linlan.emis.openapi.pojo.request.app.ResourceActionUpdateParam;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionActionGetResult;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionActionListResult;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
public interface AppPermissionActionService {
    /**
     * 获取权限列表
     *
     * @param query {@link AppPermissionListQuery}
     * @return {@link AppPermissionActionListResult}
     */
    List<AppPermissionActionListResult> getPermissionActionList(AppPermissionListQuery query);

    /**
     * 获取权限详情
     *
     * @param id {@link String}
     * @return {@link AppPermissionActionGetResult}
     */
    AppPermissionActionGetResult getPermissionAction(String id);

    /**
     * 删除权限
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deletePermissionAction(String id);

    /**
     * 创建权限
     *
     * @param param {@link AppPermissionActionCreateParam}
     * @return {@link Boolean}
     */
    Boolean createPermissionAction(AppPermissionActionCreateParam param);

    /**
     * 更新权限
     *
     * @param param {@link ResourceActionUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updatePermissionAction(ResourceActionUpdateParam param);
}
