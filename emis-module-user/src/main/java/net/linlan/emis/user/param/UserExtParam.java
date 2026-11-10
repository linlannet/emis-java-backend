/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.user.param;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * UserExt数据域:用户详情查询参数实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 15:46:35
 *
 */
@Data
public class UserExtParam extends BaseParam {

    /**
     * 用户ID
     */
    private String  id;
    /**
     * 证件类型
     */
    private String  idType;
    /**
     * 真实姓名，加密的姓名
     */
    private String  idName;
    /**
     * 身份证号码，加密的身份证号码
     */
    private String  idNum;
    /**
     * 加密的电话号码
     */
    private String  idMobile;
    /**
     * 行政区划1
     */
    private String  areaCode1;
    /**
     * 地址1，居住地址
     */
    private String  address1;
    /**
     * 行政区划2
     */
    private String  areaCode2;
    /**
     * 地址2，户籍地址
     */
    private String  address2;
    /**
     * 个人介绍
     */
    private String  intro;
    /**
     * 来自
     */
    private String  comeFrom;
    /**
     * 用户签名
     */
    private String  userSignature;
    /**
     * 个人主页URL
     */
    private String  homePage;
    /**
     * 登录IP地址(";"分隔）
     */
    private String  ipRange;
    /**
     * 允许登录MAC地址(";"分隔）
     */
    private String  macCode;
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
