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
 * @author Linlan
 */
public class UserGroupNotExistException extends SupportException {

    public UserGroupNotExistException() {
        this("用户组不存在");
    }

    public UserGroupNotExistException(String message) {
        super("user_group_not_exist", message, HttpStatus.BAD_REQUEST);
    }
}
