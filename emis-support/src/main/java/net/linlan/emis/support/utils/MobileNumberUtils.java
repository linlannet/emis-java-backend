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
package net.linlan.emis.support.utils;

import java.util.regex.Pattern;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import net.linlan.commons.core.StringUtils;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.validation.ValidationMobile;

/**
 * MobileNumberUtils
 *
 * @author Linlan
 */
public class MobileNumberUtils {

    public static final String PLUS_SIGN = "";
    public static Pattern      pattern   = ValidationMobile.pattern;

    /**
     * 获取手机号
     *
     * @param mobile {@link String}
     * @return {@link String}
     */
    public static String getMobileAreaCode(String mobile) {
        Phonenumber.PhoneNumber mobileNumber = parseMobile(mobile);
        return String.valueOf(mobileNumber.getCountryCode());
    }

    /**
     * 获取手机号
     * @param mobile {@link String}
     * @return {@link String}
     */
    public static String getMobileNumber(String mobile) {
        Phonenumber.PhoneNumber mobileNumber = parseMobile(mobile);
        return String.valueOf(mobileNumber.getNationalNumber());
    }

    /**
     * 获取PhoneNumber
     *
     * @param mobile {@link String}
     * @return {@link Phonenumber.PhoneNumber}
     */
    public static Phonenumber.PhoneNumber parseMobile(String mobile) {
        try {
            return PhoneNumberUtil.getInstance().parse(mobile, "CN");
        } catch (NumberParseException e) {
            throw new SupportException("解析手机号发生异常", e);
        }
    }

    public static boolean isMobileValidate(String mobile) {
        if (StringUtils.isNotBlank(mobile)) {
            return pattern.matcher(mobile).matches();
        } else {
            return false;
        }
    }
}
