/*
 * emis-synchronizer - synchronizer同步执行器
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
package net.linlan.emis.synchronizer.configuration;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static net.linlan.emis.core.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;
import static net.linlan.emis.support.constant.EmisConstants.V1_API_PATH;

/**
 * SynchronizerSecurityConfiguration
 *
 * @author Linlan
 * Created on 2019/9/27 22:54
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SynchronizerSecurityConfiguration {

    /**
     * SecurityFilterChain
     *
     * @param http {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @RefreshScope
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // @formatter:off
//        http
//                //认证请求
//                .authorizeHttpRequests(registry -> registry
//                        .requestMatchers(EVENT_RECEIVE_PATH+"/*").permitAll()
//                        .requestMatchers(webEndpointProperties.getBasePath()+"/**").permitAll()
//                        .anyRequest().authenticated())
//                //csrf过滤器
//                .csrf(httpSecurity -> httpSecurity.ignoringRequestMatchers(EVENT_RECEIVE_PATH+"/*"));
//        // @formatter:on
        //        return http.build();  shuxiaobo
        http.antMatcher(V1_API_PATH + "/**");
        http.authorizeRequests(registry -> {
            registry.antMatchers(EVENT_RECEIVE_PATH + "/*").permitAll()
                .antMatchers(webEndpointProperties.getBasePath() + "/**").permitAll();
            registry.anyRequest().authenticated();
        });
        //@formatter:off
        //关闭 csrf
        http.csrf(AbstractHttpConfigurer::disable);
        //关闭 cors
        http.cors(AbstractHttpConfigurer::disable);
        //关闭 session
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    private final WebEndpointProperties webEndpointProperties;
}
