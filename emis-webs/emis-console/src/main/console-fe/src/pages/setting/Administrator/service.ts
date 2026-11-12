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
import { RequestData } from '@ant-design/pro-components';
import { request } from '@@/exports';
import { filterParamConverter, sortParamConverter } from '@/utils/utils';
import { SortOrder } from 'antd/es/table/interface';
import { AdministratorList } from './data';

/**
 * 查询控制台管理员列表
 */
export async function getAdministratorList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AdministratorList>> {
  return request<API.ApiResult<AdministratorList>>('/api/V1/admin/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AdministratorList>) => {
    const data: RequestData<AdministratorList> = {
      data: result?.data?.list ? result?.data?.list : [],
      success: result?.success,
      total: result?.data?.pagination ? result?.data?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 新增控制台管理员
 *
 * @param params
 */
export async function createAdministrator(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/admin/create`, {
    data: params,
    method: 'POST',
  });
}

/**
 * 更新控制台管理员
 *
 * @param params
 */
export async function updateAdministrator(
  params: Record<string, unknown>,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/admin/update`, {
    data: { ...params },
    method: 'PUT',
  });
}

/**
 * Remove Administrator
 */
export async function deleteAdministrator(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/admin/delete/${id}`, {
    method: 'DELETE',
  });
}

/**
 * Get Administrator Details
 */
export async function getAdministrator(id: string): Promise<API.ApiResult<Record<string, string>>> {
  return request(`/api/V1/admin/get/${id}`, {
    method: 'GET',
  });
}

/**
 * Enable Administrator
 */
export async function enableAdministrator(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/admin/enable/${id}`, {
    method: 'PUT',
  });
}

/**
 * Disable Administrator
 */
export async function disableAdministrator(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/admin/disable/${id}`, {
    method: 'PUT',
  });
}

/**
 * 重置控制台管理员密码
 *
 * @param id
 * @param password
 */
export async function resetAdministratorPassword(
  id: string,
  password: string,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/admin/reset_password`, {
    params: { id, password },
    requestType: 'form',
    method: 'PUT',
  });
}

/**
 * 验证控制台管理员用户信息
 *
 * @param type
 * @param value
 * @param id
 */
export async function administratorParamCheck(
  type: string,
  value: string,
  id?: string,
): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/admin/param_check`, {
    params: { id, type, value },
    method: 'GET',
  });
}
