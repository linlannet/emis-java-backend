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
package net.linlan.emis.protocol.oidc.constant;

import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

import net.linlan.emis.protocol.constant.ProtocolConstants;

/**
 * @author Linlan
 * Created on 2023/7/4 21:19
 */
public class OidcProtocolConstants {

    /**
     * 协议缓存前缀
     */
    public static final String          OIDC_PROTOCOL_CACHE_PREFIX = ProtocolConstants.PROTOCOL_CACHE_PREFIX
                                                                     + "oidc" + ":";

    public static final OAuth2TokenType ID_TOKEN                   = new OAuth2TokenType(
        "id_token");

    public static final String          OIDC_ERROR_URI             = "https://linlan.net/emis/docs/application/oidc/faq/#%E9%94%99%E8%AF%AF%E7%A0%81";

    public final class ConfigurationSettingNames {
        private static final String SETTINGS_NAMESPACE = "settings.";

        private ConfigurationSettingNames() {
        }

        /**
         * The names for token configuration settings.
         */
        public final class Token {
            private final String TOKEN_SETTINGS_NAMESPACE = SETTINGS_NAMESPACE.concat("token.");

            /**
             * id_token 扩展字段
             */
            public final String  ID_TOKEN_CUSTOM_CLAIMS   = TOKEN_SETTINGS_NAMESPACE
                .concat("id-token-custom-claims");

            private Token() {
            }

        }

    }
}
