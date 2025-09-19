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
package net.linlan.emis.core.constant;

import net.linlan.emis.support.constant.EmisConstants;

/**
 * SessionConstants
 *
 * @author Linlan
 */
public final class SessionConstants {

    /**
     * 组名称
     */
    public static final String SESSION_GROUP_NAME = "会话状态";

    /**
     * 会话路径
     */
    public final static String SESSION_PATH       = EmisConstants.V1_API_PATH + "/session";

    /**
     * 会话当前用户，CURRENT_USER
     */
    public static final String CURRENT_USER       = SESSION_PATH + "/current_user";

    /**
     * session 当前状态
     */
    public static final String CURRENT_STATUS     = SESSION_PATH + "/current_status";

}
