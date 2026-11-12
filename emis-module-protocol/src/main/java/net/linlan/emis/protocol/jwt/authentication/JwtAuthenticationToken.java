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
package net.linlan.emis.protocol.jwt.authentication;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

import lombok.Getter;
import lombok.Setter;

import net.linlan.emis.protocol.jwt.model.JwtProtocolConfig;
import net.linlan.emis.protocol.jwt.token.IdToken;

/**
 *
 * @author Linlan
 * Created on 2023/7/8 00:08
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final String            id;

    /**
     * principal
     */
    private final Authentication    principal;

    /**
     * idToken
     */
    @Getter
    private final IdToken           idToken;

    /**
     * 协议配置
     */
    @Getter
    private final JwtProtocolConfig config;

    @Getter
    @Setter
    private String                  targetUrl;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param principal   {@link Authentication}
     * @param idToken {@link IdToken}
     * @param config {@link JwtProtocolConfig}
     */
    public JwtAuthenticationToken(Authentication principal, JwtProtocolConfig config,
                                  IdToken idToken) {
        super(new ArrayList<>());
        this.principal = principal;
        this.idToken = idToken;
        this.config = config;
        this.id = UUID.randomUUID().toString();
        setAuthenticated(true);
    }

    /**
     * The credentials that prove the principal is correct. This is usually a password,
     * but could be anything relevant to the <code>AuthenticationManager</code>. Callers
     * are expected to populate the credentials.
     *
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * The identity of the principal being authenticated. In the case of an authentication
     * request with username and password, this would be the username. Callers are
     * expected to populate the principal for an authentication request.
     * <p>
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a
     * {@code UserDetails} object as the principal.
     *
     * @return the <code>Principal</code> being authenticated or the authenticated
     * principal after authentication.
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }
}
