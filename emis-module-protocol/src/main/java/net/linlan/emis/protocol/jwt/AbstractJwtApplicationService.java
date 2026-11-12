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
package net.linlan.emis.protocol.jwt;

import java.util.Objects;
import java.util.Optional;

import net.linlan.emis.app.constant.AppCertUsingType;
import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.protocol.base.AbstractCertApplicationService;
import net.linlan.emis.protocol.base.exception.AppCertNotExistException;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.protocol.constant.JwtBindingType;
import net.linlan.emis.protocol.constant.JwtIdTokenSubjectType;
import net.linlan.emis.protocol.dto.ProtocolJwtConfigDto;
import net.linlan.emis.protocol.jwt.model.JwtProtocolConfig;
import net.linlan.emis.protocol.repository.*;

/**
 * JWT 应用配置
 *
 * @author Linlan
 * Created on 2022/8/23 21:58
 */
public abstract class AbstractJwtApplicationService extends AbstractCertApplicationService
                                                    implements JwtApplicationService {

    /**
     * AppCertRepository
     */
    protected final AppCertRepository      appCertRepository;
    /**
     * ApplicationRepository
     */
    protected final AppRepository          appRepository;

    protected final AppJwtConfigRepository appJwtConfigRepository;

    @Override
    public void delete(String appId) {
        //删除应用
        appRepository.deleteById(appId);
        //删除应用账户
        appAccountRepository.deleteAllByAppId(appId);
        // 删除应用配置
        appJwtConfigRepository.deleteByAppId(appId);
        // 删除证书
        appCertRepository.deleteByAppId(appId);
    }

    @Override
    public JwtProtocolConfig getProtocolConfig(String appCode) {
        ProtocolJwtConfigDto configPo = appJwtConfigRepository.findByAppCode(appCode);
        if (Objects.isNull(configPo)) {
            throw new AppNotExistException();
        }
        Optional<AppCertification> entity = appCertRepository
            .findByAppIdAndUsingType(configPo.getAppId(), AppCertUsingType.JWT_ENCRYPT);
        if (!entity.isPresent()) {
            throw new AppCertNotExistException();
        }

        JwtProtocolConfig.JwtProtocolConfigBuilder<?, ?> jwtProtocolConfig = JwtProtocolConfig
            .builder();

        //@formatter:off
        jwtProtocolConfig.appId(String.valueOf(configPo.getAppId()))
            .clientId(configPo.getClientId())
            .appName(configPo.getAppName())
            .clientSecret(configPo.getClientSecret())
            .appCode(configPo.getAppCode())
            .appTemplate(configPo.getAppTemplate())
            .redirectUrl(configPo.getRedirectUrl())
            .targetLinkUrl(configPo.getTargetLinkUrl())
            .bindingType(JwtBindingType.getType(configPo.getBindingType()))
            .idTokenTimeToLive(String.valueOf(configPo.getIdTokenTimeToLive()))
            .idTokenSubjectType(JwtIdTokenSubjectType.getType(configPo.getIdTokenSubjectType()))
            .configured(configPo.getConfigured());

        entity.ifPresent(appCert -> {
            jwtProtocolConfig.jwtPrivateKey(appCert.getPrivateKey());
            jwtProtocolConfig.jwtPublicKey(appCert.getPublicKey());
        });
        //@formatter:on
        return jwtProtocolConfig.build();
    }

    protected AbstractJwtApplicationService(AppJwtConfigRepository appJwtConfigRepository,
                                            AppGroupAssociationRepository appGroupAssociationRepository,
                                            AppCertRepository appCertRepository,
                                            AppRepository appRepository,
                                            AppAccountRepository appAccountRepository,
                                            AppAccessPolicyRepository appAccessPolicyRepository) {
        super(appCertRepository, appGroupAssociationRepository, appAccountRepository,
            appAccessPolicyRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appRepository = appRepository;
        this.appJwtConfigRepository = appJwtConfigRepository;
    }
}
