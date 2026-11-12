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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.app.dto.AppPermissionPolicyDto;
import net.linlan.emis.app.entity.AppPermissionPolicy;
import net.linlan.emis.app.param.AppPermissionPolicyParam;
import net.linlan.emis.console.converter.app.AppPermissionPolicyConverter;
import net.linlan.emis.console.pojo.query.app.AppPolicyQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionPolicyGetResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionPolicyListResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionPolicyCreateParam;
import net.linlan.emis.console.pojo.update.app.AppPermissionPolicyUpdateParam;
import net.linlan.emis.console.service.app.AppPermissionPolicyService;
import net.linlan.emis.core.exception.app.AppPolicyNotExistException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 权限策略 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
@Service
@RequiredArgsConstructor
public class AppPermissionPolicyServiceImpl implements AppPermissionPolicyService {

    /**
     * 获取策略列表
     *
     * @param page  {@link PageModel}
     * @param query {@link AppPolicyQuery}
     * @return {@link AppPermissionPolicyListResult}
     */
    @Override
    public Page<AppPermissionPolicyListResult> getPermissionPolicyList(PageModel page,
                                                                       AppPolicyQuery query) {
        AppPermissionPolicyParam param = new AppPermissionPolicyParam();
        param.setAppId(query.getAppId());
        param.setSubjectId(query.getSubjectId());
        param.setSubjectType(query.getSubjectType());
        param.setObjectId(query.getObjectId());
        param.setObjectType(query.getObjectType());
        param.setEffect(query.getEffect());
        param.setPage(page.getCurrent() + 1);
        param.setLimit(page.getPageSize());
        com.github.pagehelper.Page<AppPermissionPolicyDto> data = appPermissionPolicyService
            .getPageDto(param);
        return appPermissionPolicyConverter.entityConvertToPolicyListResult(data);
    }

    /**
     * 获取策略
     *
     * @param id {@link String}
     * @return {@link AppPermissionPolicyGetResult}
     */
    @Override
    public AppPermissionPolicyGetResult getPermissionPolicy(String id) {
        return null;
    }

    /**
     * 删除策略
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean deletePermissionPolicy(String id) {
        String policyId = String.valueOf(id);
        AppPermissionPolicy policy = appPermissionPolicyService.findById(policyId);
        if (policy == null) {
            throw new AppPolicyNotExistException();
        }
        appPermissionPolicyService.deleteById(policyId);
        return true;
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
        AppPermissionPolicy resource = appPermissionPolicyConverter
            .policyCreateParamConvertToEntity(param);
        // 新增策略
        appPermissionPolicyService.save(resource);
        return true;
    }

    /**
     * 更新策略
     *
     * @param param {@link AppPermissionPolicyUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updatePermissionPolicy(AppPermissionPolicyUpdateParam param) {
        AppPermissionPolicy resource = appPermissionPolicyConverter
            .policyUpdateParamConvertToEntity(param);
        // 更新策略
        appPermissionPolicyService.update(resource);
        return null;
    }

    private final AppPermissionPolicyConverter  appPermissionPolicyConverter;

    private final net.linlan.emis.app.service.AppPermissionPolicyService appPermissionPolicyService;
}
