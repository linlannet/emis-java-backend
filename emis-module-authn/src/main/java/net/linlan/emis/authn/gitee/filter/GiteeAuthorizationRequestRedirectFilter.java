/*
 * emis-module-authn - authn认证模块
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
package net.linlan.emis.authn.gitee.filter;

import java.io.IOException;
import java.util.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.emis.authn.gitee.GiteeIdentityProviderOAuth2Config;
import net.linlan.emis.authn.gitee.constant.GiteeAuthenticationConstants;
import net.linlan.emis.manage.client.RegisteredIdentityProviderClient;
import net.linlan.emis.manage.client.RegisteredIdentityProviderClientRepository;
import net.linlan.emis.provider.constant.AuthenticationConstants;
import net.linlan.emis.provider.constant.IdentityProviderType;

/**
 * Gitee 登录请求重定向过滤器
 *
 * @author Linlan
 */
@SuppressWarnings("DuplicatedCode")
public class GiteeAuthorizationRequestRedirectFilter extends OncePerRequestFilter {

    private final Logger                                                     logger                         = LoggerFactory
        .getLogger(GiteeAuthorizationRequestRedirectFilter.class);

    /**
     * AntPathRequestMatcher
     */
    public static final AntPathRequestMatcher                                GITEE_REQUEST_MATCHER          = new AntPathRequestMatcher(
        IdentityProviderType.GITEE_OAUTH
            .getAuthorizationPathPrefix() + "/" + "{" + AuthenticationConstants.PROVIDER_CODE + "}",
        HttpMethod.GET.name());

    /**
     * 重定向策略
     */
    private final RedirectStrategy                                           authorizationRedirectStrategy  = new DefaultRedirectStrategy();

    /**
     * 认证请求存储库
     */
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();

    private static final StringKeyGenerator                                  DEFAULT_STATE_GENERATOR        = new Base64StringKeyGenerator(
        Base64.getUrlEncoder());
    private final RegisteredIdentityProviderClientRepository                 registeredIdentityProviderClientRepository;

    public GiteeAuthorizationRequestRedirectFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository) {
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException,
                                                                      ServletException {
        RequestMatcher.MatchResult matcher = GITEE_REQUEST_MATCHER.matcher(request);
        if (!matcher.isMatch()) {
            filterChain.doFilter(request, response);
            return;
        }
        RequestIdUtils.put(UUID.randomUUID().toString());
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(AuthenticationConstants.PROVIDER_CODE);
        Optional<RegisteredIdentityProviderClient<GiteeIdentityProviderOAuth2Config>> optional = registeredIdentityProviderClientRepository
            .findByCode(providerCode);
        if (!optional.isPresent()) {
            throw new NullPointerException("未查询到身份提供商信息");
        }
        RegisteredIdentityProviderClient<GiteeIdentityProviderOAuth2Config> entity = optional.get();
        GiteeIdentityProviderOAuth2Config config = entity.getConfig();
        Assert.notNull(config, "Gitee 登录配置不能为空");
        //构建授权请求
        //@formatter:off
        HashMap<@Nullable String, @Nullable Object> attributes = Maps.newHashMap();
        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(config.getClientId())
                .scopes(Sets.newHashSet(GiteeAuthenticationConstants.USER_INFO_SCOPE))
                .authorizationUri(GiteeAuthenticationConstants.AUTHORIZATION_REQUEST)
                .redirectUri(GiteeLoginAuthenticationFilter.getLoginUrl(optional.get().getCode()))
                .state(DEFAULT_STATE_GENERATOR.generateKey())
                .attributes(attributes);
        //@formatter:on
        this.sendRedirectForAuthorization(request, response, builder.build());
    }

    private void sendRedirectForAuthorization(HttpServletRequest request,
                                              HttpServletResponse response,
                                              OAuth2AuthorizationRequest authorizationRequest) throws IOException {
        this.authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request,
            response);
        this.authorizationRedirectStrategy.sendRedirect(request, response,
            authorizationRequest.getAuthorizationRequestUri());
    }

    public static RequestMatcher getRequestMatcher() {
        return GITEE_REQUEST_MATCHER;
    }
}
