/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.app.constant.InitLoginType;
import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * Form用户名，密码加密类型
 *
 * @author Linlan
 *
 */
public enum FormEncryptType implements BaseEnumString {
                                                       /**
                                                        * aes
                                                        */
                                                       AES("aes", "AES加密"),
                                                       /**
                                                        * MD5编码
                                                        */
                                                       MD5("md5", "MD5编码"),
                                                       /**
                                                        * bas64
                                                        */
                                                       BASE64("base64", "BASE64编码");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    FormEncryptType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    /**
     * 获取类型
     *
     * @param key {@link String}
     * @return {@link InitLoginType}
     */
    @EnumConvert
    public static FormEncryptType getType(String key) {
        if (key == null) {
            return null;
        }
        FormEncryptType[] values = values();
        for (FormEncryptType status : values) {
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
