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
 * 权限策略主体类型
 * 用户、角色、分组、组织机构
 *
 * @author Linlan
 */
public enum AppPolicySubjectType implements BaseEnumString {
                                                            /**
                                                             * 角色
                                                             */
                                                            ROLE("ROLE", "角色"),
                                                            /**
                                                             * 用户
                                                             */
                                                            USER("USER", "用户"),
                                                            /**
                                                             * 组织机构
                                                             */
                                                            ORGANIZATION("ORGANIZATION", "组织机构"),
                                                            /**
                                                             * 分组
                                                             */
                                                            USER_GROUP("USER_GROUP", "分组"),
                                                            /**
                                                             * TODO 客户端
                                                             */
                                                            CLIENT("CLIENT", "客户端");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    AppPolicySubjectType(String key, String value) {
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
     * @return {@link AppPolicySubjectType}
     */
    @EnumConvert
    public static AppPolicySubjectType getType(String key) {
        AppPolicySubjectType[] values = values();
        for (AppPolicySubjectType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }
}
