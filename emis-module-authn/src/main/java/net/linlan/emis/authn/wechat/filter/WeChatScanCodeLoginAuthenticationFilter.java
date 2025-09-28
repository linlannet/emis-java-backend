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
package net.linlan.emis.authn.wechat.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import net.linlan.emis.authn.wechat.WeChatIdentityProviderOAuth2Config;
import net.linlan.emis.authn.wechat.constant.WeChatAuthenticationConstants;
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
import net.linlan.utils.http.HttpClientUtils;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;

/**
 * 微信扫码登录过滤器
 *
 * @author Linlan
 */
@SuppressWarnings("DuplicatedCode")
public class WeChatScanCodeLoginAuthenticationFilter extends
                                                     AbstractIdentityProviderAuthenticationProcessingFilter {

    public final static String                DEFAULT_FILTER_PROCESSES_URI = IdentityProviderType.WECHAT
        .getLoginPathPrefix() + "/" + "{" + AuthenticationConstants.PROVIDER_CODE + "}";
    public static final AntPathRequestMatcher REQUEST_MATCHER              = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.GET.name());

    /**
     * Creates a new instance
     *
     * @param registeredIdentityProviderClientRepository the {@link RegisteredIdentityProviderClientRepository}
     * @param identityProviderAuthenticationService  {@link  IdentityProviderAuthenticationService}
     */
    public WeChatScanCodeLoginAuthenticationFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                                   IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(REQUEST_MATCHER, identityProviderAuthenticationService,
            registeredIdentityProviderClientRepository);
    }

    /**
     * 微信认证
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
            logger.error("微信开放平台扫码登录 code 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_CODE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // state
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (StringUtils.isEmpty(state)) {
            logger.error("微信开放平台扫码登录 state 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        if (!authorizationRequest.getState().equals(state)) {
            logger.error("微信开放平台扫码登录 state 匹配不一致，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取身份提供商
        RegisteredIdentityProviderClient<WeChatIdentityProviderOAuth2Config> provider = getRegisteredIdentityProviderClient(
            providerCode);
        WeChatIdentityProviderOAuth2Config config = provider.getConfig();
        if (Objects.isNull(config)) {
            logger.error("未查询到微信扫码登录配置");
            //无效身份提供商
            OAuth2Error oauth2Error = new OAuth2Error(
                AbstractIdentityProviderAuthenticationProcessingFilter.INVALID_IDP_CONFIG);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取access token
        HashMap<String, String> param = new HashMap<>(16);
        param.put(WeChatAuthenticationConstants.QrConnect.APP_ID, config.getAppId());
        param.put(WeChatAuthenticationConstants.QrConnect.SECRET, config.getAppSecret());
        param.put(OAuth2ParameterNames.CODE, code);
        param.put(OAuth2ParameterNames.GRANT_TYPE, AUTHORIZATION_CODE.getValue());
        JSONObject result = JSON.parseObject(
            HttpClientUtils.get(WeChatAuthenticationConstants.QrConnect.ACCESS_TOKEN, param));
        if (result.containsKey(WeChatAuthenticationConstants.QrConnect.ERROR_CODE)) {
            logger.error("获取access_token发生错误:  " + result.toJSONString());
            throw new SupportException("获取access_token发生错误:  " + result.toJSONString());
        }
        // 获取user信息
        param = new HashMap<>(16);
        param.put(OAuth2ParameterNames.ACCESS_TOKEN,
            result.getString(OAuth2ParameterNames.ACCESS_TOKEN));
        param.put(OidcScopes.OPENID, result.getString(OidcScopes.OPENID));
        param.put(WeChatAuthenticationConstants.QrConnect.LANG, "zh_CN");
        result = JSON.parseObject(
            HttpClientUtils.get(WeChatAuthenticationConstants.QrConnect.USER_INFO, param));
        if (result.containsKey(WeChatAuthenticationConstants.QrConnect.ERROR_CODE)) {
            logger.error("获取微信用户个人信息发生错误:  " + result.toJSONString());
            throw new SupportException("获取微信用户个人信息发生错误:  " + result.toJSONString());
        }
        // 返回
        IdentityProviderUserDetails identityProviderUserDetails = IdentityProviderUserDetails
            .builder().openId(param.get(OidcScopes.OPENID))
            .unionId(result.getString(WeChatAuthenticationConstants.QrConnect.UNION_ID))
            .avatarUrl(result.getString(WeChatAuthenticationConstants.QrConnect.HEADIMGURL))
            .providerCode(providerCode)
            .nickName(result.getString(WeChatAuthenticationConstants.QrConnect.NICK_NAME))
            .providerId(providerId).providerType(IdentityProviderType.WECHAT).build();
        return attemptAuthentication(request, response, identityProviderUserDetails);
    }

    public static String getLoginUrl(String providerId) {
        String url = PropertiesContextHelper.getPortalPublicBaseUrl()
                     + IdentityProviderType.WECHAT.getLoginPathPrefix() + "/" + providerId;
        return UrlUtils.format(url);
    }

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
