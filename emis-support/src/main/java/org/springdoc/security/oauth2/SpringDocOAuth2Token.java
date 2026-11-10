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

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Spring doc o auth 2 token.
 *
 * @see <a href="https://github.com/spring-projects/spring-security/blob/main/oauth2/oauth2-core/src/main/java/org/springframework/security/oauth2/core/endpoint/DefaultOAuth2AccessTokenResponseMapConverter.java">DefaultOAuth2AccessTokenResponseMapConverter</a>
 * @author yuta.saito
 */
@Schema(name = "OAuth2Token")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface SpringDocOAuth2Token {
    String getAccessToken();

    String getTokenType();

    long getExpiresIn();

    String getScope();

    String getRefreshToken();
}
