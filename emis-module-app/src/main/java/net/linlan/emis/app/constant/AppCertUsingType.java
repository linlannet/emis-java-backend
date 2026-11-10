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
 * 证书使用类型
 *
 * @author Linlan
 */
public enum AppCertUsingType implements BaseEnumString {
                                                        /**
                                                         * OIDC JWK
                                                         */
                                                        OIDC_JWK("oidc_jwk", "OIDC JWK"),

                                                        /**
                                                         * JWT 加密
                                                         */
                                                        JWT_ENCRYPT("jwt_encrypt", "JWT 加密"),
                                                        /**
                                                         * 安全声明标记语言签名
                                                         */
                                                        SAML_SIGN("saml_sign", "SAML SIGN"),

                                                        /**
                                                         * 安全声明标记语言加密
                                                         */
                                                        SAML_ENCRYPT("saml_encrypt",
                                                                     "SAML ENCRYPT"),

    ;

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    AppCertUsingType(String key, String value) {
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
    public static AppCertUsingType getType(String key) {
        AppCertUsingType[] values = values();
        for (AppCertUsingType type : values) {
            if (String.valueOf(type.getKey()).equals(key)) {
                return type;
            }
        }
        return null;
    }
}
