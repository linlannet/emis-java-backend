/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.user.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.emis.core.exception.user.UserNotFoundException;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserExtService;
import net.linlan.emis.user.service.UserInfoService;
import net.linlan.utils.SpringContextUtils;

/**
 * 用户工具
 *
 * @author Linlan
 */
@Slf4j
public final class UserUtils {

    public static UserInfo getUser() {
        return getUser(SecurityUtils.getCurrentUserId());
    }

    public static UserInfo getUser(String userId) {
        UserInfo optional = SpringContextUtils.getBean(UserInfoService.class).findById(userId);
        if (ObjectUtils.isNotEmpty(optional)) {
            return optional;
        }
        SecurityContextHolder.clearContext();
        log.error("根据用户ID: [{}] 未查询到用户信息", userId);
        throw new UserNotFoundException();
    }

    public static UserExt getUserDetails() {
        return getUserDetails(SecurityUtils.getCurrentUserId());
    }

    public static UserExt getUserDetails(String userId) {
        UserExt optional = SpringContextUtils.getBean(UserExtService.class).findById(userId);
        if (ObjectUtils.isNotEmpty(optional)) {
            return optional;
        }
        SecurityContextHolder.clearContext();
        log.error("根据用户ID: [{}] 未查询到用户信息", userId);
        throw new UserNotFoundException();
    }
}
