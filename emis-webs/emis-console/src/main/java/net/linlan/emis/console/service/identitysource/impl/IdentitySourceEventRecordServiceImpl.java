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
package net.linlan.emis.console.service.identitysource.impl;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import net.linlan.emis.console.converter.identitysource.IdentitySourceEventRecordConverter;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import net.linlan.emis.console.service.identitysource.IdentitySourceEventRecordService;
import net.linlan.emis.idsource.param.IdentitySourceEventRecordParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * 身份源事件记录
 *
 * @author Linlan
 * Created on  2022/3/16 21:04
 */
@Service
@AllArgsConstructor
public class IdentitySourceEventRecordServiceImpl implements IdentitySourceEventRecordService {

    /**
     * 身份源事件记录  列表
     *
     * @param query     {@link  IdentitySourceEventRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceEventRecordListResult}
     */
    @Override
    public Page<IdentitySourceEventRecordListResult> getIdentitySourceEventRecordList(IdentitySourceEventRecordListQuery query,
                                                                                      PageModel pageModel) {
        //查询条件
        IdentitySourceEventRecordParam param = new IdentitySourceEventRecordParam();
        param.setIdsourceId(query.getIdentitySourceId());
        param.setActionType(
            query.getActionType() == null ? null : query.getActionType().getKey());
        param.setObjectType(
            query.getObjectType() == null ? null : query.getObjectType().getKey());
        param.setStatus(query.getStatus() == null ? null : query.getStatus().getKey());
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        return identitySourceEventRecordConverter.entityConvertToIdentitySourceSyncRecordListResult(
            identitySourceEventRecordService.getPageDto(param));
    }

    /**
     * 身份源时间记录
     */
    private final net.linlan.emis.idsource.service.IdentitySourceEventRecordService identitySourceEventRecordService;

    private final IdentitySourceEventRecordConverter  identitySourceEventRecordConverter;
}
