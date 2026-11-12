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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.Lists;

import net.linlan.emis.core.security.constant.SecuritySettingConstants;
import net.linlan.emis.manage.core.AuthenticationTrustResolverImpl;
import net.linlan.emis.portal.authentication.PortalAccessDeniedHandler;
import net.linlan.emis.portal.authentication.PortalAuthenticationEntryPoint;
import net.linlan.emis.portal.authentication.PortalLogoutSuccessHandler;
import net.linlan.emis.portal.authentication.PortalSessionInformationExpiredStrategy;
import net.linlan.emis.support.cache.KeyStringRedisSerializer;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import static net.linlan.emis.core.security.constant.SecuritySettingConstants.*;
import static net.linlan.emis.support.constant.EmisConstants.DEFAULT_CSRF_COOKIE_NAME;
import static net.linlan.emis.support.constant.EmisConstants.DEFAULT_CSRF_HEADER_NAME;
import static net.linlan.emis.support.security.constant.SecurityConstants.LOGOUT_PATH;

/**
 * AbstractConfiguration
 *
 * @author Linlan
 * Created on  2023/7/3 21:08
 */
public class AbstractSecurityConfiguration {

    @Value("${linlan.cors.allowedOriginPatterns:'*'}")
    private String[] allowedOriginPatterns;
    @Value("${linlan.cors.allowCredentials:false}")
    private Boolean  allowCredentials;

    /**
     * Cors 过滤器
     *
     * @return {@link HeadersConfigurer}
     */
    public Customizer<CorsConfigurer<HttpSecurity>> withCorsConfigurerDefaults() {
        CorsConfiguration configuration = new CorsConfiguration()
            .setAllowedOriginPatterns(Lists.newArrayList(allowedOriginPatterns))
            .applyPermitDefaultValues();
        configuration.setAllowCredentials(allowCredentials);
        configuration.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return configurer -> configurer.configurationSource(source);
    }

    /**
     * session 管理器
     *
     * @return {@link SessionManagementConfigurer}
     */
    public Customizer<SessionManagementConfigurer<HttpSecurity>> withSessionManagementConfigurerDefaults() {
        //@formatter:off
        return configurer -> {
            configurer.sessionFixation().changeSessionId();
            //用户并发
            String defaultSessionMaximum = SecuritySettingConstants.SECURITY_BASIC_DEFAULT_SETTINGS
                .get(SECURITY_SESSION_MAXIMUM);
            configurer.maximumSessions(Integer.parseInt(defaultSessionMaximum))
                .expiredSessionStrategy(new PortalSessionInformationExpiredStrategy());
        };
        //@formatter:on
    }

    /**
     * session 退出过滤器
     *
     * @return {@link LogoutConfigurer}
     */
    public Customizer<LogoutConfigurer<HttpSecurity>> withLogoutConfigurerDefaults() {
        return configurer -> {
            configurer.logoutUrl(LOGOUT_PATH);
            configurer.logoutSuccessHandler(new PortalLogoutSuccessHandler());
            configurer.permitAll();
        };
    }

    /**
     * headers 过滤器
     *
     * @return {@link HeadersConfigurer}
     */
    public Customizer<HeadersConfigurer<HttpSecurity>> withHeadersConfigurerDefaults() {
        //内容安全策略
        String contentSecurityPolicy = SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
            .get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);

        //@formatter:off
        return configurer -> {
            configurer
                    .xssProtection(xssProtection -> xssProtection.block(true))
                    .contentSecurityPolicy(config-> config.policyDirectives(contentSecurityPolicy))
                    .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                    .contentTypeOptions(contentTypeOptionsConfig-> {})
                    .permissionsPolicy(permissionsPolicyConfig -> permissionsPolicyConfig.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"));
        };
        //@formatter:on
    }

    /**
     * 异常处理器
     *
     * @return {@link ExceptionHandlingConfigurer}
     */
    public Customizer<ExceptionHandlingConfigurer<HttpSecurity>> withExceptionConfigurerDefaults() {
        return configurer -> {
            configurer
                //                .authenticationEntryPoint(new PortalAuthenticationEntryPoint(userAgentParser));
                .authenticationEntryPoint(new PortalAuthenticationEntryPoint());
            configurer.accessDeniedHandler(new PortalAccessDeniedHandler());
            configurer
                .withObjectPostProcessor(new ObjectPostProcessor<ExceptionTranslationFilter>() {
                    @Override
                    public <O extends ExceptionTranslationFilter> O postProcess(O filter) {
                        filter
                            .setAuthenticationTrustResolver(new AuthenticationTrustResolverImpl());
                        return filter;
                    }
                });
        };
    }

    /**
     * withRememberMeConfigurerDefaults
     *
     * @return {@link RememberMeConfigurer}
     */
    public Customizer<RememberMeConfigurer<HttpSecurity>> withRememberMeConfigurerDefaults() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(false);
        String rememberMeValiditySeconds = SecuritySettingConstants.SECURITY_BASIC_DEFAULT_SETTINGS
            .get(SECURITY_BASIC_REMEMBER_ME_VALID_TIME);
        rememberMeServices.setValiditySeconds(Integer.parseInt(rememberMeValiditySeconds));
        return configurer -> configurer.rememberMeServices(rememberMeServices);
    }

    /**
     * csrf
     *
     * @return {@link CsrfConfigurer}
     */
    public Customizer<CsrfConfigurer<HttpSecurity>> withCsrfConfigurerDefaults(RequestMatcher... requestMatcher) {
        return csrf -> {
            CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
            repository.setCookieName(DEFAULT_CSRF_COOKIE_NAME);
            repository.setHeaderName(DEFAULT_CSRF_HEADER_NAME);
            csrf.ignoringRequestMatchers(requestMatcher);
        };
    }

    protected RedisTemplate<String, String> getStringRedisTemplate(@Nullable RedisConnectionFactory connectionFactory,
                                                                   CacheProperties cacheProperties) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        KeyStringRedisSerializer keyStringRedisSerializer = new KeyStringRedisSerializer(
            cacheProperties.getRedis().getKeyPrefix());
        redisTemplate.setKeySerializer(keyStringRedisSerializer);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     *  安全上下文
     *
     * @return {@link SecurityContextConfigurer}
     */
    public Customizer<SecurityContextConfigurer<HttpSecurity>> securityContext() {
        return configurer -> {
        };
    }

    /**
     * 用户代理
     */
    private final UserAgentParser userAgentParser;

    public AbstractSecurityConfiguration(UserAgentParser userAgentParser) {
        Assert.notNull(userAgentParser, "The userAgentParser cannot be null");
        this.userAgentParser = userAgentParser;
    }

}
