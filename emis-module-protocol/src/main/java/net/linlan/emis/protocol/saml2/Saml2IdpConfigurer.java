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
package net.linlan.emis.protocol.saml2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.repository.AppSaml2ConfigRepository;
import net.linlan.emis.protocol.saml2.endpoint.Saml2IdpMetadataEndpointFilter;
import net.linlan.emis.protocol.saml2.endpoint.Saml2IdpSingleSignOnEndpointFilter;
import net.linlan.emis.protocol.saml2.endpoint.Saml2IdpSingleSignOutEndpointFilter;
import net.linlan.emis.protocol.saml2.endpoint.Saml2InitSingleSignOnEndpointFilter;
import net.linlan.emis.protocol.saml2.filter.Saml2AuthorizationServerContextFilter;
import net.linlan.emis.protocol.saml2.util.Saml2Utils;

/**
 * 认证配置
 *
 * @author Linlan
 * Created on  2021/9/10 22:58
 */
public final class Saml2IdpConfigurer<B extends HttpSecurityBuilder<B>>
                                     extends AbstractHttpConfigurer<Saml2IdpConfigurer<B>, B> {

    @Override
    public void configure(B http) {
        AppSaml2ConfigRepository appSaml2ConfigRepository = Saml2Utils
            .getAppSaml2ConfigRepository(http);
        SessionRegistry sessionRegistry = Saml2Utils.getSessionRegistry(http);
        ApplicationServiceLoader applicationServiceLoader = Saml2Utils
            .getApplicationServiceLoader(http);
        http.addFilterAfter(new Saml2IdpSingleSignOnEndpointFilter(applicationServiceLoader),
            UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new Saml2InitSingleSignOnEndpointFilter(applicationServiceLoader),
            Saml2IdpSingleSignOnEndpointFilter.class);
        //登出过滤器
        http.addFilterAfter(
            new Saml2IdpSingleSignOutEndpointFilter(applicationServiceLoader, sessionRegistry),
            Saml2InitSingleSignOnEndpointFilter.class);
        //元数据过滤器
        http.addFilterBefore(new Saml2IdpMetadataEndpointFilter(applicationServiceLoader),
            Saml2IdpSingleSignOnEndpointFilter.class);
        //SAML2 授权服务器应用上下文过滤器
        http.addFilterBefore(new Saml2AuthorizationServerContextFilter(getEndpointsMatcher(),
            appSaml2ConfigRepository), Saml2IdpMetadataEndpointFilter.class);
    }

    public RequestMatcher getEndpointsMatcher() {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        //SAML 元数据
        requestMatchers.add(Saml2IdpMetadataEndpointFilter.getRequestMatcher());
        //SAML SP发起登录
        requestMatchers.add(Saml2IdpSingleSignOnEndpointFilter.getRequestMatcher());
        //SAML IDP 发起登录
        requestMatchers.add(Saml2InitSingleSignOnEndpointFilter.getRequestMatcher());
        //SAML SP 登出
        requestMatchers.add(Saml2IdpSingleSignOutEndpointFilter.getRequestMatcher());
        return new OrRequestMatcher(requestMatchers);
    }
}
