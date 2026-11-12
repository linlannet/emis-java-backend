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
export default [
  {
    layout: false,
    hideInMenu: true,
    name: 'login',
    path: '/login',
    component: './Login',
  },
  {
    layout: false,
    hideInMenu: true,
    name: 'session-expired',
    path: '/session-expired',
    component: './SessionExpired',
  },
  {
    path: '/',
    redirect: '/application',
  },
  //工作台菜单
  {
    name: 'workplace',
    icon: 'dashboard',
    path: '/workplace',
    component: './Workplace',
  },
  // 我的应用
  {
    name: 'application',
    icon: 'form',
    path: '/application',
    component: './Application',
  },
  // 我的账户
  {
    name: 'account',
    icon: 'user',
    path: '/account',
    component: './Account',
  },
  // 系统审计
  {
    name: 'audit',
    icon: 'history',
    path: '/audit',
    component: './Audit',
  },
  // 会话管理
  {
    name: 'session',
    icon: 'notification',
    path: '/session',
    component: './Session',
  },
  {
    path: '*',
    layout: false,
    component: './404',
  },
];
