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
package net.linlan.emis.support.manage.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.constant.EmisConstants;
import net.linlan.emis.support.userdetails.OrganizationType;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 秘钥类型
 *
 * @author Linlan
 */
public enum SecretType implements BaseEnumString {
                                                  /**
                                                   * 登录
                                                   */
                                                  LOGIN("login", EmisConstants.LOGIN_SECRET, "登录"),
                                                  /**
                                                   * 加密
                                                   */
                                                  ENCRYPT("encrypt", EmisConstants.ENCRYPT_SECRET,
                                                          "加密");

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
     * 路径
     */
    private final String path;

    SecretType(String key, String path, String value) {
        this.key = key;
        this.path = path;
        this.value = value;
    }

    /**
     * 获取来源
     *
     * @param key {@link String}
     * @return {@link OrganizationType}
     */
    @EnumConvert
    public static SecretType getType(String key) {
        SecretType[] values = values();
        for (SecretType source : values) {
            if (String.valueOf(source.getKey()).equals(key)) {
                return source;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }
}
