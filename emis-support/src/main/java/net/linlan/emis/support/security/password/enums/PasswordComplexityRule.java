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
package net.linlan.emis.support.security.password.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 密码复杂度规则
 *
 * @author Linlan
 */
public enum PasswordComplexityRule implements BaseEnumString {
                                                              /**
                                                               * 无密码规则
                                                               */
                                                              NONE("0", "任意密码"),

                                                              /**
                                                               * 必须包含数字和字母
                                                               */
                                                              MUST_NUMBERS_AND_LETTERS("1",
                                                                                       "必须包含数字和字母"),
                                                              /**
                                                               * 必须包含数字和大写字母
                                                               */
                                                              MUST_NUMBERS_AND_CAPITAL_LETTERS("2",
                                                                                               "必须包含数字和大写字母"),
                                                              /**
                                                               * 必须包含数字、大写字母、小写字母、和特殊字符
                                                               */
                                                              MUST_CONTAIN_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS("3",
                                                                                                                                              "必须包含数字、大写字母、小写字母、和特殊字符"),
                                                              /**
                                                               * 至少包含数字、字母、和特殊字符中的两种
                                                               */
                                                              CONTAIN_AT_LEAST_TWO_OF_NUMBERS_LETTERS_AND_SPECIAL_CHARACTERS("4",
                                                                                                                             "至少包含数字、字母、和特殊字符中的两种"),
                                                              /**
                                                               * 至少包含数字、大写字母、小写字母、和特殊字符中的三种
                                                               */
                                                              CONTAIN_AT_LEAST_THREE_OF_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS("5",
                                                                                                                                                           "至少包含数字、大写字母、小写字母、和特殊字符中的三种");

    /**
     * key
     */
    @JsonValue
    private final String key;
    /**
     * value
     */
    private final String value;

    PasswordComplexityRule(String key, String value) {
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

    @EnumConvert
    public static PasswordComplexityRule getType(String key) {
        PasswordComplexityRule[] values = values();
        for (PasswordComplexityRule status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该规则");
    }

    @Override
    public String toString() {
        return this.key;
    }
}
