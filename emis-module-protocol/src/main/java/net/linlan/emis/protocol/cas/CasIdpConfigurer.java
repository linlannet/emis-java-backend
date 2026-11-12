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
package net.linlan.emis.protocol.cas;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.cas.auth.CentralAuthenticationService;
import net.linlan.emis.protocol.cas.endpoint.CasIdpSingleSignOnEndpointFilter;
import net.linlan.emis.protocol.cas.endpoint.CasIdpValidateEndpointFilter;
import net.linlan.emis.protocol.cas.filter.CasAuthorizationServerContextFilter;
import net.linlan.emis.protocol.cas.util.CasUtils;
import net.linlan.emis.protocol.repository.AppCasConfigRepository;

/**
 * 认证配置
 *
 * @author Linlan
 * Created on  2022/12/29 16:23
 */
public class CasIdpConfigurer<B extends HttpSecurityBuilder<B>>
                             extends AbstractHttpConfigurer<CasIdpConfigurer<B>, B> {

    @Override
    public void configure(B http) {
        AppCasConfigRepository appCasConfigRepository = CasUtils.getAppCasConfigRepository(http);
        ApplicationServiceLoader applicationServiceLoader = CasUtils
            .getApplicationServiceLoader(http);
        SessionRegistry sessionRegistry = CasUtils.getSessionRegistry(http);
        CentralAuthenticationService centralAuthenticationService = CasUtils
            .getCentralAuthenticationService(http);
        DocumentBuilder documentBuilder = CasUtils.getDocumentBuilder(http);
        //CAS 登录过滤器
        http.addFilterAfter(new CasIdpSingleSignOnEndpointFilter(applicationServiceLoader,
            centralAuthenticationService), UsernamePasswordAuthenticationFilter.class);

        //cas 验证过滤器
        http.addFilterBefore(
            new CasIdpValidateEndpointFilter(applicationServiceLoader, sessionRegistry,
                centralAuthenticationService, documentBuilder),
            CasIdpSingleSignOnEndpointFilter.class);

        //CAS 授权服务器应用上下文过滤器
        http.addFilterBefore(
            new CasAuthorizationServerContextFilter(getEndpointsMatcher(), appCasConfigRepository),
            CasIdpValidateEndpointFilter.class);
    }

    public RequestMatcher getEndpointsMatcher() {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        requestMatchers.add(CasIdpSingleSignOnEndpointFilter.getRequestMatcher());
        requestMatchers.add(CasIdpValidateEndpointFilter.getRequestMatcher());
        return new OrRequestMatcher(requestMatchers);
    }
}
