/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.idsource.constant;

import net.linlan.emis.support.constant.EmisConstants;

/**
 * 身份源常量
 *
 * @author Linlan
 *
 */
public final class IdentityConstants {

    /**
     * 组名称
     */
    public static final String IDENTITY_GROUP_NAME  = "身份源管理";

    /**
     * 身份源API
     */
    public final static String IDENTITY_SOURCE_PATH = EmisConstants.V1_API_PATH
                                                      + "/identity_source";

}
