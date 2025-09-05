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
package net.linlan.emis.support.manage.version;

/**
 *
 * @author Linlan
 */
public final class Version {
    /**
     * 重要
     */
    private static final int    MAJOR              = 1;
    /**
     * 次要的
     */
    private static final int    MINOR              = 3;
    /**
     * 修补
     */
    private static final int    PATCH              = 0;
    /**
     * 版本
     */
    private static final String EDITION            = "release";

    /**
     * Global Serialization value for Spring Security Authorization Server classes.
     */
    public static final long    SERIAL_VERSION_UID = getVersion().hashCode();

    public static String getVersion() {
        return MAJOR + "." + MINOR + "." + PATCH + "-" + EDITION;
    }
}
