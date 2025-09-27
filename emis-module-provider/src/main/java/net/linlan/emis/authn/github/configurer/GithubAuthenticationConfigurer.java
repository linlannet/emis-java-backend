/*
 * emis-module-provider - provider身份提供商
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
package net.linlan.emis.authn.github.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import lombok.NonNull;
import lombok.Setter;

import net.linlan.emis.authn.github.filter.GithubOAuth2AuthorizationRequestRedirectFilter;
import net.linlan.emis.authn.github.filter.GithubOAuth2LoginAuthenticationFilter;
import net.linlan.emis.manage.IdentityProviderAuthenticationService;
import net.linlan.emis.manage.client.RegisteredIdentityProviderClientRepository;
import static net.linlan.emis.support.security.util.HttpSecurityFilterOrderRegistrationUtils.putFilterBefore;

/**
 * 认证配置
 *
 * @author Linlan
 */
public final class GithubAuthenticationConfigurer extends
                                                  AbstractAuthenticationFilterConfigurer<HttpSecurity, GithubAuthenticationConfigurer, GithubOAuth2LoginAuthenticationFilter> {
    @Setter
    @NonNull
    private String                                           loginProcessingUrl = GithubOAuth2LoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;
    private final RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository;
    private final IdentityProviderAuthenticationService      identityProviderAuthenticationService;

    GithubAuthenticationConfigurer(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                   IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        Assert.notNull(registeredIdentityProviderClientRepository,
            "registeredIdentityProviderClientRepository must not be null");
        Assert.notNull(identityProviderAuthenticationService, "userIdpService must not be null");
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
        this.identityProviderAuthenticationService = identityProviderAuthenticationService;
    }

    /**
     * Create the {@link RequestMatcher} given a loginProcessingUrl
     *
     * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the
     *                           loginProcessingUrl
     * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
     */
    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.GET.name());
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        //Github扫码登录认证
        this.setAuthenticationFilter(new GithubOAuth2LoginAuthenticationFilter(
            registeredIdentityProviderClientRepository, identityProviderAuthenticationService));
        putFilterBefore(http, this.getAuthenticationFilter(),
            OAuth2LoginAuthenticationFilter.class);

        //GITHUB请求重定向
        http.addFilterBefore(
            new GithubOAuth2AuthorizationRequestRedirectFilter(
                registeredIdentityProviderClientRepository),
            OAuth2AuthorizationRequestRedirectFilter.class);

        //登录处理地址
        super.loginProcessingUrl(this.loginProcessingUrl);
        super.init(http);
    }

    public RequestMatcher getRequestMatcher() {
        return new OrRequestMatcher(
            GithubOAuth2AuthorizationRequestRedirectFilter.getRequestMatcher(),
            GithubOAuth2LoginAuthenticationFilter.getRequestMatcher());
    }

    public static GithubAuthenticationConfigurer githubOAuth2(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                                              IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        return new GithubAuthenticationConfigurer(registeredIdentityProviderClientRepository,
            identityProviderAuthenticationService);
    }

}
