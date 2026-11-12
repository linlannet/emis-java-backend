/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.app.constant.AuthorizationType;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.entity.AppGroupAssociation;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.core.exception.app.AppAccountNotExistException;
import net.linlan.emis.protocol.repository.AppAccountRepository;
import net.linlan.emis.protocol.repository.AppGroupAssociationRepository;
import net.linlan.emis.protocol.repository.AppRepository;

/**
 * AbstractApplicationService
 *
 * @author Linlan
 * Created on 2022/8/31 22:34
 */
@Slf4j
public abstract class AbstractApplicationService implements ApplicationService {

    protected final ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取默认应用账户
     *
     * @param appId {@link String}
     * @param userId {@link String}
     * @return {@link AppAccount} AppAccount
     */
    @Override
    public AppAccount getDefaultAppAccount(String appId, String userId) {
        AppAccount entity = appAccountRepository
            .findByAppIdAndUserIdAndDefaultedIsTrue(appId, userId)
            .orElseThrow(AppAccountNotExistException::new);
        AppAccount account = new AppAccount();
        account.setAppId(entity.getAppId());
        account.setPassword(entity.getPassword());
        account.setAccount(entity.getAccount());
        return account;
    }

    /**
     * 创建应用
     *
     * @param name              {@link String}
     * @param icon              {@link String}
     * @param remark            {@link String}
     * @param authorizationType {@link AuthorizationType}
     * @return {@link AppInfo}
     */
    @Override
    public AppInfo createApp(String name, String icon, String remark,
                             AuthorizationType authorizationType) {
        AppInfo appEntity = new AppInfo();
        appEntity.setName(name);
        appEntity.setIcon(icon);
        appEntity.setCode(RandomStringUtils.randomAlphanumeric(32).toLowerCase());
        appEntity.setTemplate(getCode());
        appEntity.setType(getType().getKey());
        appEntity.setIsEnabled(true);
        appEntity.setIsConfigured(false);
        appEntity.setProtocol(getProtocol().getKey());
        appEntity.setClientId(idGenerator.generateId().toString().replace("-", ""));
        appEntity.setClientSecret(idGenerator.generateId().toString().replace("-", ""));
        appEntity.setAuthorizationType(authorizationType.getKey());
        appEntity.setDescription(remark);
        return appRepository.save(appEntity);
    }

    @Override
    public AppInfo createApp(String name, String icon, String remark, List<String> groups,
                             AuthorizationType authorizationType) {

        AppInfo appEntity = createApp(name, icon, remark, authorizationType);
        List<AppGroupAssociation> list = new ArrayList<>();
        for (String id : groups) {
            AppGroupAssociation appGroupAssociationEntity = new AppGroupAssociation();
            appGroupAssociationEntity.setGroupId(id);
            appGroupAssociationEntity.setAppId(appEntity.getId());
            list.add(appGroupAssociationEntity);
        }
        appGroupAssociationRepository.saveAll(list);
        return appEntity;
    }

    /**
     * AppAccountRepository
     */
    protected final AppAccountRepository          appAccountRepository;

    /**
     * ApplicationRepository
     */
    protected final AppRepository                 appRepository;

    /**
     * AppGroupAssociationRepository
     */
    protected final AppGroupAssociationRepository appGroupAssociationRepository;

    /**
     * IdGenerator
     */
    protected final IdGenerator                   idGenerator;

    protected AbstractApplicationService(AppAccountRepository appAccountRepository,
                                         AppGroupAssociationRepository appGroupAssociationRepository,
                                         AppRepository appRepository) {
        this.appAccountRepository = appAccountRepository;
        this.appGroupAssociationRepository = appGroupAssociationRepository;
        this.appRepository = appRepository;
        this.idGenerator = new AlternativeJdkIdGenerator();
    }
}
