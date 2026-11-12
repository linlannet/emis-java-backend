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
package net.linlan.emis.openapi.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.app.constant.AppPolicyEffect;
import net.linlan.emis.app.constant.AppPolicyObjectType;
import net.linlan.emis.app.constant.AppPolicySubjectType;
import net.linlan.emis.app.dto.AppPermissionPolicyDto;
import net.linlan.emis.app.entity.AppPermissionPolicy;
import net.linlan.emis.app.param.AppPermissionPolicyParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionPolicyCreateParam;
import net.linlan.emis.openapi.pojo.request.app.AppPermissionPolicyUpdateParam;
import net.linlan.emis.openapi.pojo.response.app.AppPermissionPolicyGetResult;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.Page.Pagination;

/**
 * 策略映射
 *
 * @author Linlan
 * Created on 2020/8/21 11:41
 */
@Component
public class AppPermissionPolicyConverter {

    /**
     * 策略创建参数转实体
     *
     * @param param {@link AppPermissionPolicyCreateParam}
     * @return {@link AppPermissionPolicy}
     */
    public AppPermissionPolicy policyCreateParamConvertToEntity(
        AppPermissionPolicyCreateParam param) {
        if (param == null) {
            return null;
        }
        AppPermissionPolicy entity = new AppPermissionPolicy();
        entity.setAppId(param.getAppId() == null ? null : String.valueOf(param.getAppId()));
        entity.setSubjectId(param.getSubjectId());
        entity.setSubjectType(
            param.getSubjectType() == null ? null : param.getSubjectType().getKey());
        entity.setObjectId(param.getObjectId() == null ? null : String.valueOf(param.getObjectId()));
        entity.setObjectType(
            param.getObjectType() == null ? null : param.getObjectType().getKey());
        entity.setEffect(param.getEffect() == null ? null : param.getEffect().getKey());
        return entity;
    }

    /**
     * 策略修改参数转实体
     *
     * @param param {@link AppPermissionPolicyUpdateParam}
     * @return {@link AppPermissionPolicy}
     */
    public AppPermissionPolicy policyUpdateParamConvertToEntity(
        AppPermissionPolicyUpdateParam param) {
        if (param == null) {
            return null;
        }
        AppPermissionPolicy entity = new AppPermissionPolicy();
        entity.setId(param.getId() == null ? null : String.valueOf(param.getId()));
        entity.setAppId(param.getAppId() == null ? null : String.valueOf(param.getAppId()));
        entity.setSubjectId(param.getSubjectId());
        entity.setSubjectType(
            param.getSubjectType() == null ? null : param.getSubjectType().getKey());
        entity.setObjectId(param.getObjectId() == null ? null : String.valueOf(param.getObjectId()));
        entity.setObjectType(
            param.getObjectType() == null ? null : param.getObjectType().getKey());
        entity.setEffect(param.getEffect() == null ? null : param.getEffect().getKey());
        return entity;
    }

    /**
     * 实体转获取详情返回
     *
     * @param policy {@link AppPermissionPolicy}
     * @return {@link AppPermissionPolicyGetResult}
     */
    public AppPermissionPolicyGetResult entityConvertToPolicyGetResult(
        AppPermissionPolicy policy) {
        if (policy == null) {
            return null;
        }
        AppPermissionPolicyGetResult result = new AppPermissionPolicyGetResult();
        result.setId(policy.getId());
        result.setSubjectId(policy.getSubjectId());
        result.setSubjectType(policy.getSubjectType() == null ? null
            : AppPolicySubjectType.getType(policy.getSubjectType()));
        result.setObjectId(policy.getObjectId() == null ? null : Long.valueOf(policy.getObjectId()));
        result.setObjectType(policy.getObjectType() == null ? null
            : AppPolicyObjectType.getType(policy.getObjectType()));
        result.setEffect(policy.getEffect() == null ? null
            : AppPolicyEffect.getType(policy.getEffect()));
        return result;
    }

    /**
     * 分页实体转换为分页查询参数
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    public Page<AppPermissionPolicyParam> entityConvertToPolicyParamPage(
        com.github.pagehelper.Page<AppPermissionPolicyDto> page, long total, int current,
        int totalPages) {
        Page<AppPermissionPolicyParam> result = new Page<>();
        if (page != null && !CollectionUtils.isEmpty(page.getResult())) {
            List<AppPermissionPolicyParam> list = new ArrayList<>();
            for (AppPermissionPolicyDto policy : page.getResult()) {
                AppPermissionPolicyParam param = new AppPermissionPolicyParam();
                param.setSubjectId(policy.getSubjectId());
                param.setSubjectType(policy.getSubjectType());
                param.setObjectId(policy.getObjectId());
                param.setObjectType(policy.getObjectType());
                param.setEffect(policy.getEffect());
                list.add(param);
            }
            //@formatter:off
            result.setPagination(Pagination.builder()
                    .total(total)
                    .totalPages(totalPages)
                    .current(current)
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

}
