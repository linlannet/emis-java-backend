/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.configuration;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.console.authentication.*;
import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.core.endpoint.PublicSecretEndpoint;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.core.security.form.FormLoginSecretFilter;
import net.linlan.emis.manage.core.AuthenticationTrustResolverImpl;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetailsSource;
import net.linlan.emis.support.security.constant.SecurityConstants;
import net.linlan.emis.support.userdetails.geo.GeoLocationParser;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import static org.springframework.boot.autoconfigure.security.StaticResourceLocation.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.Customizer.withDefaults;

import static net.linlan.emis.core.constant.AuthnConstants.FE_LOGIN;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static net.linlan.emis.core.constant.SessionConstants.CURRENT_STATUS;
import static net.linlan.emis.core.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;
import static net.linlan.emis.core.endpoint.PublicSecretEndpoint.PUBLIC_SECRET_PATH;
import static net.linlan.emis.core.security.constant.SecuritySettingConstants.*;
import static net.linlan.emis.support.constant.EmisConstants.*;
import static net.linlan.emis.support.security.constant.SecurityConstants.LOGIN_PATH;
import static net.linlan.emis.support.security.constant.SecurityConstants.LOGOUT_PATH;

/**
 * ConsoleSecurityConfiguration
 *
 * @author Linlan
 * Created on  2020/9/27 22:54
 */
