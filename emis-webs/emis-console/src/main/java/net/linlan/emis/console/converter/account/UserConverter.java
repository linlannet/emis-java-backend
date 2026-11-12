/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.converter.account;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.service.AppInfoService;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.AuditInfo;
import net.linlan.emis.audit.entity.GeoLocation;
import net.linlan.emis.audit.entity.UserAgent;
import net.linlan.emis.audit.event.type.PortalEventType;
import net.linlan.emis.console.pojo.result.account.BatchUserResult;
import net.linlan.emis.console.pojo.result.account.UserListResult;
import net.linlan.emis.console.pojo.result.account.UserLoginAuditListResult;
import net.linlan.emis.console.pojo.result.account.UserResult;
import net.linlan.emis.console.pojo.save.account.UserCreateParam;
import net.linlan.emis.console.pojo.update.account.UserUpdateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.userdetails.DataOrigin;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.utils.SpringContextUtils;
import static net.linlan.emis.support.utils.ImageAvatarUtils.*;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileAreaCode;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileNumber;

/**
 * 用户映射
 *
 * @author Linlan
 * Created on  2020/8/14 21:45
 */
@Mapper(componentModel = "spring")
public interface UserConverter {
    final Logger logger = LoggerFactory.getLogger(UserConverter.class);

    /**
     * 用户实体转换为用户分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<UserListResult> userPoConvertToUserListResult(com.github.pagehelper.Page<UserInfoDto> page) {
        Page<UserListResult> result = new Page<>();
        List<UserListResult> list = new ArrayList<>();
        if (!page.isEmpty()) {
            for (UserInfoDto user : page) {
                UserListResult userListResult = userPoConvertToUserListResult(user);
                if (!StringUtils.hasText(userListResult.getAvatarUrl())) {
                    userListResult.setAvatarUrl(bufferedImageToBase64(generateAvatarImg()));
                } else {
                    userListResult.setAvatarUrl(userListResult.getAvatarUrl());
                }
                if (StringUtils.hasText(user.getMobile())) {
                    userListResult.setMobile((StringUtils.hasText(user.getMobileAreaCode())
                        ? "+" + user.getMobileAreaCode()
                        : "") + user.getMobile());
                }
                list.add(userListResult);
            }
        }
        //@formatter:off
        result.setPagination(Page.Pagination.builder()
                .total(page.getTotal())
                .totalPages(page.getPages())
                .current(page.getPageNum())
                .build());
        //@formatter:on
        result.setList(list);
        return result;
    }

    /**
     * 用户创建参数转换为用户实体
     *
     * @param param {@link UserCreateParam}
     * @return {@link UserInfo}
     */
    default UserInfo userCreateParamConvertToUserEntity(UserCreateParam param) {
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
            Objects.isNull(param.getExpireDate()) ? java.time.LocalDate.of(2116, 12, 31)
                : param.getExpireDate());
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
    default UserInfo userUpdateParamConvertToUserEntity(UserUpdateParam param) {
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
        userEntity.setStatus(param.getStatus() == null ? null : param.getStatus().getKey());
        userEntity.setExpireDate(param.getExpireDate());
        return userEntity;
    }

