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

import java.util.List;

import net.linlan.emis.console.pojo.query.app.AppGroupAssociationListQuery;
import net.linlan.emis.console.pojo.query.app.AppGroupListQuery;
import net.linlan.emis.console.pojo.result.app.AppGroupGetResult;
import net.linlan.emis.console.pojo.result.app.AppGroupListResult;
import net.linlan.emis.console.pojo.result.app.AppListResult;
import net.linlan.emis.console.pojo.save.app.AppGroupCreateParam;
import net.linlan.emis.console.pojo.update.app.AppGroupUpdateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 应用分组管理 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2023-08-31
 */
public interface AppGroupService {

    /**
     * 获取应用分组（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppGroupListQuery}
     * @return {@link AppGroupListResult}
     */
    Page<AppGroupListResult> getAppGroupList(PageModel pageModel, AppGroupListQuery query);

    /**
     * 创建应用分组
     *
     * @param param {@link AppGroupCreateParam}
     * @return {@link Boolean}
     */
    Boolean createAppGroup(AppGroupCreateParam param);

    /**
     * 修改应用分组
     *
     * @param param {@link AppGroupUpdateParam}
     * @return {@link Boolean}
     */
    boolean updateAppGroup(AppGroupUpdateParam param);

    /**
     * 删除应用分组
     *
     * @param id {@link  Long}
     * @return {@link Boolean}
     */
    boolean deleteAppGroup(String id);

    /**
     * 获取单个应用分组详情
     *
     * @param id {@link Long}
     * @return {@link AppGroupGetResult}
     */
    AppGroupGetResult getAppGroup(String id);

    /**
     * 批量移除应用
     *
     * @param id      {@link String}
     * @param appIds {@link String}
     * @return {@link Boolean}
     */
    Boolean batchRemoveAssociation(String id, List<String> appIds);

    /**
     * 获取应用组内应用列表
     *
     * @param query {@link AppGroupAssociationListQuery}
     * @param page  {@link PageModel}
     * @return {@link AppListResult}
     */
    Page<AppListResult> getAppGroupAssociationList(PageModel page,
                                                   AppGroupAssociationListQuery query);
}
