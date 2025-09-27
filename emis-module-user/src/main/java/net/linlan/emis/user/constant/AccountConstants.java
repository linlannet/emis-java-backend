/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.user.constant;

import net.linlan.emis.support.constant.EmisConstants;

/**
 * 账户常量
 *
 * @author Linlan
 */
public final class AccountConstants {

    /**
     * 组名称
     */
    public static final String   ACCOUNT_GROUP_NAME      = "系统账户";

    /**
     * 账户API
     */
    public static final String   ACCOUNT_PATH            = EmisConstants.V1_API_PATH + "/account";
    /**
     * 用户API
     */
    public final static String   USER_PATH               = EmisConstants.V1_API_PATH + "/user";
    /**
     * 用户个性化API
     */
    public final static String   USER_PROFILE_PATH       = USER_PATH + "/profile";
    /**
     * 组织机构API
     */
    public final static String   ORGAN_PATH              = EmisConstants.V1_API_PATH + "/organ";
    /**
     * 用户组API
     */
    public final static String   USER_GROUP_PATH         = EmisConstants.V1_API_PATH
                                                           + "/user_group";
    /**
     * 身份源API
     */
    public final static String   IDENTITY_SOURCE_PATH    = EmisConstants.V1_API_PATH
                                                           + "/identity_source";

    /**
     * ACCOUNT_V1_API_PATHS
     */
    public static final String[] V1_ACCOUNT_API_PATHS    = { USER_PATH + "/**", ORGAN_PATH + "/**",
                                                             USER_GROUP_PATH + "/**",
                                                             IDENTITY_SOURCE_PATH + "/**" };
    /**
     * 身份源缓存 cacheName
     */
    public static final String   IDS_CACHE_NAME          = "ids";

    /**
     * user 缓存 cacheName
     */
    public static final String   USER_CACHE_NAME         = "user";

    /**
     * 根部门 ID
     */
    public static final String   ROOT_DEPT_ID            = "root";

    /**
     * 根部门名称
     */
    public static final String   ROOT_DEPT_NAME          = "Linlan Enterprise";

    /**
     * org 缓存 cacheName
     */
    public static final String   ORG_CACHE_NAME          = "organization";

    /**
     * 忘记密码预认证
     */
    public static final String   PREPARE_FORGET_PASSWORD = "/prepare_forget_password";

    /**
     * 忘记密码
     */
    public static final String   FORGET_PASSWORD         = "/forget_password";

    /**
     * 忘记密码发送验证码
     */
    public static final String   FORGET_PASSWORD_CODE    = "/forget_password_code";

}
