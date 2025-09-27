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

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

import net.linlan.emis.support.security.password.authentication.NeedChangePasswordAuthenticationToken;

/**
 *
 * @author Linlan
 * Created on  2023/3/22 21:02
 */
public class AuthenticationTrustResolverImpl implements AuthenticationTrustResolver {
    private final AuthenticationTrustResolver delegate = new org.springframework.security.authentication.AuthenticationTrustResolverImpl();

    @Override
    public boolean isAnonymous(Authentication authentication) {
        return this.delegate.isAnonymous(authentication)
               || (authentication instanceof NeedChangePasswordAuthenticationToken)
               //  IDP 提供商，未认证为匿名
               || (authentication instanceof IdentityProviderNotBindAuthentication);
    }

    @Override
    public boolean isRememberMe(Authentication authentication) {
        return this.delegate.isRememberMe(authentication);
    }
}
