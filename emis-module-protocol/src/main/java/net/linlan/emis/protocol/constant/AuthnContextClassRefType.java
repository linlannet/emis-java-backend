/*
 * emis-java-backend - 基于JAVA开发的企业级多身份管理系统后端工程，依托EFRAME框架开发，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。
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
