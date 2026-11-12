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
package net.linlan.emis.console.controller.analysis;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.repository.result.AuthnQuantityResult;
import net.linlan.emis.audit.repository.result.AuthnZoneResult;
import net.linlan.emis.console.pojo.query.analysis.AnalysisQuery;
import net.linlan.emis.console.pojo.result.analysis.AppVisitRankResult;
import net.linlan.emis.console.pojo.result.analysis.AuthnHotProviderResult;
import net.linlan.emis.console.pojo.result.analysis.OverviewResult;
import net.linlan.emis.console.service.analysis.AnalysisService;
import static net.linlan.emis.core.constant.AnalysisConstants.ANALYSIS_PATH;

/**
 * 统计分析
 *
 * @author Linlan
 * Created on  2022/10/13 22:00
 */
@Validated
@Tag(name = "统计分析")
@RestController
@RequestMapping(ANALYSIS_PATH)
public class AnalysisController {

    /**
     * 概述
     *
     * @return {@link OverviewResult}
     */
    @GetMapping("/overview")
    @Operation(summary = "概述")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<OverviewResult> overview() {
        return ResponseResult.ok(analysisService.overview());
    }

    /**
     * 认证量
     *
     * @param query {@link AnalysisQuery}
     * @return {@link AuthnQuantityResult}
     */
    @GetMapping("/authn/quantity")
    @Operation(summary = "认证量")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<AuthnQuantityResult>> authnQuantity(@Validated AnalysisQuery query) {
        return ResponseResult.ok(analysisService.authnQuantity(query));
    }

    /**
     * 热门认证提供商
     *
     * @return {@link List}
     */
    @GetMapping("/authn/hot_provider")
    @Operation(summary = "热门认证提供商")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<AuthnHotProviderResult>> authnHotProvider(@Validated AnalysisQuery query) {
        return ResponseResult.ok(analysisService.authnHotProvider(query));
    }

    /**
     * 登录区域
     */
    @GetMapping("/authn/zone")
    @Operation(summary = "登录区域")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<AuthnZoneResult>> authnZone(@Validated AnalysisQuery query) {
        return ResponseResult.ok(analysisService.authnZone(query));
    }

    /**
     * 访问应用排名
     *
     * @param query {@link AnalysisQuery}
     * @return {@link AuthnQuantityResult}
     */
    @GetMapping("/app/visit_rank")
    @Operation(summary = "访问应用排名")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<AppVisitRankResult>> appVisitRank(@Validated AnalysisQuery query) {
        return ResponseResult.ok(analysisService.appVisitRank(query));
    }

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

}
