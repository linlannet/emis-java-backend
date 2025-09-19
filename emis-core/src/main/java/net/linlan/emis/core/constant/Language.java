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

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 语言
 *
 * @author Linlan
 */
public enum Language implements BaseEnumString {
                                                /**
                                                 * 英语
                                                 */
                                                EN("en", "英语"),
                                                /**
                                                 * 中文
                                                 */
                                                ZH("zh", "中文");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    Language(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getLocale() {
        return key;
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
     * @param code {@link String}
     * @return {@link Language}
     */
    @EnumConvert
    public static Language getType(String code) {
        Language[] values = values();
        for (Language status : values) {
            if (String.valueOf(status.getLocale()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getLocale();
    }

}
