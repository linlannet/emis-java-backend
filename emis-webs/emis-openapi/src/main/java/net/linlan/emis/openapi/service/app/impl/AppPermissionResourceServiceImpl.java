/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.service.app.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dto.AppPermissionResourceDto;
import net.linlan.emis.app.entity.AppPermissionAction;
import net.linlan.emis.app.entity.AppPermissionResource;
import net.linlan.emis.app.param.AppPermissionResourceParam;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.openapi.converter.app.AppPermissionResourceConverter;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionResourceCreateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionResourceUpdateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionsActionParam;
import net.linlan.emis.openapi.pojo.request.app.AppResourceListQuery;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionResourceGetResult;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionResourceListResult;
import net.linlan.emis.openapi.service.app.AppPermissionResourceService;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 资源权限 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on 2020-08-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppPermissionResourceServiceImpl implements AppPermissionResourceService {

    /**
     * 获取资源列表
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppResourceListQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppPermissionResourceListResult> getPermissionResourceList(PageModel pageModel,
                                                                          AppResourceListQuery query) {
        AppPermissionResourceParam param = new AppPermissionResourceParam();
        if (Objects.nonNull(query)) {
            param.setName(query.getName());
            param.setIsEnabled(query.getEnabled());
        }
        param.setPage(pageModel.getCurrent() + 1);
        param.setLimit(pageModel.getPageSize());
        com.github.pagehelper.Page<AppPermissionResourceDto> page = appPermissionResourceService
            .getPageDto(param);
        return appPermissionResourceConverter.entityConvertToResourceListResult(page,
            page.getTotal(), page.getPageNum(), page.getPages());
    }

    /**
     * 获取资源
     *
     * @param id {@link String}
     * @return {@link AppPermissionResourceGetResult}
     */
    @Override
    public AppPermissionResourceGetResult getPermissionResource(String id) {
        AppPermissionResource appPermissionResource = appPermissionResourceService.findById(id);
        List<AppPermissionAction> actions = new ArrayList<>();
        if (appPermissionResource != null) {
            actions = appPermissionActionService.findAllByResourceId(id);
        }
        return appPermissionResourceConverter
            .entityConvertToResourceGetResult(appPermissionResource, actions);
    }

    /**
     * 删除资源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePermissionResource(String id) {
        AppPermissionResource appPermissionResource = appPermissionResourceService.findById(id);
        if (Objects.isNull(appPermissionResource)) {
            return Boolean.FALSE;
        }
        List<AppPermissionAction> appPermissionActions = appPermissionActionService
            .findAllByResourceId(id);
        List<String> objectIdList = new ArrayList<>();
        objectIdList.add(id);
        if (!CollectionUtils.isEmpty(appPermissionActions)) {
            appPermissionActions.forEach(appPermissionAction -> objectIdList
                .add(appPermissionAction.getId()));
        }
        //删除该资源所有策略
        appPermissionPolicyService.deleteAllByObjectIdIn(objectIdList.toArray(new String[0]));
        //删除该资源所有动作
        if (!CollectionUtils.isEmpty(appPermissionActions)) {
            appPermissionActionService.deleteByIds(
                appPermissionActions.stream().map(AppPermissionAction::getId).toArray(String[]::new));
        }
        appPermissionResourceService.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name(appPermissionResource.getName())
            .type(TargetType.APP_PERMISSION_RESOURCE).build());
        return Boolean.TRUE;
    }

    /**
     * 创建资源
     *
     * @param param {@link AppPermissionResourceCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPermissionResource(AppPermissionResourceCreateParam param) {
        //判断名称是否存在
        if (!permissionResourceParamCheck(CheckValidityType.NAME, param.getName(),
            String.valueOf(param.getAppId()), null)) {
            return Boolean.FALSE;
        }
        //判断编码是否存在
        if (!permissionResourceParamCheck(CheckValidityType.CODE, param.getCode(),
            String.valueOf(param.getAppId()), null)) {
            return Boolean.FALSE;
        }
        AppPermissionResource entity = appPermissionResourceConverter
            .resourceCreateParamConvertToEntity(param);
        appPermissionResourceService.save(entity);
        List<AppPermissionAction> actionEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(param.getActions())) {
            for (AppPermissionsActionParam action : param.getActions()) {
                AppPermissionAction actionEntity = new AppPermissionAction();
                actionEntity.setResourceId(entity.getId());
                actionEntity.setType(action.getType() == null ? null : action.getType().getKey());
                actionEntity.setContent(action.getValue());
                actionEntity.setName(action.getName());
                actionEntities.add(actionEntity);
            }
            appPermissionActionService.batchSave(actionEntities);
        }
        List<Target> targets = new ArrayList<>();
        targets.add(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.APP_PERMISSION_RESOURCE).build());
        actionEntities.forEach(action -> targets.add(Target.builder().id(action.getId())
            .name(action.getName()).type(TargetType.APP_PERMISSION_RESOURCE).build()));
        AuditContext.setTarget(targets.toArray(new Target[0]));
        return Boolean.TRUE;
    }

    /**
     * 更新资源
     *
     * @param param {@link AppPermissionResourceUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePermissionResource(AppPermissionResourceUpdateParam param) {
        AppPermissionResource entity = appPermissionResourceConverter
            .resourceUpdateParamConvertToEntity(param);
        AppPermissionResource appPermissionResource = appPermissionResourceService
            .findById(param.getId());
        if (Objects.isNull(appPermissionResource)) {
            return Boolean.FALSE;
        }
        if (!entity.getName().equals(appPermissionResource.getName())) {
            //判断名称是否重复
            if (!permissionResourceParamCheck(CheckValidityType.NAME, entity.getName(),
                appPermissionResource.getAppId(), param.getId())) {
                return Boolean.FALSE;
            }
        }
        List<AppPermissionAction> actionEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(param.getActions())) {
            for (AppPermissionsActionParam action : param.getActions()) {
                AppPermissionAction actionEntity = new AppPermissionAction();
                actionEntity.setResourceId(param.getId());
                actionEntity.setType(action.getType() == null ? null : action.getType().getKey());
                actionEntity.setContent(action.getValue());
                actionEntity.setName(action.getName());
                actionEntities.add(actionEntity);
            }
        }
        //移除原有动作及其策略
        List<AppPermissionAction> appPermissionActions = appPermissionActionService
            .findAllByResourceId(param.getId());
        if (!CollectionUtils.isEmpty(appPermissionActions)) {
            List<String> actionIds = appPermissionActions.stream().map(AppPermissionAction::getId)
                .collect(java.util.stream.Collectors.toList());
            appPermissionPolicyService.deleteAllByObjectIdIn(actionIds.toArray(new String[0]));
            appPermissionActionService.deleteByIds(actionIds.toArray(new String[0]));
        }
        if (!actionEntities.isEmpty()) {
            appPermissionActionService.batchSave(actionEntities);
        }
        appPermissionResourceService.update(entity);
        AuditContext.setTarget(Target.builder().id(param.getId()).name(entity.getName())
            .type(TargetType.APP_PERMISSION_RESOURCE).build());
        return Boolean.TRUE;
    }

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param appId {@link String}
     * @param id    {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean permissionResourceParamCheck(CheckValidityType type, String value, String appId,
                                                String id) {
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            AppPermissionResource entity = appPermissionResourceService.findById(id);
            if (Objects.isNull(entity)) {
                return Boolean.FALSE;
            }
            if (CheckValidityType.NAME.equals(type) && value.equals(entity.getName())) {
                return Boolean.TRUE;
            }
            if (CheckValidityType.CODE.equals(type) && value.equals(entity.getCode())) {
                return Boolean.TRUE;
            }
        }
        StringMap param = new StringMap();
        param.put("appId", appId);
        if (CheckValidityType.NAME.equals(type)) {
            param.put("name", value);
        }
        if (CheckValidityType.CODE.equals(type)) {
            param.put("code", value);
        }
        return appPermissionResourceService.getCount(param.map()) == 0;
    }

    /**
     * AppPermissionResourceConverter
     */
    private final AppPermissionResourceConverter          appPermissionResourceConverter;

    /**
     * AppPermissionResourceService
     */
    private final net.linlan.emis.app.service.AppPermissionResourceService appPermissionResourceService;

    /**
     * AppPermissionActionService
     */
    private final net.linlan.emis.app.service.AppPermissionActionService appPermissionActionService;

    /**
     * AppPermissionPolicyService
     */
    private final net.linlan.emis.app.service.AppPermissionPolicyService appPermissionPolicyService;
}
