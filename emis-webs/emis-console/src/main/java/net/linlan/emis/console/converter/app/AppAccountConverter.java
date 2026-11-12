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

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.app.dto.AppAccountDto;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.param.AppAccountParam;
import net.linlan.emis.console.pojo.query.app.AppAccountQuery;
import net.linlan.emis.console.pojo.result.app.AppAccountListResult;
import net.linlan.emis.console.pojo.save.app.AppAccountCreateParam;
import net.linlan.emis.support.repository.domain.Page;

/**
 * 应用账户映射
 *
 * @author Linlan
 * Created on  2022/6/4 21:08
 */
@Mapper(componentModel = "spring")
public interface AppAccountConverter {

    /**
     * 应用账户分页实体转换应用账户分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<AppAccountListResult> appAccountEntityConvertToAppAccountResult(com.github.pagehelper.Page<AppAccountDto> page) {
        Page<AppAccountListResult> result = new Page<>();
        if (!CollectionUtils.isEmpty(page)) {
            List<AppAccountListResult> list = new ArrayList<>();
            for (AppAccountDto po : page) {
                list.add(entityConvertToAppAccountResult(po));
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
     * 应用账户实体转换为应用账户结果
     *
     * @param appAccountPo {@link AppAccountDto}
     * @return {@link AppAccountListResult}
     */
    default AppAccountListResult entityConvertToAppAccountResult(AppAccountDto appAccountPo) {
        if (appAccountPo == null) {
            return null;
        }
        AppAccountListResult result = new AppAccountListResult();
        result.setId(appAccountPo.getId());
        result.setAppId(appAccountPo.getAppId());
        result.setAppName(appAccountPo.getAppName());
        result.setAppTemplate(appAccountPo.getAppTemplate());
        result.setAppProtocol(appAccountPo.getAppProtocol() == null ? null
            : appAccountPo.getAppProtocol().name());
        result.setAppType(
            appAccountPo.getAppType() == null ? null : appAccountPo.getAppType().name());
        result.setUserId(appAccountPo.getUserId());
        result.setUsername(appAccountPo.getUsername());
        result.setAccount(appAccountPo.getAccount());
        result.setCreateTime(appAccountPo.getCreateTime() == null ? null
            : java.time.LocalDateTime.ofInstant(appAccountPo.getCreateTime().toInstant(),
                java.time.ZoneId.systemDefault()));
        result.setDefaulted(appAccountPo.getIsDefault());
        return result;
    }

    /**
     * 应用账户新增参数转换应用账户实体
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link AppAccount}
     */

    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppAccount appAccountCreateParamConvertToEntity(AppAccountCreateParam param);

    /**
     * 查询参数转换
     *
     * @param query {@link AppAccountQuery}
     * @return {@link AppAccountQueryParam}
     */
    AppAccountParam appAccountQueryToQueryParam(AppAccountQuery query);
}
