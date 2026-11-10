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
package net.linlan.emis.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Linlan
 */
public class PasswordIncludeUserInfoException extends InvalidPasswordException {
    public PasswordIncludeUserInfoException(String msg, Throwable t) {
        super(msg, t);
    }

    public PasswordIncludeUserInfoException(String msg) {
        super(msg);
    }

    public PasswordIncludeUserInfoException(String msg, HttpStatus status) {
        super(msg, status);
    }

    public PasswordIncludeUserInfoException(String error, String description, HttpStatus status) {
        super(error, description, status);
    }

    public PasswordIncludeUserInfoException(Throwable cause, String error, String description,
                                            HttpStatus status) {
        super(cause, error, description, status);
    }
}
