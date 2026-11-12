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
package net.linlan.emis.console.converter.identitysource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.Page;

import net.linlan.emis.console.pojo.result.account.UserGroupListResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import net.linlan.emis.core.constant.TriggerType;
import net.linlan.emis.idsource.constant.IdentitySourceActionType;
import net.linlan.emis.idsource.constant.IdentitySourceObjectType;
import net.linlan.emis.idsource.entity.IdentitySourceSyncHistory;
import net.linlan.emis.idsource.entity.IdentitySourceSyncRecord;
import net.linlan.emis.user.entity.UserGroup;

/**
 * 身份源转换器
 *
 * @author Linlan
 * Created on  2022/2/13 21:37
 */
@Mapper(componentModel = "spring")
public interface IdentitySourceSyncConverter {

    /**
     * 身份源同步实体转换为身份源同步分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default net.linlan.emis.support.repository.domain.Page<IdentitySourceSyncHistoryListResult> entityConvertToIdentitySourceSyncHistoryListResult(Page<? extends IdentitySourceSyncHistory> page) {
        net.linlan.emis.support.repository.domain.Page<IdentitySourceSyncHistoryListResult> result = new net.linlan.emis.support.repository.domain.Page<>();
        if (!CollectionUtils.isEmpty(page)) {
            List<IdentitySourceSyncHistoryListResult> list = new ArrayList<>();
            for (IdentitySourceSyncHistory entity : page) {
                list.add(entityConvertToIdentitySourceSyncHistoryListResult(entity));
            }
            //@formatter:off
            result.setPagination(net.linlan.emis.support.repository.domain.Page.Pagination.builder()
                    .total(page.getTotal())
                    .totalPages(page.getPages())
                    .current(page.getPageNum())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 身份源同步转换为身份源同步分页结果
     *
     * @param entity {@link UserGroup}
     * @return {@link UserGroupListResult}
     */
    default IdentitySourceSyncHistoryListResult entityConvertToIdentitySourceSyncHistoryListResult(IdentitySourceSyncHistory entity) {
        if (entity == null) {
            return null;
        }

        IdentitySourceSyncHistoryListResult identitySourceSyncHistoryListResult = new IdentitySourceSyncHistoryListResult();

        if (entity.getId() != null) {
            identitySourceSyncHistoryListResult.setId(String.valueOf(entity.getId()));
        }
        identitySourceSyncHistoryListResult.setBatch(entity.getBatch());
        if (entity.getIdsourceId() != null) {
            identitySourceSyncHistoryListResult
                .setIdentitySourceId(String.valueOf(entity.getIdsourceId()));
        }
        if (entity.getCreatedCount() != null) {
            identitySourceSyncHistoryListResult
                .setCreatedCount(String.valueOf(entity.getCreatedCount()));
        }
        if (entity.getUpdatedCount() != null) {
            identitySourceSyncHistoryListResult
                .setUpdatedCount(String.valueOf(entity.getUpdatedCount()));
        }
        if (entity.getSkippedCount() != null) {
            identitySourceSyncHistoryListResult
                .setSkippedCount(String.valueOf(entity.getSkippedCount()));
        }
        if (entity.getDeletedCount() != null) {
            identitySourceSyncHistoryListResult
                .setDeletedCount(String.valueOf(entity.getDeletedCount()));
        }
        identitySourceSyncHistoryListResult.setStartTime(toLocalDateTime(entity.getStartTime()));
        identitySourceSyncHistoryListResult.setEndTime(toLocalDateTime(entity.getEndTime()));
        identitySourceSyncHistoryListResult
            .setObjectType(IdentitySourceObjectType.getType(entity.getObjectType()));
        identitySourceSyncHistoryListResult
            .setTriggerType(TriggerType.getType(entity.getTriggerType()));
        identitySourceSyncHistoryListResult.setStatus(entity.getStatus());
        if (entity.getEndTime() != null && entity.getStartTime() != null) {
            identitySourceSyncHistoryListResult.setSpendTime(
                String.valueOf((entity.getEndTime().getTime() - entity.getStartTime().getTime())
                               / 1000));
        }
        return identitySourceSyncHistoryListResult;
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date {@link java.util.Date}
     * @return {@link LocalDateTime}
     */
    default LocalDateTime toLocalDateTime(java.util.Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }

    /**
     * 身份源同步详情转换为身份源详情分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default net.linlan.emis.support.repository.domain.Page<IdentitySourceSyncRecordListResult> entityConvertToIdentitySourceSyncRecordListResult(Page<? extends IdentitySourceSyncRecord> page) {
        net.linlan.emis.support.repository.domain.Page<IdentitySourceSyncRecordListResult> result = new net.linlan.emis.support.repository.domain.Page<>();
        if (!CollectionUtils.isEmpty(page)) {
            List<IdentitySourceSyncRecordListResult> list = new ArrayList<>();
            for (IdentitySourceSyncRecord entity : page) {
                list.add(entityConvertToIdentitySourceSyncRecordListResult(entity));
            }
            //@formatter:off
            result.setPagination(net.linlan.emis.support.repository.domain.Page.Pagination.builder()
                    .total(page.getTotal())
                    .totalPages(page.getPages())
                    .current(page.getPageNum())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 身份源同步详情实体转换为身份源同步详情分页结果
     *
     * @param entity {@link IdentitySourceSyncRecord}
     * @return {@link IdentitySourceSyncRecordListResult}
     */
    default IdentitySourceSyncRecordListResult entityConvertToIdentitySourceSyncRecordListResult(IdentitySourceSyncRecord entity) {
        if (entity == null) {
            return null;
        }
        IdentitySourceSyncRecordListResult result = new IdentitySourceSyncRecordListResult();
        result.setId(entity.getId());
        result.setActionType(IdentitySourceActionType.getType(entity.getActionType()));
        result.setObjectId(entity.getObjectId());
        result.setObjectName(entity.getObjectName());
        result.setObjectType(IdentitySourceObjectType.getType(entity.getObjectType()));
        result.setStatus(entity.getStatus());
        return result;
    }
}
