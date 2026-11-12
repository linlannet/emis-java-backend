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
package net.linlan.emis.openapi.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.app.dto.AppAccountDto;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.openapi.pojo.result.AppAccountListResult;
import net.linlan.emis.openapi.pojo.save.AppAccountCreateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.Page.Pagination;

/**
 * 应用账户映射
 *
 * @author Linlan
 * Created on 2022/6/4 19:08
 */
@Component
public class AppAccountConverter {

    /**
     * 应用账户分页实体转换应用账户分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    public Page<AppAccountListResult> appAccountDtoConvertToAppAccountResult(
        com.github.pagehelper.Page<AppAccountDto> page, long total, int current, int totalPages) {
        Page<AppAccountListResult> result = new Page<>();
        if (page != null && !CollectionUtils.isEmpty(page.getResult())) {
            List<AppAccountListResult> list = new ArrayList<>();
            for (AppAccountDto po : page.getResult()) {
                list.add(entityConvertToAppAccountResult(po));
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

    /**
     * 应用账户实体转换为应用账户结果
     *
     * @param appAccountPo {@link AppAccountDto}
     * @return {@link AppAccountListResult}
     */
    public AppAccountListResult entityConvertToAppAccountResult(AppAccountDto appAccountPo) {
        if (appAccountPo == null) {
            return null;
        }
        AppAccountListResult result = new AppAccountListResult();
        result.setId(appAccountPo.getId());
        result.setAppId(appAccountPo.getAppId());
        result.setAppName(appAccountPo.getAppName());
        result.setAppTemplate(appAccountPo.getAppTemplate());
        result.setAppProtocol(
            appAccountPo.getAppProtocol() == null ? null : appAccountPo.getAppProtocol().name());
        result.setAppType(appAccountPo.getAppType() == null ? null : appAccountPo.getAppType().name());
        result.setUserId(appAccountPo.getUserId());
        result.setUsername(appAccountPo.getUsername());
        result.setAccount(appAccountPo.getAccount());
        result.setCreateTime(dateToLocalDateTime(appAccountPo.getCreateTime()));
        return result;
    }

    /**
     * 应用账户新增参数转换应用账户实体
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link AppAccount}
     */
    public AppAccount appAccountCreateParamConvertToEntity(AppAccountCreateParam param) {
        if (param == null) {
            return null;
        }
        AppAccount entity = new AppAccount();
        entity.setAppId(param.getAppId());
        entity.setUserId(param.getUserId());
        entity.setAccount(param.getAccount());
        entity.setIsDefault(param.getDefaulted());
        return entity;
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
