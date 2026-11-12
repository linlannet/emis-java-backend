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
package net.linlan.emis.protocol.oidc;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.jwk.RSAKey;

import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.protocol.base.AbstractCertApplicationService;
import net.linlan.emis.protocol.base.exception.AppCertNotExistException;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.protocol.dto.ProtocolOidcConfigDto;
import net.linlan.emis.protocol.oidc.model.OidcProtocolConfig;
import net.linlan.emis.protocol.repository.*;
import static net.linlan.emis.app.constant.AppCertUsingType.OIDC_JWK;
import static net.linlan.emis.support.utils.CertUtils.readPrivateKey;
import static net.linlan.emis.support.utils.CertUtils.readPublicKey;

/**
 * OIDC 应用配置
 *
 * @author Linlan
 * Created on 2022/8/23 21:58
 */
public abstract class AbstractOidcApplicationService extends AbstractCertApplicationService
                                                     implements OidcApplicationService {

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
        //删除OIDC配置
        appOidcConfigRepository.deleteByAppId(appId);
    }

    /**
     * 获取协议配置
     *
     * @param appCode {@link String}
     * @return {@link OidcProtocolConfig}
     */
    @Override
    public OidcProtocolConfig getProtocolConfig(String appCode) {
        ProtocolOidcConfigDto appConfig = appOidcConfigRepository.findByAppCode(appCode);
        if (Objects.isNull(appConfig)) {
            throw new AppNotExistException();
        }
        Optional<AppCertification> appCertOptional = appCertRepository
            .findByAppIdAndUsingType(appConfig.getAppId(), OIDC_JWK);
        if (appCertOptional.isPresent()) {
            throw new AppCertNotExistException();
        }
        AppCertification appCert = appCertOptional.get();
        //@formatter:off
        try {

            PrivateKey rsaPrivateKey = readPrivateKey(appCert.getPrivateKey(), "");
            RSAPublicKey rsaPublicKey = (RSAPublicKey) readPublicKey(appCert.getPublicKey(), "");

            RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
                    .privateKey(rsaPrivateKey)
                    .keyID(appCert.getId())
                    .build();

            return OidcProtocolConfig.builder()
                    .appId(appConfig.getAppId())
                    .appName(appConfig.getAppName())
                    .clientId(appConfig.getClientId())
                    .clientSecret(appConfig.getClientSecret())
                    .appCode(appConfig.getAppCode())
                    .appTemplate(appConfig.getAppTemplate())
                    .clientAuthMethods(appConfig.getClientAuthMethods())
                    .authGrantTypes(appConfig.getAuthGrantTypes())
                    .responseTypes(appConfig.getResponseTypes())
                    .redirectUris(appConfig.getRedirectUris())
                    .postLogoutRedirectUris(appConfig.getPostLogoutRedirectUris())
                    .grantScopes(appConfig.getGrantScopes())
                    .requireAuthConsent(appConfig.getRequireAuthConsent())
                    .requireProofKey(appConfig.getRequireProofKey())
                    .tokenEndpointAuthSigningAlgorithm(appConfig.getTokenEndpointAuthSigningAlgorithm())
                    .refreshTokenTimeToLive(java.time.Duration.ofSeconds(appConfig.getRefreshTokenTimeToLive()))
                    .authorizationCodeTimeToLive(java.time.Duration.ofSeconds(appConfig.getAuthorizationCodeTimeToLive()))
                    .deviceCodeTimeToLive(java.time.Duration.ofSeconds(appConfig.getDeviceCodeTimeToLive()))
                    .idTokenSignatureAlgorithm(appConfig.getIdTokenSignatureAlgorithm())
                    .idTokenTimeToLive(java.time.Duration.ofSeconds(appConfig.getIdTokenTimeToLive()))
                    .accessTokenFormat(appConfig.getAccessTokenFormat())
                    .accessTokenTimeToLive(java.time.Duration.ofSeconds(appConfig.getAccessTokenTimeToLive()))
                    .reuseRefreshToken(appConfig.getReuseRefreshToken())
                    .jwks(Collections.singletonList(rsaKey))
                    .configured(appConfig.getConfigured())
                    .build();

            //@formatter:on
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to select the JWK(s) -> " + ex.getMessage(),
                ex);
        }

    }

    /**
     * AppCertRepository
     */
    protected final AppCertRepository       appCertRepository;
    /**
     * ApplicationRepository
     */
    protected final AppRepository           appRepository;
    /**
     * AppOidcConfigRepository
     */
    protected final AppOidcConfigRepository appOidcConfigRepository;

    protected AbstractOidcApplicationService(AppCertRepository appCertRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppAccessPolicyRepository appAccessPolicyRepository,
                                             AppRepository appRepository,
                                             AppOidcConfigRepository appOidcConfigRepository) {
        super(appCertRepository, appGroupAssociationRepository, appAccountRepository,
            appAccessPolicyRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appRepository = appRepository;
        this.appOidcConfigRepository = appOidcConfigRepository;
    }
}
