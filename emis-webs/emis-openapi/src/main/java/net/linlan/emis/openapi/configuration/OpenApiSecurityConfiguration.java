/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import net.linlan.emis.openapi.authorization.AccessTokenAuthenticationEntryPoint;
import net.linlan.emis.openapi.authorization.AccessTokenAuthenticationFilter;
import net.linlan.emis.openapi.authorization.AccessTokenAuthenticationProvider;
import net.linlan.emis.openapi.authorization.store.AccessTokenStore;
import net.linlan.emis.openapi.authorization.store.RedisAccessTokenStore;
import net.linlan.emis.openapi.constant.OpenApiConstants;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;

/**
 * OpenaiSecurityConfiguration，开放平台安全配置
 *
 * @author Linlan
 * Created on 2020/9/27 22:54
 */
@EnableMethodSecurity
@Configuration
public class OpenApiSecurityConfiguration {

    /**
     * securityFilterChain
     *
     * @param http {@link HttpSecurity}
     * @param accessTokenStore {@link AccessTokenStore}
     * @return {@link SecurityFilterChain}
     */
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AccessTokenStore accessTokenStore) throws Exception {
        ProviderManager providerManager = new ProviderManager(
            new AccessTokenAuthenticationProvider(accessTokenStore));
        http.requestMatcher(new AntPathRequestMatcher(OpenApiConstants.OPEN_API_PATH + "/**"));
        http.authorizeHttpRequests(registry -> {
            registry
                .requestMatchers(
                    new AntPathRequestMatcher(OpenApiConstants.AUTH_PATH + "/access_token"))
                .permitAll();
            registry.anyRequest().authenticated();
        });
        //@formatter:off
        //关闭 csrf
        http.csrf(AbstractHttpConfigurer::disable);
        //关闭 cors
        http.cors(AbstractHttpConfigurer::disable);
        //关闭 session
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //异常处理器
        http.exceptionHandling(configurer -> configurer.authenticationEntryPoint(new AccessTokenAuthenticationEntryPoint(userAgentParser)));
        http.addFilterBefore(new AccessTokenAuthenticationFilter(providerManager), BasicAuthenticationFilter.class);
        return http.build();
        //@formatter:on
    }

    /**
     * TokenStore
     *
     * @param redisTemplate {@link RedisTemplate}
     * @return {@link AccessTokenStore}
     */
    @Bean
    public AccessTokenStore tokenStore(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisAccessTokenStore(redisTemplate);
    }

    private final UserAgentParser userAgentParser;

    public OpenApiSecurityConfiguration(UserAgentParser userAgentParser) {
        this.userAgentParser = userAgentParser;
    }

}
