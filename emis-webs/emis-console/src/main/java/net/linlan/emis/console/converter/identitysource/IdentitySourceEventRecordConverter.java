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

import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import net.linlan.emis.core.constant.SyncStatus;
import net.linlan.emis.idsource.constant.IdentitySourceActionType;
import net.linlan.emis.idsource.constant.IdentitySourceObjectType;
import net.linlan.emis.idsource.entity.IdentitySourceEventRecord;
import net.linlan.utils.SpringContextUtils;

/**
 * 身份源事件记录转换器
 *
 * @author Linlan
 * Created on  2022/2/13 21:37
 */
@Mapper(componentModel = "spring")
public interface IdentitySourceEventRecordConverter {

    /**
     * 身份源事件记录实体转换为身份源事件记录分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default net.linlan.emis.support.repository.domain.Page<IdentitySourceEventRecordListResult> entityConvertToIdentitySourceSyncRecordListResult(Page<net.linlan.emis.idsource.dto.IdentitySourceEventRecordDto> page) {
        net.linlan.emis.support.repository.domain.Page<IdentitySourceEventRecordListResult> result = new net.linlan.emis.support.repository.domain.Page<>();
        if (!CollectionUtils.isEmpty(page)) {
            List<IdentitySourceEventRecordListResult> list = new ArrayList<>();
            for (IdentitySourceEventRecord entity : page) {
                IdentitySourceEventRecordConverter bean = SpringContextUtils
                    .getBean(IdentitySourceEventRecordConverter.class);
                list.add(bean.entityConvertToIdentitySourceSyncRecordListResult(entity));
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
     * 身份源事件记录转换为身份源事件记录分页结果
     *
     * @param entity {@link IdentitySourceEventRecord}
     * @return {@link IdentitySourceEventRecordListResult}
     */
    default IdentitySourceEventRecordListResult entityConvertToIdentitySourceSyncRecordListResult(IdentitySourceEventRecord entity) {
        if (entity == null) {
            return null;
        }
        IdentitySourceEventRecordListResult result = new IdentitySourceEventRecordListResult();
        result.setId(entity.getId());
        result.setActionType(IdentitySourceActionType.getType(entity.getActionType()));
        result.setObjectId(entity.getObjectId());
        result.setObjectName(entity.getObjectName());
        result.setObjectType(IdentitySourceObjectType.getType(entity.getObjectType()));
        if (entity.getEventTime() != null) {
            result.setEventTime(LocalDateTime.ofInstant(entity.getEventTime().toInstant(),
                ZoneId.systemDefault()));
        }
        result.setStatus(SyncStatus.getStatus(entity.getStatus()));
        return result;
    }
}
