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
import { request } from '@umijs/max';

/**
 * 获取IP地理位置配置
 */
export async function getGeoIpConfig(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/V1/setting/geo_ip/config`);
}

/**
 * 保存IP地理位置配置
 *
 * @param params
 */
export async function saveGeoIpConfig(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/setting/geo_ip/save`, {
    data: { ...params },
    method: 'POST',
  });
}

/**
 * 关闭地理位置服务启用/禁用
 *
 */
export async function disableGeoIp(): Promise<API.ApiResult<Record<string, any>>> {
  return request(`/api/V1/setting/geo_ip/disable`, {
    method: 'PUT',
  });
}
