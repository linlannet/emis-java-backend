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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import net.linlan.emis.console.service.identitysource.IdentitySourceEventRecordService;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.idsource.constant.IdentityConstants.IDENTITY_GROUP_NAME;
import static net.linlan.emis.idsource.constant.IdentityConstants.IDENTITY_SOURCE_PATH;

/**
 * 身份源事件记录
 *
 * @author Linlan
 * Created on  2020/7/11 21:18
 */
@Validated
@Tag(name = IDENTITY_GROUP_NAME + "-" + "身份源事件")
@RestController
@AllArgsConstructor
@RequestMapping(value = IDENTITY_SOURCE_PATH
                        + "/event", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentitySourceEventController {

    /**
     * 身份源事件列表
     *
     * @return {@link IdentitySourceEventRecordListResult}
     */
    @Operation(summary = "身份源事件列表")
    @GetMapping(value = "/record_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<IdentitySourceEventRecordListResult>> getIdentitySourceSynchronizeRecordList(PageModel pageModel,
                                                                                                            IdentitySourceEventRecordListQuery query) {
        Page<IdentitySourceEventRecordListResult> result = identitySourceEventRecordService
            .getIdentitySourceEventRecordList(query, pageModel);
        return ResponseResult.ok(result);
    }

    /**
     * 身份源同步Service
     */
    private final IdentitySourceEventRecordService identitySourceEventRecordService;
}
