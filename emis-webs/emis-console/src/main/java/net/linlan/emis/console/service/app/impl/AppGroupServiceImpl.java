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
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dto.AppGroupAssociationDto;
import net.linlan.emis.app.dto.AppGroupDto;
import net.linlan.emis.app.dto.AppInfoDto;
import net.linlan.emis.app.entity.AppGroup;
import net.linlan.emis.app.param.AppGroupAssociationParam;
import net.linlan.emis.app.param.AppGroupParam;
import net.linlan.emis.app.service.AppGroupAssociationService;
import net.linlan.emis.app.service.AppInfoService;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.app.AppConverter;
import net.linlan.emis.console.converter.app.AppGroupConverter;
import net.linlan.emis.console.pojo.query.app.AppGroupAssociationListQuery;
import net.linlan.emis.console.pojo.query.app.AppGroupListQuery;
import net.linlan.emis.console.pojo.result.app.AppGroupGetResult;
import net.linlan.emis.console.pojo.result.app.AppGroupListResult;
import net.linlan.emis.console.pojo.result.app.AppListResult;
import net.linlan.emis.console.pojo.save.app.AppGroupCreateParam;
import net.linlan.emis.console.pojo.update.app.AppGroupUpdateParam;
import net.linlan.emis.console.service.app.AppGroupService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.support.exception.InfoValidityFailException;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * AppGroupServiceImpl
 *
 * @author Linlan
 * Created on  2023/8/31 14:23
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppGroupServiceImpl implements AppGroupService {

    /**
     * 获取应用分组（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppGroupListQuery}
     * @return {@link AppGroupListResult}
     */
    @Override
    public net.linlan.emis.support.repository.domain.Page<AppGroupListResult> getAppGroupList(PageModel pageModel, AppGroupListQuery query) {
        AppGroupParam param = appGroupConverter.appGroupQueryToQueryParam(query);
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        //查询映射
        Page<AppGroupDto> list = appGroupService.getPageDto(param);
        return appGroupConverter.entityConvertToAppGroupListResult(list);
    }

    /**
     * 创建应用分组
     *
     * @param param {@link AppGroupCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAppGroup(AppGroupCreateParam param) {
        if (StringUtils.isNotBlank(param.getName())) {
            Boolean validityName = appGroupParamCheck(CheckValidityType.NAME, param.getName(),
                null);
            if (!validityName) {
                throw new InfoValidityFailException("分组名称已存在");
            }
        }
        AppGroup entity = appGroupConverter.appGroupCreateParamConvertToEntity(param);
        entity.setType(net.linlan.emis.app.constant.AppGroupType.CUSTOM.getKey());
        appGroupService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.APP_GROUP).build());
        return true;
    }

    /**
     * 修改应用分组
     *
     * @param param {@link AppGroupUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public boolean updateAppGroup(AppGroupUpdateParam param) {
        if (StringUtils.isNotBlank(param.getName())) {
            Boolean validityName = appGroupParamCheck(CheckValidityType.NAME, param.getName(),
                param.getId());
            if (!validityName) {
                throw new InfoValidityFailException("分组名称已存在");
            }
        }
        AppGroup appGroup = appGroupRequireNonNull(param.getId());
        AppGroup entity = appGroupConverter.appGroupUpdateParamConverterToEntity(param);
        BeanUtils.merge(entity, appGroup, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        appGroupService.update(appGroup);
        AuditContext.setTarget(Target.builder().id(param.getId()).name(param.getName())
            .type(TargetType.APP_GROUP).build());
        return true;
    }

    /**
     * 删除应用分组
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAppGroup(String id) {
        AppGroup entity = appGroupRequireNonNull(id);
        appGroupService.deleteById(id);
        appGroupAssociationService.deleteAllByGroupId(id);
        AuditContext.setTarget(
            Target.builder().id(id).name(entity.getName()).type(TargetType.APP_GROUP).build());
        return true;
    }

    /**
     * 获取单个应用分组详情
     *
     * @param id {@link Long}
     * @return {@link AppGroup}
     */
    @Override
    public AppGroupGetResult getAppGroup(String id) {
        AppGroup optional = appGroupService.findById(id);
        if (optional != null) {
            AppGroup entity = optional;
            return appGroupConverter.entityConvertToAppGroupResult(entity);
        }
        return null;

    }

    /**
     * 查询并检查分组是否为空，非空返回
     *
     * @param id {@link Long}
     * @return {@link AppGroup}
     */
    private AppGroup appGroupRequireNonNull(String id) {
        AppGroup optional = appGroupService.findById(id);
        if (optional == null) {
            AuditContext.setContent("操作失败，应用分组不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        return optional;
    }

    /**
     * 批量移除应用
     *
     * @param appIds {@link String}
     * @param id     {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean batchRemoveAssociation(String id, List<String> appIds) {
        AppGroup optional = appGroupService.findById(id);
        //用户组不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，应用组不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        appIds.forEach(appId -> appGroupAssociationService.deleteByGroupIdAndAppId(id, appId));
        List<Target> targets = new ArrayList<>(appIds.stream()
            .map(i -> Target.builder().id(i).name("批量移除应用").type(TargetType.APPLICATION).build())
            .collect(Collectors.toList()));
        targets.add(Target.builder().id(id).name("批量移除应用").type(TargetType.APP_GROUP).build());
        AuditContext.setTarget(targets);
        return true;
    }

    /**
     * 获取应用组内应用列表
     *
     * @param query {@link AppGroupAssociationListQuery}
     * @return {@link AppListResult}
     */
    @Override
    public net.linlan.emis.support.repository.domain.Page<AppListResult> getAppGroupAssociationList(PageModel model,
                                                          AppGroupAssociationListQuery query) {
        //@formatter:off
        AppGroupAssociationParam param = appConverter.appGroupAssociationListQueryToQueryParam(query);
        //@formatter:on
        param.setPage(model.getCurrent());
        param.setLimit(model.getPageSize());
        Page<AppGroupAssociationDto> page = appGroupAssociationService.getPageDto(param);
        Page<AppInfoDto> appPage = new Page<>(page.getPageNum(), page.getPageSize());
        appPage.setTotal(page.getTotal());
        for (AppGroupAssociationDto association : page) {
            AppInfoDto appInfo = appInfoService.getDtoById(association.getAppId());
            if (appInfo != null) {
                appPage.add(appInfo);
            }
        }
        return appConverter.entityConvertToAppListResult(appPage);
    }

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean}
     */
    private Boolean appGroupParamCheck(CheckValidityType type, String value, String id) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        //取出前后空格
        value = value.trim();
        AppGroup entity = new AppGroup();
        boolean result = false;
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            entity = appGroupService.findById(id);
            if (entity == null) {
                entity = new AppGroup();
            }
        }
        //分组名称
        if (CheckValidityType.NAME.equals(type)) {
            if (StringUtils.equals(entity.getName(), value)) {
                return true;
            }
            result = appGroupService
                .getList(new StringMap().put("name", value).map()).isEmpty();
        }
        return result;
    }

    /**
     * 应用分组服务
     */
    private final net.linlan.emis.app.service.AppGroupService appGroupService;

    /**
     * AppGroupConverter
     */
    private final AppGroupConverter             appGroupConverter;

    private final AppConverter                  appConverter;

    /**
     * 应用分组关联服务
     */
    private final AppGroupAssociationService    appGroupAssociationService;

    /**
     * 应用信息
     */
    private final AppInfoService                appInfoService;
}
