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
import { Alert } from 'antd';
import React from 'react';
import Marquee from 'react-fast-marquee';

const message = () => {
  return (
    <>
      <span>⭐️ 如果你喜欢 EMIS，请给它一个 Star </span>
      <a target="_blank" rel="noopener noreferrer" href="https://github.com/linlannet/java-emis-parent">
        GitHub
      </a>
      <span> && </span>
      <a target="_blank" rel="noopener noreferrer" href="https://gitee.com/linlannet/java-emis-parent">
        Gitee
      </a>
      <span>
        ， 你的支持将是我们前行的动力，项目正在积极开发， 欢迎 PR、提供建议，共建社区生态。👏🏻
      </span>
    </>
  );
};
export default (props: { play?: boolean }) => {
  const { play } = props;
  return (
    <Alert
      style={{
        padding: 0,
        background:
          'repeating-linear-gradient(35deg, hsl(196 120% 85%), hsl(196 120% 85%) 20px, hsl(196 120% 95%) 10px, hsl(196 120% 95%) 40px)',
      }}
      message={
        <div
          style={{
            whiteSpace: 'nowrap', //强制文本在一行内输出
            overflow: 'hidden', //隐藏溢出部分
            textOverflow: 'ellipsis', //对溢出部分加上...
            textAlign: 'center',
            color: 'black',
          }}
        >
          {play ? (
            <Marquee pauseOnHover gradient={false}>
              {message()}
            </Marquee>
          ) : (
            message()
          )}
        </div>
      }
      showIcon={false}
      banner
      type={'info'}
    />
  );
};
