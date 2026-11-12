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
package net.linlan.emis.protocol.jwt.constant;

import net.linlan.emis.protocol.constant.ProtocolConstants;

/**
 * 协议常量
 *
 * @author Linlan
 * Created on 2021/12/8 21:29
 */
public class JwtProtocolConstants {

    /**
     * 协议缓存前缀
     */
    public static final String JWT_PROTOCOL_CACHE_PREFIX = ProtocolConstants.PROTOCOL_CACHE_PREFIX
                                                           + "jwt" + ":";

    public static final String TARGET_URL                = "target_url";
    public static final String ID_TOKEN                  = "id_token";
    public static final String NONCE                     = "nonce";

    public static final String URL                       = "url";
    public static final String BINDING_TYPE              = "binding_type";

    public static final String JWT_ERROR_URI             = "https://linlan.net/emis";

    public static final String POST_LOGOUT_REDIRECT_URI  = "post_logout_redirect_uri";

}
