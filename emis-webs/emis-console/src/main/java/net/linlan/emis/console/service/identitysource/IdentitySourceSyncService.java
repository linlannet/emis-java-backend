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
package net.linlan.emis.console.service.identitysource;

import net.linlan.emis.console.pojo.query.identity.IdentitySourceSyncHistoryListQuery;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceSyncRecordListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * 身份源同步service接口
 *
 * @author Linlan
 * Created on  2022/3/16 22:56
 */
public interface IdentitySourceSyncService {
    /**
     * 执行身份源同步
     *
     * @param id {@link  String} 身份源ID
     */
    void executeIdentitySourceSync(String id);

    /**
     * 查询身份源同步列表
     *
     * @param query     {@link  IdentitySourceSyncHistoryListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncHistoryListResult}
     */
    Page<IdentitySourceSyncHistoryListResult> getIdentitySourceSyncHistoryList(IdentitySourceSyncHistoryListQuery query,
                                                                               PageModel pageModel);

    /**
     * 查询身份源同步详情
     *
     * @param query     {@link  IdentitySourceSyncRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncRecordListResult}
     */
    Page<IdentitySourceSyncRecordListResult> getIdentitySourceSyncRecordList(IdentitySourceSyncRecordListQuery query,
                                                                             PageModel pageModel);
}
