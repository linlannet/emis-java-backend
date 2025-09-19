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

import net.linlan.utils.enums.BaseEnumString;

/**
 * 权限类型
 *
 * @author Linlan
 */
public enum PermissionActionType implements BaseEnumString {
                                                            /**
                                                             * 菜单
                                                             */
                                                            MENU("MENU", "菜单"),
                                                            /**
                                                             * 按钮
                                                             */
                                                            BUTTON("BUTTON", "按钮"),
                                                            /**
                                                             * API
                                                             */
                                                            API("API", "API"),
                                                            /**
                                                             * 数据
                                                             */
                                                            DATA("DATA", "数据"),
                                                            /**
                                                             * 其他
                                                             */
                                                            OTHER("OTHER", "其他");

    /**
     * key
     */
    @JsonValue
    private final String key;
    /**
     * value
     */
    private final String value;

    PermissionActionType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static PermissionActionType getType(String key) {
        PermissionActionType[] values = values();
        for (PermissionActionType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }
}
