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
package net.linlan.emis.protocol.cas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.emis.protocol.base.AbstractCertApplicationService;
import net.linlan.emis.protocol.constant.CasSsoModel;
import net.linlan.emis.protocol.dto.ProtocolCasConfigDto;
import net.linlan.emis.protocol.repository.*;

/**
 * CAS 应用配置
 *
 * @author Linlan
 * Created on  2022/8/23 20:58
 */
public abstract class AbstractCasApplicationService extends AbstractCertApplicationService
                                                    implements CasApplicationService {

    private static final Logger logger = LoggerFactory
        .getLogger(AbstractCasApplicationService.class);

    /**
     * 删除应用
     *
     * @param appId {@link String} 应用ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String appId) {
        //删除应用
        appRepository.deleteById(appId);
        //删除证书
        appCertRepository.deleteByAppId(appId);
        //删除应用账户
        appAccountRepository.deleteAllByAppId(appId);
        //删除应用权限策略
        appAccessPolicyRepository.deleteAllByAppId(appId);
        //删除配置
        appCasConfigRepository.deleteByAppId(appId);
    }

    @Override
    public CasSsoModel getSsoModel(String appId) {
        ProtocolCasConfigDto appCasConfigPO = appCasConfigRepository.getByAppId(appId);
        return CasSsoModel.builder().ssoCallbackUrl(appCasConfigPO.getSpCallbackUrl()).build();
    }

    /**
     * AppCertRepository
     */
    protected final AppCertRepository      appCertRepository;
    /**
     * ApplicationRepository
     */
    protected final AppRepository          appRepository;
    /**
     * AppCasConfigRepository
     */
    protected final AppCasConfigRepository appCasConfigRepository;

    protected AbstractCasApplicationService(AppCertRepository appCertRepository,
                                            AppGroupAssociationRepository appGroupAssociationRepository,
                                            AppAccountRepository appAccountRepository,
                                            AppAccessPolicyRepository appAccessPolicyRepository,
                                            AppRepository appRepository,
                                            AppCasConfigRepository appCasConfigRepository) {
        super(appCertRepository, appGroupAssociationRepository, appAccountRepository,
            appAccessPolicyRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appRepository = appRepository;
        this.appCasConfigRepository = appCasConfigRepository;
    }

}
