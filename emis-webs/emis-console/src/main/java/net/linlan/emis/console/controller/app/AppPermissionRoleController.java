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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.query.app.AppPermissionRoleListQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleListResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionRoleCreateParam;
import net.linlan.emis.console.pojo.update.app.PermissionRoleUpdateParam;
import net.linlan.emis.console.service.app.AppPermissionRoleService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 应用角色
 *
 * @author Linlan
 * Created on  2020/8/26 20:28
 */
@RequiredArgsConstructor
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用权限角色")
@RequestMapping(value = APP_PATH + "/permission/role", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AppPermissionRoleController {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(AppPermissionRoleController.class);

    /**
     * 获取所有角色（分页）
     *
     * @param page {@link PageModel}
     * @return {@link AppPermissionRoleListResult}
     */
    @Operation(summary = "获取角色列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppPermissionRoleListResult>> getPermissionRoleList(PageModel page,
                                                                                   @Validated AppPermissionRoleListQuery query) {
        Page<AppPermissionRoleListResult> result = appPermissionRoleService
            .getPermissionRoleList(page, query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建角色
     *
     * @param param {@link AppPermissionRoleCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建角色")
    @Audit(type = EventType.SAVE_APP_PERMISSION_ROLE)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createPermissionRole(@Validated @RequestBody AppPermissionRoleCreateParam param) {
        boolean result = appPermissionRoleService.createPermissionRole(param);
        return ResponseResult.ok(result);
    }

    /**
     * 修改角色
     *
     * @param param {@link PermissionRoleUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改角色")
    @Audit(type = EventType.UPDATE_APP_PERMISSION_ROLE)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updatePermissionRole(@RequestBody @Validated PermissionRoleUpdateParam param) {
        boolean result = appPermissionRoleService.updatePermissionRole(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除角色
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除角色")
    @Audit(type = EventType.DELETE_APP_PERMISSION_ROLE)
    @DeleteMapping(value = "/delete/{ids}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deletePermissionRole(@PathVariable(value = "ids") String ids) {
        boolean result = appPermissionRoleService.deletePermissionRole(ids);
        return ResponseResult.ok(result);
    }

    /**
     * 获取角色
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取角色信息")
    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<AppPermissionRoleResult> getPermissionRole(@PathVariable(value = "id") String id) {
        AppPermissionRoleResult result = appPermissionRoleService.getPermissionRole(id);
        return ResponseResult.ok(result);
    }

    /**
     * 启用角色
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用角色")
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enablePermissionRole(@PathVariable(value = "id") String id) {
        Boolean result = appPermissionRoleService.updatePermissionRoleStatus(id, Boolean.TRUE);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用角色
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用角色")
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disablePermissionRole(@PathVariable(value = "id") String id) {
        Boolean result = appPermissionRoleService.updatePermissionRoleStatus(id, Boolean.FALSE);
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
    public ResponseResult<Boolean> permissionRoleParamCheck(@Parameter(description = "验证类型") @NotNull(message = "验证类型不能为空") CheckValidityType type,
                                                            @Parameter(description = "值") @NotEmpty(message = "验证值不能为空") String value,
                                                            @Parameter(description = "应用ID") @NotNull(message = "应用ID不能为空") String appId,
                                                            @Parameter(description = "ID") String id) {
        Boolean result = appPermissionRoleService.permissionRoleParamCheck(type, value, appId, id);
        //返回
        return ResponseResult.ok(result);
    }

    /**
     * 角色服务类
     */
    private final AppPermissionRoleService appPermissionRoleService;
}
