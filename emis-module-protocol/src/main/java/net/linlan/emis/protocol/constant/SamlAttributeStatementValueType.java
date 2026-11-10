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
import net.linlan.utils.enums.BaseEnumString;
import static org.apache.commons.text.StringSubstitutor.DEFAULT_VAR_END;
import static org.apache.commons.text.StringSubstitutor.DEFAULT_VAR_START;

/**
 * Saml 2 AttributeStatementValue 类型
 *
 * @author Linlan
 * 
 */
public enum SamlAttributeStatementValueType implements BaseEnumString {
                                                                       /**
                                                                        * 手机号
                                                                        */
                                                                       PHONE("user.mobile",
                                                                             DEFAULT_VAR_START
                                                                                            + "user.mobile"
                                                                                            + DEFAULT_VAR_END),
                                                                       /**
                                                                        * 用户名
                                                                        */
                                                                       USERNAME("user.username",
                                                                                DEFAULT_VAR_START + "user.username"
                                                                                                 + DEFAULT_VAR_END),
                                                                       /**
                                                                        * 昵称
                                                                        */
                                                                       NICK_NAME("user.nickName",
                                                                                 DEFAULT_VAR_START + "user.nickName"
                                                                                                  + DEFAULT_VAR_END),
                                                                       /**
                                                                        * 邮箱
                                                                        */
                                                                       EMAIL("user.email",
                                                                             DEFAULT_VAR_START
                                                                                           + "user.email"
                                                                                           + DEFAULT_VAR_END),
                                                                       /**
                                                                        * 应用用户名
                                                                        */
                                                                       APP_USERNAME("app_user.username",
                                                                                    DEFAULT_VAR_START + "app_user.username"
                                                                                                         + DEFAULT_VAR_END);

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    SamlAttributeStatementValueType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @EnumConvert
    public static SamlAttributeStatementValueType getType(String key) {
        SamlAttributeStatementValueType[] values = values();
        for (SamlAttributeStatementValueType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return null;
    }

    public String getExpression() {
        return value;
    }

    @Override
    public String toString() {
        return key;
    }
}