@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class ConsoleSecurityConfiguration {

    @Value("${linlan.cors.allowedOriginPatterns:'*'}")
    private String[] allowedOriginPatterns;
    @Value("${linlan.cors.allowCredentials:false}")
    private Boolean  allowCredentials;

    /**
     * webSecurityCustomizer
     *
     * @return {@link WebSecurityCustomizer} WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            new AntPathRequestMatcher("/css/**", GET.name()),
            new AntPathRequestMatcher("/js/**", GET.name()),
            new AntPathRequestMatcher("/webjars/**", GET.name()),
            new AntPathRequestMatcher("/images/**", GET.name()),
            new AntPathRequestMatcher("/favicon.ico", GET.name()));
    }

    /**
     * SecurityFilterChain
     *
     * @param httpSecurity {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @RefreshScope
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        CustomWebAuthenticationDetailsSource authenticationDetailsSource = httpSecurity
            .getSharedObject(ApplicationContext.class)
            .getBean(CustomWebAuthenticationDetailsSource.class);
        //        WebAuthenticationDetailsSource authenticationDetailsSource = getAuthenticationDetailsSource(httpSecurity);
        // @formatter:off
        // 系统配置
        httpSecurity
                //认证请求
                .authorizeHttpRequests(authorizeRequests())
                //表单登录配置
                .formLogin(withFormLoginConfigurerDefaults(authenticationDetailsSource))
                //x509
                .x509(withDefaults())
                //异常处理
                .exceptionHandling(withExceptionConfigurerDefaults())
                //记住我
                .rememberMe(withRememberMeConfigurerDefaults(settingService))
                //CSRF
//                .csrf(withCsrfConfigurerDefaults())
                .csrf().disable()
                //headers
                .headers(withHeadersConfigurerDefaults())
                //cors
                .cors(withCorsConfigurerDefaults())
                //退出配置
                .logout(withLogoutConfigurerDefaults())
                //会话管理器
                .sessionManagement(withSessionManagementConfigurerDefaults(settingService));
        //表单登录解密过滤器
        httpSecurity.addFilterBefore(new FormLoginSecretFilter(), UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
        return httpSecurity.build();
    }

    /**
     * 认证请求
     *
     * @return {@link AuthorizeHttpRequestsConfigurer}
     */
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeRequests() {
        //@formatter:off
        return registry -> {
            //静态资源
            registry.antMatchers(
                    CSS.getPatterns().collect(Collectors.joining()),
                    JAVA_SCRIPT.getPatterns().collect(Collectors.joining()),
                    IMAGES.getPatterns().collect(Collectors.joining()),
                    WEB_JARS.getPatterns().collect(Collectors.joining()),
                    FAVICON.getPatterns().collect(Collectors.joining())).permitAll();
            //当前会话状态
            registry.antMatchers(HttpMethod.GET, CURRENT_STATUS).permitAll();
            //公钥
            registry.antMatchers(HttpMethod.GET, PublicSecretEndpoint.PUBLIC_SECRET_PATH).permitAll();
            //发送OPT
            registry.antMatchers(HttpMethod.POST, LOGIN_PATH + "/opt/send").permitAll();
            //健康检查端点
            registry.antMatchers(webEndpointBasePath+"/**").permitAll();
            //其他全部认证
            registry.antMatchers(V1_API_PATH+"/**").authenticated();
        };
    }

    /**
     * 认证请求
     *
     * @return {@link AuthorizeHttpRequestsConfigurer}
     */
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequests() {
        //@formatter:off
        return registry -> {
            registry.requestMatchers(new AntPathRequestMatcher(EVENT_RECEIVE_PATH+"/{code}")).permitAll();
            //当前会话状态
            registry.requestMatchers(new AntPathRequestMatcher(CURRENT_STATUS, HttpMethod.GET.name())).permitAll();
            //公钥
            registry.requestMatchers(new AntPathRequestMatcher(PUBLIC_SECRET_PATH, HttpMethod.GET.name())).permitAll();
            registry.anyRequest().authenticated();
        };
        //@formatter:on
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
     * session 管理器
     *
     * @return {@link SessionManagementConfigurer}
     */
    public Customizer<SessionManagementConfigurer<HttpSecurity>> withSessionManagementConfigurerDefaults(EmisSettingService settingService) {
        EmisSettingVo setting = settingService.getByKey(SECURITY_SESSION_MAXIMUM);
        return configurer -> {
            configurer.sessionFixation().changeSessionId();
            //用户并发
            String defaultSessionMaximum = SECURITY_BASIC_DEFAULT_SETTINGS
                .get(SECURITY_SESSION_MAXIMUM);
            String sessionMaximum = Objects.isNull(setting) ? defaultSessionMaximum
                : "0".equals(setting.getContent()) ? defaultSessionMaximum : setting.getContent();
            configurer.maximumSessions(Integer.parseInt(sessionMaximum))
                .expiredSessionStrategy(new ConsoleSessionInformationExpiredStrategy());
        };
    }

    /**
     * session 退出过滤器
     *
     * @return {@link LogoutConfigurer}
     */
    public Customizer<LogoutConfigurer<HttpSecurity>> withLogoutConfigurerDefaults() {
        return configurer -> {
            configurer.logoutUrl(LOGOUT_PATH)
                .logoutSuccessHandler(new ConsoleLogoutSuccessHandler()).permitAll();
        };
    }

    /**
     * headers 过滤器
     *
     * @param settingService {@link EmisSettingService}
     * @return {@link HeadersConfigurer}
     */
    public Customizer<HeadersConfigurer<HttpSecurity>> withHeadersConfigurerDefaults(EmisSettingService settingService) {
        List<EmisSettingVo> list = settingService.getByKeys(SECURITY_DEFENSE_POLICY_KEY);
        // 转MAP
        Map<String, String> map = list.stream().collect(Collectors.toMap(EmisSettingVo::getName,
            EmisSettingVo::getContent, (key1, key2) -> key2));
        //内容安全策略
        String contentSecurityPolicy = map
            .containsKey(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY)
                ? map.get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY).replace("\n", "")
                    .replace("\r\n", "")
                : SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                    .get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);

        //@formatter:off
        return configurer -> {
            configurer.xssProtection(xssProtection -> xssProtection.block(true))
            .contentSecurityPolicy(config-> config.policyDirectives(contentSecurityPolicy))
            .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
            .contentTypeOptions(contentTypeOptionsConfig-> {})
            .permissionsPolicy(permissionsPolicyConfig -> permissionsPolicyConfig.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"));
        };
        //@formatter:on
    }

    /**
     * headers 过滤器
     *
     * @return {@link HeadersConfigurer}
     */
    public Customizer<HeadersConfigurer<HttpSecurity>> withHeadersConfigurerDefaults() {
        //@formatter:off
        return configurer -> {
            configurer.xssProtection(xssProtection -> xssProtection.disable());
            configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
            configurer.contentSecurityPolicy(
                    "default-src 'self' data:; " +
                            "frame-src 'self' login.dingtalk.com open.weixin.qq.com open.work.weixin.qq.com passport.feishu.cn data:; " +
                            "frame-ancestors 'self' https://linlan.net/emis data:; " +
                            "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com sf3-cn.feishucdn.com;" +
                            "style-src 'self' https://fonts.googleapis.com https://cdn.jsdelivr.net 'unsafe-inline'; " +
                            "img-src 'self' https://img.alicdn.com https://static-legacy.dingtalk.com  https://joeschmoe.io https://api.multiavatar.com data:; " +
                            "font-src 'self' https://fonts.gstatic.com data:; "+
                            "worker-src 'self' https://storage.googleapis.com blob:;");
            configurer.referrerPolicy(
                    ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN);
            configurer
                    .referrerPolicy(
                            ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    .and().permissionsPolicy().policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()");
            configurer.frameOptions().deny();
        };
        //@formatter:on
    }

    /**
     * form
     *
     * @return {@link FormLoginConfigurer}
     */
    public Customizer<FormLoginConfigurer<HttpSecurity>> withFormLoginConfigurerDefaults(CustomWebAuthenticationDetailsSource authenticationDetailsSource) {
        // @formatter:off
        return configurer -> {
            configurer.loginPage(FE_LOGIN);
            configurer.loginProcessingUrl(SecurityConstants.FORM_LOGIN);
            configurer.successHandler(new ConsoleAuthenticationSuccessHandler(administratorRepository,  auditEventPublish));
            configurer.failureHandler(new ConsoleAuthenticationFailureHandler());
            configurer.authenticationDetailsSource(authenticationDetailsSource); // 关键注入
        };
        // @formatter:on
    }

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
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return configurer -> configurer.configurationSource(source);
    }

    /**
     * 异常处理器
     *
     * @return {@link ExceptionHandlingConfigurer}
     */
    public Customizer<ExceptionHandlingConfigurer<HttpSecurity>> withExceptionConfigurerDefaults() {
        return configurer -> {
            configurer
                .authenticationEntryPoint(new ConsoleAuthenticationEntryPoint(userAgentParser));
            configurer.accessDeniedHandler(new ConsoleAccessDeniedHandler());
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
    public Customizer<RememberMeConfigurer<HttpSecurity>> withRememberMeConfigurerDefaults(EmisSettingService settingService) {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(false);
        EmisSettingVo setting = settingService.getByKey(SECURITY_BASIC_REMEMBER_ME_VALID_TIME);
        String rememberMeValiditySeconds = Objects.isNull(setting)
            ? SECURITY_BASIC_DEFAULT_SETTINGS.get(SECURITY_BASIC_REMEMBER_ME_VALID_TIME)
            : setting.getContent();
        rememberMeServices.setValiditySeconds(Integer.parseInt(rememberMeValiditySeconds));
        return configurer -> configurer.rememberMeServices(rememberMeServices);
    }

    /**
     * csrf
     *
     * @return {@link CsrfConfigurer}
     */
    public Customizer<CsrfConfigurer<HttpSecurity>> withCsrfConfigurerDefaults(RequestMatcher... ignoringRequestMatchers) {
        return csrf -> {
            CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
            repository.setCookieName(DEFAULT_CSRF_COOKIE_NAME);
            repository.setHeaderName(DEFAULT_CSRF_HEADER_NAME);
            csrf.ignoringRequestMatchers(ignoringRequestMatchers).csrfTokenRepository(repository);
        };
    }

    /**
     * 表单登录
     *
     * @return {@link FormLoginConfigurer}
     */
    public FormLoginConfigurer<HttpSecurity> withFormLoginConfigurer() {
        // @formatter:off
        AuthenticationSuccessHandler successHandler = new ConsoleAuthenticationSuccessHandler(administratorRepository,  auditEventPublish);
        FormLoginConfigurer<HttpSecurity> configurer=new FormLoginConfigurer<>();
        configurer.successHandler(successHandler);
        configurer.failureHandler(new ConsoleAuthenticationFailureHandler());
        return configurer;
        // @formatter:on
    }

    /**
     * 身份验证成功事件监听器
     *
     * @return {@link  ConsoleAuthenticationSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleAuthenticationSuccessEventListener authenticationSuccessEventListener() {
        //        return new ConsoleAuthenticationSuccessEventListener(geoLocationParser);
        return new ConsoleAuthenticationSuccessEventListener();
    }

    /**
     * 身份验证失败事件监听器
     *
     * @return {@link  ConsoleAuthenticationFailureEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleAuthenticationFailureEventListener authenticationFailureEventListener() {
        return new ConsoleAuthenticationFailureEventListener();
    }

    /**
     * 退出成功事件监听器
     *
     * @return {@link  ConsoleLogoutSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleLogoutSuccessEventListener logoutSuccessEventListener() {
        return new ConsoleLogoutSuccessEventListener();
    }

    /**
     * CustomWebAuthenticationDetailsSource
     *
     * @param geoLocationParser {@link GeoLocationParser}
     * @param userAgentParser {@link UserAgentParser}
     * @return {@link CustomWebAuthenticationDetailsSource}
     */
    @Bean
    public CustomWebAuthenticationDetailsSource authenticationDetailsSource(GeoLocationParser geoLocationParser,
                                                                            UserAgentParser userAgentParser) {
        return new CustomWebAuthenticationDetailsSource(geoLocationParser, userAgentParser);
    }

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    /**
     * EmisSettingService
     */
    private final EmisSettingService       settingService;

    /**
     * AuditEventPublish
     */
    private final AuditEventPublish       auditEventPublish;

    /**
     * GeoLocationService
     */
    private final GeoLocationParser       geoLocationParser;

    /**
     * 管理端接口地址前缀
     */
    @Value("${management.endpoints.web.base-path:/actuator}")
    private String                        webEndpointBasePath;

    /**
     * UserAgentParser
     */
    private final UserAgentParser         userAgentParser;

}
