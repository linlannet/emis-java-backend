/*
 * emis-module-provider - provider身份提供商
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
package net.linlan.emis.manage.core;

import java.util.ArrayList;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 *
 * @author Linlan
 */
public class IdentityProviderNotBindAuthentication extends AbstractAuthenticationToken
                                                   implements java.io.Serializable {

    /**
     * 提供商ID
     */
    private final IdentityProviderUserDetails principal;

    public IdentityProviderNotBindAuthentication(IdentityProviderUserDetails principal) {
        super(new ArrayList<>());
        this.principal = principal;
    }

    /**
     * The credentials that prove the principal is correct. This is usually a password,
     * but could be anything relevant to the AuthenticationManagercore.encrypt.EncryptContextHelper. Callers
     * are expected to populate the credentials.
     *
     * @return the credentials that prove the identity of the Principalcore.encrypt.EncryptContextHelper
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * The identity of the principal being authenticated. In the case of an authentication
     * request with username and password, this would be the username. Callers are
     * expected to populate the principal for an authentication request.
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a
     * {@code UserDetails} object as the principal.
     *
     * @return the Principalcore.encrypt.EncryptContextHelper being authenticated or the authenticated
     * principal after authentication.
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }
}
