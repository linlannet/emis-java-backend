/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.param;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * ProtocolTsaConfig数据域:应用TSA配置查询参数实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolTsaConfigParam extends BaseParam {

    /**
     * 主键ID
     */
    private String  id;
    /**
     * 应用ID
     */
    private String  appId;
    /**
     * 外键ID
     */
    private String  foreignId;
    /**
     * 登录页面URL
     */
    private String  loginPage;
    /**
     * 自动登录步骤
     */
    private String  autoLoginSteps;
    /**
     * 创建账号步骤
     */
    private String  createAccountSteps;
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
