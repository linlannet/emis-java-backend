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
import static net.linlan.frame.comm.constant.AdminType.ADMIN;

/**
 * 账户资源
 *
 * @author Linlan
 */
public class AccountEventType {

    /**
     * 创建用户
     */
    public static Type CREATE_USER                           = new Type(
        "emis:event:account:create_user", "创建用户", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 编辑用户
     */
    public static Type UPDATE_USER                           = new Type(
        "emis:event:account:update_user", "修改用户", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 删除用户
     */
    public static Type DELETE_USER                           = new Type(
        "emis:event:account:delete_user", "删除用户", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 禁用用户
     */
    public static Type DISABLE_USER                          = new Type(
        "emis:event:account:disabled_user", "禁用用户", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 启用用户
     */
    public static Type ENABLE_USER                           = new Type(
        "emis:event:account:enabled_user", "启用用户", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 创建组织
     */
    public static Type CREATE_ORG                            = new Type(
        "emis:event:account:create_organization", "创建组织", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 编辑组织
     */
    public static Type UPDATE_ORG                            = new Type(
        "emis:event:account:update_organization", "修改组织", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 删除组织
     */
    public static Type DELETE_ORGANIZATION                   = new Type(
        "emis:event:account:delete_organization", "删除组织", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 移动组织
     */
    public static Type MOVE_ORGANIZATION                     = new Type(
        "emis:event:account:move_organization", "移动组织", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 添加组织用户
     */
    public static Type CREATE_ORGANIZATION_MEMBER            = new Type(
        "emis:event:account:create_organization_member", "添加组织用户",
        ConsoleResource.ORG_ACCOUNT_RESOURCE, Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 用户离职
     */
    public static Type USER_RESIGN                           = new Type(
        "emis:event:account:user_resign", "用户离职", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 修改邮箱
     */
    public static Type MODIFY_USER_EMAIL                     = new Type(
        "emis:event:account:update_email", "修改邮箱", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 修改手机号
     */
    public static Type MODIFY_USER_PHONE                     = new Type(
        "emis:event:account:update_mobile", "修改手机号", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 修改密码
     */
    public static Type MODIFY_USER_PASSWORD                  = new Type(
        "emis:event:account:update_password", "修改密码", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 解锁用户
     */
    public static Type UNLOCK_USER                           = new Type(
        "emis:event:account:unlock_user", "解锁用户", ConsoleResource.ORG_ACCOUNT_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 创建用户组
     */
    public static Type CREATE_USER_GROUP                     = new Type(
        "emis:event:account:create_user_group", "创建静态用户组", ConsoleResource.USER_GROUP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 编辑用户组
     */
    public static Type UPDATE_USER_GROUP                     = new Type(
        "emis:event:account:update_user_group", "修改静态用户组", ConsoleResource.USER_GROUP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 删除用户组
     */
    public static Type DELETE_USER_GROUP                     = new Type(
        "emis:event:account:delete_user_group", "删除静态用户组", ConsoleResource.USER_GROUP_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 添加用户组成员
     */
    public static Type ADD_USER_GROUP_MEMBER                 = new Type(
        "emis:event:account:add_user_group_member", "添加静态用户组成员",
        ConsoleResource.USER_GROUP_RESOURCE, Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 移除用户组成员
     */
    public static Type REMOVE_USER_GROUP_MEMBER              = new Type(
        "emis:event:account:remove_user_group_member", "移除静态用户组成员",
        ConsoleResource.USER_GROUP_RESOURCE, Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 创建动态用户组
     */
    public static Type CREATE_USER_DYNAMIC_GROUP             = new Type(
        "emis:event:account:create_user_dynamic_group", "创建动态用户组",
        ConsoleResource.USER_GROUP_RESOURCE, Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 编辑动态用户组
     */
    public static Type UPDATE_USER_DYNAMIC_GROUP             = new Type(
        "emis:event:account:update_user_dynamic_group", "修改动态用户组",
        ConsoleResource.USER_GROUP_RESOURCE, Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 编辑动态用户组过滤规则
     */
    public static Type UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE = new Type(
        "emis:event:account:update_user_dynamic_group:filter_rule", "编辑动态用户组过滤规则",
        ConsoleResource.USER_GROUP_RESOURCE, Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 删除动态用户组
     */
    public static Type DELETE_USER_DYNAMIC_GROUP             = new Type(
        "emis:event:account:delete_user_dynamic_group", "删除动态用户组",
        ConsoleResource.USER_GROUP_RESOURCE, Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 创建身份源
     */
    public static Type CREATE_IDENTITY_RESOURCE              = new Type(
        "emis:event:account:create_identity_resource", "创建身份源",
        ConsoleResource.IDENTITY_SOURCE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 编辑身份源
     */
    public static Type UPDATE_IDENTITY_RESOURCE              = new Type(
        "emis:event:account:update_identity_resource", "修改身份源",
        ConsoleResource.IDENTITY_SOURCE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));
    /**
     * 删除身份源
     */
    public static Type DELETE_IDENTITY_RESOURCE              = new Type(
        "emis:event:account:delete_identity_resource", "删除身份源",
        ConsoleResource.IDENTITY_SOURCE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 启用身份源
     */
    public static Type ENABLE_IDENTITY_RESOURCE              = new Type(
        "emis:event:account:enable_identity_resource", "启用身份源",
        ConsoleResource.IDENTITY_SOURCE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 禁用身份源
     */
    public static Type DISABLE_IDENTITY_RESOURCE             = new Type(
        "emis:event:account:disable_identity_resource", "禁用身份源",
        ConsoleResource.IDENTITY_SOURCE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

    /**
     * 禁用身份源
     */
    public static Type IDENTITY_RESOURCE_SYNC                = new Type(
        "emis:event:account:identity_resource_sync", "身份源同步",
        ConsoleResource.IDENTITY_SOURCE_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(ADMIN)));

}
