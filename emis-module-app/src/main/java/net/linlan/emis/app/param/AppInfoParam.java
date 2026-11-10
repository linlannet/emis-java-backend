/*
 * emis-module-app - app应用管理
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
package net.linlan.emis.app.param;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * AppInfo数据域:应用协议查询参数实体类
 * @author Linlan
 *
 *
 */
@Data
public class AppInfoParam extends BaseParam {

    /**
     * 主键ID
     */
    private String  id;
    /**
     * 外键ID
     */
    private String  foreignId;
    /**
     * 应用名称
     */
    private String  name;
    /**
     * 编码
     */
    private String  code;
    /**
     * 客户端ID
     */
    private String  clientId;
    /**
     * 客户端秘钥
     */
    private String  clientSecret;
    /**
     * 协议类型
     */
    private String  protocol;
    /**
     * 应用类型
     */
    private String  type;
    /**
     * 应用模板
     */
    private String  template;
    /**
     * 应用图标
     */
    private String  icon;
    /**
     * SSO 发起登录类型
     */
    private String  initLoginType;
    /**
     * SSO 发起登录URL
     */
    private String  initLoginUrl;
    /**
     * SSO 授权类型
     */
    private String  authorizationType;
    /**
     * 是否已配置
     */
    private Boolean isConfigured;
    /**
     * 是否启用
     */
    private Boolean isEnabled;
    /**
     * 适配器ID
     */
    private String  adapterId;
    /**
     * 创建人ID
     */
    private String  creatorId;
    /**
     * 删除标记，0未删除1已删除
     */
    private Integer delFlag;
    /**
     * 备注
     */
    private String  description;
    /**
     * 备用1
     */
    private String  spare1;
    /**
     * 备用2
     */
    private String  spare2;

}
