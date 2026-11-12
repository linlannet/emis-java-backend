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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.console.pojo.query.app.AppPermissionActionListQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionActionListResult;
import net.linlan.emis.console.service.app.AppPermissionActionService;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 应用权限-权限
 *
 * @author Linlan
 * Created on  2020/8/26 20:28
 */
@RequiredArgsConstructor
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用权限操作")
@RequestMapping(value = APP_PATH
                        + "/permission/action", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AppPermissionActionController {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(AppPermissionActionController.class);

    /**
     * 获取所有权限
     *
     * @return {@link AppPermissionActionListResult}
     */
    @Operation(summary = "获取权限项列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<AppPermissionActionListResult>> getPermissionActionList(@Validated AppPermissionActionListQuery query) {
        List<AppPermissionActionListResult> result = appPermissionActionService
            .getPermissionActionList(query);
        return ResponseResult.ok(result);

    }

    private final AppPermissionActionService appPermissionActionService;
}
