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
package net.linlan.emis.console.controller.account;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.update.user.*;
import net.linlan.emis.console.service.user.UserProfileService;
import static net.linlan.emis.user.constant.AccountConstants.*;

/**
 * 账户管理
 *
 * @author Linlan
 * Created on  2021/9/12 21:39
 */
@Tag(name = ACCOUNT_GROUP_NAME + "-" + "账户信息管理")
@RestController
@RequestMapping(value = USER_PROFILE_PATH)
public class UserProfileController {

    /**
     * 修改账户信息
     *
     * @return {@link  ResponseResult}
     */
    @Audit(type = EventType.MODIFY_ACCOUNT_INFO_PORTAL)
    @Operation(summary = "修改账户信息")
    @PutMapping("/change_info")
    public ResponseResult<Boolean> changeInfo(@RequestBody @Validated UpdateUserInfoRequest param) {
        Boolean result = userProfileService.changeInfo(param);
        return ResponseResult.ok(result);
    }

    /**
     * 修改密码
     *
     * @return {@link  ResponseResult}
     */
    @Audit(type = EventType.MODIFY_USER_PASSWORD_PORTAL)
    @Operation(summary = "修改账户密码")
    @PutMapping("/change_password")
    public ResponseResult<Boolean> changePassword(@RequestBody @Validated ChangePasswordRequest param) {
        return ResponseResult.ok(userProfileService.changePassword(param));
    }

    /**
     * 准备修改手机
     *
     * @return {@link  ResponseResult}
     */
    @Audit(type = EventType.PREPARE_MODIFY_MOBILE)
    @Operation(summary = "准备修改手机")
    @PostMapping("/prepare_change_mobile")
    public ResponseResult<Boolean> prepareChangeMobile(@RequestBody @Validated PrepareChangeMobileRequest param) {
        return ResponseResult.ok(userProfileService.prepareChangeMobile(param));
    }

    /**
     * 修改手机
     *
     * @return {@link  ResponseResult}
     */
    @Audit(type = EventType.MODIFY_USER_PHONE_PORTAL)
    @Operation(summary = "修改手机")
    @PutMapping("/change_mobile")
    public ResponseResult<Boolean> changeMobile(@RequestBody @Validated ChangeMobileRequest param) {
        return ResponseResult.ok(userProfileService.changeMobile(param));
    }

    /**
     * 准备修改邮箱
     *
     * @return {@link  ResponseResult}
     */
    @Audit(type = EventType.PREPARE_MODIFY_EMAIL)
    @Operation(summary = "准备修改邮箱")
    @PostMapping("/prepare_change_email")
    public ResponseResult<Boolean> prepareChangeEmail(@RequestBody @Validated PrepareChangeEmailRequest param) {
        return ResponseResult.ok(userProfileService.prepareChangeEmail(param));
    }

    /**
     * 修改邮箱
     *
     * @return {@link  ResponseResult}
     */
    @Audit(type = EventType.MODIFY_USER_EMAIL_PORTAL)
    @Operation(summary = "修改邮箱")
    @PutMapping("/change_email")
    public ResponseResult<Boolean> changeEmail(@RequestBody @Validated ChangeEmailRequest param) {
        return ResponseResult.ok(userProfileService.changeEmail(param));
    }

    /**
     * 忘记密码发送验证码
     *
     * @return {@link  ResponseResult}
     */
    @Operation(summary = "忘记密码发送验证码")
    @GetMapping(FORGET_PASSWORD_CODE)
    public ResponseResult<Boolean> forgetPasswordCode(@Parameter(description = "验证码接收者（邮箱/手机号）") @RequestParam String recipient) {
        return ResponseResult.ok(userProfileService.forgetPasswordCode(recipient));
    }

    /**
     * 忘记密码预认证
     *
     * @return {@link  ResponseResult}
     */
    @Operation(summary = "忘记密码预认证")
    @PostMapping(PREPARE_FORGET_PASSWORD)
    public ResponseResult<Boolean> prepareForgetPassword(@RequestBody @Validated PrepareForgetPasswordRequest param) {
        return ResponseResult
            .ok(userProfileService.prepareForgetPassword(param.getRecipient(), param.getCode()));
    }

    /**
     * 忘记密码
     *
     * @return {@link  ResponseResult}
     */
    @Operation(summary = "忘记密码")
    @PutMapping(FORGET_PASSWORD)
    public ResponseResult<Boolean> forgetPassword(@RequestBody @Validated ForgetPasswordRequest forgetPasswordRequest) {
        return ResponseResult.ok(userProfileService.forgetPassword(forgetPasswordRequest));
    }

    /**
     * 用户资料service
     */
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
}
