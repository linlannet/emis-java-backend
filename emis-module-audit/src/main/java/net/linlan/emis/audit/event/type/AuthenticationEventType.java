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
package net.linlan.emis.audit.event.type;

import java.util.Arrays;
import java.util.Collections;

import net.linlan.emis.audit.event.ConsoleResource;
import net.linlan.emis.audit.event.Type;
import net.linlan.frame.comm.constant.AdminType;

/**
 * 认证资源
 *
 * @author Linlan
 */
public class AuthenticationEventType {

    /**
     * 添加身份提供商
     */
    public static Type ADD_IDP        = new Type("emis:event:idp_add", "添加身份提供商",
        ConsoleResource.IDP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 编辑身份提供商
     */
    public static Type UPDATE_IDP     = new Type("emis:event:idp_update", "修改身份提供商",
        ConsoleResource.IDP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 启用身份提供商
     */
    public static Type ENABLE_IDP     = new Type("emis:event:idp_enabled", "启用身份提供商",
        ConsoleResource.IDP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 禁用身份提供商
     */
    public static Type DISABLE_IDP    = new Type("emis:event:idp_disabled", "禁用身份提供商",
        ConsoleResource.IDP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 删除身份提供商
     */
    public static Type DELETE_IDP     = new Type("emis:event:idp_delete", "删除身份提供商",
        ConsoleResource.IDP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 登录控制台
     */
    public static Type LOGIN_CONSOLE  = new Type("emis:event:login:console", "登录控制台",
        ConsoleResource.AUTHENTICATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 登录门户
     */
    public static Type LOGIN_PORTAL   = new Type("emis:event:login:portal", "登录门户",
        ConsoleResource.AUTHENTICATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 退出控制台
     */
    public static Type LOGOUT_CONSOLE = new Type("emis:event:logout:console", "退出控制台",
        ConsoleResource.AUTHENTICATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

}
