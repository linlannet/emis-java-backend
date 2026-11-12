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
import net.linlan.emis.protocol.form.FormAuthorizationService;
import net.linlan.emis.protocol.form.RedisFormAuthorizationService;
import net.linlan.emis.protocol.form.authentication.FormAuthenticationFailureEventListener;
import net.linlan.emis.protocol.form.authentication.FormAuthenticationSuccessEventListener;
import net.linlan.emis.protocol.form.configurers.FormAuthorizationServerConfigurer;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import static net.linlan.emis.core.constant.ConfigBeanNameConstants.FORM_PROTOCOL_SECURITY_FILTER_CHAIN;

/**
 *
 * @author Linlan
 * Created on  2023/7/3 21:15
 */
@Configuration(proxyBeanMethods = false)
public class FormProtocolSecurityConfiguration extends AbstractSecurityConfiguration {

    /**
     * FormProtocolSecurityFilterChain
     *
     * @param httpSecurity {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(value = FORM_PROTOCOL_SECURITY_FILTER_CHAIN)
    @RefreshScope
    public SecurityFilterChain formProtocolSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //@formatter:off
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).parentAuthenticationManager(null);
        //Form IDP 配置
        FormAuthorizationServerConfigurer serverConfigurer = new FormAuthorizationServerConfigurer(userAgentParser);
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
     * Form 成功监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link AuthenticationSuccessEvent}
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> formAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        return new FormAuthenticationSuccessEventListener(auditEventPublish);
    }

    /**
     * Form 失败监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link FormAuthenticationFailureEventListener}
     */
    @Bean
    public ApplicationListener<AbstractAuthenticationFailureEvent> formAuthenticationFailureEventListener(AuditEventPublish auditEventPublish) {
        return new FormAuthenticationFailureEventListener(auditEventPublish);
    }

    @Bean
    public FormAuthorizationService formAuthorizationService(RedisConnectionFactory redisConnectionFactory,
                                                             CacheProperties cacheProperties,
                                                             AutowireCapableBeanFactory beanFactory,
                                                             ApplicationServiceLoader applicationServiceLoader) {
        RedisTemplate<String, String> redisTemplate = getStringRedisTemplate(redisConnectionFactory,
            cacheProperties);
        return new RedisFormAuthorizationService(redisTemplate, beanFactory,
            applicationServiceLoader);
    }

    /**
     * UserAgentParser
     */
    private final UserAgentParser userAgentParser;

    public FormProtocolSecurityConfiguration(UserAgentParser userAgentParser) {
        super(userAgentParser);
        this.userAgentParser = userAgentParser;
    }

}
