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
package net.linlan.emis.protocol.cas.util;

import javax.xml.parsers.DocumentBuilder;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.core.session.SessionRegistry;

import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.cas.auth.CentralAuthenticationService;
import net.linlan.emis.protocol.repository.AppCasConfigRepository;

/**
 * @author Linlan
 * Created on  2022/12/29 16:25
 */
public class CasUtils {

    public static <B extends HttpSecurityBuilder<B>> AppCasConfigRepository getAppCasConfigRepository(B builder) {
        AppCasConfigRepository appRepository = builder
            .getSharedObject(AppCasConfigRepository.class);
        if (appRepository == null) {
            appRepository = builder.getSharedObject(ApplicationContext.class)
                .getBean(AppCasConfigRepository.class);
            builder.setSharedObject(AppCasConfigRepository.class, appRepository);
        }
        return appRepository;
    }

    public static <B extends HttpSecurityBuilder<B>> SessionRegistry getSessionRegistry(B builder) {
        SessionRegistry sessionRegistry = builder.getSharedObject(SessionRegistry.class);
        if (sessionRegistry == null) {
            sessionRegistry = getBean(builder, SessionRegistry.class);
            builder.setSharedObject(SessionRegistry.class, sessionRegistry);
        }
        return sessionRegistry;
    }

    public static <B extends HttpSecurityBuilder<B>> CentralAuthenticationService getCentralAuthenticationService(B builder) {
        CentralAuthenticationService authenticationService = builder
            .getSharedObject(CentralAuthenticationService.class);
        if (authenticationService == null) {
            authenticationService = getBean(builder, CentralAuthenticationService.class);
            builder.setSharedObject(CentralAuthenticationService.class, authenticationService);
        }
        return authenticationService;
    }

    public static <B extends HttpSecurityBuilder<B>> DocumentBuilder getDocumentBuilder(B builder) {
        DocumentBuilder documentBuilder = builder.getSharedObject(DocumentBuilder.class);
        if (documentBuilder == null) {
            documentBuilder = getBean(builder, DocumentBuilder.class);
            builder.setSharedObject(DocumentBuilder.class, documentBuilder);
        }
        return documentBuilder;
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

    public static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder, Class<T> type) {
        return builder.getSharedObject(ApplicationContext.class).getBean(type);
    }

}
