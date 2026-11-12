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

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * ProtocolOauth2Config数据域:应用OAUTH2配置实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolOauth2Config extends BaseEntity {

    /**
     * 主键ID
     */
    private String  id;

    /**
     * 应用ID
     */
    private String  appId;

    /**
     * 外键ID
     */
    private String  foreignId;

    /**
     * 客户端ID
     */
    private String  clientId;

    /**
     * 客户端密匙
     */
    private String  clientSecret;

    /**
     * 客户端申请的权限范围
     */
    private String  scope;

    /**
     * 资源ID集合,多个资源时用逗号(,)分隔
     */
    private String  resourceIds;

    /**
     * 客户端支持的grant_type
     */
    private String  authorizedGrantTypes;

    /**
     * 重定向URI
     */
    private String  webServerRedirectUri;

    /**
     * 客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔
     */
    private String  authorities;

    /**
     * 访问令牌有效时间值(单位:秒)
     */
    private Integer accessTokenValidity;

    /**
     * 预留字段
     */
    private String  additionalInformation;

    /**
     * 更新令牌有效时间值(单位:秒)
     */
    private Integer refreshTokenValidity;

    /**
     * 用户是否自动Approval操作
     */
    private String  autoapprove;

    /**
     * 算法
     */
    private String  algorithm;

    /**
     * 算法加密值
     */
    private String  algorithmKey;

    /**
     * 删除标记，0未删除1已删除
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String  description;

    /**
     * 备用1
     */
    private String  spare1;

    /**
     * 备用2
     */
    private String  spare2;

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
