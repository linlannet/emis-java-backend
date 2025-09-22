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
import net.linlan.emis.audit.event.PortalResource;
import net.linlan.emis.audit.event.Type;
import net.linlan.frame.comm.constant.AdminType;

/**
 * 门户端审计事件类型
 *
 * @author Linlan
 */
public class PortalEventType {
    /**
     * 单点登录
     */
    public static Type APP_SSO                 = new Type("emis:event:app:sso", "单点登录",
        PortalResource.MY_APP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.USER)));
    /**
     * 退出应用
     */
    public static Type APP_SLO                 = new Type("emis:event:app:login", "单点登出",
        PortalResource.MY_APP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 绑定账号
     */
    public static Type BIND_IDP_USER           = new Type("emis:event:account:bind_idp_user",
        "绑定 IDP", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));
    /**
     * 解绑账号
     */
    public static Type UNBIND_IDP_USER         = new Type("emis:event:account:unbind_idp_user",
        "解绑 IDP", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 修改账户信息
     */
    public static Type MODIFY_ACCOUNT_INFO     = new Type("emis:event:account:update_account_info",
        "修改账户", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 修改邮箱
     */
    public static Type MODIFY_USER_EMAIL       = new Type("emis:event:account:update_email", "修改邮箱",
        PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));
    /**
     * 修改手机号
     */
    public static Type MODIFY_USER_PHONE       = new Type("emis:event:account:update_mobile",
        "修改手机号", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));
    /**
     * 修改密码
     */
    public static Type MODIFY_USER_PASSWORD    = new Type("emis:event:account:update_password",
        "修改密码", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 绑定MFA
     */
    public static Type BIND_MFA                = new Type("emis:event:account:bind_maf", "绑定 MFA",
        PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 准备修改密码
     */
    public static Type PREPARE_MODIFY_PASSWORD = new Type(
        "emis:event:account:prepare_modify_password", "准备修改密码", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 准备修改手机
     */
    public static Type PREPARE_MODIFY_MOBILE   = new Type(
        "emis:event:account:prepare_modify_mobile", "准备修改手机", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 准备修改邮箱
     */
    public static Type PREPARE_MODIFY_EMAIL    = new Type("emis:event:account:prepare_modify_email",
        "准备修改邮箱", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 准备绑定MFA
     */
    public static Type PREPARE_BIND_MFA        = new Type("emis:event:account:prepare_bind_mfa",
        "准备绑定MFA", PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 解绑mfa
     */
    public static Type UNBIND_MFA              = new Type("emis:event:account:unbind_maf", "解绑 MFA",
        PortalResource.MY_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 登录门户
     */
    public static Type LOGIN_PORTAL            = new Type("emis:event:login:portal", "登录门户",
        ConsoleResource.AUTHENTICATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));

    /**
     * 退出门户
     */
    public static Type LOGOUT_PORTAL           = new Type("emis:event:logout:portal", "退出门户",
        ConsoleResource.AUTHENTICATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.USER)));
}
