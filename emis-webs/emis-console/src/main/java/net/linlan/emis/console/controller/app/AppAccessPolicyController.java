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
import net.linlan.emis.console.pojo.query.app.AppAccessPolicyQuery;
import net.linlan.emis.console.pojo.result.app.AppAccessPolicyResult;
import net.linlan.emis.console.pojo.save.app.AppAccessPolicyCreateParam;
import net.linlan.emis.console.pojo.save.app.AppAccountCreateParam;
import net.linlan.emis.console.service.app.AppAccessPolicyService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 应用访问授权策略
 *
 * @author Linlan
 * Created on  2022/6/4 21:58
 */
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用访问授权策略")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/access_policy", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppAccessPolicyController {

    /**
     * 获取应用访问授权策略列表
     *
     * @param page  {@link PageModel}
     * @param query {@link AppAccessPolicyQuery}
     * @return {@link AppAccessPolicyResult}
     */
    @Operation(summary = "获取应用访问授权策略列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AppAccessPolicyResult>> getAppAccessPolicyList(PageModel page,
                                                                              @Validated AppAccessPolicyQuery query) {
        Page<AppAccessPolicyResult> result = appAccessPolicyService.getAppAccessPolicyList(page,
            query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建应用访问授权策略
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用访问授权")
    @Audit(type = EventType.APP_AUTHORIZATION)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createAppAccessPolicy(@RequestBody @Validated AppAccessPolicyCreateParam param) {
        boolean result = appAccessPolicyService.createAppAccessPolicy(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除应用访问授权策略
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "取消应用访问授权")
    @Audit(type = EventType.APP_CANCEL_ACCESS_POLICY)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteAppAccessPolicy(@PathVariable(value = "id") String id) {
        boolean result = appAccessPolicyService.deleteAppAccessPolicy(id);
        return ResponseResult.ok(result);
    }

    /**
     * 启用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用应用访问授权")
    @Audit(type = EventType.ENABLE_APP_ACCESS_POLICY)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enableAppAccessPolicy(@PathVariable(value = "id") String id) {
        Boolean result = appAccessPolicyService.enableAppAccessPolicy(id);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用应用访问授权")
    @Audit(type = EventType.DISABLE_APP_ACCESS_POLICY)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableAppAccessPolicy(@PathVariable(value = "id") String id) {
        Boolean result = appAccessPolicyService.disableAppAccessPolicy(id);
        return ResponseResult.ok(result);
    }

    /**
     * AppPolicyService
     */
    private final AppAccessPolicyService appAccessPolicyService;
}
