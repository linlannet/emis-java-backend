/*
 * emis-module-app - app应用管理
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
package net.linlan.emis.app.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 权限策略客体类型
 *
 * @author Linlan
 */
public enum AppPolicyObjectType implements BaseEnumString {
                                                           /**
                                                            * 角色
                                                            */
                                                           ROLE("ROLE", "角色"),
                                                           /**
                                                            * 权限
                                                            */
                                                           PERMISSION("PERMISSION", "权限"),
                                                           /**
                                                            * 资源
                                                            */
                                                           RESOURCE("RESOURCE", "资源"),;

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    AppPolicyObjectType(String key, String value) {
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
     * 获取类型
     *
     * @param key {@link String}
     * @return {@link AppPolicyObjectType}
     */
    @EnumConvert
    public static AppPolicyObjectType getType(String key) {
        AppPolicyObjectType[] values = values();
        for (AppPolicyObjectType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }
}
