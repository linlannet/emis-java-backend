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

import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Spring doc o auth 2 token introspection.
 *
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/http/converter/OAuth2TokenIntrospectionHttpMessageConverter.java">OAuth2TokenIntrospectionHttpMessageConverter</a>
 * @see <a href="https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/OAuth2TokenIntrospection.java">OAuth2TokenIntrospection</a>
 * @author bnasslahsen
 * @author yuta.saito
 */
@Schema(name = "OAuth2TokenIntrospection")
public interface SpringDocOAuth2TokenIntrospection {
    @JsonProperty(OAuth2TokenIntrospectionClaimNames.ACTIVE)
    boolean getActive();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.SCOPE)
    String getScope();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.CLIENT_ID)
    String getClientId();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.USERNAME)
    String getUsername();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.TOKEN_TYPE)
    String getTokenType();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.EXP)
    long getExpiresAt();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.IAT)
    long getIssueAt();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.NBF)
    long getNotBefore();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.SUB)
    String getSubject();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.AUD)
    List<String> getAudience();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.ISS)
    String getIssuer();

    @JsonProperty(OAuth2TokenIntrospectionClaimNames.JTI)
    String getId();
}
