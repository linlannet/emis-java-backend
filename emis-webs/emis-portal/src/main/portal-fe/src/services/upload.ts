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
import type { RcFile } from 'antd/es/upload';
import { request } from '@umijs/max';

/**
 * 上传文件
 * @param file
 * @param fileName
 * @returns
 */
export async function uploadFile(
  file: string | RcFile | Blob,
  fileName?: string,
): Promise<API.ApiResult<string>> {
  const formData = new FormData();
  formData.append('file', file);
  if (fileName) formData.append('fileName', fileName);
  return request('/api/V1/setting/storage/upload', { method: 'POST', data: formData });
}
