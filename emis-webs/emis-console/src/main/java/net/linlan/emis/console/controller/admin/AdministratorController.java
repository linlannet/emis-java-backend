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
package net.linlan.emis.console.controller.admin;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.query.admin.AdministratorListQuery;
import net.linlan.emis.console.pojo.result.admin.AdministratorListResult;
import net.linlan.emis.console.pojo.result.admin.AdministratorResult;
import net.linlan.emis.console.pojo.save.admin.AdministratorCreateParam;
import net.linlan.emis.console.pojo.update.admin.AdministratorUpdateParam;
import net.linlan.emis.console.service.admin.AdministratorService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.user.constant.UserStatus;
import static net.linlan.emis.core.constant.AdministratorConstants.ADMIN_PATH;

/**
 * 管理员
 *
 * @author Linlan
 * Created on  2021/11/13 22:09
 */
@Validated
@Tag(name = "工作台管理员")
@RestController
@AllArgsConstructor
@RequestMapping(value = ADMIN_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorController {
    /**
     * 获取管理员列表
     *
     * @param model {@link PageModel}
     * @return {@link AdministratorListResult}
     */
    @Operation(summary = "获取管理员列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<AdministratorListResult>> getAdministratorList(PageModel model,
                                                                              AdministratorListQuery query) {
        Page<AdministratorListResult> result = administratorService.getAdministratorList(model,
            query);
        return ResponseResult.ok(result);
    }

    /**
     * 创建管理员
     *
     * @param param {@link AdministratorCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建管理员")
    @Audit(type = EventType.ADD_ADMINISTRATOR)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createAdministrator(@RequestBody @Validated AdministratorCreateParam param) {
        Boolean result = administratorService.createAdministrator(param);
        return ResponseResult.ok(result);
    }

    /**
     * 修改管理员
     *
     * @param param {@link AdministratorUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改管理员")
    @Audit(type = EventType.UPDATE_ADMINISTRATOR)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateAdministrator(@RequestBody @Validated AdministratorUpdateParam param) {
        Boolean result = administratorService.updateAdministrator(param);
        return ResponseResult.ok(result);
    }

    /**
     * 删除管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除管理员")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    @Audit(type = EventType.DELETE_ADMINISTRATOR)
    public ResponseResult<Boolean> deleteAdministrator(@PathVariable(value = "id") String id) {
        Boolean result = administratorService.deleteAdministrator(id);
        return ResponseResult.ok(result);
    }

    /**
     * 启用管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用管理员")
    @Audit(type = EventType.ENABLE_ADMINISTRATOR)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enableAdministrator(@PathVariable(value = "id") String id) {
        Boolean result = administratorService.updateAdministratorStatus(id, UserStatus.ENABLED);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用管理员")
    @Audit(type = EventType.DISABLE_ADMINISTRATOR)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableAdministrator(@PathVariable(value = "id") String id) {
        Boolean result = administratorService.updateAdministratorStatus(id, UserStatus.DISABLED);
        return ResponseResult.ok(result);
    }

    /**
     * 重置管理员密码
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "重置管理员密码")
    @Audit(type = EventType.RESET_ADMINISTRATOR_PASSWORD)
    @PutMapping(value = "/reset_password")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> resetAdministratorPassword(@NotEmpty(message = "ID不能为空") @Parameter(description = "ID") String id,
                                                              @NotEmpty(message = "密码不能为空") @Parameter(description = "密码") String password) {
        Boolean result = administratorService.resetAdministratorPassword(id, password);
        return ResponseResult.ok(result);
    }

    /**
     * 根据ID获取管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取管理员信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<AdministratorResult> getAdministrator(@PathVariable(value = "id") String id) {
        AdministratorResult result = administratorService.getAdministrator(id);
        //返回
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
    public ResponseResult<Boolean> administratorParamCheck(@Parameter(description = "验证类型") @NotNull(message = "验证类型不能为空") CheckValidityType type,
                                                           @Parameter(description = "值") @NotEmpty(message = "验证值不能为空") String value,
                                                           @Parameter(description = "ID") String id) {
        Boolean result = administratorService.administratorParamCheck(type, value, id);
        //返回
        return ResponseResult.ok(result);
    }

    /**
     * AdministratorService
     */
    private final AdministratorService administratorService;
}
