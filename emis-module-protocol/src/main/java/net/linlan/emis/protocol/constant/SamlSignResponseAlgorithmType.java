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

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import static org.opensaml.xmlsec.signature.support.SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256;

/**
 * Saml Response 使用的非对称算法
 *
 * @author Linlan
 *
 */
public enum SamlSignResponseAlgorithmType {

                                           /**
                                            * Signature - Required RSAwithSHA256.
                                            */
                                           RSA_SHA256(ALGO_ID_SIGNATURE_RSA_SHA256);

    @JsonValue
    private final String value;

    SamlSignResponseAlgorithmType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @EnumConvert
    public static SamlSignResponseAlgorithmType getType(String code) {
        SamlSignResponseAlgorithmType[] values = values();
        for (SamlSignResponseAlgorithmType status : values) {
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
