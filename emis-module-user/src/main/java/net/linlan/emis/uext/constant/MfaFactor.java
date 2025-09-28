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
package net.linlan.emis.uext.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.emis.support.userdetails.OrganizationType;
import net.linlan.utils.enums.BaseEnumString;

/**
 * MFA方式
 *
 * @author Linlan
 */
public enum MfaFactor implements BaseEnumString {
                                                 /**
                                                  * SMS
                                                  */
                                                 SMS_OTP("sms_otp", "短信验证码"),
                                                 /**
                                                  * 邮件验证码
                                                  */
                                                 EMAIL_OTP("email_otp", "邮件验证码"),

                                                 /**
                                                  * APP TOTP
                                                  */
                                                 APP_TOTP("app_totp", "APP TOTP");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    MfaFactor(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    /**
     * 获取类型
     *
     * @param key {@link String}
     * @return {@link OrganizationType}
     */
    @EnumConvert
    public static MfaFactor getType(String key) {
        MfaFactor[] values = values();
        for (MfaFactor status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
