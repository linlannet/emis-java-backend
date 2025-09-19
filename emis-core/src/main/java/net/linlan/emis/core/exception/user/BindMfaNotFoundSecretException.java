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
package net.linlan.emis.core.exception.user;

import org.springframework.http.HttpStatus;

import net.linlan.emis.support.exception.SupportException;

/**
 * 绑定MFA 不存在秘钥异常
 *
 * @author Linlan
 */
public class BindMfaNotFoundSecretException extends SupportException {
    public BindMfaNotFoundSecretException() {
        super("bind_mfa_not_found_secret_error", "绑定TOTP 不存在秘钥", HttpStatus.BAD_REQUEST);
    }
}
