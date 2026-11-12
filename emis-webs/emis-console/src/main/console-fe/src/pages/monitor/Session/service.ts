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
import { request } from '@@/plugin-request/request';
import type { RequestData } from '@ant-design/pro-components';
import { SessionList } from './data.d';

/**
 * 获取在线用户列表
 */
export async function getSessionList(
  params: Record<string, string>,
): Promise<RequestData<SessionList>> {
  return request<API.ApiResult<SessionList>>('/api/V1/session/list', {
    method: 'GET',
    params,
  }).then((result) => {
    const data: RequestData<SessionList> = {
      data: result?.data ? result?.data : [],
      success: result?.success,
    };
    return Promise.resolve(data);
  });
}

/**
 * 下线服务
 */
export async function removeSessions(sessionIds: string): Promise<API.ApiResult<boolean>> {
  return request<API.ApiResult<boolean>>('/api/V1/session/remove', {
    method: 'DELETE',
    params: { sessionIds },
  });
}
