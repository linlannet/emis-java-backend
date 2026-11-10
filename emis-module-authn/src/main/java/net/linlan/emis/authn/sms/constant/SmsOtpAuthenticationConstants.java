/*
 * emis-module-authn - authn认证模块
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
package net.linlan.emis.authn.sms.constant;

import static net.linlan.emis.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 * 验证码认证常量
 *
 * @author Linlan
 */
public final class SmsOtpAuthenticationConstants {

    public static final String RECIPIENT_KEY = "recipient";
    public static final String CODE_KEY      = "code";
    /**
     * 验证码登录路径
     */
    public static final String OTP_LOGIN     = LOGIN_PATH + "/otp";
}