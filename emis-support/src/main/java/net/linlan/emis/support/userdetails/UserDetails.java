/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.userdetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import net.linlan.emis.support.constant.EmisConstants;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.useragent.UserAgent;
import net.linlan.frame.comm.constant.AdminType;

/**
 * 用户详情
 *
 * @author Linlan
 */
@Setter
@Getter
public class UserDetails extends User
                         implements org.springframework.security.core.userdetails.UserDetails {
    private static final long  serialVersionUID = -8631010419770301116L;

    /**
     * 用户ID
     */
    private String             id;

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
     * 手机号
     */
    private String             mobileAreaCode;

    /**
     * 手机有效
     */
    private Boolean            mobileVerified;

    /**
     * 头像URL
     */
    private String             avatarUrl;

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
     * 最后修改时间
     */
    @LastModifiedDate
    @JsonFormat
    private LocalDateTime      updateTime;

    /**
     * 外部ID
     */
    private String             foreignId;

    /**
     * 过期时间
     */
    private LocalDate          expireDate;

    /**
     * 扩展参数
     */
    private String             expandParam;

    /**
     * 地址位置相关
     */
    private GeoLocation        geoLocation;

    /**
     * userAgent
     */
    private UserAgent          userAgent;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = EmisConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime      loginTime;
    /**
     * 用户可访问应用列表
     */
    @JsonProperty("applications")
    private List<Application>  applications;

    /**
     * 用户所在机构
     */
    @JsonProperty("organizations")
    private List<Organization> organizations;
    /**
     * 用户工作组
     */
    @JsonProperty("groups")
    private List<Group>        groups;
    /**
     * 身份验证类型
     */
    @JsonProperty("authType")
    private String             authType;
    /**
     * 用户类型
     */
    @JsonProperty("userType")
    private String             userType;

    /**
     * @param id                    用户ID
     * @param username              用户名
     * @param userType              用户类型
     * @param enabled               是否失效
     * @param accountNonExpired     账户是否过期
     * @param credentialsNonExpired 凭证是否未过期
     * @param accountNonLocked      帐户是否锁定
     * @param authorities           权限
     * @param organizations         机构Ids
     * @param groups                用户组Ids
     * @param applications          授权应用Ids
     */
    public UserDetails(String id, String username, String userType, boolean enabled,
                       boolean accountNonExpired, boolean credentialsNonExpired,
                       boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                       List<Organization> organizations, List<Group> groups,
                       List<Application> applications) {
        super(username, "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
            authorities);
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.organizations = organizations;
        this.groups = groups;
        this.applications = applications;
    }

    /**
     * @param id                    用户ID
     * @param username              用户名
     * @param password              密码
     * @param userType              用户类型
     * @param enabled               是否失效
     * @param accountNonExpired     账户是否过期
     * @param credentialsNonExpired 凭证是否未过期
     * @param accountNonLocked      帐户是否锁定
     * @param authorities           权限
     * @param organizations         机构Ids
     * @param groups                用户组Ids
     * @param applications          授权应用Ids
     */
    public UserDetails(String id, String username, String password, String userType,
                       boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                       boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                       List<Organization> organizations, List<Group> groups,
                       List<Application> applications) {
        super(username, Objects.isNull(password) ? "" : password, enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.organizations = organizations;
        this.groups = groups;
        this.applications = applications;
    }

    /**
     * @param id                    用户ID
     * @param username              用户名
     * @param password              密码
     * @param userType              用户类型
     * @param enabled               是否失效
     * @param accountNonExpired     账户是否过期
     * @param credentialsNonExpired 凭证是否未过期
     * @param accountNonLocked      帐户是否锁定
     * @param authorities           权限
     */
    public UserDetails(String id, String username, String password, String userType,
                       boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                       boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities) {
        super(username, Objects.isNull(password) ? "" : password, enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.organizations = null;
        this.groups = null;
        this.applications = null;
    }

    /**
     * @param id                    用户ID
     * @param username              用户名
     * @param password              密码
     */
    public UserDetails(String id, String username, String password) {
        super(username, Objects.isNull(password) ? "" : password, true, true, true, true, null);
        this.id = id;
        this.username = username;
        this.userType = AdminType.USER.getType();
        this.organizations = null;
        this.groups = null;
        this.applications = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        UserDetails details = (UserDetails) o;
        return StringUtils.equals(username, details.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username);
    }

    @Override
    public String toString() {
        return username;
    }

}
