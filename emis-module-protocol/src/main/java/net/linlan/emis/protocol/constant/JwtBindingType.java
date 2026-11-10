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

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.app.constant.InitLoginType;
import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * Jwt 跳转方式
 *
 * @author Linlan
 *
 */
public enum JwtBindingType implements BaseEnumString {
                                                      /**
                                                       * redirect
                                                       */
                                                      REDIRECT("get", HttpMethod.GET, "重定向跳转"),
                                                      /**
                                                       * post
                                                       */
                                                      POST("post", HttpMethod.POST, "页面请求跳转");

    /**
     * 键
     */
    @JsonValue
    private final String     key;
    /**
     * 值
     */
    private final String     value;

    /**
     * http method
     */
    private final HttpMethod httpMethod;

    JwtBindingType(String key, HttpMethod httpMethod, String value) {
        this.key = key;
        this.httpMethod = httpMethod;
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

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * 获取类型
     *
     * @param key {@link String}
     * @return {@link InitLoginType}
     */
    @EnumConvert
    public static JwtBindingType getType(String key) {
        JwtBindingType[] values = values();
        for (JwtBindingType status : values) {
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
