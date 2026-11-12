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
import net.linlan.emis.console.pojo.query.app.AppResourceListQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionResourceGetResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionResourceListResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionRoleListResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionResourceCreateParam;
import net.linlan.emis.console.pojo.save.app.AppPermissionRoleCreateParam;
import net.linlan.emis.console.pojo.update.app.AppPermissionResourceUpdateParam;
import net.linlan.emis.console.pojo.update.app.PermissionRoleUpdateParam;
import net.linlan.emis.console.service.app.AppPermissionResourceService;
import net.linlan.emis.core.constant.CheckValidityType;
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
@Tag(name = APP_GROUP_NAME + "-" + "应用权限资源")
@RequestMapping(value = APP_PATH
                        + "/permission/resource", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class AppPermissionResourceController {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(AppPermissionResourceController.class);

    /**
     * 获取所有资源（分页）
     *
     * @param page {@link PageModel}
     * @return {@link AppPermissionRoleListResult}
     */
    @Operation(summary = "获取资源列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppPermissionResourceListResult>> getPermissionResourceList(PageModel page,
                                                                                           @Validated AppResourceListQuery query) {
        Page<AppPermissionResourceListResult> result = appPermissionResourceService
            .getPermissionResourceList(page, query);
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
    @Audit(type = EventType.SAVE_APP_PERMISSION_RESOURCE)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createResource(@Validated @RequestBody AppPermissionResourceCreateParam param) {
        boolean result = appPermissionResourceService.createPermissionResource(param);
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
    @Audit(type = EventType.UPDATE_APP_PERMISSION_RESOURCE)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateResource(@RequestBody @Validated AppPermissionResourceUpdateParam param) {
        boolean result = appPermissionResourceService.updatePermissionResource(param);
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
    @Audit(type = EventType.DELETE_APP_PERMISSION_RESOURCE)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deletePermissionResource(@PathVariable(value = "id") String id) {
        boolean result = appPermissionResourceService.deletePermissionResource(id);
        return ResponseResult.ok(result);
    }

    /**
     * 获取资源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取资源信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<AppPermissionResourceGetResult> getPermissionResource(@PathVariable(value = "id") String id) {
        //返回
        AppPermissionResourceGetResult result = appPermissionResourceService
            .getPermissionResource(id);
        return ResponseResult.ok(result);
    }

    /**
     * 启用资源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用资源")
    @Audit(type = EventType.ENABLE_APP_PERMISSION_RESOURCE)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enableOrganization(@PathVariable(value = "id") String id) {
        boolean result = appPermissionResourceService.updateStatus(id, Boolean.TRUE);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用资源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用资源")
    @Audit(type = EventType.DISABLE_APP_PERMISSION_RESOURCE)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableOrganization(@PathVariable(value = "id") String id) {
        boolean result = appPermissionResourceService.updateStatus(id, Boolean.FALSE);
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
    public ResponseResult<Boolean> resourceParamCheck(@Parameter(description = "验证类型") @NotNull(message = "验证类型不能为空") CheckValidityType type,
                                                      @Parameter(description = "值") @NotEmpty(message = "验证值不能为空") String value,
                                                      @Parameter(description = "应用ID") @NotNull(message = "应用ID不能为空") String appId,
                                                      @Parameter(description = "ID") String id) {
        Boolean result = appPermissionResourceService.permissionResourceParamCheck(type, value,
            appId, id);
        //返回
        return ResponseResult.ok(result);
    }

    /**
     * 资源服务类
     */
    private final AppPermissionResourceService appPermissionResourceService;
}
