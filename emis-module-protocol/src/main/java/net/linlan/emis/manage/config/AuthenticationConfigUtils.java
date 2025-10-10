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
package net.linlan.emis.manage.config;

import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import net.linlan.emis.ssodef.base.service.ApplicationServiceLoader;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetailsSource;

/**
 *
 * @author Linlan
 *
 */
public class AuthenticationConfigUtils {

    public static <B extends HttpSecurityBuilder<B>> SessionRegistry getSessionRegistry(B builder) {
        SessionRegistry sessionRegistry = builder.getSharedObject(SessionRegistry.class);
        if (sessionRegistry == null) {
            sessionRegistry = getBean(builder, SessionRegistry.class);
            builder.setSharedObject(SessionRegistry.class, sessionRegistry);
        }
        return sessionRegistry;
    }

    public static <B extends HttpSecurityBuilder<B>> ApplicationServiceLoader getApplicationServiceLoader(B builder) {
        ApplicationServiceLoader applicationServiceLoader = builder
            .getSharedObject(ApplicationServiceLoader.class);
        if (applicationServiceLoader == null) {
            applicationServiceLoader = getBean(builder, ApplicationServiceLoader.class);
            builder.setSharedObject(ApplicationServiceLoader.class, applicationServiceLoader);
        }
        return applicationServiceLoader;
    }

    public static <B extends HttpSecurityBuilder<B>> CustomWebAuthenticationDetailsSource getCustomAuthenticationDetailsSource(B builder) {
        CustomWebAuthenticationDetailsSource authenticationDetailsSource = builder
            .getSharedObject(CustomWebAuthenticationDetailsSource.class);
        if (authenticationDetailsSource == null) {
            authenticationDetailsSource = getBean(builder,
                CustomWebAuthenticationDetailsSource.class);
            builder.setSharedObject(CustomWebAuthenticationDetailsSource.class,
                authenticationDetailsSource);
        }
        return authenticationDetailsSource;
    }

    public static <B extends HttpSecurityBuilder<B>> WebAuthenticationDetailsSource getAuthenticationDetailsSource(B builder) {
        WebAuthenticationDetailsSource authenticationDetailsSource = builder
            .getSharedObject(WebAuthenticationDetailsSource.class);
        if (authenticationDetailsSource == null) {
            authenticationDetailsSource = getBean(builder, WebAuthenticationDetailsSource.class);
            builder.setSharedObject(WebAuthenticationDetailsSource.class,
                authenticationDetailsSource);
        }
        return authenticationDetailsSource;
    }

    public static <T> T getOptionalBean(HttpSecurity httpSecurity, Class<T> type) {
        Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
            httpSecurity.getSharedObject(ApplicationContext.class), type);
        if (beansMap.size() > 1) {
            throw new NoUniqueBeanDefinitionException(
                type, beansMap
                    .size(),
                "Expected single matching bean of type '" + type.getName() + "' but found "
                             + beansMap.size() + ": "
                             + StringUtils.collectionToCommaDelimitedString(beansMap.keySet()));
        }
        return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
    }

    public static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder, Class<T> type) {
        return builder.getSharedObject(ApplicationContext.class).getBean(type);
    }
}
