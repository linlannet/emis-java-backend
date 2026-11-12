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
package net.linlan.emis.protocol.saml2.converter;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.linlan.emis.protocol.constant.AuthnContextClassRefType;
import net.linlan.emis.protocol.constant.SamlEncryptAssertAlgorithmType;
import net.linlan.emis.protocol.constant.SamlNameIdFormatType;
import net.linlan.emis.protocol.constant.SamlNameIdValueType;
import net.linlan.emis.protocol.constant.SamlSignAssertAlgorithmType;
import net.linlan.emis.protocol.dto.ProtocolSaml2ConfigDto;
import net.linlan.emis.protocol.entity.ProtocolSaml2Config;
import net.linlan.emis.protocol.saml2.model.AppSaml2StandardConfigGetResult;
import net.linlan.emis.protocol.saml2.model.AppSaml2StandardSaveConfigParam;
import net.linlan.emis.protocol.saml2.model.Saml2ConverterUtils;

/**
 * 应用映射
 *
 * @author Linlan
 * Created on 2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface AppSaml2StandardConfigConverter {
    /**
     * 实体转Saml2配置结果
     *
     * @param config {@link ProtocolSaml2ConfigDto}
     * @return {@link AppSaml2StandardConfigGetResult}
     */
    default AppSaml2StandardConfigGetResult entityConverterToSaml2ConfigResult(ProtocolSaml2ConfigDto config) {
        if (config == null) {
            return null;
        }
        AppSaml2StandardConfigGetResult result = new AppSaml2StandardConfigGetResult();
        //协议端点域
        result.setProtocolEndpoint(
            Saml2ConverterUtils.getProtocolEndpointDomain(config.getAppCode().toString()));
        //SpEntityId
        result.setSpEntityId(config.getSpEntityId());
        //SP 单点登录 ACS URL
        result.setSpAcsUrl(config.getSpAcsUrl());
        //单点登录 ACS BINDING
        result.setAcsBinding(config.getAcsBinding());
        //是否对 SAML Request 签名进行验证 ，用来对SAML Request签名进行验证，对应SP元数据文件中“AuthnRequestsSigned”值
        result.setSpRequestsSigned(config.getSpRequestsSigned());
        //SP公钥证书，用来验证SAML request的签名，对应SP元数据文件中 use='signing' 证书内容
        result.setSpSignCert(config.getSpSignCert());
        //Audience
        result.setSpAudience(config.getAudience());
        //NameId 值类型
        result.setNameIdValueType(SamlNameIdValueType.getType(config.getNameidValueType()));
        //SAML Response 中指定账户标识 NameID 字段格式。
        result.setNameIdFormat(SamlNameIdFormatType.getType(config.getNameidFormat()));
        //是否对断言签名
        result.setAssertSigned(config.getAssertSigned());
        //断言签名使用的非对称算法
        result.setAssertSignAlgorithm(
            SamlSignAssertAlgorithmType.getType(config.getAssertSignAlgorithm()));
        //是否对断言加密
        result.setAssertEncrypted(config.getAssertEncrypted());
        //断言加密使用的非对称算法
        result.setAssertEncryptAlgorithm(
            SamlEncryptAssertAlgorithmType.getType(config.getAssertEncryptAlgorithm()));
        //是否对响应加密
        result.setResponseSigned(config.getResponseSigned());
        //响应加密使用的非对称算法
        result.setResponseSignAlgorithm(
            SamlSignAssertAlgorithmType.getType(config.getResponseSignAlgorithm()));
        //SAML 身份认证上下文
        result.setAuthnContextClassRef(
            AuthnContextClassRefType.getType(config.getAuthnContextClassref()));
        //IDP 发起 SSO 登录成功后，应用应自动跳转的地址。在 SAML Response 中会在 RelayState 参数中传递，应用读取后实现跳转。
        result.setRelayState(config.getRelayState());
        //SSO 发起方
        result.setInitLoginType(config.getInitLoginType());
        //登录发起地址
        result.setInitLoginUrl(config.getInitLoginUrl());
        //授权类型
        result.setAuthorizationType(config.getAuthorizationType());
        //属性声明
        List<ProtocolSaml2Config.AttributeStatement> list = mapAttributeStatements(
            config.getAttributeStatements());
        if (list != null) {
            result.setAttributeStatements(list);
        }
        result.setAdditionalConfig(config.getAdditionalConfig());
        return result;
    }

    /**
     * 将 Saml 2 配置转换器保存到实体
     *
     * @param param {@link AppSaml2StandardSaveConfigParam}
     * @return {@link ProtocolSaml2Config}
     */
    @Mapping(target = "slsBinding", ignore = true)
    @Mapping(target = "recipient", ignore = true)
    @Mapping(target = "appId", ignore = true)
    @Mapping(target = "spMetadata", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    ProtocolSaml2Config saveSaml2ConfigConverterToEntity(AppSaml2StandardSaveConfigParam param);

    /**
     * 属性语句列表转换为 JSON 字符串
     *
     * @param value {@link List}
     * @return {@link String}
     */
    default String mapAttributeStatements(List<ProtocolSaml2Config.AttributeStatement> value) {
        if (value == null) {
            return null;
        }
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(value);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

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
}
