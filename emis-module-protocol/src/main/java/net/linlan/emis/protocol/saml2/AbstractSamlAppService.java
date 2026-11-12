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
package net.linlan.emis.protocol.saml2;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.app.constant.AppCertUsingType;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.core.exception.app.AppAccountNotExistException;
import net.linlan.emis.protocol.base.AbstractCertApplicationService;
import net.linlan.emis.protocol.base.exception.AppCertNotExistException;
import net.linlan.emis.protocol.constant.Saml2ProtocolConfig;
import net.linlan.emis.protocol.constant.Saml2SsoModel;
import net.linlan.emis.protocol.constant.SamlAttributeStatementValueType;
import net.linlan.emis.protocol.constant.SamlNameIdValueType;
import net.linlan.emis.protocol.dto.ProtocolSaml2ConfigDto;
import net.linlan.emis.protocol.entity.ProtocolSaml2Config;
import net.linlan.emis.protocol.repository.*;
import net.linlan.emis.protocol.util.SamlKeyStoreProvider;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserInfoService;
import net.linlan.utils.SpringContextUtils;
import static net.linlan.emis.protocol.constant.SamlNameIdValueType.*;
import static net.linlan.emis.user.utils.UserUtils.getUser;

/**
 * SAML 应用配置
 *
 * @author Linlan
 * Created on  2022/8/23 20:58
 */
