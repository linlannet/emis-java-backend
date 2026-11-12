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
package net.linlan.emis.protocol.oidc;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;
import static org.apache.commons.text.StringSubstitutor.DEFAULT_VAR_END;
import static org.apache.commons.text.StringSubstitutor.DEFAULT_VAR_START;

/**
 * IdTokenCustomClaimValueType
 *
 * @author Linlan
 * Created on 2022/5/22 23:26
 */
public enum IdTokenCustomClaimValueType implements BaseEnumString {
                                                                   /**
                                                                    * 手机号
                                                                    */
                                                                   MOBILE("user.mobile",
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
                                                                    * 员工工号
                                                                    */
                                                                   USER_EMPLOYEE_NUMBER("user.employee_number",
                                                                                        DEFAULT_VAR_START + "user.employee_number"
                                                                                                                + DEFAULT_VAR_END),
                                                                   /**
                                                                    * 应用用户名
                                                                    */
                                                                   APP_USERNAME("app_user.username",
                                                                                DEFAULT_VAR_START + "app_user.username"
                                                                                                     + DEFAULT_VAR_END);

    @JsonValue
    private final String key;
    private final String value;

    IdTokenCustomClaimValueType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @EnumConvert
    public static IdTokenCustomClaimValueType getType(String key) {
        IdTokenCustomClaimValueType[] values = values();
        for (IdTokenCustomClaimValueType status : values) {
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

    @Override
    public String toString() {
        return key;
    }
}
