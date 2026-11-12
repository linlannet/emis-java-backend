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
import net.linlan.emis.console.pojo.query.app.AppCertQuery;
import net.linlan.emis.console.pojo.result.app.AppCertListResult;
import net.linlan.emis.console.service.app.AppCertService;
import static net.linlan.emis.app.constant.AppConstants.APP_GROUP_NAME;
import static net.linlan.emis.app.constant.AppConstants.APP_PATH;

/**
 * 应用证书
 *
 * @author Linlan
 * Created on  2022/5/31 21:38
 */
@Validated
@Tag(name = APP_GROUP_NAME + "-" + "应用证书")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/cert", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppCertController {

    /**
     * 获取应用证书列表
     *
     * @param query {@link AppCertQuery}
     * @return {@link AppCertListResult}
     */
    @Operation(summary = "获取应用证书列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<AppCertListResult>> getAppCertListResult(@Validated AppCertQuery query) {
        List<AppCertListResult> result = appCertService.getAppCertListResult(query);
        return ResponseResult.ok(result);
    }

    private final AppCertService appCertService;
}
