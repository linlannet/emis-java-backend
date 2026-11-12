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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.app.constant.AppPolicySubjectType;
import net.linlan.emis.app.dto.AppAccessPolicyDto;
import net.linlan.emis.app.entity.AppAccessPolicy;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.param.AppAccessPolicyParam;
import net.linlan.emis.app.service.AppAccessPolicyService;
import net.linlan.emis.app.service.AppInfoService;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.app.AppAccessPolicyConverter;
import net.linlan.emis.console.pojo.query.app.AppAccessPolicyQuery;
import net.linlan.emis.console.pojo.result.app.AppAccessPolicyResult;
import net.linlan.emis.console.pojo.save.app.AppAccessPolicyCreateParam;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.organ.service.EmisOrganService;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.user.entity.UserGroup;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserGroupService;
import net.linlan.emis.user.service.UserInfoService;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 应用访问权限策略 Service
 *
 * @author Linlan
 * Created on  2022/6/4 21:56
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppAccessPolicyServiceImpl implements net.linlan.emis.console.service.app.AppAccessPolicyService {

    /**
     * 查询应用授权策略列表
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppAccessPolicyQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppAccessPolicyResult> getAppAccessPolicyList(PageModel pageModel,
                                                              AppAccessPolicyQuery query) {
        //@formatter:off
        AppAccessPolicyParam param = appAccessPolicyConverter.appAccessPolicyQueryToQueryParam(query);
        //@formatter:on
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        //查询映射
        com.github.pagehelper.Page<AppAccessPolicyDto> list = appAccessPolicyService
            .getPageDto(param);
        return appAccessPolicyConverter.appPolicyEntityListConvertToAppPolicyResult(list);
    }

    /**
     * 创建应用授权策略
     *
     * @param param {@link AppAccessPolicyCreateParam}
     * @return @{link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAppAccessPolicy(AppAccessPolicyCreateParam param) {
        List<AppAccessPolicy> list = appAccessPolicyConverter
            .appPolicyCreateParamConvertToEntity(param);
        //判断是否已经存在
        for (AppAccessPolicy policy : list) {
            AppAccessPolicy existing = appAccessPolicyService.findByParams(policy.getAppId(),
                policy.getSubjectId(), policy.getSubjectType());
            if (existing != null) {
                // 更新逻辑
                BeanUtils.merge(policy, existing, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
                appAccessPolicyService.update(existing);
                setAuditTarget(existing);
            } else {
                // 新增逻辑
                appAccessPolicyService.save(policy);
                setAuditTarget(policy);
            }
        }
        return true;
    }

    /**
     * 删除应用授权策略
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAppAccessPolicy(String id) {
        AppAccessPolicy entity = appAccessPolicyService.findById(id);
        //策略不存在
        if (entity == null) {
            AuditContext.setContent("删除失败，应用授权策略不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        appAccessPolicyService.deleteById(id);
        setAuditTarget(entity);
        return true;
    }

    /**
     * 启用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean enableAppAccessPolicy(String id) {
        AppAccessPolicy entity = appAccessPolicyService.findById(id);
        //策略不存在
        if (entity == null) {
            AuditContext.setContent("启用失败，应用授权策略不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        appAccessPolicyService.updateStatus(id, Boolean.TRUE);
        setAuditTarget(entity);
        return true;
    }

    /**
     * 禁用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableAppAccessPolicy(String id) {
        AppAccessPolicy entity = appAccessPolicyService.findById(id);
        //策略不存在
        if (entity == null) {
            AuditContext.setContent("禁用失败，应用授权策略不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        appAccessPolicyService.updateStatus(id, Boolean.FALSE);
        setAuditTarget(entity);
        return true;
    }

    private void setAuditTarget(AppAccessPolicy entity) {
        AppInfo appEntity = appInfoService.findById(entity.getAppId());
        if (appEntity != null) {
            AuditContext.addTarget(Target.builder().id(entity.getAppId()).name(appEntity.getName())
                .type(TargetType.APPLICATION).build());
        }

        AppPolicySubjectType subjectType = AppPolicySubjectType.getType(entity.getSubjectType());
        if (subjectType == null) {
            return;
        }
        switch (subjectType) {
            case USER:
                UserInfo userEntity = userInfoService.findById(entity.getSubjectId());
                if (userEntity != null) {
                    AuditContext.addTarget(Target.builder().id(entity.getSubjectId())
                        .name(userEntity.getUsername()).type(TargetType.USER).build());
                }
                break;
            case USER_GROUP:
                UserGroup groupEntity = userGroupService.findById(entity.getSubjectId());
                if (groupEntity != null) {
                    AuditContext.addTarget(Target.builder().id(entity.getSubjectId())
                        .name(groupEntity.getName()).type(TargetType.USER_GROUP).build());
                }
                break;
            case ORGANIZATION:
                EmisOrganVo organizationEntity = organService
                    .getVoById(Long.valueOf(entity.getSubjectId()));
                if (organizationEntity != null) {
                    AuditContext.addTarget(Target.builder().id(entity.getSubjectId())
                        .name(organizationEntity.getName()).type(TargetType.ORGANIZATION).build());
                }
                break;
            default:
                break;
        }
    }

    /**
     * AppPolicyConverter
     */
    private final AppAccessPolicyConverter appAccessPolicyConverter;

    /**
     * AppAccessPolicyService
     */
    private final AppAccessPolicyService    appAccessPolicyService;

    /**
     * UserInfoService
     */
    private final UserInfoService           userInfoService;

    /**
     * EmisOrganService
     */
    private final EmisOrganService          organService;

    /**
     * UserGroupService
     */
    private final UserGroupService          userGroupService;

    /**
     * AppInfoService
     */
    private final AppInfoService            appInfoService;

}
