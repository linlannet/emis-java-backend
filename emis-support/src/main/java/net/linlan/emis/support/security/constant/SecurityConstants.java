/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.security.constant;

import net.linlan.emis.support.constant.EmisConstants;

/**
 * SecurityConstants
 *
 * @author Linlan
 */
public final class SecurityConstants {
    /**
     * LOGIN
     */
    public static final String LOGIN_PATH             = EmisConstants.V1_API_PATH + "/login";

    /**
     * form 表单登录
     */
    public static final String FORM_LOGIN             = LOGIN_PATH;

    /**
     * sms login 路径
     */
    public static final String SMS_LOGIN              = LOGIN_PATH + "/sms";

    /**
     * maf 验证
     */
    public static final String MFA_VALIDATE           = LOGIN_PATH + "/mfa/validate";

    /**
     * mfa 登录提供者
     */
    public static final String LOGIN_MFA_FACTORS      = LOGIN_PATH + "/mfa/factors";

    /**
     * 发送OTP
     */
    public static final String LOGIN_OTP_SEND         = LOGIN_PATH + "/otp/send";

    /**
     * idp 绑定用户 路径
     */
    public static final String USER_BIND_IDP          = LOGIN_PATH + "/idp_bind_user";

    /**
     * LOGOUT
     */
    public final static String LOGOUT_PATH            = EmisConstants.V1_API_PATH + "/logout";

    /**
     * 需要重置密码
     */
    public final static String REQUIRE_RESET_PASSWORD = "require_reset_password";

}
