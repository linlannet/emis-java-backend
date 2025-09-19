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
import net.linlan.emis.support.security.constant.SecurityConstants;

/**
 * 认证授权常量
 *
 * @author Linlan
 */
public class AuthnConstants {
    /**
     * 认证管理
     */
    public static final String AUTHN_GROUP_NAME          = "认证管理";
    /**
     * 授权路径
     */
    public final static String AUTHN_PATH                = EmisConstants.V1_API_PATH + "/authn";

    /**
     * 登录配置
     */
    public static final String LOGIN_CONFIG              = SecurityConstants.LOGIN_PATH + "/config";

    /**
     * 前端登录路由
     */
    public static final String FE_LOGIN                  = "/login";
    /**
     * 认证请求URL
     */
    public static final String AUTHORIZATION_REQUEST_URI = EmisConstants.V1_API_PATH
                                                           + "/authorization";

}
