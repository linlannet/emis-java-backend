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
package net.linlan.emis.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.app.dto.AppAccessPolicyDto;
import net.linlan.emis.app.entity.AppAccessPolicy;
import net.linlan.emis.app.param.AppAccessPolicyParam;
import net.linlan.emis.console.pojo.query.app.AppAccessPolicyQuery;
import net.linlan.emis.console.pojo.result.app.AppAccessPolicyResult;
import net.linlan.emis.console.pojo.save.app.AppAccessPolicyCreateParam;
import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.utils.SpringContextUtils;

/**
 * 应用授权策略 Converter
 *
 * @author Linlan
 * Created on  2022/6/4 21:57
 */
@Mapper(componentModel = "spring")
public interface AppAccessPolicyConverter {

    /**
     * 应用授权策略分页列表转换为应用授权策略分页结果
     *
     * @param page {@link AppAccessPolicy}
     * @return {@link AppAccessPolicyResult}
     */
    default Page<AppAccessPolicyResult> appPolicyEntityListConvertToAppPolicyResult(com.github.pagehelper.Page<AppAccessPolicyDto> page) {
        Page<AppAccessPolicyResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page)) {
            List<AppAccessPolicyResult> list = new ArrayList<>();
            for (AppAccessPolicyDto po : page) {
                AppAccessPolicyResult policy = entityConvertToAppPolicyResult(po);
                //图标未配置，所以先从模版中拿
                if (StringUtils.isBlank(policy.getAppIcon())) {
                    ApplicationService applicationService = getApplicationServiceLoader()
                        .getApplicationService(policy.getAppTemplate());
                    policy.setAppIcon(applicationService.getBase64Icon());
                }
                list.add(policy);
            }
            //@formatter:off
            result.setPagination(Page.Pagination.builder()
                    .total(page.getTotal())
                    .totalPages(page.getPages())
                    .current(page.getPageNum())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 应用授权策略实体转换为应用授权策略结果
     *
     * @param entity {@link AppAccessPolicyDto}
     * @return {@link AppAccessPolicy}
     */
    default AppAccessPolicyResult entityConvertToAppPolicyResult(AppAccessPolicyDto entity) {
        if (entity == null) {
            return null;
        }
        AppAccessPolicyResult result = new AppAccessPolicyResult();
        result.setId(entity.getId());
        result.setAppId(entity.getAppId());
        result.setAppName(entity.getAppName());
        result.setAppIcon(entity.getAppIcon());
        result.setAppTemplate(entity.getAppTemplate());
        result.setAppProtocol(
            entity.getAppProtocol() == null ? null : entity.getAppProtocol().name());
        result.setAppType(entity.getAppType() == null ? null : entity.getAppType().name());
        result.setSubjectId(entity.getSubjectId());
        result.setSubjectName(entity.getSubjectName());
        result.setSubjectType(
            net.linlan.emis.app.constant.AppPolicySubjectType.getType(entity.getSubjectType()));
        result.setEnabled(entity.getIsEnabled());
        result.setCreateTime(entity.getCreateTime() == null ? null
            : java.time.LocalDateTime.ofInstant(entity.getCreateTime().toInstant(),
                java.time.ZoneId.systemDefault()));
        return result;
    }

    /**
     * 应用授权策略添加参数转换为应用授权策略实体
     *
     * @param param {@link  AppAccessPolicyCreateParam}
     * @return {@link AppAccessPolicy}
     */
    default List<AppAccessPolicy> appPolicyCreateParamConvertToEntity(AppAccessPolicyCreateParam param) {
        if (param == null) {
            return new ArrayList<>();
        }
        List<AppAccessPolicy> list = new ArrayList<>();
        for (String subjectId : param.getSubjectIds()) {
            AppAccessPolicy entity = new AppAccessPolicy();
            entity.setAppId(param.getAppId());
            entity.setSubjectType(
                param.getSubjectType() == null ? null : param.getSubjectType().getKey());
            entity.setIsEnabled(true);
            entity.setSubjectId(subjectId);
            list.add(entity);
        }
        return list;
    }

    /**
     * 获取 ApplicationServiceLoader
     *
     * @return {@link ApplicationServiceLoader}
     */
    default ApplicationServiceLoader getApplicationServiceLoader() {
        return SpringContextUtils.getBean(ApplicationServiceLoader.class);
    }

    /**
     * 转查询参数
     *
     * @param query {@link AppAccessPolicyQuery}
     * @return {@link AppAccessPolicyQueryParam}
     */
    AppAccessPolicyParam appAccessPolicyQueryToQueryParam(AppAccessPolicyQuery query);

}
