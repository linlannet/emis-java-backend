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
package net.linlan.emis.protocol.oidc.endpoint;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration;
import org.springframework.security.oauth2.server.authorization.oidc.http.converter.OidcProviderConfigurationHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.oidc.authentication.OAuth2AuthorizationImplicitRequestAuthenticationProvider;
import net.linlan.emis.protocol.oidc.authentication.OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken;
import net.linlan.emis.protocol.oidc.endpoint.authentication.OAuth2AuthorizationImplicitRequestAuthenticationConverter;

/**
 * A {@code Filter} that processes OpenID Provider Configuration Requests.
 *
 * @author Linlan
 * @see <a target="_blank" href="https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderConfigurationRequest">4.1. OpenID Provider Configuration Request</a>
 */
public final class OidcProviderConfigurationEndpointFilter extends OncePerRequestFilter {
    /**
     * The default endpoint {@code URI} for OpenID Provider Configuration requests.
     */
    private static final String  DEFAULT_OIDC_PROVIDER_CONFIGURATION_ENDPOINT_URI = ProtocolConstants.OidcEndpointConstants.WELL_KNOWN_OPENID_CONFIGURATION;

    private final RequestMatcher requestMatcher;

    public OidcProviderConfigurationEndpointFilter() {
        requestMatcher = new AntPathRequestMatcher(DEFAULT_OIDC_PROVIDER_CONFIGURATION_ENDPOINT_URI,
            HttpMethod.GET.name());
    }

    public OidcProviderConfigurationEndpointFilter(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    private final OidcProviderConfigurationHttpMessageConverter providerConfigurationHttpMessageConverter = new OidcProviderConfigurationHttpMessageConverter();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {

        if (!this.requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthorizationServerContext authorizationServerContext = AuthorizationServerContextHolder
            .getContext();
        String issuer = authorizationServerContext.getIssuer();
        AuthorizationServerSettings authorizationServerSettings = authorizationServerContext
            .getAuthorizationServerSettings();

        OidcProviderConfiguration.Builder providerConfiguration = OidcProviderConfiguration
            .builder().issuer(issuer)
            //认证端点
            .authorizationEndpoint(asUrl(PropertiesContextHelper.getPortalPublicBaseUrl(),
                authorizationServerSettings.getAuthorizationEndpoint()))
            //令牌端点
            .tokenEndpoint(asUrl(PropertiesContextHelper.getPortalPublicBaseUrl(),
                authorizationServerSettings.getTokenEndpoint()))
            //令牌端点身份验证方法
            .tokenEndpointAuthenticationMethods(clientAuthenticationMethods())
            //JWK
            .jwkSetUrl(asUrl(PropertiesContextHelper.getPortalPublicBaseUrl(),
                authorizationServerSettings.getJwkSetEndpoint()))
            //用户信息端点
            .userInfoEndpoint(asUrl(PropertiesContextHelper.getPortalPublicBaseUrl(),
                authorizationServerSettings.getOidcUserInfoEndpoint())) //响应类型
            .responseTypes(responseTypes())
            //动态处理授权类型
            .grantTypes(grantTypes())
            //令牌吊销端点
            .tokenRevocationEndpoint(asUrl(PropertiesContextHelper.getPortalPublicBaseUrl(),
                authorizationServerSettings.getTokenRevocationEndpoint()))
            //令牌吊销端点身份验证方法
            .tokenRevocationEndpointAuthenticationMethods(clientAuthenticationMethods())
            //令牌解析端点
            .tokenIntrospectionEndpoint(asUrl(PropertiesContextHelper.getPortalPublicBaseUrl(),
                authorizationServerSettings.getTokenIntrospectionEndpoint()))
            //令牌解析身份验证方法
            .tokenIntrospectionEndpointAuthenticationMethods(clientAuthenticationMethods())
            .subjectType("public")
            //ID_TOKEN签名端点
            .idTokenSigningAlgorithm(SignatureAlgorithm.RS256.getName())
            //scope
            .scopes(scopes());

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        this.providerConfigurationHttpMessageConverter.write(providerConfiguration.build(),
            MediaType.APPLICATION_JSON, httpResponse);
    }

    /**
     * scopes
     *
     * @return {@link List}
     */
    private static Consumer<List<String>> scopes() {
        return strings -> {
            strings.add(OidcScopes.OPENID);
            strings.add(OidcScopes.PHONE);
            strings.add(OidcScopes.EMAIL);
            strings.add(OidcScopes.PROFILE);
        };
    }

    /**
     * 响应类型
     *
     * @return {@link List}
     */
    private static Consumer<List<String>> responseTypes() {
        return strings -> {
            strings.add(OAuth2AuthorizationResponseType.CODE.getValue());
            strings.add(OAuth2AuthorizationImplicitRequestAuthenticationConverter.TOKEN.getValue());
            strings
                .add(OAuth2AuthorizationImplicitRequestAuthenticationConverter.ID_TOKEN.getValue());
        };
    }

    /**
     * 授权类型
     *
     * @return {@link List}
     */
    private static Consumer<List<String>> grantTypes() {
        return strings -> {
            strings.add(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
            strings.add(
                OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken.PASSWORD.getValue());
            strings
                .add(OAuth2AuthorizationImplicitRequestAuthenticationProvider.IMPLICIT.getValue());
            strings.add(AuthorizationGrantType.REFRESH_TOKEN.getValue());
        };
    }

    /**
     * 客户端身份验证方法
     *
     * @return {@link List}
     */
    private static Consumer<List<String>> clientAuthenticationMethods() {
        return (authenticationMethods) -> {
            authenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue());
            authenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue());
            authenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue());
            authenticationMethods.add(ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue());
        };
    }

    private static String asUrl(String issuer, String endpoint) {
        return UriComponentsBuilder.fromUriString(issuer).path(endpoint).build().toUriString();
    }

}
