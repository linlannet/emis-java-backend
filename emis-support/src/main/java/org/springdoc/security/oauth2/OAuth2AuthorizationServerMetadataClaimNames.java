/*
 * emis-support - support支撑模块
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
package org.springdoc.security.oauth2;

/**
 * The names of the "claims" an Authorization Server describes about its configuration,
 * used in OAuth 2.0 Authorization Server Metadata and OpenID Connect Discovery 1.0.
 *
 * @author Daniel Garnier-Moiroux
 * @since 0.1.1
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc8414#section-2">2. Authorization Server Metadata</a>
 * @see <a target="_blank" href="https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata">3. OpenID Provider Metadata</a>
 * @see <a target="_blank" href="https://www.rfc-editor.org/rfc/rfc8628.html#section-4">4. Device Authorization Grant Metadata</a>
 */
public class OAuth2AuthorizationServerMetadataClaimNames {

    /**
     * {@code issuer} - the {@code URL} the Authorization Server asserts as its Issuer Identifier
     */
    public static final String ISSUER                                        = "issuer";

    /**
     * {@code authorization_endpoint} - the {@code URL} of the OAuth 2.0 Authorization Endpoint
     */
    public static final String AUTHORIZATION_ENDPOINT                        = "authorization_endpoint";

    /**
     * {@code device_authorization_endpoint} - the {@code URL} of the OAuth 2.0 Device Authorization Endpoint
     * @since 1.1
     */
    public static final String DEVICE_AUTHORIZATION_ENDPOINT                 = "device_authorization_endpoint";

    /**
     * {@code token_endpoint} - the {@code URL} of the OAuth 2.0 Token Endpoint
     */
    public static final String TOKEN_ENDPOINT                                = "token_endpoint";

    /**
     * {@code token_endpoint_auth_methods_supported} - the client authentication methods supported by the OAuth 2.0 Token Endpoint
     */
    public static final String TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED         = "token_endpoint_auth_methods_supported";

    /**
     * {@code jwks_uri} - the {@code URL} of the JSON Web Key Set
     */
    public static final String JWKS_URI                                      = "jwks_uri";

    /**
     * {@code scopes_supported} - the OAuth 2.0 {@code scope} values supported
     */
    public static final String SCOPES_SUPPORTED                              = "scopes_supported";

    /**
     * {@code response_types_supported} - the OAuth 2.0 {@code response_type} values supported
     */
    public static final String RESPONSE_TYPES_SUPPORTED                      = "response_types_supported";

    /**
     * {@code grant_types_supported} - the OAuth 2.0 {@code grant_type} values supported
     */
    public static final String GRANT_TYPES_SUPPORTED                         = "grant_types_supported";

    /**
     * {@code revocation_endpoint} - the {@code URL} of the OAuth 2.0 Token Revocation Endpoint
     */
    public static final String REVOCATION_ENDPOINT                           = "revocation_endpoint";

    /**
     * {@code revocation_endpoint_auth_methods_supported} - the client authentication methods supported by the OAuth 2.0 Token Revocation Endpoint
     */
    public static final String REVOCATION_ENDPOINT_AUTH_METHODS_SUPPORTED    = "revocation_endpoint_auth_methods_supported";

    /**
     * {@code introspection_endpoint} - the {@code URL} of the OAuth 2.0 Token Introspection Endpoint
     */
    public static final String INTROSPECTION_ENDPOINT                        = "introspection_endpoint";

    /**
     * {@code introspection_endpoint_auth_methods_supported} - the client authentication methods supported by the OAuth 2.0 Token Introspection Endpoint
     */
    public static final String INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED = "introspection_endpoint_auth_methods_supported";

    /**
     * {@code registration_endpoint} - the {@code URL} of the OAuth 2.0 Dynamic Client Registration Endpoint
     * @since 0.4.0
     */
    public static final String REGISTRATION_ENDPOINT                         = "registration_endpoint";

    /**
     * {@code code_challenge_methods_supported} - the Proof Key for Code Exchange (PKCE) {@code code_challenge_method} values supported
     */
    public static final String CODE_CHALLENGE_METHODS_SUPPORTED              = "code_challenge_methods_supported";

    protected OAuth2AuthorizationServerMetadataClaimNames() {
    }

}
