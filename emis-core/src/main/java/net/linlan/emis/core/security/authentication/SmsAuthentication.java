/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.collect.Lists;

/**
 * 短信OPT认证
 *
 * @author Linlan
 */
public class SmsAuthentication extends AbstractAuthenticationToken implements java.io.Serializable {

    // ~ Instance fields

    private static final long serialVersionUID = -1506897701981698420L;
    /**
     * principal
     */
    private final Object      principal;
    /**
     * 手机号
     */
    private final String      mobile;

    // ~ Constructors

    /**
     * This constructor can be safely used by any code that wishes to create a
     * UsernamePasswordAuthenticationTokencore.encrypt.EncryptContextHelper, as the {@link #isAuthenticated()}
     * will return falsecore.encrypt.EncryptContextHelper.
     * @param principal 当前用户
     * @param mobile    手机号码
     */
    public SmsAuthentication(Object principal, String mobile) {
        super(Lists.newArrayList());
        this.principal = principal;
        this.mobile = mobile;
        setAuthenticated(false);
    }

    /**
     * This constructor can be safely used by any code that wishes to create a
     * UsernamePasswordAuthenticationTokencore.encrypt.EncryptContextHelper, as the {@link #isAuthenticated()}
     * will return falsecore.encrypt.EncryptContextHelper.
     * @param principal 当前用户
     * @param mobile    手机号码
     * @param authenticated    是否授权
     */
    public SmsAuthentication(Object principal, String mobile, boolean authenticated) {
        super(Lists.newArrayList());
        this.principal = principal;
        this.mobile = mobile;
        setAuthenticated(authenticated);
    }

    /**
     * This constructor should only be used by AuthenticationManagercore.encrypt.EncryptContextHelper or
     * AuthenticationProvidercore.encrypt.EncryptContextHelper implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = truecore.encrypt.EncryptContextHelper)
     * authentication token.
     *
     * @param principal {@link Object}
     * @param mobile      {@link  String}
     * @param authorities {@link  GrantedAuthority}
     */
    public SmsAuthentication(Object principal, String mobile,
                             Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.mobile = mobile;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.mobile;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
