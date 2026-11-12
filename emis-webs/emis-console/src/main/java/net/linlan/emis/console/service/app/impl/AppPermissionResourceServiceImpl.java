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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dto.AppPermissionResourceDto;
import net.linlan.emis.app.entity.AppPermissionAction;
import net.linlan.emis.app.entity.AppPermissionResource;
import net.linlan.emis.app.param.AppPermissionResourceParam;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.app.AppPermissionResourceConverter;
import net.linlan.emis.console.pojo.query.app.AppResourceListQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionResourceGetResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionResourceListResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionResourceCreateParam;
import net.linlan.emis.console.pojo.save.app.AppPermissionsActionParam;
import net.linlan.emis.console.pojo.update.app.AppPermissionResourceUpdateParam;
import net.linlan.emis.console.service.app.AppPermissionResourceService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.core.exception.app.AppResourceNotExistException;
import net.linlan.emis.support.exception.BadParamsException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * <p>
 * 资源权限 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
@Service
@RequiredArgsConstructor
public class AppPermissionResourceServiceImpl implements AppPermissionResourceService {

    /**
     * 获取资源列表
     *
     * @param page  {@link PageModel}
     * @param query {@link AppResourceListQuery}
     * @return {@link AppPermissionResourceListResult}
     */
    @Override
    public Page<AppPermissionResourceListResult> getPermissionResourceList(PageModel page,
                                                                           AppResourceListQuery query) {
        AppPermissionResourceParam param = new AppPermissionResourceParam();
        param.setName(query.getName());
        param.setAppId(query.getAppId());
        param.setPage(page.getCurrent() + 1);
        param.setLimit(page.getPageSize());
        com.github.pagehelper.Page<AppPermissionResourceDto> data = appResourceService
            .getPageDto(param);
        return appPermissionResourceConverter.entityConvertToResourceListResult(data);
    }

    /**
     * 获取资源
     *
     * @param id {@link String}
     * @return {@link AppPermissionResourceGetResult}
     */
    @Override
    public AppPermissionResourceGetResult getPermissionResource(String id) {
        AppPermissionResource resource = appResourceService.findById(id);
        if (resource == null) {
            throw new AppResourceNotExistException();
        }
        return appPermissionResourceConverter.entityConvertToResourceGetResult(resource);
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
        String resourceId = String.valueOf(id);
        AppPermissionResource resource = appResourceService.findById(resourceId);
        if (resource == null) {
            throw new AppResourceNotExistException();
        }
        List<AppPermissionAction> actionList = appPermissionActionService
            .findAllByResourceId(resourceId);
        List<String> objectIdList = new ArrayList<>(
            actionList.stream().map(AppPermissionAction::getId).collect(Collectors.toList()));
        objectIdList.add(resourceId);
        appPermissionPolicyService.deleteAllByObjectIdIn(objectIdList.toArray(new String[0]));
        appResourceService.deleteById(resourceId);
        AuditContext.setTarget(
            Target.builder().id(id).name("删除资源").type(TargetType.APP_PERMISSION_RESOURCE).build());
        return true;
    }

