/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.controller.session;

import java.io.Serializable;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.manage.core.AuthenticationTrustResolverImpl;
import net.linlan.emis.support.security.password.authentication.NeedChangePasswordAuthenticationToken;
import static net.linlan.emis.core.constant.SessionConstants.CURRENT_STATUS;
import static net.linlan.emis.core.constant.SessionConstants.SESSION_GROUP_NAME;
import static net.linlan.emis.support.security.constant.SecurityConstants.REQUIRE_RESET_PASSWORD;

/**
 * 会话状态
 *
 * @author Linlan
 * Created on  2022/7/30 21:09
 */
@Slf4j
@Tag(name = SESSION_GROUP_NAME + "-" + "会话状态")
@RestController
@RequestMapping
public class CurrentSessionStatusEndpoint {
    private final AuthenticationTrustResolver delegate = new AuthenticationTrustResolverImpl();

    /**
     * 获取当前登录状态
     *
     * @return {@link CurrentStatusResult}
     */
    @Operation(summary = "获取当前登录状态")
    @GetMapping(value = CURRENT_STATUS)
    public ResponseResult<CurrentStatusResult> getCurrentStatus() {
        CurrentStatusResult.CurrentStatusResultBuilder builder = CurrentStatusResult.builder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        builder.authenticated(authentication.isAuthenticated());
        if (delegate.isAnonymous(authentication)) {
            builder.authenticated(false);
        }
        if (authentication instanceof NeedChangePasswordAuthenticationToken) {
            builder.status(REQUIRE_RESET_PASSWORD);
        }
        return ResponseResult.ok(builder.build());
    }

    /**
     * 当前状态返回结果
     *
     * @author Linlan
     * Created on  2020/10/26 21:16
     */
    @Data
    @Builder
    @Schema(description = "当前状态响应")
    public static class CurrentStatusResult implements Serializable {
        /**
         * 是否认证
         */
        private Boolean authenticated;

        /**
         * status
         */
        private String  status;
    }
}
