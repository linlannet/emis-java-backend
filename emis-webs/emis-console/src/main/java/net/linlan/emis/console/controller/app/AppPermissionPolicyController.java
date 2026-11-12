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
package net.linlan.emis.console.controller.app;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.console.pojo.query.app.AppPolicyQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionPolicyGetResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionPolicyListResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleListResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionPolicyCreateParam;
import net.linlan.emis.console.pojo.save.app.AppPermissionRoleCreateParam;
import net.linlan.emis.console.pojo.update.app.AppPermissionPolicyUpdateParam;
import net.linlan.emis.console.pojo.update.app.PermissionRoleUpdateParam;
import net.linlan.emis.console.service.app.AppPermissionPolicyService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 应用权限
 *
 * @author Linlan
 * Created on  2022/7/12 22:30
 */
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用权限策略")
@RequestMapping(value = APP_PATH
                        + "/permission/policy", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AppPermissionPolicyController {

    /**
     * 获取所有策略（分页）
     *
     * @param page {@link PageModel}
     * @return {@link AppPermissionRoleListResult}
     */
    @Operation(summary = "获取策略列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppPermissionPolicyListResult>> getPermissionPolicyList(PageModel page,
                                                                                       @Validated AppPolicyQuery query) {
        Page<AppPermissionPolicyListResult> result = permissionPolicyService
            .getPermissionPolicyList(page, query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建资源
     *
     * @param param {@link AppPermissionRoleCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建资源")
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createPolicy(@Validated @RequestBody AppPermissionPolicyCreateParam param) {
        boolean result = permissionPolicyService.createPermissionPolicy(param);
        return ResponseResult.ok(result);
    }

    /**
     * 修改资源
     *
     * @param param {@link PermissionRoleUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改资源")
    @PutMapping(value = "/update/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updatePolicy(@Validated AppPermissionPolicyUpdateParam param) {
        boolean result = permissionPolicyService.updatePermissionPolicy(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除资源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除资源")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deletePermissionPolicy(@PathVariable(value = "id") String id) {
        boolean result = permissionPolicyService.deletePermissionPolicy(id);
        return ResponseResult.ok(result);
    }

    /**
     * 获取资源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "获取资源信息")
    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<AppPermissionPolicyGetResult> getPermissionPolicy(@PathVariable(value = "id") String id) {
        //返回
        AppPermissionPolicyGetResult result = permissionPolicyService.getPermissionPolicy(id);
        return ResponseResult.ok(result);
    }

    private final AppPermissionPolicyService permissionPolicyService;
}
