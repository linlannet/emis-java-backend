/*
 * emis-module-audit - audit安全审计
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
 * 应用资源
 *
 * @author Linlan
 */
public class AppEventType {

    /**
     * 添加应用
     */
    public static Type ADD_APP                         = new Type("emis:event:app:create", "添加应用",
        ConsoleResource.APP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 启用应用
     */
    public static Type ENABLE_APP                      = new Type("emis:event:app:enabled", "启用应用",
        ConsoleResource.APP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 禁用应用
     */
    public static Type DISABLE_APP                     = new Type("emis:event:app:disabled", "禁用应用",
        ConsoleResource.APP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 编辑应用
     */
    public static Type UPDATE_APP                      = new Type("emis:event:app:update", "修改应用",
        ConsoleResource.APP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 保存应用配置
     */
    public static Type SAVE_APP_CONFIG                 = new Type("emis:event:app:save:config",
        "保存应用配置", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除应用
     */
    public static Type DELETE_APP                      = new Type("emis:event:app:delete", "删除应用",
        ConsoleResource.APP_RESOURCE, Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 应用访问授权
     */
    public static Type APP_ACCESS_POLICY               = new Type("emis:event:app:access_policy",
        "添加应用访问授权", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 启用应用访问授权
     */
    public static Type ENABLE_APP_ACCESS_POLICY        = new Type(
        "emis:event:app:enable_access_policy", "启用应用访问授权", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 禁用应用访问授权
     */
    public static Type DISABLE_APP_ACCESS_POLICY       = new Type(
        "emis:event:app:disable_access_policy", "禁用应用访问授权", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除访问授权
     */
    public static Type APP_CANCEL_ACCESS_POLICY        = new Type(
        "emis:event:app:cancel_access_policy", "取消应用访问授权", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 添加应用账户
     */
    public static Type ADD_APP_ACCOUNT                 = new Type("emis:event:app:add_app_account",
        "添加应用账户", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 更新默认应用账户
     */
    public static Type UPDATE_DEFAULT_APP_ACCOUNT      = new Type(
        "emis:event:app:update_default_app_account", "更新默认应用账户", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 删除应用账户
     */
    public static Type DELETE_APP_ACCOUNT              = new Type(
        "emis:event:app:delete_app_account", "删除应用账户", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存SSO配置
     */
    public static Type SAVE_SSO_CONFIG                 = new Type("emis:event:app:save_sso_config",
        "保存SSO配置", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 保存应用权限资源
     */
    public static Type SAVE_APP_PERMISSION_RESOURCE    = new Type(
        "emis:event:app:save_app_permission_resource", "保存应用权限资源", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 修改应用权限资源
     */
    public static Type UPDATE_APP_PERMISSION_RESOURCE  = new Type(
        "emis:event:app:update_app_permission_resource", "修改应用权限资源", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除应用权限资源
     */
    public static Type DELETE_APP_PERMISSION_RESOURCE  = new Type(
        "emis:event:app:delete_app_permission_resource", "删除应用权限资源", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 启用应用权限资源
     */
    public static Type ENABLE_APP_PERMISSION_RESOURCE  = new Type(
        "emis:event:app:enable_app_permission_resource", "启用应用权限资源", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 禁用应用权限资源
     */
    public static Type DISABLE_APP_PERMISSION_RESOURCE = new Type(
        "emis:event:app:disable_app_permission_resource", "禁用应用权限资源", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除用户身份提供商绑定
     */
    public static Type DELETE_USER_IDP_BIND            = new Type(
        "emis:event:app:delete_user_idp_bind", "删除用户身份提供商绑定", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 添加应用角色
     */
    public static Type SAVE_APP_PERMISSION_ROLE        = new Type(
        "emis:event:app:save_app_permission_role", "添加应用角色", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 修改应用角色
     */
    public static Type UPDATE_APP_PERMISSION_ROLE      = new Type(
        "emis:event:app:update_app_permission_role", "修改应用角色", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除应用角色
     */
    public static Type DELETE_APP_PERMISSION_ROLE      = new Type(
        "emis:event:app:delete_app_permission_role", "删除应用角色", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 添加应用分组
     */
    public static Type ADD_APP_GROUP                   = new Type("emis:event:app_group:create",
        "添加应用分组", ConsoleResource.APP_GROUP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 编辑应用分组
     */
    public static Type UPDATE_APP_GROUP                = new Type("emis:event:app_group:update",
        "修改应用分组", ConsoleResource.APP_GROUP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 删除应用分组
     */
    public static Type DELETE_APP_GROUP                = new Type("emis:event:app_group:delete",
        "删除应用分组", ConsoleResource.APP_GROUP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 移除应用分组关联
     */
    public static Type REMOVE_APP_GROUP_ASSOCIATION    = new Type(
        "emis:event:remove_app_group_association", "移除应用分组关联", ConsoleResource.APP_GROUP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));

    /**
     * 应用单点登录
     */
    public static Type APP_SSO                         = new Type("emis:event:app_sso_association",
        "应用单点登录", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 应用单点登出
     */
    public static Type APP_SSO_LOGOUT                  = new Type("emis:event:app_sso_logout",
        "应用单点登录", ConsoleResource.APP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
}
