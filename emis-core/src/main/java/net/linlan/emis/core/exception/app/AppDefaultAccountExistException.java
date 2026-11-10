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
package net.linlan.emis.core.exception.app;

import net.linlan.emis.support.exception.SupportException;

/**
 * 默认应用账户已存在
 *
 * @author Linlan
 */
public class AppDefaultAccountExistException extends SupportException {

    public AppDefaultAccountExistException() {
        super("app_default_account_exist", "默认应用账户已存在", DEFAULT_STATUS);
    }
}
