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

import net.linlan.emis.console.pojo.query.app.AppPolicyQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionPolicyGetResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionPolicyListResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionPolicyCreateParam;
import net.linlan.emis.console.pojo.update.app.AppPermissionPolicyUpdateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 权限策略 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
public interface AppPermissionPolicyService {
    /**
     * 获取资源列表
     *
     * @param page  {@link PageModel}
     * @param query {@link AppPolicyQuery}
     * @return {@link AppPermissionPolicyListResult}
     */
    Page<AppPermissionPolicyListResult> getPermissionPolicyList(PageModel page,
                                                                AppPolicyQuery query);

    /**
     * 获取资源
     *
     * @param id {@link String}
     * @return {@link AppPermissionPolicyGetResult}
     */
    AppPermissionPolicyGetResult getPermissionPolicy(String id);

    /**
     * 删除资源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deletePermissionPolicy(String id);

    /**
     * 创建资源
     *
     * @param param {@link AppPermissionPolicyCreateParam}
     * @return {@link Boolean}
     */
    Boolean createPermissionPolicy(AppPermissionPolicyCreateParam param);

    /**
     * 更新资源
     *
     * @param param {@link AppPermissionPolicyUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updatePermissionPolicy(AppPermissionPolicyUpdateParam param);
}