    /**
     * 实体转为用户详情返回
     *
     * @param user   {@link UserInfo}
     * @param detail {@link UserExt}
     * @return {@link UserInfo} 用户详情
     */
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "dataOrigin", source = "user.createType")
    @Mapping(target = "emailVerified", source = "user.emailVerified")
    @Mapping(target = "mobileVerified", source = "user.mobileVerified")
    @Mapping(target = "expireDate", source = "user.expireDate")
    @Mapping(target = "remark", source = "user.description")
    @Mapping(target = "createTime", expression = "java(user.getCreateTime() == null ? null : java.time.LocalDateTime.ofInstant(user.getCreateTime().toInstant(), java.time.ZoneId.systemDefault()))")
    @Mapping(target = "foreignId", source = "user.foreignId")
    @Mapping(target = "status", source = "user.status")
    @Mapping(target = "mobile", ignore = true)
    @Mapping(target = "avatarUrl", source = "user.avatarUrl")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "nickName", source = "user.nickName")
    @Mapping(target = "lastAuthIp", source = "user.lastAuthIp")
    @Mapping(target = "lastAuthTime", expression = "java(user.getLastAuthTime() == null ? null : java.time.LocalDateTime.ofInstant(user.getLastAuthTime().toInstant(), java.time.ZoneId.systemDefault()))")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "idCard", source = "detail.idNum")
    @Mapping(target = "address", source = "detail.address1")
    @Mapping(target = "authTotal", source = "user.authTotal", defaultValue = "0")
    UserResult entityConvertToUserResult(UserInfo user, UserExt detail);

    /**
     * 用户详情修改入参转换用户详情实体
     *
     * @param param {@link UserUpdateParam}
     * @return {@link UserExt}
     */

    @Mapping(target = "idType", ignore = true)
    UserExt userUpdateParamConvertToUserDetailsEntity(UserUpdateParam param);

    /**
     * 创建用户入参转用户详情
     *
     * @param param {@link  UserCreateParam}
     * @return {@link  UserExt}
     */

    @Mapping(target = "idType", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserExt userCreateParamConvertToUserDetailEntity(UserCreateParam param);

    /**
     * 审计列表转换为用户登录日志列表
     *
     * @param auditList {@link List}
     * @param total     {@link long}
     * @param page      {@link PageModel}
     * @return {@link Page}
     */
    default Page<UserLoginAuditListResult> entityConvertToAuditListResult(List<AuditInfo> auditList,
                                                                         long total,
                                                                         PageModel page) {
        List<UserLoginAuditListResult> list = new ArrayList<>();
        auditList.forEach(audit -> {
            UserLoginAuditListResult result = new UserLoginAuditListResult();
            result.setId(audit.getId());
            //单点登录
            if (audit.getEventType().getCode().equals(PortalEventType.APP_SSO.getCode())) {
                AppInfoService appInfoService = SpringContextUtils.getBean(AppInfoService.class);
                AppInfo app = appInfoService.findById(audit.getTarget().get(0).getId());
                result.setAppName(app == null ? null : app.getName());
            }
            //登录门户
            if (audit.getEventType().getCode().equals(PortalEventType.LOGIN_PORTAL.getCode())) {
                result.setAppName(TargetType.PORTAL.getValue());
            }
            UserAgent userAgent = audit.getUserAgent();
            GeoLocation geoLocation = audit.getGeoLocation();
            result.setEventTime(audit.getEventTime());
            result.setClientIp(geoLocation.getIp());
            result.setLocation(geoLocation.getCityName());
            result.setBrowser(userAgent.getBrowser());
            result.setPlatform(userAgent.getPlatform() + " " + userAgent.getPlatformVersion());
            result.setEventStatus(audit.getEventStatus());
            list.add(result);
        });
        //@formatter:off
        Page<UserLoginAuditListResult> result = new Page<>();
        result.setPagination(Page.Pagination.builder()
                .total(total)
                .totalPages(page.getPageSize() == 0 ? 0 : (int) Math.ceil((double) total / page.getPageSize()))
                .current(page.getCurrent() + 1)
                .build());
        result.setList(list);
        //@formatter:on
        return result;
    }

    /**
     * 用户实体转换为用户分页结果
     *
     * @param po {@link UserInfo}
     * @return {@link UserListResult}
     */
    @Mapping(target = "status", source = "status")
    @Mapping(target = "mobile", ignore = true)
    @Mapping(target = "authTotal", defaultValue = "0L", source = "authTotal")
    @Mapping(target = "dataOrigin", source = "dataOrigin")
    UserListResult userPoConvertToUserListResult(UserInfoDto po);

    /**
     * 实体转换为批量获取用户结果
     *
     * @param organization {@link UserInfo}
     * @return {@link BatchUserResult}
     */
    @Mapping(target = "idCard", ignore = true)
    @Mapping(target = "address", ignore = true)
    BatchUserResult entityConvertToBatchGetUserResult(UserInfo organization);

    /**
     * 实体转换为批量获取用户结果
     *
     * @param list {@link List}
     * @return {@link List}
     */
    default List<BatchUserResult> entityConvertToBatchGetUserResult(List<UserInfo> list) {
        return list.stream().map(this::entityConvertToBatchGetUserResult)
            .collect(Collectors.toList());
    }
}
