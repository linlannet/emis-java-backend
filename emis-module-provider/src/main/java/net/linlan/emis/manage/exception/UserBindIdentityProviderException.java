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
package net.linlan.emis.manage.exception;

import org.springframework.http.HttpStatus;

import net.linlan.emis.support.exception.SupportException;

/**
 *
 * @author Linlan
 */
public class UserBindIdentityProviderException extends SupportException {
    public static final String USER_BIND_IDP_ERROR = "user_bind_idp_error";

    public UserBindIdentityProviderException() {
        super(USER_BIND_IDP_ERROR, "用户绑定身份提供商错误", HttpStatus.FORBIDDEN);
    }

    public UserBindIdentityProviderException(String description, HttpStatus status) {
        super(USER_BIND_IDP_ERROR, description, status);
    }

    public UserBindIdentityProviderException(String error, String description, HttpStatus status) {
        super(error, description, status);
    }
}
