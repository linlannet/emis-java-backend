/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.jwt.endpoint;

/**
 *
 * @author Linlan
 * Created on 2023/7/19 22:09
 */
public final class JwtParameterNames {
    /**
     * {@code error} - used in Authorization Response and Access Token Response.
     */
    public static final String ERROR             = "error";

    /**
     * {@code error_description} - used in Authorization Response and Access Token
     * Response.
     */
    public static final String ERROR_DESCRIPTION = "error_description";

    /**
     * {@code error_uri} - used in Authorization Response and Access Token Response.
     */
    public static final String ERROR_URI         = "error_uri";

    private JwtParameterNames() {
    }

}
