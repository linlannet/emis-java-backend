/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import net.linlan.emis.openapi.pojo.result.UserListResult;
import net.linlan.emis.openapi.pojo.result.UserResult;
import net.linlan.emis.openapi.pojo.save.UserCreateParam;
import net.linlan.emis.openapi.pojo.update.UserUpdateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.Page.Pagination;
import net.linlan.emis.support.userdetails.DataOrigin;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.utils.SpringContextUtils;
import static net.linlan.emis.support.utils.ImageAvatarUtils.getRandomAvatar;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileAreaCode;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileNumber;

/**
 * 用户映射
 *
 * @author Linlan
 * Created on 2020/8/14 21:45
 */
@Component
public class UserConverter {

    /**
     * 用户实体转换为用户分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    public Page<UserListResult> userDtoConvertToUserListResult(Page<UserInfoDto> page) {
        Page<UserListResult> result = new Page<>();
        if (page != null && !CollectionUtils.isEmpty(page.getList())) {
            List<UserListResult> list = new ArrayList<>();
            for (UserInfoDto user : page.getList()) {
                UserListResult userListResult = userDtoConvertToUserListResult(user);
                if (StringUtils.hasText(user.getMobile())) {
                    userListResult.setMobile((StringUtils.hasText(user.getMobileAreaCode())
                        ? "+" + user.getMobileAreaCode()
                        : "") + user.getMobile());
                }
                list.add(userListResult);
            }
            //@formatter:off
            result.setPagination(Pagination.builder()
                    .total(page.getPagination() == null ? 0L : page.getPagination().getTotal())
                    .totalPages(page.getPagination() == null ? 0 : page.getPagination().getTotalPages())
                    .current(page.getPagination() == null ? 1 : page.getPagination().getCurrent())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * PageHelper 分页结果转换为统一分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    public Page<UserListResult> userDtoConvertToUserListResult(com.github.pagehelper.Page<UserInfoDto> page,
                                                               long total, int current, int totalPages) {
        Page<UserListResult> result = new Page<>();
        if (page != null && !CollectionUtils.isEmpty(page.getResult())) {
            List<UserListResult> list = new ArrayList<>();
            for (UserInfoDto user : page.getResult()) {
                UserListResult userListResult = userDtoConvertToUserListResult(user);
                if (StringUtils.hasText(user.getMobile())) {
                    userListResult.setMobile((StringUtils.hasText(user.getMobileAreaCode())
                        ? "+" + user.getMobileAreaCode()
                        : "") + user.getMobile());
                }
                list.add(userListResult);
            }
            //@formatter:off
            result.setPagination(Pagination.builder()
                    .total(total)
                    .totalPages(totalPages)
                    .current(current)
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 用户创建参数转换为用户实体
     *
     * @param param {@link UserCreateParam}
     * @return {@link UserInfo}
     */
    public UserInfo userCreateParamConvertToUserEntity(UserCreateParam param) {
        if (param == null) {
            return null;
        }
        UserInfo userEntity = new UserInfo();
        userEntity.setDescription(param.getRemark());
        userEntity.setUsername(param.getUsername());
        //邮箱
        if (StringUtils.hasText(param.getEmail())) {
            userEntity.setEmail(param.getEmail());
            userEntity.setEmailVerified(Boolean.TRUE);
        }
        //手机号
        if (StringUtils.hasText(param.getMobile())) {
            userEntity.setMobile(getMobileNumber(param.getMobile()));
            userEntity.setMobileVerified(Boolean.TRUE);
            userEntity.setMobileAreaCode(getMobileAreaCode(param.getMobile()));
        }
        userEntity.setFullName(param.getFullName());
        userEntity.setNickName(param.getNickName());
        userEntity.setLastResetTime(LocalDateTime.now());
        userEntity.setStatus(UserStatus.ENABLED.getKey());
        userEntity.setAvatarUrl(getRandomAvatar());
        userEntity.setCreateType(DataOrigin.INPUT.getType());
        userEntity.setExpireDate(
            Objects.isNull(param.getExpireDate()) ? LocalDate.of(2116, 12, 31) : param.getExpireDate());
        userEntity.setAuthTotal(0L);
        userEntity.setPassword(SpringContextUtils
            .getBean(org.springframework.security.crypto.password.PasswordEncoder.class)
            .encode(param.getPassword()));

        return userEntity;
    }

