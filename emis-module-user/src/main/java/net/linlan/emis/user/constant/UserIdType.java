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

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.emis.support.userdetails.OrganizationType;
import net.linlan.utils.enums.BaseEnumString;

/**
 *
 * @author Linlan
 * Created on  2022/10/6 22:11
 */
public enum UserIdType implements BaseEnumString {
                                                  /**
                                                   * 身份证号
                                                   */
                                                  IDENTITY_CARD("identity_card", "身份证号");

    /**
     * 键
     */
    @JSONField
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    UserIdType(String key, String value) {
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
     * 获取来源
     *
     * @param key {@link String}
     * @return {@link OrganizationType}
     */
    @EnumConvert
    public static UserIdType getType(String key) {
        UserIdType[] values = values();
        for (UserIdType source : values) {
            if (String.valueOf(source.getKey()).equals(key)) {
                return source;
            }
        }
        return null;
    }

    public static UserIdType getName(String name) {
        UserIdType[] values = values();
        for (UserIdType source : values) {
            if (source.name().equals(name)) {
                return source;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
