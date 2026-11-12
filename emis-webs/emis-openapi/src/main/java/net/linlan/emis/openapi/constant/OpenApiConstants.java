/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.constant;

import static net.linlan.emis.support.constant.EmisConstants.V1_API_PATH;

/**
 * Open API 常量
 *
 *
 * @author Linlan
 * Created on  2022/9/5 20:58
 */
public class OpenApiConstants {
    /**
     * OpenAPI 路径
     */
    public final static String  OPEN_API_PATH           = V1_API_PATH + "/open";

    /**
     * TOKEN过期时间，2小时
     */
    public final static Integer ACCESS_TOKEN_EXPIRES_IN = 7200;

    /**
     * 组名称
     */
    public static final String  OPEN_API_NAME           = "EMIS多身份集成系统开放接口";

    /**
     * 访问凭证
     */
    public final static String  AUTH_PATH               = OPEN_API_PATH + "/auth";

    /**
     * 用户
     */
    public final static String  USER_PATH               = OPEN_API_PATH + "/user";

    /**
     * 组织
     */
    public final static String  ORGAN_PATH              = OPEN_API_PATH + "/organ";

    /**
     * 应用账户
     */
    public final static String  APP_ACCOUNT_PATH        = OPEN_API_PATH + "/app/account";

}
