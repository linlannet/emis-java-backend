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
 * ProtocolOauth2Config数据域:应用OAUTH2配置查询参数实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolOauth2ConfigParam extends BaseParam {

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
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端密匙
     */
    private String clientSecret;
    /**
     * 客户端申请的权限范围
     */
    private String scope;
    /**
     * 资源ID集合,多个资源时用逗号(,)分隔
     */
    private String resourceIds;
    /**
     * 客户端支持的grant_type
     */
    private String authorizedGrantTypes;
    /**
     * 重定向URI
     */
    private String webServerRedirectUri;
    /**
     * 客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔
     */
    private String authorities;
    /**
     * 访问令牌有效时间值(单位:秒)
     */
    private Integer accessTokenValidity;
    /**
     * 预留字段
     */
    private String additionalInformation;
    /**
     * 更新令牌有效时间值(单位:秒)
     */
    private Integer refreshTokenValidity;
    /**
     * 用户是否自动Approval操作
     */
    private String autoapprove;
    /**
     * 算法
     */
    private String algorithm;
    /**
     * 算法加密值
     */
    private String algorithmKey;
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
