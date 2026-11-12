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
package net.linlan.emis.protocol.oidc.jackson;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * OAuth2AuthorizationCodeMixin
 *
 * @author Linlan
 * Created on 2023/6/30 21:08
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel",
                    "AlibabaAbstractClassShouldStartWithAbstractNaming" })
abstract class OAuth2AuthorizationCodeMixin {

    @JsonCreator
    OAuth2AuthorizationCodeMixin(@JsonProperty("tokenValue") String tokenValue,
                                 @JsonProperty("issuedAt") Instant issuedAt,
                                 @JsonProperty("expiresAt") Instant expiresAt) {
    }

}
