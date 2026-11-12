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

import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * OAuth2AuthorizationModule
 *
 * @author Linlan
 * Created on 2023/6/30 21:07
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationModule extends SimpleModule {

    public OAuth2AuthorizationModule() {
        super(OAuth2AuthorizationModule.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        SecurityJackson2Modules.enableDefaultTyping(context.getOwner());

        context.setMixInAnnotations(OAuth2Authorization.class, OAuth2AuthorizationMixin.class);
        context.setMixInAnnotations(OAuth2AuthorizationCode.class,
            OAuth2AuthorizationCodeMixin.class);
        context.setMixInAnnotations(OAuth2Authorization.Token.class, TokenMixin.class);
        context.setMixInAnnotations(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);
        context.setMixInAnnotations(OAuth2RefreshToken.class, OAuth2RefreshTokenMixin.class);
        context.setMixInAnnotations(AuthorizationGrantType.class,
            AuthorizationGrantTypeMixin.class);
        context.setMixInAnnotations(OAuth2AccessToken.TokenType.class, TokenTypeMixin.class);
        context.setMixInAnnotations(OidcIdToken.class, OidcIdTokenMixin.class);
    }

}
