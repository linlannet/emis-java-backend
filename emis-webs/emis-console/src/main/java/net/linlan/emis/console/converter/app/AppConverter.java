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
package net.linlan.emis.console.converter.app;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.google.common.collect.Lists;

import net.linlan.emis.app.dto.AppInfoDto;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.param.AppGroupAssociationParam;
import net.linlan.emis.console.pojo.query.app.AppGroupAssociationListQuery;
import net.linlan.emis.console.pojo.result.app.AppGetResult;
import net.linlan.emis.console.pojo.result.app.AppListResult;
import net.linlan.emis.console.pojo.update.app.AppUpdateParam;
import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.utils.SpringContextUtils;

/**
 * 应用映射
 *
 * @author Linlan
 * Created on  2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface AppConverter {

    /**
     * 实体转换为应用列表结果
     *
     * @param entityPage {@link List}
     * @return {@link List}
     */
    default Page<AppListResult> entityConvertToAppListResult(com.github.pagehelper.Page<AppInfoDto> entityPage) {
        Page<AppListResult> page = new Page<>();
        List<AppListResult> list = Lists.newArrayList();
        for (AppInfo entity : entityPage) {
            AppListResult result = entityConvertToAppListResult(entity);
            // 如果备注为空、放置详情，由于 entityConvertToAppListResult 为共用，所以这里单独处理。
            if (StringUtils.isBlank(result.getRemark())) {
                ApplicationServiceLoader loader = SpringContextUtils
                    .getBean(ApplicationServiceLoader.class);
                ApplicationService applicationService = loader
                    .getApplicationService(entity.getTemplate());
                if (!Objects.isNull(applicationService)) {
                    result.setRemark(applicationService.getDescription());
                }
            }
            list.add(result);
        }
        page.setList(list);
        //@formatter:off
        page.setPagination(Page.Pagination.builder()
                .total(entityPage.getTotal())
                .totalPages(entityPage.getPages())
                .current(entityPage.getPageNum())
                .build());
        //@formatter:on
        return page;
    }

    /**
     * 实体转应用管理列表
     *
     * @param entity {@link AppInfo}
     * @return {@link AppListResult}
     */
    default AppListResult entityConvertToAppListResult(AppInfo entity) {
        if (entity == null) {
            return null;
        }

        AppListResult appListResult = new AppListResult();
        if (entity.getId() != null) {
            appListResult.setId(String.valueOf(entity.getId()));
        }
        appListResult.setName(entity.getName());
        appListResult.setType(net.linlan.emis.app.constant.AppType.getType(entity.getType()));
        appListResult.setIcon(entity.getIcon());
        //图标未配置，所以先从模版中拿
        if (StringUtils.isBlank(entity.getIcon())) {
            ApplicationService applicationService = getApplicationServiceLoader()
                .getApplicationService(entity.getTemplate());
            appListResult.setIcon(applicationService.getBase64Icon());
        }
        appListResult.setTemplate(entity.getTemplate());
        appListResult.setProtocol(
            net.linlan.emis.app.constant.AppProtocol.getType(entity.getProtocol()));
        appListResult.setEnabled(entity.getIsEnabled());
        appListResult.setRemark(entity.getDescription());
        return appListResult;
    }

    /**
     * 实体转应用返回
     *
     * @param entity {@link AppInfo}
     * @param groupIds {@link List}
     * @return {@link AppGetResult}
     */
    default AppGetResult entityConvertToAppResult(AppInfo entity, List<String> groupIds) {
        if (entity == null) {
            return null;
        }
        AppGetResult appGetResult = new AppGetResult();

        if (entity.getId() != null) {
            appGetResult.setId(String.valueOf(entity.getId()));
        }
        appGetResult.setName(entity.getName());
        appGetResult.setCode(entity.getCode());
        appGetResult.setClientId(entity.getClientId());
        appGetResult.setClientSecret(entity.getClientSecret());
        appGetResult.setType(net.linlan.emis.app.constant.AppType.getType(entity.getType()));
        appGetResult
            .setGroupIds(groupIds.stream().map(String::valueOf).collect(Collectors.toList()));
        //图标未配置，所以先从模版中拿
        if (StringUtils.isBlank(entity.getIcon())) {
            ApplicationService applicationService = getApplicationServiceLoader()
                .getApplicationService(entity.getTemplate());
            appGetResult.setIcon(applicationService.getBase64Icon());
        } else {
            appGetResult.setIcon(entity.getIcon());
        }
        appGetResult.setTemplate(entity.getTemplate());
        appGetResult.setProtocol(
            net.linlan.emis.app.constant.AppProtocol.getType(entity.getProtocol()));
        appGetResult.setAuthorizationType(
            net.linlan.emis.app.constant.AuthorizationType.getType(entity.getAuthorizationType()));
        appGetResult.setProtocolName(
            net.linlan.emis.app.constant.AppProtocol.getType(entity.getProtocol()).getValue());
        appGetResult.setEnabled(entity.getIsEnabled());
        appGetResult.setCreateTime(toLocalDateTime(entity.getCreateTime()));
        appGetResult.setRemark(entity.getDescription());
        return appGetResult;
    }

    /**
     * Date转LocalDateTime
     *
     * @param date {@link Date}
     * @return {@link LocalDateTime}
     */
    default LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 将应用修改对象转换为entity
     *
     * @param param {@link AppUpdateParam}
     * @return {@link AppInfo}
     */
    @Mapping(target = "groupAssociations", ignore = true)
    @Mapping(target = "configured", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "initLoginUrl", ignore = true)
    @Mapping(target = "clientSecret", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "protocol", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppInfo appUpdateParamConverterToEntity(AppUpdateParam param);

    /**
     * 获取 ApplicationServiceLoader
     *
     * @return {@link ApplicationServiceLoader}
     */
    default ApplicationServiceLoader getApplicationServiceLoader() {
        return SpringContextUtils.getBean(ApplicationServiceLoader.class);
    }

    /**
     * 转查询参数
     *
     * @param query {@link AppGroupAssociationListQuery}
     * @return {@link AppGroupAssociationListQueryParam}
     */
    AppGroupAssociationParam appGroupAssociationListQueryToQueryParam(AppGroupAssociationListQuery query);
}
