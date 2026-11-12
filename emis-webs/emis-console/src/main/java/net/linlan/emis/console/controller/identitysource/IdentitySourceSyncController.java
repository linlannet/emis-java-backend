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
package net.linlan.emis.console.controller.identitysource;

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
import net.linlan.emis.console.pojo.query.identity.IdentitySourceSyncHistoryListQuery;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceSyncRecordListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import net.linlan.emis.console.service.identitysource.IdentitySourceSyncService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.idsource.constant.IdentityConstants.IDENTITY_GROUP_NAME;
import static net.linlan.emis.idsource.constant.IdentityConstants.IDENTITY_SOURCE_PATH;

/**
 * 身份源同步记录
 *
 * @author Linlan
 * Created on  2020/7/11 21:18
 */
@Validated
@Tag(name = IDENTITY_GROUP_NAME + "-" + "身份源同步")
@RestController
@AllArgsConstructor
@RequestMapping(value = IDENTITY_SOURCE_PATH + "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentitySourceSyncController {

    /**
     * 执行同步
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "执行身份源同步")
    @Audit(type = EventType.IDENTITY_RESOURCE_SYNC)
    @PostMapping(value = "/execute/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Void> executeIdentitySourceSync(@PathVariable String id) {
        identitySourceSyncService.executeIdentitySourceSync(id);
        return ResponseResult.ok();
    }

    /**
     * 同步历史列表
     *
     * @return {@link IdentitySourceSyncHistoryListResult}
     */
    @Operation(summary = "同步历史列表")
    @GetMapping(value = "/history_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<IdentitySourceSyncHistoryListResult>> getIdentitySourceSyncHistoryList(PageModel pageModel,
                                                                                                      IdentitySourceSyncHistoryListQuery query) {
        Page<IdentitySourceSyncHistoryListResult> result = identitySourceSyncService
            .getIdentitySourceSyncHistoryList(query, pageModel);
        return ResponseResult.ok(result);
    }

    /**
     * 同步记录列表
     *
     * @return {@link IdentitySourceSyncRecordListResult}
     */
    @Operation(summary = "同步记录列表")
    @GetMapping(value = "/record_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<IdentitySourceSyncRecordListResult>> getIdentitySourceSyncRecordList(PageModel pageModel,
                                                                                                    @Validated IdentitySourceSyncRecordListQuery query) {
        Page<IdentitySourceSyncRecordListResult> result = identitySourceSyncService
            .getIdentitySourceSyncRecordList(query, pageModel);
        return ResponseResult.ok(result);
    }

    /**
     * 身份源同步Service
     */
    private final IdentitySourceSyncService identitySourceSyncService;
}
