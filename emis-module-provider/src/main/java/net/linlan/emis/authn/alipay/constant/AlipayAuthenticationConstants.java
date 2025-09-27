/*
 * emis-module-provider - provider身份提供商
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
package net.linlan.emis.authn.alipay.constant;

/**
 * 支付宝 认证常量
 *
 * @author Linlan
 */
public class AlipayAuthenticationConstants {

    public static final String AUTHORIZATION_REQUEST = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";

    public static final String USER_INFO_SCOPE       = "auth_user";

    public static final String APP_ID                = "app_id";

    public static final String AUTH_CODE             = "auth_code";

    public static final String SUCCESS_CODE          = "200";

}
