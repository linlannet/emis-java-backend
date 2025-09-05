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
package net.linlan.emis.support.manage.preview;

import net.linlan.emis.support.exception.SupportException;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * 演示环境不允许操作
 *
 * @author Linlan
 */
public class DemoEnvironmentNotAllowException extends SupportException {

    public DemoEnvironmentNotAllowException() {
        super("demo_environment_does_not_allow_operation", "演示环境不允许操作", FORBIDDEN);
    }
}
