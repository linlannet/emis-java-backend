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
package net.linlan.emis.protocol.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * ProtocolFormConfig数据域:表单代填应用配置实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolFormConfig extends BaseEntity {

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
     * 登录URL
     */
    private String loginUrl;

    /**
     * 登录名属性名称
     */
    private String usernameField;

    /**
     * 登录密码属性名称
     */
    private String passwordField;

    /**
     * 登录密码加密类型
     */
    private String passwordEncryptType;

    /**
     * 登录密码加密秘钥
     */
    private String passwordEncryptKey;

    /**
     * 用户名加密类型
     */
    private String usernameEncryptType;

    /**
     * 用户名加密秘钥
     */
    private String usernameEncryptKey;

    /**
     * 登录提交方式
     */
    private String submitType;

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

    /**
     * 登录其他信息
     */
    private List<OtherField> otherField;

    @Data
    @Schema(description = "表单其他信息")
    public static class OtherField implements Serializable {

        private String fieldName;

        private String fieldValue;
    }

    /**
     * 初始化方法
     */
    public void init() {
        //添加对数据库或实体对象的默认值处理
        if(getId() == null) {
            setId(RandomUtils.randomSid());
        }

    }

}
