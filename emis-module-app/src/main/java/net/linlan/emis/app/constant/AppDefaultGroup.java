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
 * 应用默认分组
 *
 * @author Linlan
 */
public enum AppDefaultGroup implements BaseEnumString {
                                                       /**
                                                        * 办公服务类
                                                        */
                                                       OFFICE("office", "办公服务类"),
                                                       /**
                                                        * 开发测试类
                                                        */
                                                       DEVELOPMENT("development", "开发测试类"),
                                                       /**
                                                        * 其他默认类
                                                        */
                                                       OTHER("other", "其他默认类"),;

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    AppDefaultGroup(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取类型
     *
     * @param key {@link String}
     * @return {@link AppPolicyEffect}
     */
    @EnumConvert
    public static AppDefaultGroup getType(String key) {
        AppDefaultGroup[] values = values();
        for (AppDefaultGroup status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
