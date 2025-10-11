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
package net.linlan.emis.protocol.dto;

import org.opensaml.security.credential.Credential;

import lombok.Data;

import net.linlan.emis.app.constant.AuthorizationType;
import net.linlan.emis.app.constant.InitLoginType;
import net.linlan.emis.protocol.entity.ProtocolSaml2Config;

/**
 *
 * ProtocolSaml2Config数据域:应用SAML2配置Dto对象
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolSaml2ConfigDto extends ProtocolSaml2Config {
    /**
     * APP CODE
     */
    private String            appCode;

    /**
     * 模版
     */
    private String            appTemplate;

    /**
     * 客户端ID
     */
    private String            clientId;

    /**
     * 客户端秘钥
     */
    private String            clientSecret;

    /**
     * SSO 发起方
     */
    private InitLoginType     initLoginType;

    /**
     * SSO 登录链接
     */
    private String            initLoginUrl;

    /**
     * 授权范围
     */
    private AuthorizationType authorizationType;

    /**
     * IDP 签名证书
     */
    private Credential        idpSignCert;

    /**
     * IDP 加密证书
     */
    private Credential        idpEncryptCert;
}
