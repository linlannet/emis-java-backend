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
 * 认证方式
 *
 * @author Linlan
 */
public enum AuthenticationType implements BaseEnumString {
                                                          /**
                                                           * from表单
                                                           */
                                                          FORM("form", "FORM表单"),
                                                          /**
                                                           * 短信验证码
                                                           */
                                                          SMS("sms", "短信验证码"),
                                                          /**
                                                           * 社交认证
                                                           */
                                                          SOCIAL("social", "社交认证");

    /**
     * 键
     */
    @JsonValue
    private String key;
    /**
     * 值
     */
    private String value;

    AuthenticationType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取认证平台
     *
     * @param key {@link String}
     * @return {@link AuthenticationType}
     */
    @EnumConvert
    public static AuthenticationType getType(String key) {
        AuthenticationType[] values = values();
        for (AuthenticationType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未获取到对应平台");
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
