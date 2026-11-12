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

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.identitysource.IdentitySourceSyncConverter;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceSyncHistoryListQuery;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceSyncRecordListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import net.linlan.emis.console.service.identitysource.IdentitySourceService;
import net.linlan.emis.console.service.identitysource.IdentitySourceSyncService;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.idsource.param.IdentitySourceSyncHistoryParam;
import net.linlan.emis.idsource.param.IdentitySourceSyncRecordParam;
import net.linlan.emis.idsource.service.IdentitySourceSyncHistoryService;
import net.linlan.emis.idsource.service.IdentitySourceSyncRecordService;
import net.linlan.emis.manage.event.IdentitySourceEventUtils;
import net.linlan.emis.manage.exception.IdentitySourceNotExistException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * 同步身份源同步
 *
 * @author Linlan
 * Created on  2022/3/16 21:04
 */
@Slf4j
@Service
@AllArgsConstructor
public class IdentitySourceSyncServiceImpl implements IdentitySourceSyncService {
    /**
     * 查询身份源同步列表
     *
     * @param query     {@link  IdentitySourceSyncHistoryListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncRecordListResult}
     */
    @Override
    public Page<IdentitySourceSyncHistoryListResult> getIdentitySourceSyncHistoryList(IdentitySourceSyncHistoryListQuery query,
                                                                                      PageModel pageModel) {
        //查询条件
        IdentitySourceSyncHistoryParam param = new IdentitySourceSyncHistoryParam();
        param.setIdsourceId(query.getIdentitySourceId());
        param.setTriggerType(
            query.getTriggerType() == null ? null : query.getTriggerType().getKey());
        param.setStatus(query.getStatus() == null ? null : query.getStatus().getKey());
        param.setObjectType(
            query.getObjectType() == null ? null : query.getObjectType().getKey());
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        return identitySourceSyncConverter.entityConvertToIdentitySourceSyncHistoryListResult(
            identitySourceSyncHistoryService.getPageDto(param));
    }

    /**
     * 查询身份源同步详情
     *
     * @param query     {@link  IdentitySourceSyncRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncRecordListResult}
     */
    @Override
    public Page<IdentitySourceSyncRecordListResult> getIdentitySourceSyncRecordList(IdentitySourceSyncRecordListQuery query,
                                                                                    PageModel pageModel) {
        //查询条件
        IdentitySourceSyncRecordParam param = new IdentitySourceSyncRecordParam();
        param.setSyncHistoryId(query.getSyncHistoryId());
        param.setObjectType(
            query.getObjectType() == null ? null : query.getObjectType().getKey());
        param.setActionType(
            query.getActionType() == null ? null : query.getActionType().getKey());
        param.setStatus(query.getStatus() == null ? null : query.getStatus().getKey());
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        return identitySourceSyncConverter.entityConvertToIdentitySourceSyncRecordListResult(
            identitySourceSyncRecordService.getPageDto(param));
    }

    /**
     * 执行身份源同步
     *
     * @param id {@link  String} 身份源ID
     */
    @Override
    public void executeIdentitySourceSync(String id) {
        IdentitySource entity = identitySourceService.getIdentitySource(id);
        AuditContext.setTarget(Target.builder().id(id).name(entity.getName())
            .type(TargetType.IDENTITY_SOURCE).build());
        if (!ObjectUtils.isEmpty(entity)) {
            if (Objects.isNull(entity.getBasicConfig())) {
                throw new NullPointerException("请完善参数配置");
            }
            if (!entity.getIsEnabled()) {
                throw new NullPointerException("身份源已禁用");
            }
            //发送分布式事件
            IdentitySourceEventUtils.sync(id);
            return;
        }
        throw new IdentitySourceNotExistException();
    }

    /**
     * 身份源service
     */
    private final IdentitySourceService               identitySourceService;
    /**
     * 身份源同步记录
     */
    private final IdentitySourceSyncHistoryService identitySourceSyncHistoryService;
    /**
     * 身份源同步详情
     */
    private final IdentitySourceSyncRecordService  identitySourceSyncRecordService;
    /**
     * 身份源同步转换
     */
    private final IdentitySourceSyncConverter         identitySourceSyncConverter;
}