    /**
     * 启用/禁用
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateStatus(String id, boolean enabled) {
        AppPermissionResource resource = appResourceService.findById(String.valueOf(id));
        if (resource == null) {
            throw new AppResourceNotExistException();
        }
        AuditContext.setTarget(Target.builder().id(id).name(resource.getName())
            .type(TargetType.APP_PERMISSION_RESOURCE).build());
        return appResourceService.updateStatus(id, enabled) > 0;
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
        AppPermissionResource resource = appPermissionResourceConverter
            .resourceCreateParamConvertToEntity(param);
        buildActions(param.getActions(), resource);
        // 新增资源
        appResourceService.save(resource);
        for (AppPermissionAction action : resource.getActions()) {
            action.setResourceId(resource.getId());
        }
        if (!resource.getActions().isEmpty()) {
            appPermissionActionService.batchSave(resource.getActions());
        }
        AuditContext.setTarget(Target.builder().id(resource.getId()).name(resource.getName())
            .type(TargetType.APP_PERMISSION_RESOURCE).build());
        return true;
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
        AppPermissionResource resource = appPermissionResourceConverter
            .resourceUpdateParamConvertToEntity(param);
        AppPermissionResource entity = getAppPermissionResourceEntity(
            String.valueOf(param.getId()));
        buildActions(param.getActions(), resource);
        BeanUtils.merge(resource, entity, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        // 查询资源下所有权限
        List<AppPermissionAction> actionList = appPermissionActionService
            .findAllByResourceId(String.valueOf(param.getId()));
        // 取出未删除的权限id
        Set<String> reservedSet = resource.getActions().stream()
            .map(AppPermissionAction::getId).collect(Collectors.toSet());
        // 过滤要删除的权限id
        List<String> removeActions = actionList.stream()
            .filter(item -> !reservedSet.contains(item.getId()))
            .map(AppPermissionAction::getId).collect(Collectors.toList());
        appPermissionActionService.deleteByIds(removeActions.toArray(new String[0]));
        // 更新资源
        appResourceService.update(entity);
        for (AppPermissionAction action : resource.getActions()) {
            action.setResourceId(String.valueOf(param.getId()));
            if (action.getId() == null) {
                appPermissionActionService.save(action);
            } else {
                appPermissionActionService.update(action);
            }
        }
        AuditContext.setTarget(Target.builder().id(param.getId()).name(param.getName())
            .type(TargetType.APP_PERMISSION_RESOURCE).build());
        return true;
    }

    /**
     * 获取应用权限资源
     *
     * @param id {@link String}
     * @return {@link AppPermissionResource}
     */
    private AppPermissionResource getAppPermissionResourceEntity(String id) {
        AppPermissionResource resource = appResourceService.findById(id);
        if (resource == null) {
            throw new BadParamsException("应用权限资源不存在");
        }
        return resource;
    }

    /**
     * 参数有效性验证
     *
     * @param type     {@link CheckValidityType}
     * @param value    {@link String}
     * @param appId {@link String}
     * @param id       {@link String}
     * @return {@link Boolean}
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public Boolean permissionResourceParamCheck(CheckValidityType type, String value, String appId,
                                                String id) {
        AppPermissionResource entity = new AppPermissionResource();
        boolean result = false;
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            entity = appResourceService.findById(id);
            if (entity == null) {
                throw new AppResourceNotExistException();
            }
        }
        //资源名称
        if (CheckValidityType.NAME.equals(type)) {
            if (StringUtils.equals(entity.getName(), value)) {
                return true;
            }
            result = appResourceService.getList(
                new StringMap().put("name", value).put("appId", appId).map()).isEmpty();
        }
        //资源编码
        if (CheckValidityType.CODE.equals(type)) {
            if (StringUtils.equals(entity.getCode(), value)) {
                return true;
            }
            result = appResourceService.getList(
                new StringMap().put("code", value).put("appId", appId).map()).isEmpty();
        }
        return result;
    }

    /**
     * 批量处理actions
     *
     * @param permissions {@link List< AppPermissionsActionParam >}
     * @param resource {@link AppPermissionResource>}
     */
    private void buildActions(List<AppPermissionsActionParam> permissions,
                              AppPermissionResource resource) {
        // 权限
        List<AppPermissionAction> list = new ArrayList<>();
        for (AppPermissionsActionParam p : permissions) {
            AppPermissionAction entity = new AppPermissionAction();
            entity.setResourceId(resource.getId());
            entity.setType(p.getType() == null ? null : p.getType().getKey());
            entity.setName(p.getName());
            //API需要单独处理
            entity.setContent(p.getValue());
            list.add(entity);
        }
        resource.setActions(list);
    }

    private final AppPermissionResourceConverter  appPermissionResourceConverter;

    private final net.linlan.emis.app.service.AppPermissionResourceService appResourceService;
    /**
     * PolicyService
     */
    private final net.linlan.emis.app.service.AppPermissionPolicyService   appPermissionPolicyService;
    /**
     * ActionService
     */
    private final net.linlan.emis.app.service.AppPermissionActionService   appPermissionActionService;
}
