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
 * 系统设置
 *
 * @author Linlan
 */
public class SettingEventType {

    /**
     * 保存安全基础设置
     */
    public static Type SAVE_LOGIN_SECURITY_BASIC_SETTINGS         = new Type(
        "emis:event:setting:save_security_basic", "保存安全基础设置", ConsoleResource.SECURITY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存安全策略
     */
    public static Type SAVE_SECURITY_POLICY_SETTINGS              = new Type(
        "emis:event:setting:save_security_policy", "保存安全防御策略", ConsoleResource.SECURITY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 密码策略
     */
    public static Type SAVE_PASSWORD_POLICY                       = new Type(
        "emis:event:setting:save_password_policy", "保存密码策略",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存多因素认证
     */
    public static Type SAVE_MFA_SETTINGS                          = new Type(
        "emis:event:setting:save_mfa", "保存多因素认证", ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存行为验证码
     */
    public static Type SAVE_CAPTCHA_PROVIDER                      = new Type(
        "emis:event:setting:save_captcha_provider", "保存行为验证码",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存行为验证码
     */
    public static Type OFF_CAPTCHA_PROVIDER                       = new Type(
        "emis:event:setting:off_captcha_provider", "禁用行为验证码",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 修改密码策略
     */
    public static Type UPDATE_PASSWORD_POLICY                     = new Type(
        "emis:event:setting:update_password_policy", "修改密码策略",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除密码策略
     */
    public static Type DELETE_PASSWORD_POLICY                     = new Type(
        "emis:event:setting:delete_password_policy", "删除密码策略",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 启用密码策略
     */
    public static Type ENABLE_PASSWORD_POLICY                     = new Type(
        "emis:event:setting:enable_password_policy", "启用密码策略",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 禁用密码策略
     */
    public static Type DISABLE_PASSWORD_POLICY                    = new Type(
        "emis:event:setting:disable_password_policy", "禁用密码策略",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 更新密码策略优先级
     */
    public static Type SORT_PASSWORD_POLICY                       = new Type(
        "emis:event:setting:sort_password_policy", "更新密码策略优先级",
        ConsoleResource.PASSWORD_POLICY_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 添加角色
     */
    public static Type ADD_ADMINISTRATOR_ROLE                     = new Type(
        "emis:event:setting:add_administrator_role", "添加角色", ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除角色
     */
    public static Type DELETE_ADMINISTRATOR_ROLE                  = new Type(
        "emis:event:setting:delete_administrator_role", "删除角色",
        ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 修改角色
     */
    public static Type UPDATE_ADMINISTRATOR_ROLE                  = new Type(
        "emis:event:setting:update_administrator_role", "修改角色",
        ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 启用角色
     */
    public static Type ENABLE_ADMINISTRATOR_ROLE                  = new Type(
        "emis:event:setting:enable_administrator_role", "启用角色",
        ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 禁用角色
     */
    public static Type DISABLE_ADMINISTRATOR_ROLE                 = new Type(
        "emis:event:setting:disable_administrator_role", "禁用角色",
        ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 添加管理员
     */
    public static Type ADD_ADMINISTRATOR                          = new Type(
        "emis:event:setting:add_administrator", "添加管理员", ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除管理员
     */
    public static Type DELETE_ADMINISTRATOR                       = new Type(
        "emis:event:setting:delete_administrator", "删除管理员", ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 修改管理员
     */
    public static Type UPDATE_ADMINISTRATOR                       = new Type(
        "emis:event:setting:update_administrator", "修改管理员", ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 启用管理员
     */
    public static Type ENABLE_ADMINISTRATOR                       = new Type(
        "emis:event:setting:enable_administrator", "启用管理员", ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 禁用管理员
     */
    public static Type DISABLE_ADMINISTRATOR                      = new Type(
        "emis:event:setting:disable_administrator", "禁用管理员", ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 重置管理员密码
     */
    public static Type RESET_ADMINISTRATOR_PASSWORD               = new Type(
        "emis:event:setting:reset_administrator_password", "重置管理员密码",
        ConsoleResource.ADMINISTRATOR_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存邮件模板
     */
    public static Type SAVE_MAIL_TEMPLATE                         = new Type(
        "emis:event:setting:save_mail_template", "保存邮件模板", ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启邮件服务
     */
    public static Type ON_MAIL_SERVICE                            = new Type(
        "emis:event:setting:on_mail_service", "开启邮件服务", ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭邮件服务
     */
    public static Type OFF_MAIL_SERVICE                           = new Type(
        "emis:event:setting:off_mail_service", "关闭邮件服务", ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存邮件服务
     */
    public static Type SAVE_MAIL_SERVICE                          = new Type(
        "emis:event:setting:save_mail_service", "保存邮件服务", ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启短信验证服务
     */
    public static Type ON_SMS_SERVICE                             = new Type(
        "emis:event:setting:on_sms_verify_service", "开启短信验证服务", ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭短信验证服务
     */
    public static Type OFF_SMS_SERVICE                            = new Type(
        "emis:event:setting:off_sms_verify_service", "关闭短信验证服务", ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存短信验证服务
     */
    public static Type SAVE_SMS_SERVICE                           = new Type(
        "emis:event:setting:save_sms_verify_service", "保存短信验证服务", ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
    * 开启自定义修改密码邮件模板
    */
    public static Type ON_CUSTOMIZE_CHANGE_PASSWORD_MAIL          = new Type(
        "emis:event:setting:on_customize_change_password_mail", "开启自定义修改密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启自定义重置密码邮件模板
     */
    public static Type ON_CUSTOMIZE_RESET_PASSWORD_MAIL           = new Type(
        "emis:event:setting:on_customize_reset_password_mail", "开启自定义重置密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启自定义确认重置密码邮件模板
     */
    public static Type ON_CUSTOMIZE_CONFIRM_RESET_PASSWORD_MAIL   = new Type(
        "emis:event:setting:on_customize_confirm_reset_password_mail", "开启自定义确认重置密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启自定义验证邮件模板
     */
    public static Type ON_CUSTOMIZE_VERIFY_MAIL                   = new Type(
        "emis:event:setting:on_customize_verify_mail", "开启自定义验证邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启自定义欢迎邮件模板
     */
    public static Type ON_CUSTOMIZE_WELCOME_MAIL                  = new Type(
        "emis:event:setting:on_customize_welcome_mail", "开启自定义欢迎邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启自定义修改绑定邮件模板
     */
    public static Type ON_CUSTOMIZE_MODIFY_BINDING_MAIL           = new Type(
        "emis:event:setting:on_customize_modify_binding:_mail", "开启自定义修改绑定邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭自定义修改密码邮件模板
     */
    public static Type OFF_CUSTOMIZE_CHANGE_PASSWORD_MAIL         = new Type(
        "emis:event:setting:off_customize_change_password_mail", "关闭自定义修改密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭自定义重置密码邮件模板
     */
    public static Type OFF_CUSTOMIZE_RESET_PASSWORD_MAIL          = new Type(
        "emis:event:setting:off_customize_reset_password_mail", "关闭自定义重置密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭自定义确认重置密码邮件模板
     */
    public static Type OFF_CUSTOMIZE_CONFIRM_RESET_PASSWORD_MAIL  = new Type(
        "emis:event:setting:off_customize_confirm_reset_password_mail", "关闭自定义确认重置密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭自定义验证邮件模板
     */
    public static Type OFF_CUSTOMIZE_VERIFY_MAIL                  = new Type(
        "emis:event:setting:off_customize_verify_mail", "关闭自定义验证邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭自定义欢迎邮件模板
     */
    public static Type OFF_CUSTOMIZE_WELCOME_MAIL                 = new Type(
        "emis:event:setting:off_customize_welcome_mail", "关闭自定义欢迎邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭自定义修改绑定邮件模板
     */
    public static Type OFF_CUSTOMIZE_MODIFY_BINDING_MAIL          = new Type(
        "emis:event:setting:off_customize_modify_binding_mail", "关闭自定义修改绑定邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存自定义修改密码邮件模板
     */
    public static Type SAVE_CUSTOMIZE_CHANGE_PASSWORD_MAIL        = new Type(
        "emis:event:setting:save_customize_change_password_mail", "保存自定义修改密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存自定义重置密码邮件模板
     */
    public static Type SAVE_CUSTOMIZE_RESET_PASSWORD_MAIL         = new Type(
        "emis:event:setting:save_customize_reset_password_mail", "保存自定义重置密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存自定义确认重置密码邮件模板
     */
    public static Type SAVE_CUSTOMIZE_CONFIRM_RESET_PASSWORD_MAIL = new Type(
        "emis:event:setting:save_customize_confirm_reset_password_mail", "保存自定义确认重置密码邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存自定义验证邮件模板
     */
    public static Type SAVE_CUSTOMIZE_VERIFY_MAIL                 = new Type(
        "emis:event:setting:save_customize_verify_mail", "保存自定义验证邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存自定义欢迎邮件模板
     */
    public static Type SAVE_CUSTOMIZE_WELCOME_MAIL                = new Type(
        "emis:event:setting:save_customize_welcome_mail", "保存自定义欢迎邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存自定义修改绑定邮件模板
     */
    public static Type SAVE_CUSTOMIZE_MODIFY_BINDING_MAIL         = new Type(
        "emis:event:setting:save_customize_modify_binding_mail", "保存自定义修改绑定邮件模板",
        ConsoleResource.MESSAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启存储服务
     */
    public static Type ON_STORAGE_SERVICE                         = new Type(
        "emis:event:setting:on_storage_service", "开启存储服务", ConsoleResource.STORAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭存储服务
     */
    public static Type OFF_STORAGE_SERVICE                        = new Type(
        "emis:event:setting:off_storage_service", "关闭存储服务", ConsoleResource.STORAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存存储服务
     */
    public static Type SAVE_STORAGE_SERVICE                       = new Type(
        "emis:event:setting:save_storage_service", "保存存储服务", ConsoleResource.STORAGE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 开启地理位置服务
     */
    public static Type ON_GEO_LOCATION_SERVICE                    = new Type(
        "emis:event:setting:on_geoip_service", "开启地理位置服务", ConsoleResource.GEO_LOCATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 关闭地理位置服务
     */
    public static Type OFF_GEO_LOCATION_SERVICE                   = new Type(
        "emis:event:setting:off_geoip_service", "关闭地理位置服务", ConsoleResource.GEO_LOCATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存地理位置服务
     */
    public static Type SAVE_GEO_LOCATION_SERVICE                  = new Type(
        "emis:event:setting:save_geoip_service", "保存地理位置服务", ConsoleResource.GEO_LOCATION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

}
