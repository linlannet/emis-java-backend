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
package net.linlan.emis.authn.dingtalk.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponse;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.authn.dingtalk.DingTalkIdentityProviderOAuth2Config;
import net.linlan.emis.authn.dingtalk.constant.DingTalkAuthenticationConstants;
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

/**
 * 钉钉认证过滤器
 * https://open.dingtalk.com/document/orgapp-server/tutorial-obtaining-user-personal-information
 *
 * @author Linlan
 */
public class DingtalkOauth2AuthenticationFilter extends
                                                AbstractIdentityProviderAuthenticationProcessingFilter {
    public final static String                DEFAULT_FILTER_PROCESSES_URI = IdentityProviderType.DINGTALK_OAUTH
        .getLoginPathPrefix() + "/" + "{" + AuthenticationConstants.PROVIDER_CODE + "}";
    /**
     * AntPathRequestMatcher
     */
    public static final AntPathRequestMatcher REQUEST_MATCHER              = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.GET.name());

    /**
     * Creates a new instance
     *
     * @param registeredIdentityProviderClientRepository the {@link RegisteredIdentityProviderClientRepository}
     * @param identityProviderAuthenticationService  {@link  IdentityProviderAuthenticationService}
     */
    public DingtalkOauth2AuthenticationFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                              IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(REQUEST_MATCHER, identityProviderAuthenticationService,
            registeredIdentityProviderClientRepository);
    }

    /**
     * 钉钉认证
     *
     * @param request  {@link  HttpServletRequest}
     * @param response {@link  HttpServletRequest}
     * @return {@link  HttpServletRequest}
     * @throws AuthenticationException {@link  AuthenticationException} AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException,
                                                                              IOException {
        OAuth2AuthorizationRequest authorizationRequest = getOauth2AuthorizationRequest(request,
            response);
        RequestIdUtils.put(UUID.randomUUID().toString());
        RequestMatcher.MatchResult matcher = REQUEST_MATCHER.matcher(request);
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(AuthenticationConstants.PROVIDER_CODE);
        String providerId = getIdentityProviderId(providerCode);
        //code 钉钉新版登录为 authCode
        String code = request.getParameter(DingTalkAuthenticationConstants.AUTH_CODE);
        if (StringUtils.isEmpty(code)) {
            logger.error("钉钉登录 code 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_CODE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // state
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (StringUtils.isEmpty(state)) {
            logger.error("钉钉登录 state 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //验证state
        if (!authorizationRequest.getState().equals(state)) {
            logger.error("钉钉登录 state 匹配不一致，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取身份提供商
        RegisteredIdentityProviderClient<DingTalkIdentityProviderOAuth2Config> provider = getRegisteredIdentityProviderClient(
            providerCode);
        DingTalkIdentityProviderOAuth2Config idpOauthConfig = provider.getConfig();
        if (Objects.isNull(idpOauthConfig)) {
            logger.error("未查询到钉钉登录配置");
            //无效身份提供商
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_IDP_CONFIG);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        String accessToken = getToken(code, idpOauthConfig);
        Config config = new Config();
        //        config.protocol = "https";
        //        config.regionId = "central";
        config.setProtocol("https");
        config.setRegionId("central");
        GetUserHeaders getUserHeaders = new GetUserHeaders();
        //        getUserHeaders.xAcsDingtalkAccessToken = accessToken;
        getUserHeaders.setXAcsDingtalkAccessToken(accessToken);

        //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
        GetUserResponse user;
        try {
            com.aliyun.dingtalkcontact_1_0.Client client = new com.aliyun.dingtalkcontact_1_0.Client(
                config);
            user = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions());
        } catch (Exception e) {
            logger.error("钉钉认证获取用户信息失败: {}", e);
            throw new SupportException("钉钉认证获取用户信息失败", e);
        }
        //执行逻辑
        GetUserResponseBody body = user.getBody();
        IdentityProviderUserDetails identityProviderUserDetails = IdentityProviderUserDetails
            .builder().openId(body.getOpenId()).unionId(body.getUnionId()).email(body.getEmail())
            .stateCode(body.getStateCode()).mobile(body.getMobile()).nickName(body.getNick())
            .avatarUrl(body.getAvatarUrl()).providerType(IdentityProviderType.DINGTALK_OAUTH)
            .providerCode(providerCode).providerId(providerId).build();
        return attemptAuthentication(request, response, identityProviderUserDetails);
    }

    /**
     * 获取token
     *
     * @param authCode {@link  String}
     * @param config   {@link  DingTalkIdentityProviderOAuth2Config}
     * @return {@link String}
     */
    public String getToken(String authCode, DingTalkIdentityProviderOAuth2Config config) {
        String cacheKey = OAuth2ParameterNames.ACCESS_TOKEN + DigestUtils.md5Hex(config.toString());
        if (!Objects.isNull(cache)) {
            return cache.getIfPresent(cacheKey);
        }
        Config clientConfig = new Config();
        clientConfig.setProtocol("https");
        clientConfig.setRegionId("central");
        try {
            Client client = new Client(clientConfig);
            GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
                //应用基础信息-应用信息的AppKey
                .setClientId(config.getAppKey())
                //应用基础信息-应用信息的AppSecret
                .setClientSecret(config.getAppSecret()).setCode(authCode)
                .setGrantType(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());

            //获取用户个人token
            GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
            GetUserTokenResponseBody body = getUserTokenResponse.getBody();
            //放入缓存
            cache = CacheBuilder.newBuilder().expireAfterWrite(body.getExpireIn(), TimeUnit.SECONDS)
                .build();
            cache.put(cacheKey, body.getAccessToken());
            return cache.getIfPresent(cacheKey);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * 缓存
     */
    private Cache<String, String> cache;

    public static String getLoginUrl(String providerId) {
        String url = PropertiesContextHelper.getPortalPublicBaseUrl()
                     + IdentityProviderType.DINGTALK_OAUTH.getLoginPathPrefix() + "/" + providerId;
        return UrlUtils.format(url);
    }

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
