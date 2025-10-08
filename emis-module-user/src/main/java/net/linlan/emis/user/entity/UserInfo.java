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
package net.linlan.emis.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.frame.comm.constant.AdminType;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * UserInfo数据域:用户信息实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 15:46:35
 *
 */
@Data
public class UserInfo extends BaseEntity {

    /**
     * 主键ID
     */
    private String        id;

    /**
     * 行政区划编号，用户来源地
     */
    private Long          xzqhId;

    /**
     * 身份源ID
     */
    private String        idsourceId;

    /**
     * 外键ID
     */
    private String        foreignId;

    /**
     * 用户名
     */
    private String        username;

    /**
     * 密码
     */
    private String        password;

    /**
     * 混淆码
     */
    private String        salt;

    /**
     * 电话号码
     */
    private String        mobile;

    /**
     * 电话地区代码
     */
    private String        mobileAreaCode;

    /**
     * 电话验证有效，0否1是
     */
    private Boolean       mobileVerified;

    /**
     * 邮箱
     */
    private String        email;

    /**
     * 邮箱验证有效，0否1是
     */
    private Boolean       emailVerified;

    /**
     * 姓名
     */
    private String        fullName;

    /**
     * 昵称
     */
    private String        nickName;

    /**
     * 头像
     */
    private String        avatarUrl;

    /**
     * 数据来源
     */
    private String        createType;

    /**
     * 共享秘钥-TIME OTP
     */
    private String        sharedSecret;

    /**
     * 是否绑定 TOTP，0否1是
     */
    private Boolean       totpBind;

    /**
     * 外部ID
     */
    private String        outSourceId;

    /**
     * 过期日期
     */
    private LocalDate     expireDate;

    /**
     * 锁定时间
     */
    private LocalDateTime lockExpiredTime;

    /**
     * 最后登录的应用ID
     */
    private String        lastAppId;

    /**
     * 状态  ENABLE:启用 DISABLE:禁用 LOCKING:锁定
     */
    private String        status;

    /**
     * 登录次数
     */
    private Integer       loginCount;

    /**
     * 当前登录时间
     */
    private Date          currentLoginTime;

    /**
     * 当前登录IP
     */
    private String        currentLoginIp;

    /**
     * 认证次数
     */
    private Long          authTotal;

    /**
     * 上次认证IP
     */
    private String        lastAuthIp;

    /**
     * 上次认证时间
     */
    private Date          lastAuthTime;

    /**
     * 重置密码KEY
     */
    private String        resetKey;

    /**
     * 重置密码VALUE
     */
    private String        resetPwd;

    /**
     * 上次修改密码时间，最后一次修改密码时间
     */
    private LocalDateTime lastResetTime;

    /**
     * 是否激活0否1是，是否需要更改密码0否1是
     */
    private Boolean       activation;

    /**
     * 激活代码
     */
    private String        activationCode;

    /**
     * 扩展参数
     */
    private String        expandParam;

    /**
     * 删除标记，0未删除1已删除
     */
    private Integer       delFlag;

    /**
     * 备注
     */
    private String        description;

    /**
     * 备用1
     */
    private String        spare1;

    /**
     * 备用2
     */
    private String        spare2;
    /**
     * 暂存密码(明文)
     */
    @JsonIgnore
    private String        passwordPlainText;

    /**
     * 初始化方法
     */
    public void init() {
        //添加对数据库或实体对象的默认值处理
        if (getId() == null) {
            setId(RandomUtils.randomSid());
        }

    }

    public Boolean isDisabled() {
        return UserStatus.DISABLED.equals(this.getStatus());
    }

    public Boolean isLocked() {
        return UserStatus.LOCKED.equals(this.getStatus())
               || UserStatus.PASSWORD_EXPIRED_LOCKED.equals(this.getStatus())
               || UserStatus.EXPIRED_LOCKED.equals(this.getStatus());
    }

    public UserDetails toUserDetails(Collection<GrantedAuthority> authorities) {
        //@formatter:off
        UserDetails userDetails = new UserDetails(this.getId(), this.getUsername(),
                this.getPassword(), AdminType.USER.getType(), !isDisabled(), true, true,
                !isLocked(), authorities);
        userDetails.setPassword(this.getPassword());
        userDetails.setMobile(this.getMobile());
        userDetails.setAvatarUrl(this.getAvatarUrl());
        userDetails.setEmail(this.getEmail());
        userDetails.setMobileAreaCode(this.getMobileAreaCode());
        userDetails.setMobileVerified(this.getMobileVerified());
        userDetails.setEmailVerified(this.getEmailVerified());
        userDetails.setLastUpdatePasswordTime(this.getLastResetTime());
        userDetails.setFullName(this.getFullName());
        userDetails.setNeedChangePassword(this.getActivation());
        userDetails.setForeignId(this.getForeignId());
        userDetails.setExpireDate(this.getExpireDate());
        userDetails.setLastTime(this.getLastTime());
        userDetails.setNickName(this.getNickName());
        //@formatter:on
        return userDetails;
    }

}
