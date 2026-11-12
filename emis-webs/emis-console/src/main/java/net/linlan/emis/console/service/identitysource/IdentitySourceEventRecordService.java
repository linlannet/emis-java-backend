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

import net.linlan.emis.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 身份源事件记录 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-16
 */
public interface IdentitySourceEventRecordService {

    /**
     * 身份源事件记录  列表
     *
     * @param query     {@link  IdentitySourceEventRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceEventRecordListResult}
     */
    Page<IdentitySourceEventRecordListResult> getIdentitySourceEventRecordList(IdentitySourceEventRecordListQuery query,
                                                                               PageModel pageModel);
}
