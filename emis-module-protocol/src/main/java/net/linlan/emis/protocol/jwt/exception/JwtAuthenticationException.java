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
package net.linlan.emis.protocol.jwt.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import lombok.Getter;

/**
 *
 * @author Linlan
 * Created on 2023/7/8 00:27
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final JwtError error;

    public JwtAuthenticationException(JwtError error) {
        super(error.getDescription());
        this.error = error;
    }

    public JwtAuthenticationException(JwtError error, Throwable cause) {
        this(error, error.getDescription(), cause);
    }

    public JwtAuthenticationException(JwtError error, String message, Throwable cause) {
        super(message, cause);
        Assert.notNull(error, "error cannot be null");
        this.error = error;
    }
}
