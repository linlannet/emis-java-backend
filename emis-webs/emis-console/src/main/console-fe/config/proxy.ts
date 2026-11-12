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
 * 在生产环境 代理是无法生效的，所以这里没有生产环境的配置
 * The agent cannot take effect in the production environment
 * so there is no configuration of the production environment
 * For details, please see
 * https://pro.ant.design/docs/deploy
 */
export default {
  dev: {
    '/api/': {
      target: 'http://localhost:1988',
      // target: 'https://linlan.net/emis_console',
      changeOrigin: true,
      secure:false,
      pathRewrite: { '^': '' },
    },
  },
  test: {
    '/api/': {
      target: 'http://localhost:1988',
      changeOrigin: true,
      secure:false,
      pathRewrite: { '^': '' },
    },
  },
  pre: {
    '/api/': {
      target: 'http://localhost:1988',
      changeOrigin: true,
      secure:false,
      pathRewrite: { '^': '' },
    },
  },
} as Record<string, any>;
