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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Spring doc o auth 2 authorization server metadata.
 *
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/OAuth2AuthorizationServerMetadata.java">OAuth2AuthorizationServerMetadata</a>
 * @author bnasslahsen
 * @author yuta.saito
 */
@Schema(name = "OAuth2AuthorizationServerMetadata")
public interface SpringDocOAuth2AuthorizationServerMetadata {
    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.ISSUER)
    String issuer();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.AUTHORIZATION_ENDPOINT)
    String authorizationEndpoint();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT)
    String tokenEndpoint();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.TOKEN_ENDPOINT_AUTH_METHODS_SUPPORTED)
    List<String> tokenEndpointAuthMethodsSupported();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.JWKS_URI)
    String jwksUri();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.RESPONSE_TYPES_SUPPORTED)
    List<String> responseTypesSupported();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.GRANT_TYPES_SUPPORTED)
    List<String> grantTypesSupported();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT)
    String revocationEndpoint();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.REVOCATION_ENDPOINT_AUTH_METHODS_SUPPORTED)
    List<String> revocationEndpointAuthMethodsSupported();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT)
    String introspectionEndpoint();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.INTROSPECTION_ENDPOINT_AUTH_METHODS_SUPPORTED)
    List<String> introspectionEndpointAuthMethodsSupported();

    @JsonProperty(OAuth2AuthorizationServerMetadataClaimNames.CODE_CHALLENGE_METHODS_SUPPORTED)
    List<String> codeChallengeMethodsSupported();
}
