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
 * 用户状态
 *
 * @author Linlan
 */
public enum UserStatus implements BaseEnumString {
                                                  /**
                                                   * 已启用
                                                   */
                                                  ENABLED("enabled", "启用"),

                                                  /**
                                                   * 已禁用
                                                   */
                                                  DISABLED("disabled", "禁用"),

                                                  /**
                                                   * 登录失败锁定
                                                   */
                                                  LOCKED("locked", "锁定"),

                                                  /**
                                                   * 过期锁定
                                                   */
                                                  EXPIRED_LOCKED("expired_locked", "过期锁定"),

                                                  /**
                                                   * 密码过期锁定
                                                   */
                                                  PASSWORD_EXPIRED_LOCKED("password_expired_locked",
                                                                          "密码过期锁定");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    UserStatus(String key, String value) {
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
    public static UserStatus getStatus(String key) {
        UserStatus[] values = values();
        for (UserStatus status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return key;
    }

}
