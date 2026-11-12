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

import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetailsSource;

/**
 * 认证配置工具
 *
 * @author Linlan
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
