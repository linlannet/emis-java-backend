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
package net.linlan.emis.authn.qq.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.emis.authn.qq.QqIdentityProviderOAuth2Config;
import net.linlan.emis.authn.qq.constant.QqAuthenticationConstants;
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
import static com.nimbusds.oauth2.sdk.GrantType.AUTHORIZATION_CODE;

/**
 * QQ登录
 *
 * @author Linlan
 */
public class QqOAuth2LoginAuthenticationFilter extends
                                               AbstractIdentityProviderAuthenticationProcessingFilter {
    final String                              ERROR_CODE                   = "error";
    public final static String                DEFAULT_FILTER_PROCESSES_URI = IdentityProviderType.QQ_OAUTH
        .getLoginPathPrefix() + "/" + "{" + AuthenticationConstants.PROVIDER_CODE + "}";
    public static final AntPathRequestMatcher REQUEST_MATCHER              = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.GET.name());

    /**
     * Creates a new instance
     *
     * @param registeredIdentityProviderClientRepository the {@link RegisteredIdentityProviderClientRepository}
     * @param identityProviderAuthenticationService  {@link  IdentityProviderAuthenticationService}
     */
    public QqOAuth2LoginAuthenticationFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                             IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(REQUEST_MATCHER, identityProviderAuthenticationService,
            registeredIdentityProviderClientRepository);
    }

    /**
     * QQ认证
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
        //code
        String code = request.getParameter(OAuth2ParameterNames.CODE);
        if (StringUtils.isEmpty(code)) {
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_CODE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // state
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (StringUtils.isEmpty(state)) {
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        if (!authorizationRequest.getState().equals(state)) {
            OAuth2Error oauth2Error = new OAuth2Error(
                AuthenticationConstants.INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取身份提供商
        RegisteredIdentityProviderClient<QqIdentityProviderOAuth2Config> provider = getRegisteredIdentityProviderClient(
            providerCode);
        QqIdentityProviderOAuth2Config config = provider.getConfig();
        if (Objects.isNull(config)) {
            logger.error("未查询到QQ登录配置");
            //无效身份提供商
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_IDP_CONFIG);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取access token
        HashMap<String, String> param = new HashMap<>(16);
        param.put(OAuth2ParameterNames.GRANT_TYPE, AUTHORIZATION_CODE.getValue());
        param.put(OAuth2ParameterNames.CLIENT_ID, config.getAppId().trim());
        param.put(OAuth2ParameterNames.CLIENT_SECRET, config.getAppKey().trim());
        param.put(OAuth2ParameterNames.CODE, code.trim());
        param.put(OAuth2ParameterNames.REDIRECT_URI, getLoginUrl(provider.getCode()));
        param.put("fmt", "json");
        //注意：QQ不能使用编码后的get请求，否则会报 {"error_description":"redirect uri is illegal","error":100010}
        JSONObject result = JSON.parseObject(
            HttpClientUtils.doGet(QqAuthenticationConstants.URL_GET_ACCESS_TOKEN, param));
        if (!Objects.isNull(result.getString(ERROR_CODE))) {
            logger.error("获取access_token发生错误: {}" + result.toJSONString());
            throw new SupportException("获取access_token发生错误:  " + result.toJSONString());
        }
        // 获取openId信息
        param = new HashMap<>(16);
        param.put(OAuth2ParameterNames.ACCESS_TOKEN,
            result.getString(OAuth2ParameterNames.ACCESS_TOKEN));
        param.put("fmt", "json");
        result = JSON
            .parseObject(HttpClientUtils.doGet(QqAuthenticationConstants.URL_GET_OPEN_ID, param));
        if (!Objects.isNull(result.getString(ERROR_CODE))) {
            logger.error("获取QQ用户OpenID发生错误: {}" + result.toJSONString());
            throw new SupportException("获取QQ用户OpenID发生错误:  " + result.toJSONString());
        }
        // 返回
        String openId = result.getString(OidcScopes.OPENID);
        IdentityProviderUserDetails identityProviderUserDetails = IdentityProviderUserDetails
            .builder().openId(openId).providerType(IdentityProviderType.QQ_OAUTH)
            .providerCode(providerCode).providerId(providerId).build();
        return attemptAuthentication(request, response, identityProviderUserDetails);

    }

    public static String getLoginUrl(String providerId) {
        String url = PropertiesContextHelper.getPortalPublicBaseUrl() + "/"
                     + IdentityProviderType.QQ_OAUTH.getLoginPathPrefix() + "/" + providerId;
        return UrlUtils.format(url);
    }

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
