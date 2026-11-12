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

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.query.app.AppAccountQuery;
import net.linlan.emis.console.pojo.result.app.AppAccountListResult;
import net.linlan.emis.console.pojo.save.app.AppAccountCreateParam;
import net.linlan.emis.console.service.app.AppAccountService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 应用账户资源
 *
 * @author Linlan
 * Created on  2022/6/4 21:06
 */
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用账户")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppAccountController {

    /**
     * 获取应用账户列表
     *
     * @param page  {@link PageModel}
     * @param query {@link AppAccountQuery}
     * @return {@link AppAccountListResult}
     */
    @Operation(summary = "获取应用账户列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppAccountListResult>> getAppAccountList(PageModel page,
                                                                        @Validated AppAccountQuery query) {
        Page<AppAccountListResult> result = appAccountService.getAppAccountList(page, query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用账户")
    @Audit(type = EventType.ADD_APP_ACCOUNT)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createAppAccount(@RequestBody @Validated AppAccountCreateParam param) {
        boolean result = appAccountService.createAppAccount(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除应用账户")
    @Audit(type = EventType.DELETE_APP_ACCOUNT)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteAppAccount(@PathVariable(value = "id") String id) {
        boolean result = appAccountService.deleteAppAccount(id);
        return ResponseResult.ok(result);
    }

    /**
     * 设置应用账户是否默认
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "设置应用账户为默认")
    @Audit(type = EventType.UPDATE_DEFAULT_APP_ACCOUNT)
    @PutMapping(value = "/activate_default/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateAppAccountSetDefault(@PathVariable(value = "id") String id) {
        boolean result = appAccountService.updateAppAccountDefault(id, true);
        return ResponseResult.ok(result);
    }

    /**
     * 设置应用账户是否默认
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "取消应用账户为默认")
    @Audit(type = EventType.UPDATE_DEFAULT_APP_ACCOUNT)
    @PutMapping(value = "/deactivate_default/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateAppAccountUnsetDefault(@PathVariable(value = "id") String id) {
        boolean result = appAccountService.updateAppAccountDefault(id, false);
        return ResponseResult.ok(result);
    }

    /**
     * AppAccountService
     */
    private final AppAccountService appAccountService;

}
