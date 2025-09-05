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
package net.linlan.emis.support.security.password;

import net.linlan.emis.support.security.password.exception.InvalidPasswordException;

/**
 * 在创建用户或更改密码时验证密码值。应实施为系统定义的密码策略。用户界面显然也应该实现相同的策略。
 *
 * @author Linlan
 */
public interface PasswordValidator {
    /**
     * Validates the password as to whether it is valid for a specific user.
     *
     * @param password {@link String}
     * @throws InvalidPasswordException InvalidPasswordException
     */
    void validate(String password) throws InvalidPasswordException;
}
