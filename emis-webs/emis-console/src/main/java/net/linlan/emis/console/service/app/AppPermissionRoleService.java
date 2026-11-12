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
package net.linlan.emis.console.service.app;

import net.linlan.emis.console.pojo.query.app.AppPermissionRoleListQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleListResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionRoleCreateParam;
import net.linlan.emis.console.pojo.update.app.PermissionRoleUpdateParam;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
public interface AppPermissionRoleService {

    /**
     * 获取所有角色（分页）
     *
     * @param page  {@link PageModel}
     * @param query {@link AppPermissionRoleListQuery}
     * @return {@link AppPermissionRoleListResult}
     */
    Page<AppPermissionRoleListResult> getPermissionRoleList(PageModel page,
                                                            AppPermissionRoleListQuery query);

    /**
     * 创建角色
     *
     * @param param {@link AppPermissionRoleCreateParam}
     * @return {@link Boolean}
     */
    boolean createPermissionRole(AppPermissionRoleCreateParam param);

    /**
     * 更新角色
     *
     * @param param {@link PermissionRoleUpdateParam}
     * @return {@link Boolean}
     */
    boolean updatePermissionRole(PermissionRoleUpdateParam param);

    /**
     * 删除角色
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    boolean deletePermissionRole(String ids);

    /**
     * 角色详情
     *
     * @param id {@link String}
     * @return {@link AppPermissionRoleResult}
     */
    AppPermissionRoleResult getPermissionRole(String id);

    /**
     * 参数有效性验证
     *
     * @param type     {@link CheckValidityType}
     * @param value    {@link String}
     * @param appId {@link String}
     * @param id       {@link String}
     * @return {@link Boolean}
     */
    Boolean permissionRoleParamCheck(CheckValidityType type, String value, String appId, String id);

    /**
     * 更新角色状态
     *
     * @param id     {@link String}
     * @param status {@link Boolean}
     * @return {@link Boolean}
     */
    Boolean updatePermissionRoleStatus(String id, Boolean status);
}
