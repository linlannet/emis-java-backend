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
package net.linlan.emis.manage.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import net.linlan.emis.core.security.authentication.IdpAuthentication;
import net.linlan.emis.core.security.authentication.SmsAuthentication;
import net.linlan.emis.core.security.mfa.MfaAuthentication;
import net.linlan.emis.manage.core.IdentityProviderAuthentication;
import net.linlan.emis.manage.core.OtpAuthentication;
import net.linlan.emis.support.security.authentication.CustomAuthenticationProvider;
import net.linlan.emis.user.constant.IdentityProviderType;
import static net.linlan.emis.support.security.authentication.CustomAuthenticationProvider.USERNAME_PASSWORD;
import static net.linlan.emis.user.constant.IdentityProviderType.IPT_USERNAME_PASSWORD;
import static net.linlan.emis.user.constant.IdentityProviderType.SMS;

public class AuthenticationUtils {

    /**
     * 获取认证类型
     *
     * @param authentication {@link Authentication}
     * @return {@link String}
     */
    public static String geAuthTypeString(Authentication authentication) {
        //用户名密码
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return IPT_USERNAME_PASSWORD.value();
        }
        //身份提供商
        if (authentication instanceof IdpAuthentication) {
            return ((IdpAuthentication) authentication).getProviderType();
        }
        //短信登录
        if (authentication instanceof SmsAuthentication) {
            return SMS.value();
        }
        //MFA
        if (authentication instanceof MfaAuthentication) {
            return geAuthTypeString(((MfaAuthentication) authentication).getFirst());
        }
        throw new IllegalArgumentException("未知认证对象");
    }

    /**
     * 获取认证类型
     *
     * @param authentication {@link Authentication}
     * @return {@link String}
     */
    public static CustomAuthenticationProvider geAuthType(Authentication authentication) {
        //用户名密码
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            return USERNAME_PASSWORD;
        }
        //身份提供商
        if (authentication instanceof IdentityProviderAuthentication) {
            IdentityProviderType type = ((IdentityProviderAuthentication) authentication)
                .getProviderType();
            return new CustomAuthenticationProvider(type.value(), type.name());
        }
        //短信/邮箱验证码登录
        if (authentication instanceof OtpAuthentication) {
            String type = ((OtpAuthentication) authentication).getType();
            return new CustomAuthenticationProvider(type, "");
        }
        throw new IllegalArgumentException("未知认证对象");
    }

}
