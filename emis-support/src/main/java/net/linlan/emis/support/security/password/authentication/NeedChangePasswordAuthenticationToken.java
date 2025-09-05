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
package net.linlan.emis.support.security.password.authentication;

import java.util.Collections;

// import java.io.Serial;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

/**
 * @author Linlan
 */
public class NeedChangePasswordAuthenticationToken extends AbstractAuthenticationToken
                                                   implements java.io.Serializable {

    //@Serial
    private static final long    serialVersionUID = -8631010419770301115L;

    private final Authentication clientPrincipal;

    public NeedChangePasswordAuthenticationToken(Authentication clientPrincipal) {
        super(Collections.emptyList());
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.clientPrincipal = clientPrincipal;
    }

    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

}
