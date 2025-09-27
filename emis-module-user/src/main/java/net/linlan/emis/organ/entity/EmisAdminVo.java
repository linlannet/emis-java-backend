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
package net.linlan.emis.organ.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.base.Function;

import lombok.Data;

import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.frame.admin.entity.AdminUser;
import net.linlan.frame.comm.constant.AdminType;

/**
 * 管理员表
 *
 * @author Linlan
 */
@Data
public class EmisAdminVo {

    private static final long  serialVersionUID    = -8631010419770301701L;

    public static final String USERNAME_FIELD_NAME = "username";

    public static final String MOBILE_FIELD_NAME   = "mobile";

    public static final String EMAIL_FIELD_NAME    = "email";

    /**
     * 用户LID
     */
    private String             adminId;
    /**
     * 用户ID
     */
    private String             userId;
    /**
     * 用户名
     */
    private String             username;

    /**
     * 姓名
     */
    private String             fullName;

    /**
     * 昵称
     */
    private String             nickName;

    /**
     * 密码
     */
    private String             password;

    /**
     * 需要修改密码
     */
    private Boolean            needChangePassword;

    /**
     * 邮箱
     */
    private String             email;

    /**
     * 手机号
     */
    private String             mobile;

    /**
     * 手机号所在地域码
     */
    private String             mobileAreaCode;

    /**
     * 手机是否有效
     */
    private Boolean            mobileVerified;

    /**
     * 头像URL
     */
    private String             avatarUrl;

    /**
     * 状态  ENABLE:启用 DISABLE:禁用 LOCKING:锁定
     */
    private UserStatus         status;

    /**
     * 邮箱验证有效
     */
    private Boolean            emailVerified;

    /**
     * 认证次数
     */
    private Long               authTotal;
    /**
     * 上次认证IP
     */
    private String             lastAuthIp;

    /**
     * 上次认证时间
     */
    private LocalDateTime      lastAuthTime;

    /**
     * 最后修改密码时间
     */
    private LocalDateTime      lastUpdatePasswordTime;

    /**
     * 扩展参数
     */
    private String             expandParam;
    /**
     * 创建人id
     */
    private String             creatorId;
    /**
     * 创建时间
     */
    private Date               createTime;

    /**
     * 最后修改时间
     */
    private Date               lastTime;
    /**
     * 备注
     */
    private String             remark;

    public Boolean isLocked() {
        return UserStatus.LOCKED.equals(this.getStatus())
               || UserStatus.PASSWORD_EXPIRED_LOCKED.equals(this.getStatus())
               || UserStatus.EXPIRED_LOCKED.equals(this.getStatus());
    }

    public Boolean isDisabled() {
        return UserStatus.DISABLED.equals(this.getStatus());
    }

    public UserDetails toUserDetails(Collection<GrantedAuthority> authorities) {
        //@formatter:off
        UserDetails userDetails = new UserDetails(this.getAdminId(), this.getUsername(), this.getPassword(),
                AdminType.ADMIN.getType(), !isDisabled(), true,
                true, !isLocked(), authorities, null, null, null);
        userDetails.setPassword(this.getPassword());
        userDetails.setAvatarUrl(this.getAvatarUrl());
        userDetails.setMobile(this.getMobile());
        userDetails.setMobileAreaCode(this.getMobileAreaCode());
        userDetails.setMobileVerified(this.getMobileVerified());
        userDetails.setEmail(this.getEmail());
        userDetails.setEmailVerified(this.getEmailVerified());
        userDetails.setLastUpdatePasswordTime(this.getLastUpdatePasswordTime());
        userDetails.setFullName(this.getFullName());
        userDetails.setLastTime(this.getLastTime());
        userDetails.setNeedChangePassword(this.getNeedChangePassword());
        //@formatter:on
        return userDetails;
    }

    /**
     * 数据赋值TO方法，将AdminDept的input对象赋值给AdminDeptVo
     */
    public static final Function TO = new Function<AdminUser, EmisAdminVo>() {
        @Nullable
        @Override
        public EmisAdminVo apply(@Nullable AdminUser input) {
            return new EmisAdminVo(input);
        }
    };

    /**
     * @param input 数据库查询出的entity对象，对VO对象进行赋值，默认的字段为全部字段，可结合实际调整
     */
    public EmisAdminVo(AdminUser input) {
        this.adminId = input.getId().toString();
        this.creatorId = input.getCreatorId();
        this.createTime = input.getCreateTime();
        this.lastTime = input.getLastTime();
        this.remark = input.getSpare1();
    }

}
