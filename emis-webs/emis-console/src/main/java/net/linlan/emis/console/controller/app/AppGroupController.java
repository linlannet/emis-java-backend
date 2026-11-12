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
import net.linlan.emis.console.pojo.query.app.AppGroupAssociationListQuery;
import net.linlan.emis.console.pojo.query.app.AppGroupListQuery;
import net.linlan.emis.console.pojo.result.app.AppGroupGetResult;
import net.linlan.emis.console.pojo.result.app.AppGroupListResult;
import net.linlan.emis.console.pojo.result.app.AppListResult;
import net.linlan.emis.console.pojo.save.app.AppGroupCreateParam;
import net.linlan.emis.console.pojo.update.app.AppGroupUpdateParam;
import net.linlan.emis.console.service.app.AppGroupService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 分组管理
 *
 * @author Linlan
 * Created on  2023/8/31 15:35
 */
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用分组管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppGroupController {

    /**
     * 获取应用分组列表
     *
     * @param page {@link PageModel}
     * @return {@link AppGroupListQuery}
     */
    @Operation(summary = "获取分组列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppGroupListResult>> getAppGroupList(PageModel page,
                                                                    AppGroupListQuery query) {
        Page<AppGroupListResult> result = appGroupService.getAppGroupList(page, query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建应用分组
     *
     * @param param {@link AppGroupCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用分组")
    @Audit(type = EventType.ADD_APP_GROUP)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createAppGroup(@RequestBody @Validated AppGroupCreateParam param) {
        boolean result = appGroupService.createAppGroup(param);
        return ResponseResult.ok(result);
    }

    /**
     * 修改应用分组
     *
     * @param param {@link AppGroupUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改应用分组")
    @Audit(type = EventType.UPDATE_APP_GROUP)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateAppGroup(@RequestBody @Validated AppGroupUpdateParam param) {
        boolean result = appGroupService.updateAppGroup(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除应用分组
     *
     * @param id {@link Long}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除应用分组")
    @Audit(type = EventType.DELETE_APP_GROUP)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteAppGroup(@PathVariable(value = "id") String id) {
        boolean result = appGroupService.deleteAppGroup(id);
        return ResponseResult.ok(result);
    }

    /**
     * 获取应用分组信息
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取应用分组信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<AppGroupGetResult> getAppGroup(@PathVariable(value = "id") String id) {
        AppGroupGetResult result = appGroupService.getAppGroup(id);
        return ResponseResult.ok(result);
    }

    /**
     * 移除应用组关联
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "移除应用组关联")
    @Audit(type = EventType.REMOVE_APP_GROUP_ASSOCIATION)
    @DeleteMapping(value = "/remove_association/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> removeAssociation(@PathVariable(value = "id") String id,
                                                     @NotEmpty(message = "应用ID不能为空") @Parameter(description = "应用ID集合") String[] appIds) {
        boolean result = appGroupService.batchRemoveAssociation(id, Lists.newArrayList(appIds));
        return ResponseResult.ok(result);
    }

    /**
     * 获取应用组内应用
     *
     * @param query {@link UserGroupMemberListQuery} 参数
     * @return {@link Boolean}
     */
    @Operation(summary = "获取应用组内应用")
    @GetMapping(value = "/{id}/app_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppListResult>> getAppGroupAssociationList(PageModel model,
                                                                          AppGroupAssociationListQuery query) {
        Page<AppListResult> result = appGroupService.getAppGroupAssociationList(model, query);
        return ResponseResult.ok(result);
    }

    /**
     * AppGroupService
     */
    private final AppGroupService appGroupService;
}
