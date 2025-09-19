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
 * 密码验证异常
 *
 * @author Linlan
 */
public class PasswordValidatedFailException extends SupportException {
    public PasswordValidatedFailException() {
        super("password_validated_fail_error", "密码错误，身份验证失败", HttpStatus.BAD_REQUEST);
    }

    public PasswordValidatedFailException(String description) {
        super("password_validated_fail_error", description, HttpStatus.BAD_REQUEST);
    }
}
