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

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.query.account.UserListNotInGroupQuery;
import net.linlan.emis.console.pojo.query.account.UserListQuery;
import net.linlan.emis.console.pojo.result.account.*;
import net.linlan.emis.console.pojo.result.app.UserIdpBindListResult;
import net.linlan.emis.console.pojo.save.account.UserCreateParam;
import net.linlan.emis.console.pojo.update.account.ResetPasswordParam;
import net.linlan.emis.console.pojo.update.account.UserUpdateParam;
import net.linlan.emis.console.service.account.UserService;
import net.linlan.emis.console.service.app.UserIdpBindService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.uext.OtpContextHelper;
import net.linlan.emis.uext.entity.MessageNoticeChannel;
import net.linlan.emis.user.constant.UserStatus;
import static net.linlan.emis.user.constant.AccountConstants.ACCOUNT_GROUP_NAME;
import static net.linlan.emis.user.constant.AccountConstants.USER_PATH;

/**
 * 系统账户-用户
 *
 * @author Linlan
 * Created on  2020/7/11 21:18
 */
@Validated
@Tag(name = ACCOUNT_GROUP_NAME + "-" + "用户管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = USER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    /**
     * 获取用户列表
     *
     * @param page {@link PageModel}
     * @return {@link UserListQuery}
     */
    @Operation(summary = "获取用户列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<UserListResult>> getUserList(PageModel page,
                                                            @Validated UserListQuery query) {
        Page<UserListResult> result = userService.getUserList(page, query);
        return ResponseResult.ok(result);
    }

    /**
     * 获取用户列表（不在指定用户组）
     *
     * @param query {@link UserListNotInGroupQuery} 分组ID
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户列表（不在指定用户组）")
    @GetMapping(value = "/notin_group_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<UserListResult>> getUserListNotInGroup(PageModel model,
                                                                      @Validated UserListNotInGroupQuery query) {
        Page<UserListResult> result = userService.getUserListNotInGroup(model, query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建用户")
    @Audit(type = EventType.CREATE_USER)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createUser(@RequestBody @Validated UserCreateParam param) {
        boolean result = userService.createUser(param);
        return ResponseResult.ok(result);
    }

    /**
     * 更改系统用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改用户")
    @Audit(type = EventType.UPDATE_USER)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateUser(@RequestBody @Validated UserUpdateParam param) {
        boolean result = userService.updateUser(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除用户")
    @Audit(type = EventType.DELETE_USER)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteUser(@PathVariable(value = "id") String id) {
        boolean result = userService.deleteUser(id);
        return ResponseResult.ok(result);
    }

    /**
     * 批量删除用户
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "批量删除用户")
    @Audit(type = EventType.DELETE_USER)
    @DeleteMapping(value = "/batch_delete")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> batchDeleteUser(String[] ids) {
        boolean result = userService.batchDeleteUser(ids);
        return ResponseResult.ok(result);
    }

    /**
     * 获取用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户信息")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    @GetMapping(value = "/get/{id}")
    public ResponseResult<UserResult> getUser(@PathVariable(value = "id") String id) {
        UserResult result = userService.getUser(id);
        return ResponseResult.ok(result);
    }

    /**
     * 批量获取用户信息
     *
     * @param ids {@link List}
     * @return {@link BatchOrganizationResult}
     */
    @Validated
    @Operation(summary = "批量获取用户信息")
    @GetMapping(value = "/batch_get")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<BatchUserResult>> batchGetUser(@RequestParam(value = "ids", required = false) @NotNull(message = "用户ID不能为空") List<String> ids) {
        List<BatchUserResult> result = userService
            .batchGetUser(ids.stream().collect(Collectors.toList()));
        return ResponseResult.ok(result);
    }

    /**
     * 启用用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "启用用户")
    @Audit(type = EventType.ENABLE_USER)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enableUser(@PathVariable(value = "id") String id) {
        Boolean result = userService.changeUserStatus(id, UserStatus.ENABLED);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用用户")
    @Audit(type = EventType.DISABLE_USER)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableUser(@PathVariable(value = "id") String id) {
        Boolean result = userService.changeUserStatus(id, UserStatus.DISABLED);
        return ResponseResult.ok(result);
    }

    /**
     * 重置用户密码
     *
     * @param param {@link ResetPasswordParam}
     * @return Boolean
     */
    @Lock
    @Preview
    @Operation(summary = "重置用户密码")
    @Audit(type = EventType.MODIFY_USER_PASSWORD)
    @PutMapping(value = "/reset_password")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> resetUserPassword(@Validated @RequestBody ResetPasswordParam param) {
        boolean result = userService.resetUserPassword(param);
        return ResponseResult.ok(result);
    }

    /**
     * 解锁用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "解锁用户")
    @Audit(type = EventType.UNLOCK_USER)
    @PutMapping(value = "/unlock/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> unlockUser(@PathVariable(value = "id") String id) {
        Boolean result = userService.changeUserStatus(id, UserStatus.ENABLED);
        return ResponseResult.ok(result);
    }

    /**
     * 参数有效性验证
     *
     * @return {@link Boolean}
     */
    @Operation(summary = "参数有效性验证")
    @GetMapping(value = "/param_check")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> userParamCheck(@Parameter(description = "验证类型") @NotNull(message = "验证类型不能为空") CheckValidityType type,
                                                  @Parameter(description = "值") @NotEmpty(message = "验证值不能为空") String value,
                                                  @Parameter(description = "ID") String id) {
        Boolean result = userService.userParamCheck(type, value, id);
        //返回
        return ResponseResult.ok(result);
    }

    /**
     * 查询用户登录审计列表
     *
     * @param id     {@link Long}
     * @param pageModel {@link PageModel}
     * @return {@link ResponseResult}
     */
    @Operation(description = "查询用户登录审计列表")
    @GetMapping(value = "/login_audit/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<UserLoginAuditListResult>> getUserLoginAuditList(@Parameter(description = "ID") @RequestParam(value = "userId", required = false) @NotNull(message = "用户ID不能为空") String id,
                                                                                PageModel pageModel) {
        Page<UserLoginAuditListResult> list = userService.findUserLoginAuditList(id, pageModel);
        return ResponseResult.ok(list);
    }

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId {@link String}
     * @return {@link UserIdpBindListResult}
     */
    @Operation(summary = "查询用户身份提供商绑定")
    @GetMapping(value = "/idp_bind")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<UserIdpBindListResult>> getUserIdpBindList(@Validated @RequestParam(value = "userId", required = false) @Parameter(description = "ID") @NotBlank(message = "用户ID不能为空") String userId) {
        List<UserIdpBindListResult> result = userIdpBindService.getUserIdpBindList(userId);
        return ResponseResult.ok(result);
    }

    /**
     * 删除用户身份提供商绑定
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除用户身份提供商绑定")
    @Audit(type = EventType.DELETE_USER_IDP_BIND)
    @DeleteMapping(value = "/unbind_idp")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> unbindUserIdp(@Validated @RequestParam(value = "id", required = false) @Parameter(description = "ID") @NotBlank(message = "绑定ID不能为空") String id) {
        boolean result = userIdpBindService.unbindUserIdpBind(id);
        return ResponseResult.ok(result);
    }

    /**
     * 发送 OPT
     *
     * @return {@link ResponseResult}
     */
    @PostMapping("/otp")
    @Lock(namespaces = "otp")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseEntity<ResponseResult<Boolean>> send(@Validated SendOptRequest request) {
        otpContextHelp.sendOtp(request.getTarget(), request.getType(), request.getChannel());
        return ResponseEntity.ok(ResponseResult.ok());
    }

    /**
     * 发送 OTP 请求
     */
    @Data
    public static class SendOptRequest implements Serializable {
        /**
         * 发送场景
         */
        @Parameter(description = "type")
        @NotNull(message = "发送场景不能为空")
        private String               type;

        /**
         * 渠道
         */
        @Parameter(description = "channel")
        @NotNull(message = "消息渠道不能为空")
        private MessageNoticeChannel channel;

        /**
         * 目标
         */
        @Parameter(description = "target")
        private String               target;
    }

    /**
     * 用户服务类
     */
    private final UserService        userService;

    /**
     * OTP
     */
    private final OtpContextHelper   otpContextHelp;

    /**
     * UserIdpBindService
     */
    private final UserIdpBindService userIdpBindService;
}
