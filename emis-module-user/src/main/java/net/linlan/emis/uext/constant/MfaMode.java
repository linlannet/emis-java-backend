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
package net.linlan.emis.uext.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.emis.support.userdetails.OrganizationType;
import net.linlan.utils.enums.BaseEnumString;

/**
 * MFA 模式
 *
 * @author Linlan
 */
public enum MfaMode implements BaseEnumString {
                                               /**
                                                * 常开模式
                                                */
                                               ALWAYS("always", "常开模式"),
                                               /**
                                                * 关闭
                                                */
                                               NONE("none", "关闭"),
                                               /**
                                                * 智能模式
                                                */
                                               SMART("smart", "智能模式");

    /**
     * key
     */
    @JsonValue
    private final String key;
    /**
     * value
     */
    private final String value;

    MfaMode(String key, String value) {
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
     * @return {@link OrganizationType}
     */
    @EnumConvert
    public static MfaMode getType(String key) {
        MfaMode[] values = values();
        for (MfaMode status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
