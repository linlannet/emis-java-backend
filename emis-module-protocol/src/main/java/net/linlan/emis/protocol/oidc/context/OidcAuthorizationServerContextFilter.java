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
package net.linlan.emis.protocol.oidc.context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson2.JSONObject;

import lombok.Getter;

import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.core.exception.app.AppAccessDeniedException;
import net.linlan.emis.manage.endpoint.EndpointMatcher;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.base.exception.AppNotConfigException;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.oidc.OidcApplicationService;
import net.linlan.emis.protocol.oidc.model.OidcProtocolConfig;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.support.userdetails.Application;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.utils.UrlUtils;
import net.linlan.utils.http.HttpRequestUtils;
import net.linlan.utils.ip.IPUtils;
import static org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames.Token.*;

/**
 * 上下文过滤器
 *
 * @author Linlan
 * Created on 2023/6/26 23:55
 */
public final class OidcAuthorizationServerContextFilter extends OncePerRequestFilter {

    public static final String             SEPARATE = "--------------protocol-oidc------------------------";

    @Getter
    private final List<EndpointMatcher>    endpointMatchers;

    private final ApplicationServiceLoader applicationServiceLoader;

    public OidcAuthorizationServerContextFilter(List<EndpointMatcher> endpointMatchers,
                                                ApplicationServiceLoader applicationServiceLoader) {
        Assert.notNull(endpointMatchers, "requestMatchers cannot be null");
        Assert.notNull(applicationServiceLoader, "applicationServiceLoader cannot be null");
        this.applicationServiceLoader = applicationServiceLoader;
        this.endpointMatchers = endpointMatchers;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        boolean match = false, access = false;
        Map<String, String> variables = new HashMap<>(16);
        for (EndpointMatcher endpointMatcher : endpointMatchers) {
            RequestMatcher requestMatcher = endpointMatcher.getRequestMatcher();
            if (requestMatcher.matches(request)) {
                match = true;
                access = endpointMatcher.getAccess();
                variables = requestMatcher.matcher(request).getVariables();
            }
        }
        if (!match) {
            filterChain.doFilter(request, response);
            return;
        }
        String appCode = variables.get(ProtocolConstants.APP_CODE);
        //校验访问权限（未登录不校验访问权限）
        if (access && SecurityUtils.isAuthenticated()) {
            UserDetails userDetails = SecurityUtils.getCurrentUser();
            Collection<Application> applications = userDetails.getApplications();
            if (applications.stream()
                .noneMatch(application -> application.getCode().equals(appCode))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    new AppAccessDeniedException().getMessage());
                return;
            }
        }
        try {
            //@formatter:off
            if (this.logger.isTraceEnabled()) {
                String body = IOUtils.toString(request.getInputStream(),StandardCharsets.UTF_8).replaceAll("\\s+", " ");
                String logs = "\n" +
                        "┣ " + SEPARATE + "\n" +
                        "┣ App: " + appCode + "\n" +
                        "┣ Request url: " + request.getMethod() + " " + request.getRequestURL() + "\n" +
                        "┣ Request ip: " + IPUtils.getIpAddr(request) + "\n" +
                        "┣ Request headers: " + JSONObject.toJSONString(HttpRequestUtils.getRequestHeaders(request)) + "\n" +
                        "┣ Request parameters: " + JSONObject.toJSONString(request.getParameterMap()) + "\n" +
                        "┣ Request payload: " + StringUtils.defaultIfBlank(body, "-") + "\n" +
                        "┣ " + SEPARATE;
                logger.trace(logs);
            }
            OidcApplicationService applicationService = (OidcApplicationService) applicationServiceLoader.getApplicationServiceByAppCode(appCode);
            OidcProtocolConfig config = applicationService.getProtocolConfig(appCode);
            if (Objects.isNull(config)) {
                throw new AppNotExistException();
            }
            if (!config.getConfigured()){
                throw new AppNotConfigException();
            }
            //封装 ProviderSettings
            StringSubstitutor sub = new StringSubstitutor(variables, "{", "}");
            AuthorizationServerSettings providerSettings = AuthorizationServerSettings.builder()
                    .issuer(sub.replace(UrlUtils.format(PropertiesContextHelper.getPortalPublicBaseUrl() + ProtocolConstants.OidcEndpointConstants.OIDC_AUTHORIZE_PATH)))
                    .authorizationEndpoint(sub.replace(ProtocolConstants.OidcEndpointConstants.AUTHORIZATION_ENDPOINT))
                    .tokenEndpoint(sub.replace(ProtocolConstants.OidcEndpointConstants.TOKEN_ENDPOINT))
                    .jwkSetEndpoint(sub.replace(ProtocolConstants.OidcEndpointConstants.JWK_SET_ENDPOINT))
                    .oidcClientRegistrationEndpoint(sub.replace(ProtocolConstants.OidcEndpointConstants.OIDC_CLIENT_REGISTRATION_ENDPOINT))
                    .tokenIntrospectionEndpoint(sub.replace(ProtocolConstants.OidcEndpointConstants.TOKEN_INTROSPECTION_ENDPOINT))
                    .tokenRevocationEndpoint(sub.replace(ProtocolConstants.OidcEndpointConstants.TOKEN_REVOCATION_ENDPOINT))
                    .oidcUserInfoEndpoint(sub.replace(ProtocolConstants.OidcEndpointConstants.OIDC_USER_INFO_ENDPOINT))
                    .setting(ACCESS_TOKEN_FORMAT,config.getAccessTokenFormat())
                    .setting(REUSE_REFRESH_TOKENS,config.getReuseRefreshToken())
                    .setting(REFRESH_TOKEN_TIME_TO_LIVE,config.getRefreshTokenTimeToLive())
                    .setting(AUTHORIZATION_CODE_TIME_TO_LIVE,config.getAuthorizationCodeTimeToLive())
                    .setting(ACCESS_TOKEN_TIME_TO_LIVE,config.getAccessTokenTimeToLive())
                    .setting(ID_TOKEN_SIGNATURE_ALGORITHM,config.getIdTokenSignatureAlgorithm())
                    .build();
            AuthorizationServerContext providerContext = new AuthorizationServerContext() {
                @Override
                public String getIssuer() {
                    return providerSettings.getIssuer();
                }
                @Override
                public AuthorizationServerSettings getAuthorizationServerSettings() {
                    return providerSettings;
                }
            };
            //设置上下文
            AuthorizationServerContextHolder.setContext(providerContext);
            ApplicationContextHolder.setContext(new DefaultApplicationContext(config));
            filterChain.doFilter(request, response);
            //@formatter:on
        } finally {
            AuthorizationServerContextHolder.resetContext();
            ApplicationContextHolder.resetContext();
        }
    }

    private class DefaultApplicationContext implements ApplicationContext {

        private ApplicationContext config;

        private DefaultApplicationContext(OidcProtocolConfig config) {
            Assert.notNull(config, "config cannot be null");
            this.config = (ApplicationContext) config;
        }

        /**
         * 获取应用ID
         *
         * @return {@link String}
         */
        @Override
        public String getAppId() {
            return this.config.getAppId();
        }

        /**
         * 获取客户端ID
         *
         * @return {@link String}
         */
        @Override
        public String getClientId() {
            return config.getClientId();
        }

        /**
         * 获取应用编码
         *
         * @return {@link String}
         */
        @Override
        public String getAppCode() {
            return config.getAppCode();
        }

        /**
         * 获取应用模版
         *
         * @return {@link String}
         */
        @Override
        public String getAppTemplate() {
            return config.getAppTemplate();
        }

        /**
         * 获取协议配置
         *
         * @return {@link Map}
         */
        @Override
        public Map<String, Object> getConfig() {
            Map<String, Object> config = new HashMap<>(16);
            config.put(OidcProtocolConfig.class.getName(), this.config);
            return config;
        }

    }

}
