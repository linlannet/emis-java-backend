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
import { Typography } from 'antd';

const { Text } = Typography;

const EllipsisMiddle = (props: { suffixCount: number; text: string }) => {
  const { suffixCount, text } = props;
  const start = text.slice(0, text.length - suffixCount).trim();
  const suffix = text.slice(-suffixCount).trim();
  return (
    <Text style={{ maxWidth: '100%' }} ellipsis={{ suffix, tooltip: true }} copyable={!!start}>
      {start ? start : '-'}
    </Text>
  );
};
export default EllipsisMiddle;
