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
// https://umijs.org/config/
import {defineConfig} from '@umijs/max';
import defaultSettings from './defaultSettings';
import proxy from './proxy';
import routes from './routes';

const theme = require('@ant-design/antd-theme-variable');

const { REACT_APP_ENV } = process.env;

export default defineConfig({
  title: defaultSettings.title as string,
  favicons: ['/favicon.ico'],
  metas: [
    {
      name: 'viewport',
      content: 'width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0',
    },
    {
      name: 'description',
      content:
        ' 🚀 企业级身份管理和访问管理程序，为企业实现统一认证和单点登录、为数字身份安全赋能！',
    },
  ],
  /**
   * @name 开启 hash 模式
   * @description 让 build 之后的产物包含 hash 后缀。通常用于增量发布和避免浏览器加载缓存。
   * @doc https://umijs.org/docs/api/config#hash
   */
  hash: true,
  /**
   * @name 配置输出路径
   * @description 注意：不允许设定为 src、public、pages、mock、config、locales、models 等约定式功能相关的目录。
   * @doc https://umijs.org/docs/api/config#outputpath
   */
  outputPath: 'build',
  /**
   * @name 路由的配置，不在路由中引入的文件不会编译
   * @description 只支持 path，component，routes，redirect，wrappers，title 的配置
   * @doc https://umijs.org/docs/guides/routes
   */
  // umi routes: https://umijs.org/docs/routing
  routes,
  /**
   * @name 主题的配置
   * @description 虽然叫主题，但是其实只是 less 的变量设置
   * @doc antd的主题设置 https://ant.design/docs/react/customize-theme-cn
   * @doc umi 的theme 配置 https://umijs.org/docs/api/config#theme
   */
  theme: {
    ...theme,
    // 如果不想要 configProvide 动态设置主题需要把这个设置为 default
    // 只有设置为 variable， 才能使用 configProvide 动态设置主色调
    'primary-color': defaultSettings.colorPrimary,
    'root-entry-name': 'variable',
  },
  /**
   * @name moment 的国际化配置
   * @description 如果对国际化没有要求，打开之后能减少js的包大小
   * @doc https://umijs.org/docs/api/config#ignoremomentlocale
   */
  ignoreMomentLocale: false,
  /**
   * @name 代理配置
   * @description 可以让你的本地服务器代理到你的服务器上，这样你就可以访问服务器的数据了
   * @see 要注意以下 代理只能在本地开发时使用，build 之后就无法使用了。
   * @doc 代理介绍 https://umijs.org/docs/guides/proxy
   * @doc 代理配置 https://umijs.org/docs/api/config#proxy
   */
  proxy: proxy[REACT_APP_ENV || 'dev'],
  /**
   * @name 快速热更新配置
   * @description 一个不错的热更新组件，更新时可以保留 state
   */
  fastRefresh: true,
  //============== 以下都是max的插件配置 ===============
  /**
   * @name 数据流插件
   * @@doc https://umijs.org/docs/max/data-flow
   */
  model: {},
  /**
   * @name valtio 数据流方案
   * @@doc https://umijs.org/docs/max/valtio
   */
  valtio: false,
  /**
   * 一个全局的初始数据流，可以用它在插件之间共享数据
   * @description 可以用来存放一些全局的数据，比如用户信息，或者一些全局的状态，全局初始状态在整个 Umi 项目的最开始创建。
   * @doc https://umijs.org/docs/max/data-flow#%E5%85%A8%E5%B1%80%E5%88%9D%E5%A7%8B%E7%8A%B6%E6%80%81
   */
  initialState: {
    loading: '@/components/PageLoading',
  },
  /**
   * @name layout 插件
   * @doc https://umijs.org/docs/max/layout-menu
   */
  layout: {
    ...defaultSettings,
  },
  /**
   * @name moment2dayjs 插件
   * @description 将项目中的 moment 替换为 dayjs
   * @doc https://umijs.org/docs/max/moment2dayjs
   */
  moment2dayjs: {
    preset: 'antd',
    plugins: ['duration'],
  },
  /**
   * @name 国际化插件
   * @doc https://umijs.org/docs/max/i18n
   */
  locale: {
    // default zh-CN
    default: 'zh-CN',
    antd: true,
    // default true, when it is true, will use `navigator.language` overwrite default
    baseNavigator: true,
  },
  /**
   * @name 网络请求配置
   * @description 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
   * @doc https://umijs.org/docs/max/request
   */
  request: {},
  /**
   * @name 权限插件
   * @description 基于 initialState 的权限插件，必须先打开 initialState
   * @doc https://umijs.org/docs/max/access
   */
  access: {},
  //================ pro 插件配置 =================
  presets: ['umi-presets-pro'],
  /**
   * @name mfsu 的配置
   * @description 开启 MFSU 可以大幅减少热更新所需的时间了
   * @doc https://umijs.org/docs/guides/mfsu
   */
  mfsu: {},
  /**
   * @name esbuildMinifyIIFE 的配置
   * @description 修复 esbuild 压缩器自动引入的全局变量导致的命名冲突问题。
   * @doc https://umijs.org/docs/api/config#esbuildminifyiife
   */
  esbuildMinifyIIFE: true,
  /**
   * @name mfsu 的配置
   * @description 开启 MFSU 可以大幅减少热更新所需的时间了
   * @doc https://umijs.org/blog/code-splitting
   */
  codeSplitting: {
    jsStrategy: 'granularChunks',
  },
  /**
   * @name jsMinifier 的配置
   * @description 配置构建时压缩 JavaScript 的工具；可选值 esbuild, terser, swc, uglifyJs, none
   * @doc https://umijs.org/docs/api/config#jsminifier-webpack
   */
  jsMinifier: 'esbuild',
  /**
   * @name antd 的配置
   * @description 整合 antd 组件库
   * @doc https://umijs.org/docs/max/antd#antd
   */
  antd: {
    configProvider: {},
    appConfig: {},
    styleProvider: {
      hashPriority: 'high',
      legacyTransformer: true,
    },
    theme: {
      token: {},
    },
  },
});
