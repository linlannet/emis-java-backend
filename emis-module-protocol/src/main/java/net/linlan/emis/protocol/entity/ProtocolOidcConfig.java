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
package net.linlan.emis.protocol.entity;

import java.util.Set;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * ProtocolOidcConfig数据域:应用OIDC配置实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolOidcConfig extends BaseEntity {

    /**
     * 主键ID
     */
    private String      id;

    /**
     * 应用ID
     */
    private String      appId;

    /**
     * 外键ID
     */
    private String      foreignId;

    /**
     * 客户端认证方法
     */
    private Set<String> clientAuthMethods;

    /**
     * 认证授权类型
     */
    private Set<String> authGrantTypes;

    /**
     * 响应类型
     */
    private Set<String> responseTypes;

    /**
     * 重定向URIs
     */
    private Set<String> redirectUris;

    /**
     * 登出重定向URIs
     */
    private Set<String> postLogoutRedirectUris;

    /**
     * 授权范围
     */
    private Set<String> grantScopes;

    /**
     * 是否需要授权同意
     */
    private Boolean     requireAuthConsent;

    /**
     * 需要PKCE
     */
    private Boolean     requireProofKey;

    /**
     * 令牌Endpoint身份验证签名算法
     */
    private String      tokenEndpointAuthSigningAlgorithm;

    /**
     * refresh_token 生存时间
     */
    private Long        refreshTokenTimeToLive;

    /**
     * 授权码生存时间
     */
    private Long        authorizationCodeTimeToLive;

    /**
     * 设备授权码生存时间
     */
    private Long        deviceCodeTimeToLive;

    /**
     * ACCESS_token 生存时间
     */
    private Long        accessTokenTimeToLive;

    /**
     * 生存时间ID_TOKEN
     */
    private Long        idTokenTimeToLive;

    /**
     * 签名算法ID_TOKEN
     */
    private String      idTokenSignatureAlgorithm;

    /**
     * AccessToken 格式
     */
    private String      accessTokenFormat;

    /**
     * 是否重用刷新令牌
     */
    private Boolean     reuseRefreshToken;

    /**
     * 删除标记，0未删除1已删除
     */
    private Integer     delFlag;

    /**
     * 备注
     */
    private String      description;

    /**
     * 备用1
     */
    private String      spare1;

    /**
     * 备用2
     */
    private String      spare2;

    /**
     * 初始化方法
     */
    public void init() {
        //添加对数据库或实体对象的默认值处理
        if (getId() == null) {
            setId(RandomUtils.UUID32());
        }

    }

}
