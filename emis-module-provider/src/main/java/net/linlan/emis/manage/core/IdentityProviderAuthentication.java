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

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

import net.linlan.emis.user.constant.IdentityProviderType;

/**
 * Idp Authentication
 *
 * @author Linlan
 */
public class IdentityProviderAuthentication extends AbstractAuthenticationToken
                                            implements java.io.Serializable {

    // ~ Instance fields

    private static final long          serialVersionUID = -1506897701981698420L;

    private final Object               principal;

    /**
     * 提供商类型
     */
    @Getter
    private final IdentityProviderType providerType;

    /**
     * 提供商ID
     */
    @Getter
    private final String               providerId;

    public IdentityProviderAuthentication(Object principal, IdentityProviderType providerType,
                                          String providerId,
                                          Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.providerId = providerId;
        this.providerType = providerType;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}
