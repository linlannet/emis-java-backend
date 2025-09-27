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

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 用户性别
 *
 * @author Linlan
 * Created on  2021/7/17 19:28
 */
public enum UserGender implements BaseEnumString {
                                                  /**
                                                   * 男
                                                   */
                                                  MALE("1", "男"),
                                                  /**
                                                   * 女
                                                   */
                                                  FEMALE("0", "女"),
                                                  /**
                                                   * 未知
                                                   */
                                                  UNKNOWN("-1", "未知");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    UserGender(String key, String value) {
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
     * 获取认证平台
     *
     * @param key {@link String}
     * @return {@link UserGender}
     */
    @EnumConvert
    public static UserGender getType(String key) {
        UserGender[] values = values();
        for (UserGender status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未获取到对应性别");
    }
}
