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

import java.util.Date;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * AppCertifacation数据域:应用证书查询参数实体类
 * @author Linlan
 *
 *
 */
@Data
public class AppCertificationParam extends BaseParam {

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
     * 证书序列号
     */
    private String  serial;
    /**
     * 主题信息
     */
    private String  subject;
    /**
     * 签发者信息
     */
    private String  issuer;
    /**
     * 开始时间
     */
    private Date    beginDate;
    /**
     * 结束时间
     */
    private Date    endDate;
    /**
     * 有效天数
     */
    private Integer validity;
    /**
     * 算法
     */
    private String  signAlgo;
    /**
     * 私钥长度
     */
    private Integer keyLong;
    /**
     * 私钥
     */
    private String  privateKey;
    /**
     * 公钥
     */
    private String  publicKey;
    /**
     * 证书
     */
    private String  certification;
    /**
     * 使用类型
     */
    private String  usingType;
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
