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

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.app.dto.AppAccountDto;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.param.AppAccountParam;
import net.linlan.emis.app.service.AppAccountService;
import net.linlan.emis.app.service.AppInfoService;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.app.AppAccountConverter;
import net.linlan.emis.console.pojo.query.app.AppAccountQuery;
import net.linlan.emis.console.pojo.result.app.AppAccountListResult;
import net.linlan.emis.console.pojo.save.app.AppAccountCreateParam;
import net.linlan.emis.core.encrypt.EncryptContextHelper;
import net.linlan.emis.core.exception.app.AppAccountExistException;
import net.linlan.emis.core.exception.app.AppAccountNotExistException;
import net.linlan.emis.core.exception.app.AppDefaultAccountExistException;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserInfoService;

/**
 * 应用账户
 *
 * @author Linlan
 * Created on  2022/6/4 21:07
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppAccountServiceImpl
    implements net.linlan.emis.console.service.app.AppAccountService {

    /**
     * 查询应用账户
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link  AppAccountQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppAccountListResult> getAppAccountList(PageModel pageModel,
                                                        AppAccountQuery query) {
        AppAccountParam param = appAccountConverter.appAccountQueryToQueryParam(query);
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        //查询映射
        com.github.pagehelper.Page<AppAccountDto> list = appAccountService.getPageDto(param);
        return appAccountConverter.appAccountEntityConvertToAppAccountResult(list);
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
        List<AppAccount> appAccounts = appAccountService
            .findByAppIdAndUserId(param.getAppId(), SecurityUtils.getCurrentUserId());
        if (!CollectionUtils.isEmpty(appAccounts)) {
            if (appAccounts.stream()
                .anyMatch(appAccount -> appAccount.getAccount().equals(param.getAccount()))) {
                throw new AppAccountExistException();
            }
            if (param.getDefaulted()
                && appAccounts.stream().anyMatch(AppAccount::getIsDefault)) {
                throw new AppDefaultAccountExistException();
            }
        }
        AppAccount entity = appAccountConverter.appAccountCreateParamConvertToEntity(param);
        //密码不为空
        if (!StringUtils.isBlank(param.getPassword())) {
            Base64 base64 = new Base64();
            String password = new String(base64.decode(param.getPassword()),
                StandardCharsets.UTF_8);
            entity.setPassword(EncryptContextHelper.encrypt(password));
        }
        appAccountService.save(entity);
        setAuditTarget(entity);
        return true;
    }

    private void setAuditTarget(AppAccount entity) {
        AppInfo appEntity = appInfoService.findById(entity.getAppId());
        UserInfo userEntity = userInfoService.findById(entity.getUserId());
        if (appEntity != null) {
            AuditContext.addTarget(Target.builder().id(entity.getAppId()).name(appEntity.getName())
                .type(TargetType.APPLICATION).build());
        }
        if (userEntity != null) {
            AuditContext.addTarget(Target.builder().id(entity.getUserId())
                .name(userEntity.getUsername()).type(TargetType.USER).build());
        }
    }

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link String}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAppAccount(String id) {
        AppAccount entity = appAccountService.findById(id);
        //管理员不存在
        if (entity == null) {
            AuditContext.setContent("删除失败，应用账户不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        appAccountService.deleteById(id);
        setAuditTarget(entity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAppAccountDefault(String id, Boolean defaulted) {
        AppAccount updateEntity = appAccountService.findById(id);
        if (updateEntity == null) {
            throw new AppAccountNotExistException();
        }
        if (defaulted.equals(updateEntity.getIsDefault())
            || (!defaulted && updateEntity.getIsDefault() == null)) {
            return Boolean.TRUE;
        }
        if (defaulted) {
            List<AppAccount> appAccounts = appAccountService
                .findByAppIdAndUserId(updateEntity.getAppId(), updateEntity.getUserId());
            if (!CollectionUtils.isEmpty(appAccounts)
                && appAccounts.stream().anyMatch(AppAccount::getIsDefault)) {
                throw new AppDefaultAccountExistException();
            }
        }
        updateEntity.setIsDefault(Boolean.FALSE.equals(defaulted) ? null : Boolean.TRUE);
        appAccountService.update(updateEntity);
        setAuditTarget(updateEntity);
        return Boolean.TRUE;
    }

    /**
     * AppAccountConverter
     */
    private final AppAccountConverter appAccountConverter;

    /**
     * AppAccountService
     */
    private final AppAccountService    appAccountService;

    /**
     * AppInfoService
     */
    private final AppInfoService       appInfoService;

    /**
     * UserInfoService
     */
    private final UserInfoService      userInfoService;
}
