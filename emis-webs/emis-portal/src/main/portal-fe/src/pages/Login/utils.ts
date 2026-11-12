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
import { history } from '@@/core/history';
import { parse } from 'querystring';

export const goto = (jump?: boolean) => {
  if (!history) return;
  const query = parse(history.location.search);
  if (!jump) {
    const { redirect_uri } = query as { redirect_uri: string };
    window.location.replace(redirect_uri || '/');
    return;
  }
  //跳转
  window.location.replace('/api/V1/jump');
};
