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

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.query.app.AppQuery;
import net.linlan.emis.console.pojo.result.app.AppCreateResult;
import net.linlan.emis.console.pojo.result.app.AppGetResult;
import net.linlan.emis.console.pojo.result.app.AppListResult;
import net.linlan.emis.console.pojo.save.app.AppCreateParam;
import net.linlan.emis.console.pojo.update.app.AppSaveConfigParam;
import net.linlan.emis.console.pojo.update.app.AppUpdateParam;
import net.linlan.emis.console.service.app.AppService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 应用管理
 *
 * @author Linlan
 * Created on  2020/7/24 22:35
 */
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AppController {

    /**
     * 获取应用列表
     *
     * @param page {@link PageModel}
     * @return {@link AppQuery}
     */
    @Operation(summary = "获取应用列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppListResult>> getAppList(PageModel page, AppQuery query) {
        Page<AppListResult> result = appService.getAppList(page, query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建应用
     *
     * @param param {@link AppCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用")
    @Audit(type = EventType.ADD_APP)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<AppCreateResult> createApp(@RequestBody @Validated AppCreateParam param) {
        AppCreateResult result = appService.createApp(param);
        return ResponseResult.ok(result);
    }

    /**
     * 修改应用
     *
     * @param param {@link AppUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改应用")
    @Audit(type = EventType.UPDATE_APP)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateApp(@RequestBody @Validated AppUpdateParam param) {
        boolean result = appService.updateApp(param);
        return ResponseResult.ok(result);
    }

    /**
     * 更新应用配置
     *
     * @param param {@link AppSaveConfigParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存应用配置")
    @Audit(type = EventType.UPDATE_APP)
    @PutMapping(value = "/save/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveAppConfig(@RequestBody @Valid AppSaveConfigParam param) {
        boolean result = appService.saveAppConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 获取应用配置
     *
     * @param appId {@link String}
     * @return {@link Object}
     */
    @Operation(summary = "获取应用配置")
    @GetMapping(value = "/get/config/{appId}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Object> getAppConfig(@PathVariable String appId) {
        Object result = appService.getAppConfig(appId);
        return ResponseResult.ok(result);
    }

    /**
     * 删除应用
     *
     * @param id {@link Long}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除应用")
    @Audit(type = EventType.DELETE_APP)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteApp(@PathVariable(value = "id") String id) {
        boolean result = appService.deleteApp(id);
        return ResponseResult.ok(result);
    }

    /**
     * 获取应用信息
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取应用信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<AppGetResult> getApp(@PathVariable(value = "id") String id) {
        AppGetResult result = appService.getApp(id);
        return ResponseResult.ok(result);
    }

    /**
     * 启用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用应用")
    @Audit(type = EventType.ENABLE_APP)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enableIdentitySource(@PathVariable(value = "id") String id) {
        boolean result = appService.enableApp(id);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用应用")
    @Audit(type = EventType.DISABLE_APP)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableIdentitySource(@PathVariable(value = "id") String id) {
        boolean result = appService.disableApp(id);
        return ResponseResult.ok(result);
    }

    /**
     * ApplicationService
     */
    private final AppService appService;
}
