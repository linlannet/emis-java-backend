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
/**
 * 账户信息
 */
export type AccountInfo = {
  /** 用户ID */
  id: string;
  avatarUrl: string;
  username: string;
  mobile: string;
  access: string;
};

export interface GetBoundIdpList {
  code: string;
  name: string;
  type: string;
  category: string;
  bound: boolean;
  idpId: string;
}

/**
 * 账户菜单类型
 */
export enum AccountSettingsStateKey {
  base = 'base',
  security = 'security',
}
