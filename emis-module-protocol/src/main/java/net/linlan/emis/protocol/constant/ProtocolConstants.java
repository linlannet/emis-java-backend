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
package net.linlan.emis.protocol.constant;

import lombok.Data;

import net.linlan.commons.core.StringUtils;
import net.linlan.emis.app.constant.AppConstants;
import net.linlan.emis.support.constant.EmisConstants;
import static com.nimbusds.openid.connect.sdk.op.OIDCProviderConfigurationRequest.OPENID_PROVIDER_WELL_KNOWN_PATH;

/**
 * 协议常量
 *
 * @author Linlan
 *
 */
public abstract class ProtocolConstants {
    /**
     * 应用code
     */
    public static final String APP_CODE                 = "appCode";
    /**
     * 提供商变量
     */
    public static final String APP_CODE_VARIABLE        = "{" + APP_CODE + "}";

    /**
     * 应用账户缓存名称
     */
    public static final String APP_ACCOUNT_CACHE_NAME   = AppConstants.APP_CACHE_NAME_PREFIX
                                                          + "account";

    /**
     * SAML2 配置缓存名称
     */
    public static final String SAML2_CONFIG_CACHE_NAME  = AppConstants.APP_CACHE_NAME_PREFIX
                                                          + "saml";

    /**
     * CAS 配置缓存名称
     */
    public static final String CAS_CONFIG_CACHE_NAME    = AppConstants.APP_CACHE_NAME_PREFIX
                                                          + "cas";

    /**
     * OIDC 配置缓存名称
     */
    public static final String OIDC_CONFIG_CACHE_NAME   = AppConstants.APP_CACHE_NAME_PREFIX
                                                          + "oidc";

    /**
     * APP 认证缓存名称
     */
    public static final String APP_CERT_CACHE_NAME      = AppConstants.APP_CACHE_NAME_PREFIX
                                                          + "cert";
    /**
     * 认证路径
     */
    public final static String AUTHORIZE_PATH           = EmisConstants.V1_API_PATH + "/authorize";

    /**
     * 协议缓存前缀
     */
    public static final String PROTOCOL_CACHE_PREFIX    = "protocol" + StringUtils.COLON;
    /**
     * CAS IDP SSO 发起
     */
    public static final String IDP_CAS_SSO_INITIATOR    = AUTHORIZE_PATH + "/cas/"
                                                          + APP_CODE_VARIABLE + "/initiator";
    /**
     * FORM IDP SSO 发起
     */
    public static final String IDP_FORM_SSO_INITIATOR   = AUTHORIZE_PATH + "/form/"
                                                          + APP_CODE_VARIABLE + "/initiator";

    /**
     * JWT IDP SSO 发起
     */
    public static final String IDP_JWT_SSO_INITIATOR    = AUTHORIZE_PATH + "/jwt/"
                                                          + APP_CODE_VARIABLE + "/initiator";

    /**
     * OAuth2 IDP SSO 发起
     */
    public static final String IDP_OAUTH2_SSO_INITIATOR = AUTHORIZE_PATH + "/oauth2/"
                                                          + APP_CODE_VARIABLE + "/initiator";
    /**
     * SAML2 IDP SSO 发起
     */
    public static final String IDP_SAML2_SSO_INITIATOR  = AUTHORIZE_PATH + "/saml2/"
                                                          + APP_CODE_VARIABLE + "/initiator";

    /**
     * OIDC Endpoint config
     */
    @Data
    public static class OidcEndpointConstants {
        //@formatter:off
        /**
         * OIDC BASE 认证路径
         */
        public final static String OIDC_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/" + APP_CODE_VARIABLE;

        public final static String OIDC_AUTHORIZE_PATH = OIDC_AUTHORIZE_BASE_PATH +"/oidc";

        public final static String OAUTH2_AUTHORIZE_PATH = OIDC_AUTHORIZE_BASE_PATH +"/oauth2";

        /**
         * OpenID Provider metadata.
         */
        public static final String WELL_KNOWN_OPENID_CONFIGURATION   = OIDC_AUTHORIZE_PATH +OPENID_PROVIDER_WELL_KNOWN_PATH;

        /**
         * Jwk Set Endpoint
         */
        public static final String JWK_SET_ENDPOINT                  = OIDC_AUTHORIZE_PATH + "/jwks";

        /**
         * OIDC Client Registration Endpoint
         */
        public static final String OIDC_CLIENT_REGISTRATION_ENDPOINT = OIDC_AUTHORIZE_PATH + "/connect/register";

