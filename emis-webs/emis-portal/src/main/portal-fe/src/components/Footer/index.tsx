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
import { GithubOutlined } from '@ant-design/icons';
import type { FooterProps } from '@ant-design/pro-components';
import { DefaultFooter } from '@ant-design/pro-components';

const currentYear = new Date().getFullYear();

export default (props: FooterProps) => (
  <DefaultFooter
    style={{ backgroundColor: 'transparent' }}
    copyright={`2023-${currentYear} LINLAN 版权所有`}
    links={[
      {
        key: 'website',
        title: '官方网站',
        href: 'https://linlan.net/emis',
        blankTarget: true,
      },
      {
        key: 'github',
        title: <GithubOutlined />,
        href: 'https://github.com/linlannet/java-emis-parent',
        blankTarget: true,
      },
      {
        key: 'docs',
        title: '使用文档',
        href: 'https://linlan.net/emis/docs/introduction/overview',
        blankTarget: true,
      },
    ]}
    {...props}
  />
);
