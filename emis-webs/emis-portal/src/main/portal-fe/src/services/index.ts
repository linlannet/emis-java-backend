/**
 * emis-portal - portal门户平台
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
import { request } from '@@/plugin-request/request';

/**
 * 获取登录公钥
 */
export async function getLoginEncryptSecret(): Promise<API.ApiResult<API.EncryptSecret>> {
  return request(`/api/V1/public_secret?type=login`);
}

/**
 * 获取加密公钥
 */
export async function getEncryptSecret(): Promise<API.ApiResult<API.EncryptSecret>> {
  return request(`/api/V1/public_secret?type=encrypt`);
}

/**
 * 退出登录
 */
export async function outLogin() {
  return request('/api/V1/logout', {
    method: 'post',
  });
}

/**
 * 获取当前用户
 */
export async function getCurrentUser(): Promise<API.ApiResult<API.CurrentUser>> {
  return request<API.ApiResult<API.CurrentUser>>('/api/V1/session/current_user', {
    responseType: 'json',
  });
}

/**
 * 获取当前session状态
 */
export async function getCurrentStatus(): Promise<API.ApiResult<API.CurrentStatus>> {
  return request<API.ApiResult<API.CurrentStatus>>('/api/V1/session/current_status', {
    skipErrorHandler: true,
  });
}
