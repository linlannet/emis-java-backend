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
package net.linlan.emis.protocol.jwt.configurers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import net.linlan.emis.manage.config.AbstractConfigurer;
import net.linlan.emis.manage.config.AuthenticationConfigUtils;
import net.linlan.emis.manage.endpoint.EndpointMatcher;
import net.linlan.emis.manage.endpoint.UnauthorizedAuthenticationEntryPoint;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.jwt.context.JwtAuthorizationServerContextFilter;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;

/**
 * 认证配置
 *
 * @author Linlan
 * Created on 2023/01/14 22:58
 */
public final class JwtAuthorizationServerConfigurer extends
                                                    AbstractHttpConfigurer<JwtAuthorizationServerConfigurer, HttpSecurity> {
    private final Map<Class<? extends AbstractConfigurer>, AbstractConfigurer> configurers      = createConfigurers();

    /**
     * 端点匹配器
     */
    private final List<EndpointMatcher>                                        endpointMatchers = new ArrayList<>();

    public RequestMatcher getEndpointsMatcher() {
        // Return a deferred RequestMatcher
        // since endpointsMatcher is constructed in init(HttpSecurity).
        return request -> new OrRequestMatcher(endpointMatchers.stream()
            .map(EndpointMatcher::getRequestMatcher).collect(Collectors.toList())).matches(request);
    }

    @Override
    public void init(HttpSecurity httpSecurity) throws Exception {
        this.configurers.values().forEach(configurer -> {
            configurer.init(httpSecurity);
            endpointMatchers.add(configurer.getEndpointMatcher());
        });
        httpSecurity.exceptionHandling(exceptionHandling -> {
            if (exceptionHandling != null) {
                //身份验证入口点
                exceptionHandling.authenticationEntryPoint(
                    new UnauthorizedAuthenticationEntryPoint(userAgentParser));
            }
        });

    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        //@formatter:off
        //Authorization server context filter
        ApplicationServiceLoader applicationServiceLoader = AuthenticationConfigUtils.getApplicationServiceLoader(httpSecurity);
        JwtAuthorizationServerContextFilter authorizationServerContextFilter = new JwtAuthorizationServerContextFilter(this.endpointMatchers, applicationServiceLoader);
        httpSecurity.addFilterAfter(postProcess(authorizationServerContextFilter), SecurityContextHolderFilter.class);
        this.configurers.values().forEach(configurer -> configurer.configure(httpSecurity));
        //@formatter:on
    }

    /**
     * createConfigurers
     *
     * @return {@link AbstractConfigurer}
     */
    private Map<Class<? extends AbstractConfigurer>, AbstractConfigurer> createConfigurers() {
        //@formatter:off
        Map<Class<? extends AbstractConfigurer>, AbstractConfigurer> configurers = new LinkedHashMap<>();
        configurers.put(JwtLoginAuthorizationEndpointConfigurer.class, new JwtLoginAuthorizationEndpointConfigurer(this::postProcess));
        configurers.put(JwtLogoutAuthorizationEndpointConfigurer.class, new JwtLogoutAuthorizationEndpointConfigurer(this::postProcess));
        //@formatter:on
        return configurers;
    }

    /**
     * userAgentParser
     */
    private final UserAgentParser userAgentParser;

    public JwtAuthorizationServerConfigurer(UserAgentParser userAgentParser) {
        this.userAgentParser = userAgentParser;
    }

}
