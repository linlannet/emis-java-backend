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

import net.linlan.emis.console.pojo.query.app.AppAccessPolicyQuery;
import net.linlan.emis.console.pojo.result.app.AppAccessPolicyResult;
import net.linlan.emis.console.pojo.save.app.AppAccessPolicyCreateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * 应用访问权限策略 Service
 *
 * @author Linlan
 * Created on  2022/6/4 21:55
 */
public interface AppAccessPolicyService {

    /**
     * 查询应用授权策略列表
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppAccessPolicyQuery}
     * @return {@link Page}
     */
    Page<AppAccessPolicyResult> getAppAccessPolicyList(PageModel pageModel,
                                                       AppAccessPolicyQuery query);

    /**
     * 创建应用授权策略
     *
     * @param param {@link AppAccessPolicyCreateParam}
     * @return @{link Boolean}
     */
    Boolean createAppAccessPolicy(AppAccessPolicyCreateParam param);

    /**
     * 删除应用授权策略
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    Boolean deleteAppAccessPolicy(String id);

    /**
     * 启用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableAppAccessPolicy(String id);

    /**
     * 禁用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableAppAccessPolicy(String id);
}
