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
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.core.exception.user.UserNotFoundException;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.utils.DesensitizedUtils;
import net.linlan.emis.support.utils.ImageAvatarUtils;
import static net.linlan.emis.core.constant.SessionConstants.CURRENT_USER;
import static net.linlan.emis.core.constant.SessionConstants.SESSION_GROUP_NAME;

/**
 * 当前用户
 *
 * @author Linlan
 * Created on  2020/12/23 21:49
 */
@Slf4j
@Tag(name = SESSION_GROUP_NAME + "-" + "当前用户")
@RestController
@RequestMapping
public class CurrentUserEndpoint {

    /**
     * 获取当前用户状态
     * @return
     */
    @Operation(summary = "获取当前用户状态")
    @GetMapping(CURRENT_USER)
    public ResponseResult<CurrentUserResult> getCurrentUser() {
        //当前用户名
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Optional<EmisAdminVo> optional = administratorRepository
            .findById(userDetails.getId());
        if (!optional.isPresent()) {
            SecurityContextHolder.clearContext();
            throw new UserNotFoundException();
        }
        EmisAdminVo administrator = optional.get();
        CurrentUserResult result = new CurrentUserResult();
        //用户ID
        result.setAccountId(userDetails.getId());
        //用户名
        result.setUsername(administrator.getUsername());
        //姓名
        result.setFullName(administrator.getFullName());
        //昵称
        result.setNickName(administrator.getNickName());
        //头像
        if (StringUtils.isEmpty(administrator.getAvatarUrl())) {
            result.setAvatarUrl(
                ImageAvatarUtils.bufferedImageToBase64(ImageAvatarUtils.generateAvatarImg()));
        } else {
            result.setAvatarUrl(administrator.getAvatarUrl());
        }
        //邮箱
        result.setEmail(DesensitizedUtils.emailEncrypt(administrator.getEmail()));
        //手机号
        result.setMobile(DesensitizedUtils.mobileEncrypt(administrator.getMobile()));
        return ResponseResult.ok(result);
    }

    /**
     * 当前用户结果返回
     *
     * @author Linlan
     * Created on  2020/10/26 23:16
     */
    @Data
    @Schema(description = "当前用户响应")
    public static class CurrentUserResult implements Serializable {
        /**
         * 帐户ID
         */
        @Schema(description = "帐户ID")
        private String      accountId;

        /**
         * 用户名
         */
        @Schema(description = "用户名")
        private String      username;

        /**
         * 姓名
         */
        @Schema(description = "姓名")
        private String      fullName;

        /**
         * 昵称
         */
        @Schema(description = "昵称")
        private String      nickName;

        /**
         * 头像
         */
        @Schema(description = "头像")
        private String      avatarUrl;

        /**
         * 邮箱
         */
        @Schema(description = "邮箱")
        private String      email;

        /**
         * 手机号
         */
        @Schema(description = "手机号")
        private String      mobile;
        /**
         * 访问权限
         */
        private Set<String> access;
    }

    private final AdministratorRepository administratorRepository;

    public CurrentUserEndpoint(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }
}
