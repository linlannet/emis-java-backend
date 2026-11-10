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
package net.linlan.emis.provider.constant;

/**
 * 认证常量
 *
 * @author Linlan
 */
public final class AuthenticationConstants {

    /**
     * 提供商CODE
     */
    public static final String PROVIDER_CODE                              = "provider_code";
    /**
     * 无效状态参数
     */
    public static final String INVALID_STATE_PARAMETER_ERROR_CODE         = "invalid_state_parameter";
    /**
     * 无参数
     */
    public static final String INVALID_NONCE_PARAMETER_ERROR_CODE         = "invalid_nonce_parameter";
    /**
     * 无效代码参数
     */
    public static final String INVALID_CODE_PARAMETER_ERROR_CODE          = "invalid_code_parameter";
    /**
     * 授权后绑定
     */
    public static final String BIND_AFTER_AUTH                            = "bind_after_auth";
    /**
     * 授权请求未发现
     */
    public static final String AUTHORIZATION_REQUEST_NOT_FOUND_ERROR_CODE = "authorization_request_not_found";
    /**
     * 绑定重定向地址
     */
    public static final String BIND_REDIRECT                              = "bind_redirect";

}
