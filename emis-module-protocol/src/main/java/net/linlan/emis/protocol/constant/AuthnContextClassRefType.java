/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.constant;

import org.opensaml.saml.saml2.core.AuthnContext;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;

/**
 * Authn 上下文类引用类型
 *
 * @author Linlan
 *
 */
public enum AuthnContextClassRefType {

                                      /**
                                       * URI for unspecified authentication context.
                                       */
                                      UNSPECIFIED_AUTHN_CTX(AuthnContext.UNSPECIFIED_AUTHN_CTX),
                                      /**
                                       * URI for Password authentication context.
                                       */
                                      PASSWORD_AUTHN_CTX(AuthnContext.PASSWORD_AUTHN_CTX),

                                      /**
                                       * URI for Password Protected Transport authentication context.
                                       */
                                      PPT_AUTHN_CTX(AuthnContext.PPT_AUTHN_CTX);

    @JsonValue
    private final String value;

    AuthnContextClassRefType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @EnumConvert
    public static AuthnContextClassRefType getType(String code) {
        AuthnContextClassRefType[] values = values();
        for (AuthnContextClassRefType status : values) {
            if (String.valueOf(status.getValue()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
