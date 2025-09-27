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
import net.linlan.utils.enums.BaseEnumString;

/**
 * 邮件安全方式
 *
 * @author Linlan
 */
public enum MailSafetyType implements BaseEnumString {
                                                      /**
                                                       * 无
                                                       */
                                                      None("none", "无"),
                                                      /**
                                                       * SSL
                                                       */
                                                      SSL("ssl", "SSL");

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
     * 构造
     *
     * @param key {@link String}
     * @param value {@link String}
     */
    MailSafetyType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @EnumConvert
    public static MailSafetyType getType(String key) {
        MailSafetyType[] values = values();
        for (MailSafetyType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该类型");
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key;
    }
}
