/*
 * emis-portal - portal门户平台
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
package net.linlan.emis.portal.configuration.security;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.jwt.JwtAuthorizationService;
import net.linlan.emis.protocol.jwt.RedisJwtAuthorizationService;
import net.linlan.emis.protocol.jwt.authentication.JwtAuthenticationFailureEventListener;
import net.linlan.emis.protocol.jwt.authentication.JwtAuthenticationSuccessEventListener;
import net.linlan.emis.protocol.jwt.configurers.JwtAuthorizationServerConfigurer;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import static net.linlan.emis.core.constant.ConfigBeanNameConstants.JWT_PROTOCOL_SECURITY_FILTER_CHAIN;

/**
 *
 * @author Linlan
 * Created on  2023/7/3 21:16
 */
@Configuration(proxyBeanMethods = false)
public class JwtProtocolSecurityConfiguration extends AbstractSecurityConfiguration {

    /**
     * JwtProtocolSecurityFilterChain
     *
     * @param httpSecurity {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(value = JWT_PROTOCOL_SECURITY_FILTER_CHAIN)
    @RefreshScope
    public SecurityFilterChain jwtProtocolSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //@formatter:off
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).parentAuthenticationManager(null);
        //Jwt IDP 配置
        JwtAuthorizationServerConfigurer serverConfigurer = new JwtAuthorizationServerConfigurer(userAgentParser);
        RequestMatcher endpointsMatcher = serverConfigurer.getEndpointsMatcher();
        httpSecurity.requestMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                //安全上下文
                .securityContext(securityContext())
                //CSRF
                .csrf(withCsrfConfigurerDefaults(endpointsMatcher))
                //headers
                .headers(withHeadersConfigurerDefaults())
                //cors
                .cors(withCorsConfigurerDefaults())
                //会话管理器
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(NEVER));
        httpSecurity.apply(serverConfigurer);
        return httpSecurity.build();
        //@formatter:on
    }

    /**
     * JWT 成功监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link AuthenticationSuccessEvent}
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> jwtAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        return new JwtAuthenticationSuccessEventListener(auditEventPublish);
    }

    /**
     * Jwt 失败监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link JwtAuthenticationFailureEventListener}
     */
    @Bean
    public ApplicationListener<AbstractAuthenticationFailureEvent> jwtAuthenticationFailureEventListener(AuditEventPublish auditEventPublish) {
        return new JwtAuthenticationFailureEventListener(auditEventPublish);
    }

    @Bean
    public JwtAuthorizationService jwtAuthorizationService(RedisConnectionFactory redisConnectionFactory,
                                                           CacheProperties cacheProperties,
                                                           AutowireCapableBeanFactory beanFactory,
                                                           ApplicationServiceLoader applicationServiceLoader) {
        RedisTemplate<String, String> redisTemplate = getStringRedisTemplate(redisConnectionFactory,
            cacheProperties);
        return new RedisJwtAuthorizationService(redisTemplate, beanFactory,
            applicationServiceLoader);
    }

    /**
     * UserAgentParser
     */
    private final UserAgentParser userAgentParser;

    public JwtProtocolSecurityConfiguration(UserAgentParser userAgentParser) {
        super(userAgentParser);
        this.userAgentParser = userAgentParser;
    }

}
