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
package net.linlan.emis.support.security.util;

import org.springframework.security.authentication.event.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.frame.comm.constant.AdminType;

/**
 * Spring Security的实用程序类
 *
 * @author Linlan
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取错误信息
     *
     * @param event {@link AbstractAuthenticationFailureEvent}
     * @return {@link String}
     */
    public static String getFailureMessage(AbstractAuthenticationFailureEvent event) {
        String message = "未知错误";
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            message = "登录密码错误";
        }
        if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            message = "账户密码过期";
        }
        if (event instanceof AuthenticationFailureDisabledEvent) {
            message = "账户已禁用";
        }
        if (event instanceof AuthenticationFailureExpiredEvent) {
            message = "帐户已过期";
        }
        if (event instanceof AuthenticationFailureLockedEvent) {
            message = "账户已锁定";
        }
        if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            message = "代理不受信任";
        }
        if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            message = "提供商配置错误";
        }
        if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            message = "发生内部异常";
        }
        return message;
    }

    /**
     * 获取当前登录用户的信息，如果未登录返回null
     *
     * @return Object 当前登录用户
     */
    public static UserDetails getCurrentUser() {
        return null != getAuthentication() ? (UserDetails) getAuthentication().getPrincipal()
            : null;
    }

    /**
     * id 当前用户ID
     * @return 当前用户ID
     * */
    public static Long getCurrentUserId() {
        return null != getAuthentication()
            ? ((UserDetails) getAuthentication().getPrincipal()).getAdminId()
            : null;
    }

    /**
     * Username 当前用户名
     * @return 当前用户名称
     */
    public static String getCurrentUserName() {
        return null != getAuthentication() ? (String) getAuthentication().getPrincipal() : null;
    }

    public static String getCurrentUserType() {
        return null != getAuthentication()
            ? ((UserDetails) getAuthentication().getPrincipal()).getUserType()
            : AdminType.USER.getType();
    }

    /**
     * 获取当前登录用户的信息
     *
     * @return Authentication 权鉴对象
     */
    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication : null;
    }

    /**
     * 验证当前用户是否登录
     *
     * @return boolean 是否登录
     */
    public static boolean isAuthenticated() {
        // if security session eq s-id is not null to index
        Authentication authentication = getAuthentication();
        return (authentication != null) ? authentication.isAuthenticated() : false;
    }

    public static boolean isPrincipalAuthenticated(Authentication principal) {
        return principal.isAuthenticated();
    }

}
