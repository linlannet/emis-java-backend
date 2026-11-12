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
package net.linlan.emis.openapi.endpoint;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.openapi.pojo.query.OapiV1UserListQuery;
import net.linlan.emis.openapi.pojo.result.UserListResult;
import net.linlan.emis.openapi.pojo.result.UserResult;
import net.linlan.emis.openapi.pojo.save.UserCreateParam;
import net.linlan.emis.openapi.pojo.update.UserUpdateParam;
import net.linlan.emis.openapi.service.UserService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.user.constant.UserStatus;
import static net.linlan.emis.openapi.constant.OpenApiConstants.USER_PATH;

/**
 * 系统账户-用户管理
 *
 * @author Linlan
 * Created on 2020/7/11 21:18
 */
@Validated
@Tag(name = "用户管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = USER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    /**
     * 获取用户列表
     *
     * @param page {@link PageModel}
     * @return {@link OapiV1UserListQuery}
     */
    @Operation(summary = "获取用户列表")
    @GetMapping(value = "/list")
    public ResponseResult<Page<UserListResult>> getUserList(PageModel page,
                                                            @Validated OapiV1UserListQuery query) {
        return ResponseResult.ok((userService.getUserList(page, query)));
    }

    /**
     * 获取用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/{id}")
    public ResponseResult<UserResult> getUser(@PathVariable(value = "id") String id) {
        return ResponseResult.ok(userService.getUser(id));
    }

    /**
     * 获取用户id
     *
     * @param foreignId {@link String}
     * @param mobileNumber {@link String}
     * @param email {@link String}
     * @param username {@link String}
     * @return {@link String}
     */
    @Operation(summary = "获取用户id", description = "必须且只能有一个参数传入")
    @GetMapping(value = "/user_id")
    public ResponseResult<String> getUserIdByParams(@RequestParam(required = false) String foreignId,
                                                    @RequestParam(required = false) String mobileNumber,
                                                    @RequestParam(required = false) String email,
                                                    @RequestParam(required = false) String username) {
        return ResponseResult
            .ok(userService.getUserIdByParams(foreignId, mobileNumber, email, username));
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
    public ResponseResult<Boolean> createUser(@RequestBody @Validated UserCreateParam param) {
        userService.createUser(param);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 更新用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改用户")
    @Audit(type = EventType.UPDATE_USER)
    @PutMapping(value = "/update")
    public ResponseResult<Boolean> updateUser(@RequestBody @Validated UserUpdateParam param) {
        userService.updateUser(param);
        return ResponseResult.ok(Boolean.TRUE);
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
    public ResponseResult<Boolean> deleteUser(@PathVariable(value = "id") String id) {
        userService.deleteUser(id);
        return ResponseResult.ok(Boolean.TRUE);
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
    public ResponseResult<Boolean> enableUser(@PathVariable(value = "id") String id) {
        userService.changeUserStatus(id, UserStatus.ENABLED);
        return ResponseResult.ok(Boolean.TRUE);
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
    public ResponseResult<Boolean> disableUser(@PathVariable(value = "id") String id) {
        userService.changeUserStatus(id, UserStatus.DISABLED);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 用户服务类
     */
    private final UserService userService;
}
