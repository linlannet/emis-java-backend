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
package net.linlan.emis.console.service.analysis.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.service.AppInfoService;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.audit.repository.AuditStatisticsRepository;
import net.linlan.emis.audit.repository.result.AuditStatisticsResult;
import net.linlan.emis.audit.repository.result.AuthnQuantityResult;
import net.linlan.emis.audit.repository.result.AuthnZoneResult;
import net.linlan.emis.console.pojo.query.analysis.AnalysisQuery;
import net.linlan.emis.console.pojo.result.analysis.AppVisitRankResult;
import net.linlan.emis.console.pojo.result.analysis.AuthnHotProviderResult;
import net.linlan.emis.console.pojo.result.analysis.OverviewResult;
import net.linlan.emis.console.service.analysis.AnalysisService;
import net.linlan.emis.provider.constant.IdentityProviderType;
import net.linlan.emis.provider.service.IdentityProviderService;
import net.linlan.emis.user.service.UserInfoService;

/**
 * @author Linlan
 * Created on  2022/11/22 22:25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {
    /**
     * 概述
     *
     * @return {@link OverviewResult}
     */
    @Override
    public OverviewResult overview() {
        OverviewResult result = new OverviewResult();
        result.setAppCount(String.valueOf(appInfoService.getCount(new HashMap<>())));
        result.setUserCount(String.valueOf(userInfoService.getCount(new HashMap<>())));
        result.setIdpCount(String.valueOf(identityProviderService.getCount(new HashMap<>())));
        // 查询今日认证量条件
        result.setTodayAuthnCount(String.valueOf(auditStatisticsRepository
            .countByTypeAndTime(EventType.LOGIN_PORTAL, LocalDateTime.MIN, LocalDateTime.MAX)));
        return result;
    }

    /**
     * 认证量统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link List< AuthnQuantityResult >}
     */
    @Override
    public List<AuthnQuantityResult> authnQuantity(AnalysisQuery params) {
        LocalDateTime min = params.getStartTime();
        LocalDateTime max = params.getEndTime();
        AnalysisQuery.Interval timeInterval = params.getTimeInterval();
        return auditStatisticsRepository.authnQuantity(
            Lists.newArrayList(EventType.LOGIN_PORTAL, EventType.APP_SSO), min, max,
            timeInterval.getFormat());
    }

    /**
     * 应用热点统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link AppVisitRankResult}
     */
    @Override
    public List<AppVisitRankResult> appVisitRank(AnalysisQuery params) {
        List<AppVisitRankResult> applicationVisitList = new ArrayList<>();
        List<AuditStatisticsResult> auditRankResults = auditStatisticsRepository
            .appVisitRank(EventType.APP_SSO, params.getStartTime(), params.getEndTime());
        for (AuditStatisticsResult auditRankResult : auditRankResults) {
            // 单点登录
            String name = getAppName(auditRankResult.getKey());
            applicationVisitList.add(new AppVisitRankResult(name, auditRankResult.getCount()));
        }
        return applicationVisitList;
    }

    /**
     * 热门认证方式
     * @param params {@link AnalysisQuery}
     * @return {@link List<AuthnQuantityResult>}
     */
    @Override
    public List<AuthnHotProviderResult> authnHotProvider(AnalysisQuery params) {
        List<AuthnHotProviderResult> authTypeList = new ArrayList<>();
        List<AuditStatisticsResult> auditRankResults = auditStatisticsRepository.authnHotProvider(
            Lists.newArrayList(EventType.LOGIN_PORTAL, EventType.APP_SSO), params.getStartTime(),
            params.getEndTime());
        for (AuditStatisticsResult auditRankResult : auditRankResults) {
            // 授权类型
            if (Objects.nonNull(auditRankResult.getKey())) {
                String name = IdentityProviderType.getIdentityProviderType(auditRankResult.getKey())
                    .name();
                authTypeList.add(new AuthnHotProviderResult(name, auditRankResult.getCount()));
            }
        }
        return authTypeList;
    }

    /**
     * 登录区域统计
     *
     * @param params {@link AnalysisQuery}
     * @return {@link AuthnZoneResult}
     */
    @Override
    public List<AuthnZoneResult> authnZone(AnalysisQuery params) {
        return auditStatisticsRepository.authnZone(
            Lists.newArrayList(EventType.LOGIN_PORTAL, EventType.APP_SSO), params.getStartTime(),
            params.getEndTime());
    }

    /**
     * 获取应用名称
     *
     * @param targetId {@link String}
     * @return {@link String}
     */
    private String getAppName(String targetId) {
        if (!StringUtils.hasText(targetId)) {
            return null;
        }
        AppInfo app = appInfoService.findById(targetId);
        return app == null ? null : app.getName();
    }

    private final AuditStatisticsRepository auditStatisticsRepository;

    private final AppInfoService            appInfoService;

    private final IdentityProviderService   identityProviderService;

    private final UserInfoService           userInfoService;
}