    /**
     * 用户更新参数转换为用户实体类
     *
     * @param param {@link UserUpdateParam} 更新参数
     * @return {@link UserInfo} 用户实体
     */
    public UserInfo userUpdateParamConvertToUserEntity(UserUpdateParam param) {
        if (param == null) {
            return null;
        }
        UserInfo userEntity = new UserInfo();
        if (param.getId() != null) {
            userEntity.setId(param.getId());
        }
        userEntity.setDescription(param.getRemark());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(param.getEmail())) {
            userEntity.setEmail(param.getEmail());
            userEntity.setEmailVerified(Boolean.TRUE);
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(param.getMobile())) {
            userEntity.setMobile(getMobileNumber(param.getMobile()));
            userEntity.setMobileAreaCode(getMobileAreaCode(param.getMobile()));
        }
        userEntity.setFullName(param.getFullName());
        userEntity.setNickName(param.getNickName());
        userEntity.setAvatarUrl(param.getAvatarUrl());
        if (param.getStatus() != null) {
            userEntity.setStatus(param.getStatus().getKey());
        }
        userEntity.setExpireDate(param.getExpireDate());
        return userEntity;
    }

    /**
     * 实体转为用户详情返回
     *
     * @param user   {@link UserInfo}
     * @param detail {@link UserExt}
     * @return {@link UserResult} 用户详情
     */
    public UserResult entityConvertToUserResult(UserInfo user, UserExt detail) {
        if (user == null) {
            return null;
        }
        UserResult result = new UserResult();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setDataOrigin(user.getCreateType());
        result.setEmailVerified(user.getEmailVerified());
        result.setMobileVerified(user.getMobileVerified());
        result.setExpireDate(user.getExpireDate());
        result.setRemark(user.getDescription());
        result.setCreateTime(dateToLocalDateTime(user.getCreateTime()));
        result.setForeignId(user.getForeignId());
        result.setUpdateTime(dateToLocalDateTime(user.getLastTime()));
        result.setStatus(user.getStatus());
        result.setAvatarUrl(user.getAvatarUrl());
        result.setEmail(user.getEmail());
        result.setNickName(user.getNickName());
        result.setLastAuthIp(user.getLastAuthIp());
        result.setLastAuthTime(dateToLocalDateTime(user.getLastAuthTime()));
        result.setFullName(user.getFullName());
        if (detail != null) {
            result.setIdCard(detail.getIdNum());
            result.setAddress(detail.getAddress1());
        }
        result.setAuthTotal(user.getAuthTotal() == null ? "0" : String.valueOf(user.getAuthTotal()));
        return result;
    }

    /**
     * 用户详情修改入参转换用户详情实体
     *
     * @param param {@link UserUpdateParam}
     * @return {@link UserExt}
     */
    public UserExt userUpdateParamConvertToUserDetailsEntity(UserUpdateParam param) {
        if (param == null) {
            return null;
        }
        UserExt detail = new UserExt();
        detail.setId(param.getId());
        detail.setIdNum(param.getIdCard());
        detail.setAddress1(param.getAddress());
        return detail;
    }

    /**
     * 创建用户入参转用户详情
     *
     * @param param {@link  UserCreateParam}
     * @return {@link  UserExt}
     */
    public UserExt userCreateParamConvertToUserDetailEntity(UserCreateParam param) {
        if (param == null) {
            return null;
        }
        return new UserExt();
    }

    /**
     * 用户实体转换为用户分页结果
     *
     * @param po {@link UserInfoDto}
     * @return {@link UserListResult}
     */
    public UserListResult userDtoConvertToUserListResult(UserInfoDto po) {
        if (po == null) {
            return null;
        }
        UserListResult result = new UserListResult();
        result.setId(po.getId());
        result.setUsername(po.getUsername());
        result.setFullName(po.getFullName());
        result.setEmail(po.getEmail());
        result.setAvatarUrl(po.getAvatarUrl());
        result.setStatus(po.getStatus());
        result.setEmailVerified(po.getEmailVerified());
        result.setMobileVerified(po.getMobileVerified());
        result.setAuthTotal(po.getAuthTotal());
        result.setDataOrigin(po.getCreateType());
        result.setLastAuthTime(dateToLocalDateTime(po.getLastAuthTime()));
        result.setOrgDisplayPath(po.getOrgDisplayPath());
        result.setLastUpdatePasswordTime(po.getLastResetTime());
        return result;
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
