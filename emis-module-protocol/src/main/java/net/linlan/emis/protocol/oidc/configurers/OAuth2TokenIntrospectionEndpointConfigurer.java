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
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenIntrospectionEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2TokenIntrospectionAuthenticationConverter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import net.linlan.emis.manage.config.AbstractConfigurer;
import net.linlan.emis.manage.endpoint.EndpointMatcher;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.oidc.authorization.client.OidcConfigRegisteredClientRepositoryWrapper;
import net.linlan.emis.protocol.oidc.constant.OidcProtocolConstants;

/**
 * Configurer for the OAuth 2.0 Token Introspection Endpoint.
 *
 * @author Linlan
 * Created on 2023/6/27 21:28
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2TokenIntrospectionEndpointConfigurer extends AbstractConfigurer {

    private RequestMatcher                     requestMatcher;

    private final AuthenticationFailureHandler authenticationFailureHandler = this::sendErrorResponse;

    OAuth2TokenIntrospectionEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    @Override
    public void init(HttpSecurity httpSecurity) {
        this.requestMatcher = new AntPathRequestMatcher(
            ProtocolConstants.OidcEndpointConstants.TOKEN_INTROSPECTION_ENDPOINT,
            HttpMethod.POST.name());

        List<AuthenticationProvider> authenticationProviders = createDefaultAuthenticationProviders(
            httpSecurity);
        authenticationProviders.forEach(authenticationProvider -> httpSecurity
            .authenticationProvider(postProcess(authenticationProvider)));
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManager.class);

        OAuth2TokenIntrospectionEndpointFilter introspectionEndpointFilter = new OAuth2TokenIntrospectionEndpointFilter(
            authenticationManager,
            ProtocolConstants.OidcEndpointConstants.TOKEN_INTROSPECTION_ENDPOINT);
        introspectionEndpointFilter
            .setAuthenticationFailureHandler(this.authenticationFailureHandler);
        List<AuthenticationConverter> authenticationConverters = createDefaultAuthenticationConverters();
        introspectionEndpointFilter.setAuthenticationConverter(
            new DelegatingAuthenticationConverter(authenticationConverters));
        httpSecurity.addFilterAfter(postProcess(introspectionEndpointFilter),
            AuthorizationFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, false);
    }

    private static List<AuthenticationConverter> createDefaultAuthenticationConverters() {
        List<AuthenticationConverter> authenticationConverters = new ArrayList<>();

        authenticationConverters.add(new OAuth2TokenIntrospectionAuthenticationConverter());

        return authenticationConverters;
    }

    private static List<AuthenticationProvider> createDefaultAuthenticationProviders(HttpSecurity httpSecurity) {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();

        OAuth2TokenIntrospectionAuthenticationProvider tokenIntrospectionAuthenticationProvider = new OAuth2TokenIntrospectionAuthenticationProvider(
            new OidcConfigRegisteredClientRepositoryWrapper(
                OAuth2ConfigurerUtils.getRegisteredClientRepository(httpSecurity)),
            OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity));
        authenticationProviders.add(tokenIntrospectionAuthenticationProvider);

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
