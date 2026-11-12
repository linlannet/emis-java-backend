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
import React from 'react';
import { ProFormSelect, ProFormSelectProps } from '@ant-design/pro-components';
import { MobileAreaCode } from './data.d';

export default (props: Omit<ProFormSelectProps<MobileAreaCode>, 'request'>) => {
  const { initialValue = '+86' } = props;

  return (
    <ProFormSelect
      {...props}
      initialValue={initialValue}
      request={async () => {
        return [{ value: '+86', label: `+86 中国` }];
      }}
    />
  );
};
