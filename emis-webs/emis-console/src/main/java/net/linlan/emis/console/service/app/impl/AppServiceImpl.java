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
package net.linlan.emis.console.service.app.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dto.AppInfoDto;
import net.linlan.emis.app.entity.AppGroupAssociation;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.param.AppInfoParam;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.app.AppConverter;
import net.linlan.emis.console.pojo.query.app.AppQuery;
import net.linlan.emis.console.pojo.result.app.AppCreateResult;
import net.linlan.emis.console.pojo.result.app.AppGetResult;
import net.linlan.emis.console.pojo.result.app.AppListResult;
import net.linlan.emis.console.pojo.save.app.AppCreateParam;
import net.linlan.emis.console.pojo.update.app.AppSaveConfigParam;
import net.linlan.emis.console.pojo.update.app.AppUpdateParam;
import net.linlan.emis.console.service.app.AppService;
import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * ApplicationServiceImpl
 *
 * @author Linlan
 * Created on  2020/11/29 22:23
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppServiceImpl implements AppService {

    /**
     * 获取应用（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppQuery}
     * @return {@link AppListResult}
     */
    @Override
    public Page<AppListResult> getAppList(PageModel pageModel, AppQuery query) {
        AppInfoParam param = new AppInfoParam();
        param.setName(query.getName());
        if (query.getProtocol() != null) {
            param.setProtocol(query.getProtocol().getKey());
        }
        param.setPage(pageModel.getCurrent() + 1);
        param.setLimit(pageModel.getPageSize());
        com.github.pagehelper.Page<AppInfoDto> list = appInfoService.getPageDto(param);
        return appConverter.entityConvertToAppListResult(list);
    }

    /**
     * 创建应用
     *
     * @param param {@link AppCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppCreateResult createApp(AppCreateParam param) {
        ApplicationService applicationService = applicationServiceLoader
            .getApplicationService(param.getTemplate());
        String appId;
        if (!CollectionUtils.isEmpty(param.getGroupIds())) {
            appId = applicationService.create(param.getName(), param.getIcon(), param.getRemark(),
                param.getGroupIds());
        } else {
            appId = applicationService.create(param.getName(), param.getIcon(), param.getRemark());
        }
        AuditContext.setTarget(
            Target.builder().id(appId).name(param.getName()).type(TargetType.APPLICATION).build());
        return new AppCreateResult(appId);
    }

    /**
     * 修改应用
     *
     * @param param {@link AppUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApp(AppUpdateParam param) {
        AppInfo app = appRequireNonNull(param.getId());
        AppInfo entity = appConverter.appUpdateParamConverterToEntity(param);
        BeanUtils.merge(entity, app, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        appInfoService.update(app);
        //应用分组
        List<AppGroupAssociation> list = appGroupAssociationService
            .getList(new StringMap().put("appId", String.valueOf(app.getId())).map());
        //要添加的分组
        List<String> addGroupIds = param.getGroupIds().stream().distinct()
            .filter(id -> list.stream().noneMatch(i -> id.equals(i.getGroupId())))
            .collect(Collectors.toList());
        addGroupIds.forEach(groupId -> {
            AppGroupAssociation association = new AppGroupAssociation();
            association.setGroupId(String.valueOf(groupId));
            association.setAppId(String.valueOf(app.getId()));
            appGroupAssociationService.save(association);
        });
        //要删除的分组
        List<String> deleteGroupIds = list.stream().map(AppGroupAssociation::getGroupId)
            .collect(Collectors.toList()).stream().distinct()
            .filter(id -> param.getGroupIds().stream().noneMatch(id::equals))
            .collect(Collectors.toList());
        deleteGroupIds.forEach(groupId -> list.stream()
            .filter(item -> groupId.equals(item.getGroupId()))
            .forEach(item -> appGroupAssociationService.deleteById(item.getId())));
        AuditContext.setTarget(Target.builder().id(param.getId()).name(param.getName())
            .type(TargetType.APPLICATION).build());
        return true;
    }

    /**
     * 删除应用
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteApp(String id) {
        AppInfo app = appRequireNonNull(id);
        applicationServiceLoader.getApplicationService(app.getTemplate()).delete(id);
        appGroupAssociationService.getList(new StringMap().put("appId", id).map())
            .forEach(item -> appGroupAssociationService.deleteById(item.getId()));
        AuditContext.setTarget(
            Target.builder().id(id).name(app.getName()).type(TargetType.APPLICATION).build());
        return true;
    }

    /**
     * 获取单个应用详情
     *
     * @param id {@link Long}
     * @return {@link AppInfo}
     */
    @Override
    public AppGetResult getApp(String id) {
        AppInfo entity = appInfoService.findById(id);
        if (entity != null) {
            List<String> groupIds = appGroupAssociationService.getList(new StringMap().put("appId", id).map())
                .stream().map(AppGroupAssociation::getGroupId).collect(Collectors.toList());
            return appConverter.entityConvertToAppResult(entity, groupIds);
        }
        return null;

    }

    /**
     * 启用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean enableApp(String id) {
        AppInfo appEntity = appRequireNonNull(id);
        Integer count = appInfoService.updateStatus(id, Boolean.TRUE);
        AuditContext.setTarget(
            Target.builder().id(id).name(appEntity.getName()).type(TargetType.APPLICATION).build());
        return count > 0;
    }

    /**
     * 禁用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableApp(String id) {
        AppInfo appEntity = appRequireNonNull(id);
        Integer count = appInfoService.updateStatus(id, Boolean.FALSE);
        AuditContext.setTarget(
            Target.builder().id(id).name(appEntity.getName()).type(TargetType.APPLICATION).build());
        return count > 0;
    }

    /**
     * 更新应用配置
     *
     * @param param {@link AppSaveConfigParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveAppConfig(AppSaveConfigParam param) {
        ApplicationService applicationService = applicationServiceLoader
            .getApplicationService(param.getTemplate());
        applicationService.saveConfig(param.getId(), param.getConfig());
        AuditContext.setTarget(
            Target.builder().id(param.getId()).name("").type(TargetType.APPLICATION).build());
        return true;
    }

    /**
     * 获取应用配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    @Override
    public Object getAppConfig(String appId) {
        AppInfo app = appInfoService.findById(appId);
        if (app != null) {
            ApplicationService applicationService = applicationServiceLoader
                .getApplicationService(app.getTemplate());
            return applicationService.getConfig(appId);
        }
        throw new AppNotExistException();
    }

    /**
     * 查询并检查app是否为空，非空返回
     *
     * @param id {@link Long}
     * @return {@link AppInfo}
     */
    private AppInfo appRequireNonNull(String id) {
        AppInfo app = appInfoService.findById(id);
        if (app == null) {
            AuditContext.setContent("操作失败，应用不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        return app;
    }

    /**
     * ApplicationTemplateLoader
     */
    private final ApplicationServiceLoader      applicationServiceLoader;

    /**
     * ApplicationRepository
     */
    private final net.linlan.emis.app.service.AppInfoService  appInfoService;

    /**
     * ApplicationConverter
     */
    private final AppConverter                  appConverter;

    /**
     * AppGroupAssociationService
     */
    private final net.linlan.emis.app.service.AppGroupAssociationService appGroupAssociationService;
}
