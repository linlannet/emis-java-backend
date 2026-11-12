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
package net.linlan.emis.console.service.analysis;

import java.util.List;

import net.linlan.emis.audit.repository.result.AuthnQuantityResult;
import net.linlan.emis.audit.repository.result.AuthnZoneResult;
import net.linlan.emis.console.pojo.query.analysis.AnalysisQuery;
import net.linlan.emis.console.pojo.result.analysis.AppVisitRankResult;
import net.linlan.emis.console.pojo.result.analysis.AuthnHotProviderResult;
import net.linlan.emis.console.pojo.result.analysis.OverviewResult;

/**
 * 统计 service
 *
 * @author Linlan
 * Created on  2022/11/22 22:25
 */
public interface AnalysisService {

    /**
     * 概述
     *
     * @return {@link OverviewResult}
     */
    OverviewResult overview();

    /**
     * 认证量统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List< AuthnQuantityResult >}
     */
    List<AuthnQuantityResult> authnQuantity(AnalysisQuery params);

    /**
     * 应用热点统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List< AppVisitRankResult >}
     */
    List<AppVisitRankResult> appVisitRank(AnalysisQuery params);

    /**
     * 热门认证方式统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List< AuthnHotProviderResult >}
     */
    List<AuthnHotProviderResult> authnHotProvider(AnalysisQuery params);

    /**
     * 登录区域统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List<  AuthnZoneResult  >}
     */
    List<AuthnZoneResult> authnZone(AnalysisQuery params);
}
