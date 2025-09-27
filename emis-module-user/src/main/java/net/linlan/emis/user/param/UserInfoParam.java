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

import java.util.Date;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import net.linlan.utils.param.BaseParam;

/**
 *
 * UserInfo数据域:用户信息查询参数实体类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-06 15:46:35<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Data
public class UserInfoParam extends BaseParam {

    /**
     * 主键ID
     */
    private String  id;
    /**
     * 行政区划编号，用户来源地
     */
    private Long    xzqhId;
    /**
     * 身份源ID
     */
    private String  idsourceId;
    /**
     * 外键ID
     */
    private String  foreignId;
    /**
     * 用户名
     */
    private String  username;
    /**
     * 密码
     */
    private String  password;
    /**
     * 混淆码
     */
    private String  salt;
    /**
     * 电话号码
     */
    private String  mobile;
    /**
     * 电话地区代码
     */
    private String  mobileAreaCode;
    /**
     * 电话验证有效，0否1是
     */
    private Boolean mobileVerified;
    /**
     * 邮箱
     */
    private String  email;
    /**
     * 邮箱验证有效，0否1是
     */
    private Boolean emailVerified;
    /**
     * 姓名
     */
    private String  fullName;
    /**
     * 昵称
     */
    private String  nickName;
    /**
     * 头像
     */
    private String  avatarUrl;
    /**
     * 数据来源
     */
    private String  createType;
    /**
     * 共享秘钥-TIME OTP
     */
    private String  sharedSecret;
    /**
     * 是否绑定 TOTP，0否1是
     */
    private Boolean totpBind;
    /**
     * 外部ID
     */
    private String  outSourceId;
    /**
     * 过期日期
     */
    private Date    expireDate;
    /**
     * 锁定时间
     */
    private Date    lockExpiredTime;
    /**
     * 最后登录的应用ID
     */
    private String  lastAppId;
    /**
     * 状态  ENABLE:启用 DISABLE:禁用 LOCKING:锁定
     */
    private String  status;
    /**
     * 登录次数
     */
    private Integer loginCount;
    /**
     * 当前登录时间
     */
    private Date    currentLoginTime;
    /**
     * 当前登录IP
     */
    private String  currentLoginIp;
    /**
     * 认证次数
     */
    private Long    authTotal;
    /**
     * 上次认证IP
     */
    private String  lastAuthIp;
    /**
     * 上次认证时间
     */
    private Date    lastAuthTime;
    /**
     * 重置密码KEY
     */
    private String  resetKey;
    /**
     * 重置密码VALUE
     */
    private String  resetPwd;
    /**
     * 上次修改密码时间，最后一次修改密码时间
     */
    private Date    lastResetTime;
    /**
     * 是否激活0否1是，是否需要更改密码0否1是
     */
    private Boolean activation;
    /**
     * 激活代码
     */
    private String  activationCode;
    /**
     * 扩展参数
     */
    private String  expandParam;
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

    /**
     * 包含子组织
     */
    @Parameter(description = "包含子组织")
    private Boolean inclSubOrganization;

}
