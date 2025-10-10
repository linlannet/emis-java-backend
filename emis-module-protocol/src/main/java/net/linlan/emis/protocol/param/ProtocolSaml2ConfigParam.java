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
package net.linlan.emis.protocol.param;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * ProtocolSaml2Config数据域:应用SAML2配置查询参数实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolSaml2ConfigParam extends BaseParam {

    /**
     * 主键ID
     */
    private String id;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 外键ID
     */
    private String foreignId;
    /**
     * SP实体ID
     */
    private String spEntityId;
    /**
     * SP元数据
     */
    private String spMetadata;
    /**
     * SP断言消费地址
     */
    private String spAcsUrl;
    /**
     * SP单点登出地址
     */
    private String spSloUrl;
    /**
     * 是否对 SAML Request 签名进行验证 ，用来对SAML Request签名进行验证，对应SP元数据文件中“AuthnRequestsSigned”值
     */
    private Boolean spRequestsSigned;
    /**
     * SP公钥证书，用来验证SAML request的签名，对应SP元数据文件中 use='signing' 证书内容
     */
    private String spSignCert;
    /**
     * 断言Binding方式
     */
    private String acsBinding;
    /**
     * 单点登出Binding方式
     */
    private String slsBinding;
    /**
     * 断言接收者地址
     */
    private String recipient;
    /**
     * 指定此SAML断言的目标受众，默认和SP Entity ID相同。
     */
    private String audience;
    /**
     * 断言NameIDFormat
     */
    private String nameidFormat;
    /**
     * 断言NameID值表达式
     */
    private String nameidValueType;
    /**
     * 断言是否签名
     */
    private Boolean assertSigned;
    /**
     * 断言签名使用的非对称算法
     */
    private String assertSignAlgorithm;
    /**
     * 断言是否加密
     */
    private Boolean assertEncrypted;
    /**
     * 断言加密使用的非对称算法
     */
    private String assertEncryptAlgorithm;
    /**
     * 是否签名Response
     */
    private Boolean responseSigned;
    /**
     * 签名Response使用的非对称算法
     */
    private String responseSignAlgorithm;
    /**
     * 身份认证SAML上下文
     */
    private String authnContextClassref;
    /**
     * RelayState
     */
    private String relayState;
    /**
     * AttributeStatements
     */
    private String attributeStatements;
    /**
     * 额外配置
     */
    private String additionalConfig;
    /**
     * 创建人ID
     */
    private String creatorId;
    /**
     * 删除标记，0未删除1已删除
     */
    private Integer delFlag;
    /**
     * 备注
     */
    private String description;
    /**
     * 备用1
     */
    private String spare1;
    /**
     * 备用2
     */
    private String spare2;

}
