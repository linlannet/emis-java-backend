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

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.app.constant.AppPolicyObjectType;
import net.linlan.emis.app.constant.AppPolicySubjectType;
import net.linlan.emis.app.dto.AppPermissionPolicyDto;
import net.linlan.emis.app.entity.AppPermissionPolicy;
import net.linlan.emis.app.param.AppPermissionPolicyParam;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.openapi.converter.app.AppPermissionPolicyConverter;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionPolicyCreateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionPolicyUpdateParam;
import net.linlan.emis.openapi.pojo.request.app.OpenApiPolicyQuery;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionPolicyGetResult;
import net.linlan.emis.openapi.service.app.AppPermissionPolicyService;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 策略表 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on 2020-08-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppPermissionPolicyServiceImpl implements AppPermissionPolicyService {

    /**
     * 获取策略列表
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link OpenApiPolicyQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppPermissionPolicyParam> getPermissionPolicyList(PageModel pageModel,
                                                                 OpenApiPolicyQuery query) {
        AppPermissionPolicyParam param = new AppPermissionPolicyParam();
        if (Objects.nonNull(query)) {
            param.setSubjectId(query.getSubjectId());
            param.setSubjectType(query.getSubjectType());
            param.setObjectId(query.getObjectId());
            param.setObjectType(query.getObjectType());
            param.setEffect(query.getEffect());
        }
        param.setPage(pageModel.getCurrent() + 1);
        param.setLimit(pageModel.getPageSize());
        com.github.pagehelper.Page<AppPermissionPolicyDto> page = appPermissionPolicyService
            .getPageDto(param);
        return appPermissionPolicyConverter.entityConvertToPolicyParamPage(page, page.getTotal(),
            page.getPageNum(), page.getPages());
    }

    /**
     * 获取策略
     *
     * @param id {@link String}
     * @return {@link AppPermissionPolicyGetResult}
     */
    @Override
    public AppPermissionPolicyGetResult getPermissionPolicy(String id) {
        AppPermissionPolicy policy = appPermissionPolicyService.findById(id);
        if (Objects.isNull(policy)) {
            return null;
        }
        AppPermissionPolicyGetResult result = appPermissionPolicyConverter
            .entityConvertToPolicyGetResult(policy);
        //主体名称
        if (AppPolicySubjectType.ROLE.getKey().equals(policy.getSubjectType())) {
            net.linlan.emis.app.entity.AppPermissionRole role = appPermissionRoleService
                .findById(policy.getSubjectId());
            if (role != null) {
                result.setSubjectName(role.getName());
            }
        }
        //客体名称
        if (AppPolicyObjectType.RESOURCE.getKey().equals(policy.getObjectType())) {
            net.linlan.emis.app.entity.AppPermissionResource resource = appPermissionResourceService
                .findById(policy.getObjectId());
            if (resource != null) {
                result.setObjectName(resource.getName());
            }
        }
        if (AppPolicyObjectType.PERMISSION.getKey().equals(policy.getObjectType())) {
            net.linlan.emis.app.entity.AppPermissionAction action = appPermissionActionService
                .findById(policy.getObjectId());
            if (action != null) {
                result.setObjectName(action.getName());
            }
        }
        return result;
    }

    /**
     * 删除策略
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deletePermissionPolicy(String id) {
        AppPermissionPolicy policy = appPermissionPolicyService.findById(id);
        if (Objects.isNull(policy)) {
            return Boolean.FALSE;
        }
        appPermissionPolicyService.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name("删除策略")
            .type(TargetType.APP_PERMISSION_POLICY).build());
        return Boolean.TRUE;
    }

    /**
     * 创建策略
     *
     * @param param {@link AppPermissionPolicyCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createPermissionPolicy(AppPermissionPolicyCreateParam param) {
        AppPermissionPolicy entity = appPermissionPolicyConverter
            .policyCreateParamConvertToEntity(param);
        appPermissionPolicyService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name("新增策略")
            .type(TargetType.APP_PERMISSION_POLICY).build());
        return Boolean.TRUE;
    }

    /**
     * 更新策略
     *
     * @param param {@link AppPermissionPolicyUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePermissionPolicy(AppPermissionPolicyUpdateParam param) {
        AppPermissionPolicy policy = appPermissionPolicyService
            .findById(String.valueOf(param.getId()));
        if (Objects.isNull(policy)) {
            return Boolean.FALSE;
        }
        AppPermissionPolicy entity = appPermissionPolicyConverter
            .policyUpdateParamConvertToEntity(param);
        appPermissionPolicyService.update(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name("修改策略")
            .type(TargetType.APP_PERMISSION_POLICY).build());
        return Boolean.TRUE;
    }

    /**
     * AppPermissionPolicyConverter
     */
    private final AppPermissionPolicyConverter appPermissionPolicyConverter;

    /**
     * AppPermissionPolicyService
     */
    private final net.linlan.emis.app.service.AppPermissionPolicyService appPermissionPolicyService;

    /**
     * AppPermissionRoleService
     */
    private final net.linlan.emis.app.service.AppPermissionRoleService appPermissionRoleService;

    /**
     * AppPermissionResourceService
     */
    private final net.linlan.emis.app.service.AppPermissionResourceService appPermissionResourceService;

    /**
     * AppPermissionActionService
     */
    private final net.linlan.emis.app.service.AppPermissionActionService appPermissionActionService;
}
