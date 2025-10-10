/*
 * emis-java-backend - 基于JAVA开发的企业级多身份管理系统后端工程，依托EFRAME框架开发，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。
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
package net.linlan.emis.protocol.constant;

import java.io.Serializable;
import java.util.List;

import org.opensaml.security.credential.Credential;

import lombok.Builder;
import lombok.Data;

import net.linlan.emis.protocol.entity.ProtocolSaml2Config;

/**
 * SAML2 协议配置
 *
 * @author Linlan
 *
 */
@Data
@Builder
public class Saml2ProtocolConfig implements Serializable {

    private static final long                             serialVersionUID = -3671812647788721766L;

    /**
     * APP ID
     */
    private String                                        appId;

    /**
     * APP Code
     */
    private String                                        appCode;

    /**
     * SpEntityId
     */
    private String                                        spEntityId;

    /**
     * SP 单点登录 ACS URL
     */
    private String                                        spAcsUrl;

    /**
     * SP 单点登出 URL
     */
    private String                                        spSloUrl;

    /**
     * 是否对 SAML Request 签名进行验证 ，用来对SAML Request签名进行验证，对应SP元数据文件中“AuthnRequestsSigned”值
     */
    private Boolean                                       spRequestsSigned;

    /**
     * SP公钥证书，用来验证SAML request的签名，对应SP元数据文件中 use='signing' 证书内容
     */
    private String                                        spSignCert;

    /**
     * 单点登录 ACS BINDING
     */
    private String                                        acsBinding;

    /**
     * 单点登出 SLS BINDING
     */
    private String                                        slsBinding;

    /**
     * 接受者
     */
    private String                                        recipient;

    /**
     * 允许使用SAML断言的资源，默认和SP Entity ID相同。
     */
    private String                                        audience;

    /**
     * SAML Response 中指定账户标识 NameID 字段格式。一般无需修改。
     */
    private SamlNameIdFormatType nameIdFormat;

    /**
     * NameId
     */
    private SamlNameIdValueType nameIdValueType;

    /**
     * 签名断言
     */
    private Boolean                                       assertSigned;

    /**
     * 断言签名使用的非对称算法
     */
    private SamlSignAssertAlgorithmType assertSignAlgorithm;

    /**
     * 加密断言
     */
    private Boolean                                       assertEncrypted;

    /**
     * 断言加密使用的非对称算法
     */
    private SamlEncryptAssertAlgorithmType assertEncryptAlgorithm;

    /**
     * 响应是否加密
     */
    private Boolean                                       responseSigned;

    /**
     * 响应签名使用的非对称算法
     */
    private SamlSignAssertAlgorithmType                   responseSignAlgorithm;

    /**
     * SAML 身份认证上下文
     */
    private AuthnContextClassRefType authnContextClassRef;
    /**
     * IDP 发起 SSO 登录成功后，应用应自动跳转的地址。在 SAML Response 中会在 RelayState 参数中传递，应用读取后实现跳转。
     */
    private String                                        relayState;

    /**
     * 在 SAML Response 中，可以将额外用户字段（例如邮箱、显示名等）返回给应用解析。
     */
    private List<ProtocolSaml2Config.AttributeStatement> attributeStatements;

    /**
     * IDP 签名证书
     */
    private Credential                                    idpSignCert;
    /**
     * IDP 加密证书
     */
    private Credential                                    idpEncryptCert;
}
