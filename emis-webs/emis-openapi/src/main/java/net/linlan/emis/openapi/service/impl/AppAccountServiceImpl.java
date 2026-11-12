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
package net.linlan.emis.openapi.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.app.dto.AppAccountDto;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.param.AppAccountParam;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.core.exception.app.AppAccountExistException;
import net.linlan.emis.core.exception.app.AppDefaultAccountExistException;
import net.linlan.emis.openapi.converter.AppAccountConverter;
import net.linlan.emis.openapi.pojo.query.OapiV1AppAccountQuery;
import net.linlan.emis.openapi.pojo.result.AppAccountListResult;
import net.linlan.emis.openapi.pojo.save.AppAccountCreateParam;
import net.linlan.emis.openapi.service.AppAccountService;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.security.util.SecurityUtils;

/**
 * 应用账户
 *
 * @author Linlan
 * Created on 2022/6/4 19:07
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppAccountServiceImpl implements AppAccountService {

    /**
     * 查询应用账户
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link  OapiV1AppAccountQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppAccountListResult> getAppAccountList(PageModel pageModel,
                                                        OapiV1AppAccountQuery query) {
        AppAccountParam param = new AppAccountParam();
        if (Objects.nonNull(query)) {
            param.setAppId(query.getAppId());
            param.setUserId(query.getUserId());
            param.setUsername(query.getUsername());
            param.setAccount(query.getAccount());
            param.setAppName(query.getAppName());
        }
        param.setPage(pageModel.getCurrent() + 1);
        param.setLimit(pageModel.getPageSize());
        com.github.pagehelper.Page<AppAccountDto> page = appAccountService.getPageDto(param);
        return appAccountConverter.appAccountDtoConvertToAppAccountResult(page, page.getTotal(),
            page.getPageNum(), page.getPages());
    }

    /**
     * 新增应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAppAccount(AppAccountCreateParam param) {
        List<AppAccount> appAccounts = appAccountService.findByAppIdAndUserId(param.getAppId(),
            SecurityUtils.getCurrentUserId());
        if (!CollectionUtils.isEmpty(appAccounts)) {
            if (appAccounts.stream()
                .anyMatch(appAccount -> appAccount.getAccount().equals(param.getAccount()))) {
                throw new AppAccountExistException();
            }
            if (Boolean.TRUE.equals(param.getDefaulted()) && appAccounts.stream()
                .anyMatch(appAccount -> Boolean.TRUE.equals(appAccount.getIsDefault()))) {
                throw new AppDefaultAccountExistException();
            }
        }
        AppAccount entity = appAccountConverter.appAccountCreateParamConvertToEntity(param);
        appAccountService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name("新增应用账户")
            .type(TargetType.APPLICATION_ACCOUNT).build());
        return true;
    }

    /**
     * 删除应用账户
     *
     * @param id {@link Long}
     * @return {@link String}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAppAccount(String id) {
        AppAccount appAccount = appAccountService.findById(id);
        //管理员不存在
        if (Objects.isNull(appAccount)) {
            AuditContext.setContent("删除失败，应用账户不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        appAccountService.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name(appAccount.getAccount())
            .type(TargetType.APPLICATION_ACCOUNT).build());
        return true;
    }

    /**
     * AppAccountConverter
     */
    private final AppAccountConverter appAccountConverter;

    /**
     * AppAccountService
     */
    private final net.linlan.emis.app.service.AppAccountService appAccountService;
}
