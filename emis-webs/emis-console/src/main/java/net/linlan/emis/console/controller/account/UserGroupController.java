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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.converter.account.UserGroupConverter;
import net.linlan.emis.console.pojo.query.account.UserGroupListQuery;
import net.linlan.emis.console.pojo.query.account.UserGroupMemberListQuery;
import net.linlan.emis.console.pojo.result.account.UserGroupListResult;
import net.linlan.emis.console.pojo.result.account.UserGroupMemberListResult;
import net.linlan.emis.console.pojo.result.account.UserGroupResult;
import net.linlan.emis.console.pojo.save.account.UserCreateParam;
import net.linlan.emis.console.pojo.save.account.UserGroupCreateParam;
import net.linlan.emis.console.pojo.update.account.UserGroupUpdateParam;
import net.linlan.emis.console.service.account.UserGroupService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.user.entity.UserGroup;
import static net.linlan.emis.user.constant.AccountConstants.ACCOUNT_GROUP_NAME;
import static net.linlan.emis.user.constant.AccountConstants.USER_GROUP_PATH;

/**
 * 用户组
 *
 * @author Linlan
 * Created on  2020/7/11 21:18
 */
@Validated
@AllArgsConstructor
@Tag(name = ACCOUNT_GROUP_NAME + "-" + "用户分组")
@RestController
@RequestMapping(value = USER_GROUP_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserGroupController {

    /**
     * 获取分组列表
     *
     * @param page {@link PageModel}
     * @return {@link UserListQuery}
     */
    @Operation(summary = "获取用户组列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<UserGroupListResult>> getUserGroupList(PageModel page,
                                                                      UserGroupListQuery query) {
        Page<UserGroupListResult> result = userGroupService.getUserGroupList(page, query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建分组
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建用户组")
    @Audit(type = EventType.CREATE_USER_GROUP)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createUserGroup(@RequestBody @Validated UserGroupCreateParam param) {
        boolean result = userGroupService.createUserGroup(param);
        return ResponseResult.ok(result);
    }

    /**
     * 更改用户分组
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改用户组")
    @Audit(type = EventType.UPDATE_USER_GROUP)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateUserGroup(@RequestBody @Validated UserGroupUpdateParam param) {
        boolean result = userGroupService.updateUserGroup(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除分组
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除用户组")
    @Audit(type = EventType.DELETE_USER_GROUP)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteUserGroup(@PathVariable(value = "id") String id) {
        boolean result = userGroupService.deleteUserGroup(id);
        return ResponseResult.ok(result);
    }

    /**
     * 根据ID查询用户分组
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户组信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<UserGroupResult> getUserGroup(@PathVariable(value = "id") String id) {
        UserGroup entity = userGroupService.getUserGroup(id);
        UserGroupResult result = userGroupConverter.entityConvertToUserGroupResult(entity);
        return ResponseResult.ok(result);
    }

    /**
     * 添加分组用户
     *
     * @param userIds {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "添加用户组成员")
    @Audit(type = EventType.ADD_USER_GROUP_MEMBER)
    @PostMapping(value = "/add_member/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> addMember(@PathVariable(value = "id") String id,
                                             @NotNull(message = "成员ID不能为空") @Parameter(description = "成员ID") String[] userIds) {
        boolean result = userGroupService.addMember(id, userIds);
        return ResponseResult.ok(result);
    }

    /**
     * 移除分组用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "移除用户组成员")
    @Audit(type = EventType.REMOVE_USER_GROUP_MEMBER)
    @DeleteMapping(value = "/remove_member/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> removeMember(@PathVariable(value = "id") String id,
                                                @NotEmpty(message = "用户ID不能为空") @Parameter(description = "用户ID集合") String[] userIds) {
        boolean result = userGroupService.batchRemoveMember(id, Lists.newArrayList(userIds));
        return ResponseResult.ok(result);
    }

    /**
     * 获取分组用户
     *
     * @param query {@link UserGroupMemberListQuery} 参数
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户组成员")
    @GetMapping(value = "/{id}/member_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<UserGroupMemberListResult>> getUserGroupMemberList(PageModel model,
                                                                                  UserGroupMemberListQuery query) {
        Page<UserGroupMemberListResult> result = userGroupService.getUserGroupMemberList(model,
            query);
        return ResponseResult.ok(result);
    }

    /**
     * 用户组数据映射
     */
    private final UserGroupConverter userGroupConverter;
    /**
     * 用户分组service
     */
    private final UserGroupService   userGroupService;
}
