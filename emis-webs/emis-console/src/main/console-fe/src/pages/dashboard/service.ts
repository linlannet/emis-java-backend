/**
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
import { request } from '@@/exports';

/**
 * 获取概述
 */
export async function getAnalysisOverview(): Promise<API.ApiResult<DashboardAPI.OverviewResult>> {
  return request(`/api/V1/analysis/overview`);
}

/**
 * 获取认证数量
 */
export async function getAuthnQuantity(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AuthnQuantityResult>> {
  return request(`/api/V1/analysis/authn/quantity`, {
    params: { startTime, endTime },
  });
}

/**
 * 获取热点提供商
 */
export async function getAuthnHotProvider(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AuthnHotProviderResult>> {
  return request(`/api/V1/analysis/authn/hot_provider`, {
    params: { startTime, endTime },
  });
}

/**
 * 获取登录区域
 */
export async function getAuthnZone(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AuthnQuantityResult>> {
  return request(`/api/V1/analysis/authn/zone`, {
    params: { startTime, endTime },
  });
}

/**
 * 获取应用访问排名
 */
export async function getAppVisitRank(
  startTime: string,
  endTime: string,
): Promise<API.ApiResult<DashboardAPI.AppVisitsRank>> {
  return request(`/api/V1/analysis/app/visit_rank`, {
    params: { startTime, endTime },
  });
}
