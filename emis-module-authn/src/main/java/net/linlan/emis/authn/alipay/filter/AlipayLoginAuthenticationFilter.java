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
package net.linlan.emis.authn.alipay.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.alipay.easysdk.kernel.Client;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.Context;

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.emis.authn.alipay.AlipayIdentityProviderOAuth2Config;
import net.linlan.emis.authn.alipay.client.AlipayClient;
import net.linlan.emis.authn.alipay.client.AlipaySystemOauthTokenResponse;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.filter.AbstractIdentityProviderAuthenticationProcessingFilter;
import net.linlan.emis.manage.IdentityProviderAuthenticationService;
import net.linlan.emis.manage.client.RegisteredIdentityProviderClient;
import net.linlan.emis.manage.client.RegisteredIdentityProviderClientRepository;
import net.linlan.emis.manage.core.IdentityProviderUserDetails;
import net.linlan.emis.provider.constant.AuthenticationConstants;
import net.linlan.emis.provider.constant.IdentityProviderType;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.utils.UrlUtils;
import static net.linlan.emis.authn.alipay.constant.AlipayAuthenticationConstants.AUTH_CODE;

/**
 * 支付宝 登录过滤器
 *
 * @author Linlan
 */
@SuppressWarnings("DuplicatedCode")
public class AlipayLoginAuthenticationFilter extends
                                             AbstractIdentityProviderAuthenticationProcessingFilter {
    public final static String                DEFAULT_FILTER_PROCESSES_URI = IdentityProviderType.ALIPAY_OAUTH
        .getLoginPathPrefix() + "/" + "{" + AuthenticationConstants.PROVIDER_CODE + "}";
    public static final AntPathRequestMatcher REQUEST_MATCHER              = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.GET.name());

    /**
     * Creates a new instance
     *
     * @param identityProviderAuthenticationService             {@link  IdentityProviderAuthenticationService}
     * @param registeredIdentityProviderClientRepository {@link RegisteredIdentityProviderClientRepository}
     */
    public AlipayLoginAuthenticationFilter(IdentityProviderAuthenticationService identityProviderAuthenticationService,
                                           RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository) {
        super(REQUEST_MATCHER, identityProviderAuthenticationService,
            registeredIdentityProviderClientRepository);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException,
                                                                              IOException,
                                                                              ServletException {
        OAuth2AuthorizationRequest authorizationRequest = getOauth2AuthorizationRequest(request,
            response);
        RequestIdUtils.put(UUID.randomUUID().toString());
        RequestMatcher.MatchResult matcher = REQUEST_MATCHER.matcher(request);
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(AuthenticationConstants.PROVIDER_CODE);
        String providerId = getIdentityProviderId(providerCode);
        //code 支付宝为auth_code
        String code = request.getParameter(AUTH_CODE);
        if (StringUtils.isEmpty(code)) {
            logger.error("支付宝登录 auth_code 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_CODE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // state
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (StringUtils.isEmpty(state)) {
            logger.error("支付宝登录 state 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //验证state
        if (!authorizationRequest.getState().equals(state)) {
            logger.error("支付宝登录 state 匹配不一致，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取身份提供商
        RegisteredIdentityProviderClient<AlipayIdentityProviderOAuth2Config> provider = getRegisteredIdentityProviderClient(
            providerCode);
        AlipayIdentityProviderOAuth2Config idpOauthConfig = provider.getConfig();
        if (Objects.isNull(idpOauthConfig)) {
            logger.error("未查询到支付宝登录配置");
            //无效身份提供商
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_IDP_CONFIG);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        try {
            AlipayClient client = new AlipayClient(
                new Client(new Context(getConfig(idpOauthConfig), "emis")));
            AlipaySystemOauthTokenResponse token = client.getOauthToken(code);
            if (!StringUtils.isBlank(token.getCode())) {
                logger.error("支付宝认证获取 access_token 失败: [" + token.getHttpBody() + "]");
                throw new SupportException(token.getSubMsg());
            }
            //执行逻辑
            IdentityProviderUserDetails identityProviderUserDetails = IdentityProviderUserDetails
                .builder().openId(token.getOpenId()).providerType(IdentityProviderType.ALIPAY_OAUTH)
                .providerCode(providerCode).providerId(providerId).build();
            return attemptAuthentication(request, response, identityProviderUserDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getConfig(AlipayIdentityProviderOAuth2Config idpOauthConfig) {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.appId = idpOauthConfig.getAppId();
        config.signType = "RSA2";
        config.alipayPublicKey = idpOauthConfig.getAlipayPublicKey();
        config.merchantPrivateKey = idpOauthConfig.getAppPrivateKey();
        return config;
    }

    public static String getLoginUrl(String providerId) {
        String url = PropertiesContextHelper.getPortalPublicBaseUrl()
                     + IdentityProviderType.ALIPAY_OAUTH.getLoginPathPrefix() + "/" + providerId;
        return UrlUtils.format(url);
    }

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