public abstract class AbstractSamlAppService extends AbstractCertApplicationService
                                             implements Saml2ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSamlAppService.class);

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
        //删除SAML2配置
        appSaml2ConfigRepository.deleteByAppId(appId);
    }

    /**
     * 获取SAML2 协议配置
     *
     * @param appId {@link String}
     * @return {@link Saml2ProtocolConfig}
     */
    @Override
    public Saml2ProtocolConfig getProtocolConfig(String appId) {
        //根据提供商ID查询配置
        ProtocolSaml2ConfigDto saml2Config = appSaml2ConfigRepository.getByAppId(appId);
        Optional<AppCertification> signCert = appCertRepository.findByAppIdAndUsingType(appId,
            AppCertUsingType.SAML_SIGN);
        Optional<AppCertification> encryptCert = appCertRepository.findByAppIdAndUsingType(appId,
            AppCertUsingType.SAML_ENCRYPT);
        if (!signCert.isPresent()) {
            logger.error("SAML2 应用证书不存在 [{}] ", appId);
            throw new AppCertNotExistException();
        }
        //构建配置
        Saml2ProtocolConfig config = getSamlAppConverter()
            .appSaml2ConfigPoToSaml2ProtocolConfig(saml2Config);
        //签名证书
        signCert
            .ifPresent(appCertEntity -> config.setIdpSignCert(SamlKeyStoreProvider.getCredential(
                StringUtils.defaultString(saml2Config.getSpEntityId(),
                    UUID.randomUUID().toString()),
                appCertEntity.getPrivateKey(), appCertEntity.getCertification(), "")));
        //加密证书存在
        encryptCert
            .ifPresent(appCertEntity -> config.setIdpEncryptCert(SamlKeyStoreProvider.getCredential(
                StringUtils.defaultString(saml2Config.getSpEntityId(),
                    UUID.randomUUID().toString()),
                appCertEntity.getPrivateKey(), appCertEntity.getCertification(), "")));
        return config;
    }

    @Override
    public Saml2SsoModel getSsoModel(String appId) {
        //根据提供商ID查询配置
        Saml2ProtocolConfig saml2Config = getProtocolConfig(appId);
        //构建配置
        return getSamlAppConverter().saml2ProtocolConfigToSaml2SsoModel(saml2Config);
    }

    private SamlAppConverter getSamlAppConverter() {
        return SpringContextUtils.getBean(SamlAppConverter.class);
    }

    /**
     * AppCertRepository
     */
    protected final AppCertRepository        appCertRepository;
    /**
     * ApplicationRepository
     */
    protected final AppRepository            appRepository;
    /**
     * AppSaml2ConfigRepository
     */
    protected final AppSaml2ConfigRepository appSaml2ConfigRepository;

    protected AbstractSamlAppService(AppCertRepository appCertRepository,
                                     AppGroupAssociationRepository appGroupAssociationRepository,
                                     AppAccountRepository appAccountRepository,
                                     AppAccessPolicyRepository appAccessPolicyRepository,
                                     AppRepository appRepository,
                                     AppSaml2ConfigRepository appSaml2ConfigRepository) {
        super(appCertRepository, appGroupAssociationRepository, appAccountRepository,
            appAccessPolicyRepository, appRepository);
        this.appCertRepository = appCertRepository;
        this.appRepository = appRepository;
        this.appSaml2ConfigRepository = appSaml2ConfigRepository;
    }

    @Mapper(componentModel = "spring")
    interface SamlAppConverter {

        /**
         * 应用 Saml 2 配置实体到 Saml 2 协议配置
         *
         * @param entity {@link ProtocolSaml2Config}
         * @return {@link Saml2ProtocolConfig}
         */
        @Mapping(target = "idpSignCert", ignore = true)
        @Mapping(target = "idpEncryptCert", ignore = true)
        Saml2ProtocolConfig appSaml2ConfigPoToSaml2ProtocolConfig(ProtocolSaml2ConfigDto entity);

        /**
         * 属性语句 JSON 字符串转换为列表
         *
         * @param value {@link String}
         * @return {@link List}
         */
        default List<ProtocolSaml2Config.AttributeStatement> mapAttributeStatements(String value) {
            if (value == null || value.isEmpty()) {
                return null;
            }
            try {
                return new com.fasterxml.jackson.databind.ObjectMapper().readValue(value,
                    new com.fasterxml.jackson.core.type.TypeReference<List<ProtocolSaml2Config.AttributeStatement>>() {
                    });
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        /**
         * 应用 Saml 2 配置实体到 Saml 2 协议配置
         *
         * @param config {@link Saml2ProtocolConfig}
         * @return {@link Saml2SsoModel}
         */
        default Saml2SsoModel saml2ProtocolConfigToSaml2SsoModel(Saml2ProtocolConfig config) {
            if (config == null) {
                return null;
            }
            Saml2SsoModel.Saml2SsoModelBuilder saml2SsoModel = Saml2SsoModel.builder();
            saml2SsoModel.appId(config.getAppId());
            saml2SsoModel.appCode(config.getAppCode());
            saml2SsoModel.spEntityId(config.getSpEntityId());
            saml2SsoModel.spAcsUrl(config.getSpAcsUrl());
            saml2SsoModel.recipient(config.getRecipient());
            //Audience为空默认使用 SP EntityId
            saml2SsoModel
                .audience(StringUtils.defaultString(config.getAudience(), config.getSpEntityId()));
            saml2SsoModel.spSlsUrl(config.getSpSloUrl());
            saml2SsoModel.spRequestsSigned(config.getSpRequestsSigned());
            saml2SsoModel.spSignCert(config.getSpSignCert());
            saml2SsoModel.acsBinding(config.getAcsBinding());
            saml2SsoModel.slsBinding(config.getSlsBinding());
            saml2SsoModel.nameIdFormat(config.getNameIdFormat());
            //NameID 值
            saml2SsoModel
                .nameIdValue(getNameIdValue(config.getAppId(), config.getNameIdValueType()));
            //断言签名相关
            saml2SsoModel.assertSigned(config.getAssertSigned());
            saml2SsoModel.assertSignAlgorithm(config.getAssertSignAlgorithm());
            //断言签名相关
            saml2SsoModel.assertEncrypted(config.getAssertEncrypted());
            saml2SsoModel.assertEncryptAlgorithm(config.getAssertEncryptAlgorithm());
            //响应签名相关
            saml2SsoModel.responseSigned(config.getResponseSigned());
            saml2SsoModel.responseSignAlgorithm(config.getResponseSignAlgorithm());
            saml2SsoModel.authnContextClassRef(config.getAuthnContextClassRef());

            saml2SsoModel.relayState(config.getRelayState());
            //加密、签名相关证书
            saml2SsoModel.idpSignCert(config.getIdpSignCert());
            saml2SsoModel.idpEncryptCert(config.getIdpEncryptCert());
            //处理属性
            saml2SsoModel.attributeStatements(
                getAttributeStatementList(config.getAppId(), config.getAttributeStatements()));
            return saml2SsoModel.build();
        }

        /**
         * 获取NameId value
         *
         * @param appId {@link String} 应用ID
         * @param nameIdValueType {@link SamlNameIdValueType}
         * @return {@link String}
         */
        default String getNameIdValue(String appId, SamlNameIdValueType nameIdValueType) {
            UserInfo user = getUser();
            //@formatter:off
            //用户名
            if (USER_USERNAME.equals(nameIdValueType)) {
                return user.getUsername();
            }
            //姓名
            if (USER_FULL_NAME.equals(nameIdValueType)) {
                return user.getFullName();
            }
            //用户名
            if (USER_NICK_NAME.equals(nameIdValueType)) {
                return user.getNickName();
            }
            //邮箱
            if (USER_EMAIL.equals(nameIdValueType)) {
                return user.getEmail();
            }
            //应用用户名
            String userId = SecurityUtils.getCurrentUserId();
            Optional<AppAccount> optional = getAppAccountRepository().findByAppIdAndUserIdAndDefaultedIsTrue(appId, userId);
            if (!optional.isPresent()) {
                logger.error("用户: " + SecurityUtils.getCurrentUserName() + "应用账户不存在");
                throw new AppAccountNotExistException();
            }
            return optional.get().getAccount();
            //@formatter:on
        }

        /**
         * 获取属性List
         *
         * @param appId {@link String} 应用ID
         * @param attributeStatements {@link List}
         * @return {@link Saml2SsoModel.AttributeStatement}
         */
        default List<Saml2SsoModel.AttributeStatement> getAttributeStatementList(String appId,
                                                                                 List<ProtocolSaml2Config.AttributeStatement> attributeStatements) {
            if (!CollectionUtils.isEmpty(attributeStatements)) {
                List<Saml2SsoModel.AttributeStatement> list = new ArrayList<>();
                UserInfo user = getUser();
                //@formatter:off
                //封装变量
                for (ProtocolSaml2Config.AttributeStatement attributeStatement : attributeStatements) {
                    Saml2SsoModel.AttributeStatement attributeStatementModal = new Saml2SsoModel.AttributeStatement();
                    attributeStatementModal.setKey(attributeStatement.getName());
                    attributeStatementModal.setNameFormat(attributeStatement.getNameFormat());
                    //用户名
                    if (attributeStatement.getValueExpression().contains(SamlAttributeStatementValueType.USERNAME.getExpression())){
                        HashMap<String, Object> values = new HashMap<>(16);
                        values.put(SamlAttributeStatementValueType.USERNAME.getKey(), user.getUsername());
                        attributeStatementModal.setValue(new StringSubstitutor(values).replace(attributeStatement.getValueExpression()));
                        list.add(attributeStatementModal);
                    }
                    //昵称
                    else if (attributeStatement.getValueExpression().contains(SamlAttributeStatementValueType.NICK_NAME.getExpression())){
                        if (StringUtils.isNotBlank(user.getNickName())) {
                            HashMap<String, Object> values = new HashMap<>(16);
                            values.put(SamlAttributeStatementValueType.NICK_NAME.getKey(), user.getNickName());
                            attributeStatementModal.setValue(new StringSubstitutor(values).replace(attributeStatement.getValueExpression()));
                            list.add(attributeStatementModal);
                        }
                    }
                     //手机号
                    else if (attributeStatement.getValueExpression().contains(SamlAttributeStatementValueType.PHONE.getExpression())){
                        if (StringUtils.isNotBlank(user.getMobile())) {
                            HashMap<String, Object> values = new HashMap<>(16);
                            values.put(SamlAttributeStatementValueType.PHONE.getKey(), user.getMobile());
                            attributeStatementModal.setValue(new StringSubstitutor(values).replace(attributeStatement.getValueExpression()));
                            list.add(attributeStatementModal);
                        }
                    }
                    //邮箱
                    else  if (attributeStatement.getValueExpression().contains(SamlAttributeStatementValueType.EMAIL.getExpression())){
                        if (StringUtils.isNotBlank(user.getEmail())){
                            HashMap<String, Object> values = new HashMap<>(16);
                            values.put(SamlAttributeStatementValueType.EMAIL.getKey(), user.getEmail());
                            attributeStatementModal.setValue(new StringSubstitutor(values).replace(attributeStatement.getValueExpression()));
                            list.add(attributeStatementModal);
                        }
                    }
                    //应用用户
                    else if (attributeStatement.getValueExpression().contains(SamlAttributeStatementValueType.APP_USERNAME.getExpression())){
                        //应用用户名
                        String userId = SecurityUtils.getCurrentUserId();
                        Optional<AppAccount> optional = getAppAccountRepository().findByAppIdAndUserIdAndDefaultedIsTrue(appId, userId);
                        if (!optional.isPresent()) {
                            logger.error("用户 [{}] 应用账户不存在",SecurityUtils.getCurrentUserName() );
                            throw new AppAccountNotExistException();
                        }
                        HashMap<String, Object> values = new HashMap<>(16);
                        values.put(SamlAttributeStatementValueType.APP_USERNAME.getKey(), optional.get().getAccount());
                        attributeStatementModal.setValue(new StringSubstitutor(values).replace(attributeStatement.getValueExpression()));
                        list.add(attributeStatementModal);
                    } else {
                        attributeStatementModal.setValue(attributeStatement.getValueExpression());
                        list.add(attributeStatementModal);
                    }
                }
                //@formatter:on
                return list;
            }
            return new ArrayList<>();
        }

        /**
         * 获取 AppAccountRepository
         *
         * @return {@link AppAccountRepository}
         */
        default AppAccountRepository getAppAccountRepository() {
            return SpringContextUtils.getBean(AppAccountRepository.class);
        }

        /**
         * 获取 UserInfoService
         *
         * @return {@link UserInfoService}
         */
        default UserInfoService getUserRepository() {
            return SpringContextUtils.getBean(UserInfoService.class);
        }

    }
}