        /**
         * OIDC Logout Endpoint
         */
        public static final String OIDC_LOGOUT_ENDPOINT = OIDC_AUTHORIZE_PATH + "/connect/logout";

        /**
         * Authorization Endpoint
         */
        public static final String AUTHORIZATION_ENDPOINT            = OAUTH2_AUTHORIZE_PATH + "/auth";

        /**
         * Authorization Consent Endpoint
         */
        public static final String AUTHORIZATION_CONSENT_ENDPOINT            = AUTHORIZATION_ENDPOINT+"/consent";

        /**
         * Token Endpoint
         */
        public static final String TOKEN_ENDPOINT                    = OAUTH2_AUTHORIZE_PATH + "/token";

        /**
         * Jwk Revocation Endpoint
         */
        public static final String TOKEN_REVOCATION_ENDPOINT         = OAUTH2_AUTHORIZE_PATH + "/revoke";

        /**
         * Token Introspection Endpoint
         */
        public static final String TOKEN_INTROSPECTION_ENDPOINT      = OAUTH2_AUTHORIZE_PATH + "/introspect";

        /**
         * OIDC User Info Endpoint
         */
        public static final String OIDC_USER_INFO_ENDPOINT           = OAUTH2_AUTHORIZE_PATH + "/userinfo";

        /**
         * 设备模式授权端点
         */
        public static final String DEVICE_AUTHORIZATION_ENDPOINT = OAUTH2_AUTHORIZE_PATH+"/device_authorization";

        /**
         * 设备模式验证端点
         */
        public static final String DEVICE_VERIFICATION_ENDPOINT = OAUTH2_AUTHORIZE_PATH+"/device_verification";

        //@formatter:on
    }

    /**
     * Saml2 Endpoint config
     */
    @Data
    public static class Saml2EndpointConstants {
        /**
         * SAML2  认证路径
         */
        public final static String SAML2_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/saml2/"
                                                               + APP_CODE_VARIABLE;

        /**
         * SAML_METADATA_PATH
         */
        public static final String SAML_METADATA_PATH        = SAML2_AUTHORIZE_BASE_PATH
                                                               + "/metadata";
        /**
         * SAML_LOGOUT_PATH
         */
        public static final String SAML_LOGOUT_PATH          = SAML2_AUTHORIZE_BASE_PATH
                                                               + "/logout";
        /**
         * SAML_SSO_PATH
         */
        public static final String SAML_SSO_PATH             = SAML2_AUTHORIZE_BASE_PATH + "/sso";
    }

    @Data
    public static class CasEndpointConstants {
        /**
         * cas  根路径
         */
        public final static String CAS_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/cas/"
                                                             + APP_CODE_VARIABLE;
        /*
         * cas 登录地址
         */
        public final static String CAS_LOGIN_PATH          = CAS_AUTHORIZE_BASE_PATH + "/login";
        /*
         * cas ticket校验地址
         */
        public final static String CAS_VALIDATE_PATH       = CAS_AUTHORIZE_BASE_PATH + "/validate";

        public final static String CAS_VALIDATE_V2_PATH    = CAS_AUTHORIZE_BASE_PATH
                                                             + "/serviceValidate";

        public final static String CAS_VALIDATE_V3_PATH    = CAS_AUTHORIZE_BASE_PATH
                                                             + "/p3/serviceValidate";
    }

    /**
     * Form Endpoint config
     */
    @Data
    public static class FormEndpointConstants {

        /**
         * FORM  认证路径
         */
        public final static String FORM_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/form/"
                                                              + APP_CODE_VARIABLE;

        /**
         * FORM_SSO
         */
        public static final String FORM_SSO_PATH            = FORM_AUTHORIZE_BASE_PATH + "/sso";

        /**
         * FORM IDP SSO 发起
         */
        public static final String IDP_FORM_SSO_INITIATOR   = FORM_AUTHORIZE_BASE_PATH
                                                              + "/initiator";
    }

    /**
     * Form Endpoint config
     */
    @Data
    public static class JwtEndpointConstants {

        /**
         * JWT  认证路径
         */
        public final static String JWT_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/jwt/"
                                                             + APP_CODE_VARIABLE;

        /**
         * JWT_SSO
         */
        public static final String JWT_SSO_PATH            = JWT_AUTHORIZE_BASE_PATH + "/sso";

        /**
         * JWT_SLO
         */
        public static final String JWT_SLO_PATH            = JWT_AUTHORIZE_BASE_PATH + "/slo";

        /**
         * JWT IDP SSO 发起
         */
        public static final String IDP_JWT_SSO_INITIATOR   = JWT_AUTHORIZE_BASE_PATH + "/initiator";
    }
}
