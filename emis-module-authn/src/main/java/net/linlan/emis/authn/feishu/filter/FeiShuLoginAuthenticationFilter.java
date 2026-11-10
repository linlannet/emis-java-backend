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
package net.linlan.emis.authn.feishu.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.nimbusds.oauth2.sdk.GrantType;

import net.linlan.commons.core.StringUtils;
import net.linlan.emis.authn.feishu.FeiShuIdentityProviderOAuth2Config;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.filter.AbstractIdentityProviderAuthenticationProcessingFilter;
import net.linlan.emis.manage.IdentityProviderAuthenticationService;
import net.linlan.emis.manage.client.RegisteredIdentityProviderClient;
import net.linlan.emis.manage.client.RegisteredIdentityProviderClientRepository;
import net.linlan.emis.manage.core.IdentityProviderUserDetails;
import net.linlan.emis.provider.constant.AuthenticationConstants;
import net.linlan.emis.provider.constant.IdentityProviderType;
import net.linlan.emis.support.http.HttpClientUtils;
import net.linlan.emis.support.utils.UrlUtils;
import static net.linlan.emis.authn.feishu.constant.FeiShuAuthenticationConstants.*;

/**
 * 飞书扫码登录过滤器
 *
 * @author Linlan
 */
public class FeiShuLoginAuthenticationFilter extends
                                             AbstractIdentityProviderAuthenticationProcessingFilter {

    public final static String                DEFAULT_FILTER_PROCESSES_URI = IdentityProviderType.FEISHU_OAUTH
        .getLoginPathPrefix() + "/" + "{" + AuthenticationConstants.PROVIDER_CODE + "}";
    public static final AntPathRequestMatcher REQUEST_MATCHER              = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.GET.name());

    /**
     * Creates a new instance
     *
     * @param registeredIdentityProviderClientRepository the {@link RegisteredIdentityProviderClientRepository}
     * @param identityProviderAuthenticationService  {@link  IdentityProviderAuthenticationService}
     */
    public FeiShuLoginAuthenticationFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                           IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(REQUEST_MATCHER, identityProviderAuthenticationService,
            registeredIdentityProviderClientRepository);
    }

    /**
     * 飞书认证
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
        RequestMatcher.MatchResult matcher = REQUEST_MATCHER.matcher(request);
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(AuthenticationConstants.PROVIDER_CODE);
        String providerId = getIdentityProviderId(providerCode);
        //code
        String code = request.getParameter(OAuth2ParameterNames.CODE);
        if (StringUtils.isEmpty(code)) {
            logger.error("飞书登录 code 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_CODE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // state
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (StringUtils.isEmpty(state)) {
            logger.error("飞书登录 state 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        if (!authorizationRequest.getState().equals(state)) {
            logger.error("飞书登录 state 匹配不一致，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取身份提供商
        RegisteredIdentityProviderClient<FeiShuIdentityProviderOAuth2Config> provider = getRegisteredIdentityProviderClient(
            providerCode);
        FeiShuIdentityProviderOAuth2Config config = provider.getConfig();
        if (Objects.isNull(config)) {
            logger.error("未查询到飞书扫码登录配置");
            //无效身份提供商
            OAuth2Error oauth2Error = new OAuth2Error(
                AbstractIdentityProviderAuthenticationProcessingFilter.INVALID_IDP_CONFIG);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取access token
        HashMap<String, String> param = new HashMap<>(16);
        param.put(CLIENT_ID, config.getAppId());
        param.put(CLIENT_SECRET, config.getAppSecret());
        param.put(OAuth2ParameterNames.CODE, code);
        param.put(OAuth2ParameterNames.REDIRECT_URI, getLoginUrl(provider.getCode()));
        param.put(OAuth2ParameterNames.GRANT_TYPE, GrantType.AUTHORIZATION_CODE.getValue());
        JSONObject result = JSON.parseObject(HttpClientUtils.post(ACCESS_TOKEN, param));
        // 获取user信息
        param = new HashMap<>(16);
        BasicHeader authorization = new BasicHeader(
            "Authorization", result.getString(OAuth2ParameterNames.TOKEN_TYPE) + " "
                             + result.getString(OAuth2ParameterNames.ACCESS_TOKEN));
        result = JSON.parseObject(HttpClientUtils.get(USER_INFO, param, authorization));
        // 返回
        IdentityProviderUserDetails identityProviderUserDetails = IdentityProviderUserDetails
            .builder().openId(result.getString(OPEN_ID)).unionId(result.getString("union_id"))
            .email(result.getString("email")).mobile(result.getString("mobile"))
            .nickName(result.getString("name")).avatarUrl(result.getString("avatar_url"))
            .providerType(IdentityProviderType.FEISHU_OAUTH).providerCode(providerCode)
            .providerId(providerId).build();
        return attemptAuthentication(request, response, identityProviderUserDetails);
    }

    public static String getLoginUrl(String providerId) {
        String url = PropertiesContextHelper.getPortalPublicBaseUrl()
                     + IdentityProviderType.FEISHU_OAUTH.getLoginPathPrefix() + "/" + providerId;
        return UrlUtils.format(url);
    }

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
