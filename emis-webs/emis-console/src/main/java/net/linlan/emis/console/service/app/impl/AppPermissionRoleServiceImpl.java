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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dto.AppPermissionRoleDto;
import net.linlan.emis.app.entity.AppPermissionRole;
import net.linlan.emis.app.param.AppPermissionRoleParam;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.app.AppPermissionRoleConverter;
import net.linlan.emis.console.pojo.query.app.AppPermissionRoleListQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleListResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionRoleCreateParam;
import net.linlan.emis.console.pojo.update.app.PermissionRoleUpdateParam;
import net.linlan.emis.console.service.app.AppPermissionRoleService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.core.exception.app.AppRoleNotExistException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
@Service
@RequiredArgsConstructor
public class AppPermissionRoleServiceImpl implements AppPermissionRoleService {

    /**
     * 获取所有角色（分页）
     *
     * @param page {@link PageModel}
     * @return {@link AppPermissionRoleListResult}
     */
    @Override
    public Page<AppPermissionRoleListResult> getPermissionRoleList(PageModel page,
                                                                   AppPermissionRoleListQuery query) {
        AppPermissionRoleParam param = new AppPermissionRoleParam();
        param.setName(query.getName());
        param.setCode(query.getCode());
        param.setAppId(query.getAppId());
        param.setPage(page.getCurrent() + 1);
        param.setLimit(page.getPageSize());
        com.github.pagehelper.Page<AppPermissionRoleDto> data = appPermissionRoleService
            .getPageDto(param);
        return appPermissionRoleConverter.entityConvertToRolePaginationResult(data);
    }

    /**
     * 创建系统
     *
     * @param param {@link AppPermissionRoleCreateParam}
     * @return {@link Boolean}
     */
    @Override
    public boolean createPermissionRole(AppPermissionRoleCreateParam param) {
        AppPermissionRole entity = appPermissionRoleConverter
            .roleCreateParamConvertToEntity(param);
        appPermissionRoleService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.APP_PERMISSION_ROLE).build());
        return true;
    }

    /**
     * @param param {@link PermissionRoleUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public boolean updatePermissionRole(PermissionRoleUpdateParam param) {
        AppPermissionRole source = appPermissionRoleConverter
            .roleUpdateParamConvertToEntity(param);
        AppPermissionRole target = appPermissionRoleService.findById(param.getId());
        if (target == null) {
            throw new AppRoleNotExistException();
        }
        BeanUtils.merge(source, target, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        appPermissionRoleService.update(target);
        AuditContext.setTarget(Target.builder().id(target.getId()).name(target.getName())
            .type(TargetType.APP_PERMISSION_ROLE).build());
        return true;
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
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        appPermissionRoleService.deleteByIds(idList.toArray(new String[0]));
        // 删除对应策略
        appPermissionPolicyService.deleteAllBySubjectIdIn(idList.toArray(new String[0]));
        appPermissionPolicyService.deleteAllByObjectIdIn(idList.toArray(new String[0]));
        AuditContext.setTarget(
            Target.builder().id(ids).name("删除角色").type(TargetType.APP_PERMISSION_ROLE).build());
        return true;
    }

    /**
     * 角色详情
     *
     * @param id {@link String}
     * @return {@link AppPermissionRoleResult}
     */
    @Override
    public AppPermissionRoleResult getPermissionRole(String id) {
        //查询
        AppPermissionRole entity = appPermissionRoleService.findById(id);
        //映射
        return appPermissionRoleConverter.entityConvertToRoleDetailResult(entity);
    }

    /**
     * 参数有效性验证
     *
     * @param type     {@link CheckValidityType}
     * @param value    {@link String}
     * @param id       {@link String}
     * @param appId {@link String}
     * @return {@link Boolean}
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public Boolean permissionRoleParamCheck(CheckValidityType type, String value, String appId,
                                            String id) {
        AppPermissionRole entity = new AppPermissionRole();
        boolean result = false;
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            entity = appPermissionRoleService.findById(id);
            if (entity == null) {
                throw new AppRoleNotExistException();
            }
        }
        //角色编码
        if (CheckValidityType.CODE.equals(type)) {
            if (StringUtils.equals(entity.getCode(), value)) {
                return true;
            }
            result = appPermissionRoleService.getList(
                new StringMap().put("code", value).put("appId", appId).map()).isEmpty();
        }
        //角色名称
        if (CheckValidityType.NAME.equals(type)) {
            if (StringUtils.equals(entity.getName(), value)) {
                return true;
            }
            result = appPermissionRoleService.getList(
                new StringMap().put("name", value).put("appId", appId).map()).isEmpty();
        }
        return result;
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
        appPermissionRoleService.updateStatus(id, status);
        return true;
    }

    /**
     * 用户数据映射器
     */
    private final AppPermissionRoleConverter    appPermissionRoleConverter;
    /**
     * RoleService
     */
    private final net.linlan.emis.app.service.AppPermissionRoleService   appPermissionRoleService;
    /**
     * PolicyService
     */
    private final net.linlan.emis.app.service.AppPermissionPolicyService appPermissionPolicyService;
}
