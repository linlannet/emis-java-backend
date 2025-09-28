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
 * Sso 发起方
 *
 * @author Linlan
 */
public enum InitLoginType implements BaseEnumString {
                                                     /**
                                                      * 仅应用发起SSO，OIDC协议应用默认取值。当SAML应用指定为该方式时，InitLoginUrl必须指定。
                                                      */
                                                     APP("only_app_init_sso", "只允许应用发起"),
                                                     /**
                                                      * 支持门户或者应用发起SSO，SAML协议应用默认取值范围。当OIDC协议指定为该方式时，InitLoginUrl必须指定。
                                                      */
                                                     PORTAL_OR_APP("portal_or_app_init_sso",
                                                                   "支持门户和应用发起");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    InitLoginType(String key, String value) {
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
     * @return {@link InitLoginType}
     */
    @EnumConvert
    public static InitLoginType getType(String key) {
        InitLoginType[] values = values();
        for (InitLoginType status : values) {
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
