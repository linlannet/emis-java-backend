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
package net.linlan.emis.protocol.oidc.configurers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import net.linlan.emis.manage.config.AbstractConfigurer;
import net.linlan.emis.manage.config.AuthenticationConfigUtils;
import net.linlan.emis.manage.endpoint.EndpointMatcher;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.oidc.authentication.OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider;
import net.linlan.emis.protocol.oidc.constant.OidcProtocolConstants;
import net.linlan.emis.protocol.oidc.endpoint.authentication.OAuth2AuthorizationResourceOwnerPasswordAuthenticationConverter;
import static net.linlan.emis.support.security.util.HttpSecurityConfigUtils.getPasswordEncoder;
import static net.linlan.emis.support.security.util.HttpSecurityConfigUtils.getUserDetailsService;

/**
 * Token 端点配置器
 *
 * @author Linlan
 * Created on 2023/6/27 21:08
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2TokenEndpointConfigurer extends AbstractConfigurer {

    private RequestMatcher                     requestMatcher;

    private final AuthenticationFailureHandler authenticationFailureHandler = this::sendErrorResponse;

    OAuth2TokenEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    @Override
    public void init(HttpSecurity httpSecurity) {
        this.requestMatcher = new AntPathRequestMatcher(
            ProtocolConstants.OidcEndpointConstants.TOKEN_ENDPOINT, HttpMethod.POST.name());
        List<AuthenticationProvider> authenticationProviders = createDefaultAuthenticationProviders(
            httpSecurity);
        authenticationProviders.forEach(authenticationProvider -> httpSecurity
            .authenticationProvider(postProcess(authenticationProvider)));
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManager.class);

        OAuth2TokenEndpointFilter tokenEndpointFilter = new OAuth2TokenEndpointFilter(
            authenticationManager, ProtocolConstants.OidcEndpointConstants.TOKEN_ENDPOINT);
        //认证详情源
        tokenEndpointFilter.setAuthenticationDetailsSource(
            AuthenticationConfigUtils.getCustomAuthenticationDetailsSource(httpSecurity));
        List<AuthenticationConverter> authenticationConverters = createDefaultAuthenticationConverters();
        tokenEndpointFilter.setAuthenticationConverter(
            new DelegatingAuthenticationConverter(authenticationConverters));
        tokenEndpointFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler);
        httpSecurity.addFilterAfter(postProcess(tokenEndpointFilter), AuthorizationFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, false);
    }

    private static List<AuthenticationConverter> createDefaultAuthenticationConverters() {
        List<AuthenticationConverter> authenticationConverters = new ArrayList<>();
        //密码模式认证转换器
        authenticationConverters
            .add(new OAuth2AuthorizationResourceOwnerPasswordAuthenticationConverter());
        //授权码模式认证转换器
        authenticationConverters.add(new OAuth2AuthorizationCodeAuthenticationConverter());
        //刷新令牌模式认证转换器
        authenticationConverters.add(new OAuth2RefreshTokenAuthenticationConverter());
        return authenticationConverters;
    }

    private static List<AuthenticationProvider> createDefaultAuthenticationProviders(HttpSecurity httpSecurity) {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();

        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils
            .getAuthorizationService(httpSecurity);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils
            .getTokenGenerator(httpSecurity);
        SessionRegistry sessionRegistry = httpSecurity.getSharedObject(SessionRegistry.class);
        UserDetailsService userDetailsService = getUserDetailsService(httpSecurity);
        PasswordEncoder passwordEncoder = getPasswordEncoder(httpSecurity);

        //密码模式认证提供商
        OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider auth2AuthorizationPasswordAuthenticationProvider = new OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider(
            userDetailsService, authorizationService, tokenGenerator, passwordEncoder);
        if (sessionRegistry != null) {
            auth2AuthorizationPasswordAuthenticationProvider.setSessionRegistry(sessionRegistry);
        }
        authenticationProviders.add(auth2AuthorizationPasswordAuthenticationProvider);

        //授权码认证提供商
        OAuth2AuthorizationCodeAuthenticationProvider authorizationCodeAuthenticationProvider = new OAuth2AuthorizationCodeAuthenticationProvider(
            authorizationService, tokenGenerator);
        authenticationProviders.add(authorizationCodeAuthenticationProvider);

        //刷新令牌认证提供商
        OAuth2RefreshTokenAuthenticationProvider refreshTokenAuthenticationProvider = new OAuth2RefreshTokenAuthenticationProvider(
            authorizationService, tokenGenerator);
        authenticationProviders.add(refreshTokenAuthenticationProvider);

        return authenticationProviders;
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        //@formatter:off
        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        OAuth2Error responseError=new OAuth2Error(error.getErrorCode(),error.getDescription(), OidcProtocolConstants.OIDC_ERROR_URI);
        errorHttpResponseConverter.write(responseError, null, httpResponse);
        //@formatter:on
    }

    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

}
