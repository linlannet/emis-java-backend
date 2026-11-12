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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dto.AppPermissionRoleDto;
import net.linlan.emis.app.entity.AppPermissionRole;
import net.linlan.emis.app.param.AppPermissionRoleParam;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.openapi.converter.app.AppPermissionRoleConverter;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionRoleCreateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionRoleListQuery;
import net.linlan.emis.openapi.pojo.request.app.PermissionRoleUpdateParam;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionRoleListResult;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionRoleResult;
import net.linlan.emis.openapi.service.app.AppPermissionRoleService;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on 2020-08-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppPermissionRoleServiceImpl implements AppPermissionRoleService {

    /**
     * 获取所有角色（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppPermissionRoleListQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppPermissionRoleListResult> getPermissionRoleList(PageModel pageModel,
                                                                   AppPermissionRoleListQuery query) {
        AppPermissionRoleParam param = new AppPermissionRoleParam();
        if (Objects.nonNull(query)) {
            param.setName(query.getName());
            param.setCode(query.getCode());
            param.setIsEnabled(query.getEnabled());
        }
        param.setPage(pageModel.getCurrent() + 1);
        param.setLimit(pageModel.getPageSize());
        com.github.pagehelper.Page<AppPermissionRoleDto> page = appPermissionRoleService
            .getPageDto(param);
        return appPermissionRoleConverter.entityConvertToRolePaginationResult(page,
            page.getTotal(), page.getPageNum(), page.getPages());
    }

    /**
     * 创建角色
     *
     * @param param {@link AppPermissionRoleCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPermissionRole(AppPermissionRoleCreateParam param) {
        //判断名称是否存在
        if (!permissionRoleParamCheck(CheckValidityType.NAME, param.getName(), param.getAppId(),
            null)) {
            return Boolean.FALSE;
        }
        //判断编码是否存在
        if (!permissionRoleParamCheck(CheckValidityType.CODE, param.getCode(), param.getAppId(),
            null)) {
            return Boolean.FALSE;
        }
        AppPermissionRole entity = appPermissionRoleConverter
            .roleCreateParamConvertToEntity(param);
        appPermissionRoleService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.ROLE).build());
        return Boolean.TRUE;
    }

    /**
     * 更新角色
     *
     * @param param {@link PermissionRoleUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermissionRole(PermissionRoleUpdateParam param) {
        AppPermissionRole appPermissionRole = appPermissionRoleService.findById(param.getId());
        if (Objects.isNull(appPermissionRole)) {
            return Boolean.FALSE;
        }
        AppPermissionRole entity = appPermissionRoleConverter
            .roleUpdateParamConvertToEntity(param);
        if (!StringUtils.equals(entity.getName(), appPermissionRole.getName())) {
            //判断名称是否重复
            if (!permissionRoleParamCheck(CheckValidityType.NAME, entity.getName(),
                appPermissionRole.getAppId(), param.getId())) {
                return Boolean.FALSE;
            }
        }
        appPermissionRoleService.update(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.ROLE).build());
        return Boolean.TRUE;
    }

    /**
     * 删除角色
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermissionRole(String ids) {
        String[] idArray = StringUtils.split(ids, ",");
        appPermissionRoleService.deleteByIds(idArray);
        return Boolean.TRUE;
    }

    /**
     * 角色详情
     *
     * @param id {@link String}
     * @return {@link AppPermissionRoleResult}
     */
    @Override
    public AppPermissionRoleResult getPermissionRole(String id) {
        return appPermissionRoleConverter
            .entityConvertToRoleDetailResult(appPermissionRoleService.findById(id));
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
    public Boolean permissionRoleParamCheck(CheckValidityType type, String value, String appId,
                                            String id) {
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            AppPermissionRole entity = appPermissionRoleService.findById(id);
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
        return appPermissionRoleService.getCount(param.map()) == 0;
    }

    /**
     * 更新角色状态
     *
     * @param id     {@link String}
     * @param status {@link Boolean}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updatePermissionRoleStatus(String id, Boolean status) {
        AppPermissionRole appPermissionRole = appPermissionRoleService.findById(id);
        if (Objects.isNull(appPermissionRole)) {
            return Boolean.FALSE;
        }
        appPermissionRole.setIsEnabled(status);
        appPermissionRoleService.update(appPermissionRole);
        return Boolean.TRUE;
    }

    /**
     * AppPermissionRoleConverter
     */
    private final AppPermissionRoleConverter appPermissionRoleConverter;

    /**
     * AppPermissionRoleService
     */
    private final net.linlan.emis.app.service.AppPermissionRoleService appPermissionRoleService;
}
