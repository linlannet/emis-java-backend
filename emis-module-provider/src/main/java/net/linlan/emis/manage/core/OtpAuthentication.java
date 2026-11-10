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

import com.google.common.collect.Lists;

/**
 * 短信OPT认证
 *
 * @author Linlan
 */
public class OtpAuthentication extends AbstractAuthenticationToken implements java.io.Serializable {

    private static final long serialVersionUID = -8631010419770301790L;

    /**
     * principal
     */
    private final Object      principal;

    /**
     * 邮箱/验证码
     */
    private final String      recipient;

    /**
     * 类型
     */
    private final String      type;

    public OtpAuthentication(Object principal, String recipient, String type) {
        super(Lists.newArrayList());
        this.principal = principal;
        this.recipient = recipient;
        this.type = type;
        setAuthenticated(false);
    }

    public OtpAuthentication(Object principal, String recipient, String type,
                             boolean authenticated) {
        super(Lists.newArrayList());
        this.principal = principal;
        this.recipient = recipient;
        this.type = type;
        setAuthenticated(authenticated);
    }

    /**
     * This constructor should only be used by AuthenticationManagercore.encrypt.EncryptContextHelper or
     * AuthenticationProvidercore.encrypt.EncryptContextHelper implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = truecore.encrypt.EncryptContextHelper)
     * authentication token.
     *
     * @param recipient      {@link  String}
     * @param type {@link  String}
     * @param authorities {@link  GrantedAuthority}
     * @param principal {@link Object}
     */
    public OtpAuthentication(Object principal, String recipient, String type,
                             Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.recipient = recipient;
        this.principal = principal;
        this.type = type;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.recipient;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getType() {
        return type;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
