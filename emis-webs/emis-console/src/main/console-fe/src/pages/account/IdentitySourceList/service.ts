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
import { SortOrder } from 'antd/es/table/interface';
import { RequestData } from '@ant-design/pro-components';
import { request } from '@@/exports';
import { filterParamConverter, sortParamConverter } from '@/utils/utils';

/**
 * 获取身份源平台列表
 */
export async function getIdentityProviderList(
  params: Record<string, any>,
  sort: Record<string, SortOrder>,
  filter: Record<string, (string | number)[] | null>,
): Promise<RequestData<AccountAPI.ListIdentitySource>> {
  return request<API.ApiResult<AccountAPI.ListIdentitySource>>('/api/V1/identity_source/list', {
    params: { ...params, ...sortParamConverter(sort), ...filterParamConverter(filter) },
  }).then((result: API.ApiResult<AccountAPI.ListIdentitySource>) => {
    const data: RequestData<AccountAPI.ListIdentitySource> = {
      data: result?.data?.list ? result?.data?.list : [],
      success: result?.success,
      total: result?.data?.pagination ? result?.data?.pagination.total : 0,
    };
    return Promise.resolve(data);
  });
}

/**
 * 创建身份源
 */
export async function createIdentitySource(
  params: Record<string, any>,
): Promise<API.ApiResult<{ id: string }>> {
  return request(`/api/V1/identity_source/create`, {
    data: params,
    method: 'POST',
    requestType: 'json',
  });
}

/**
 * 启用身份源
 */
export async function enableIdentitySource(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/identity_source/enable/${id}`, { method: 'PUT' });
}

/**
 * 禁用身份源
 */
export async function disableIdentitySource(id: string): Promise<API.ApiResult<boolean>> {
  return request(`/api/V1/identity_source/disable/${id}`, { method: 'PUT' });
}
