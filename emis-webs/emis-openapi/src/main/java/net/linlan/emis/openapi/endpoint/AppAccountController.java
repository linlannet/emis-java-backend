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

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.openapi.pojo.query.OapiV1AppAccountQuery;
import net.linlan.emis.openapi.pojo.result.AppAccountListResult;
import net.linlan.emis.openapi.pojo.save.AppAccountCreateParam;
import net.linlan.emis.openapi.service.AppAccountService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.openapi.constant.OpenApiConstants.APP_ACCOUNT_PATH;

/**
 * 应用账户
 *
 * @author xlsea
 * @since 2024-01-15
 */
@Validated
@Tag(name = "应用账户管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = APP_ACCOUNT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AppAccountController {

    /**
     * 获取应用账户列表
     *
     * @param page  {@link PageModel}
     * @param query {@link OapiV1AppAccountQuery}
     * @return {@link AppAccountListResult}
     */
    @Operation(summary = "获取应用账户列表")
    @GetMapping(value = "/list")
    public ResponseResult<Page<AppAccountListResult>> getAppAccountList(PageModel page,
                                                                        @Validated OapiV1AppAccountQuery query) {
        return ResponseResult.ok(appAccountService.getAppAccountList(page, query));
    }

    /**
     * 创建应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @PostMapping(value = "/create")
    @Audit(type = EventType.ADD_APP_ACCOUNT)
    @Operation(summary = "创建应用账户")
    public ResponseResult<Boolean> createAppAccount(@RequestBody @Validated AppAccountCreateParam param) {
        appAccountService.createAppAccount(param);
        return ResponseResult.ok(Boolean.TRUE);
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
    public ResponseResult<Boolean> deleteAppAccount(@PathVariable(value = "id") String id) {
        appAccountService.deleteAppAccount(id);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 应用账户服务类
     */
    private final AppAccountService appAccountService;
}
