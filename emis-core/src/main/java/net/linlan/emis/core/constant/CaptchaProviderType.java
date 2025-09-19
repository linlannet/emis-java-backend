/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 验证码提供类型
 *
 * @author Linlan
 */
public enum CaptchaProviderType implements BaseEnumString {

                                                           /**
                                                            * ALIYUN
                                                            */
                                                           ALIYUN("aliyun", "阿里云"),
                                                           /**
                                                            *TENCENT
                                                            */
                                                           TENCENT("tencent", "腾讯"),
                                                           /**
                                                            *GEE_TEST
                                                            */
                                                           GEE_TEST("geetest", "极验"),
                                                           /**
                                                            *HCAPTCHA
                                                            */
                                                           HCAPTCHA("hcaptcha", "Hcaptcha"),
                                                           /**
                                                            *RECAPTCHA
                                                            */
                                                           RECAPTCHA("recaptcha", "reCAPTCHA");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    /**
     * 获取提供商
     *
     * @param key {@link String}
     * @return {@link CaptchaProviderType}
     */
    @EnumConvert
    public static CaptchaProviderType getType(String key) {
        CaptchaProviderType[] values = values();
        for (CaptchaProviderType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未获取到对应提供商");
    }

    CaptchaProviderType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return null;
    }
}
