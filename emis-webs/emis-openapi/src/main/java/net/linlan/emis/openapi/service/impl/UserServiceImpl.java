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
package net.linlan.emis.openapi.service.impl;

import java.time.LocalDate;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.manage.mail.MailMsgEventPublish;
import net.linlan.emis.manage.password.PasswordPolicyManager;
import net.linlan.emis.manage.sms.SmsMsgEventPublish;
import net.linlan.emis.openapi.constant.OpenApiStatus;
import net.linlan.emis.openapi.converter.UserConverter;
import net.linlan.emis.openapi.exception.OpenApiException;
import net.linlan.emis.openapi.pojo.query.OapiV1UserListQuery;
import net.linlan.emis.openapi.pojo.result.UserListResult;
import net.linlan.emis.openapi.pojo.result.UserResult;
import net.linlan.emis.openapi.pojo.save.UserCreateParam;
import net.linlan.emis.openapi.pojo.update.UserUpdateParam;
import net.linlan.emis.openapi.service.UserService;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.service.UserOrganRelationService;
import net.linlan.emis.support.repository.base.BaseEntity;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.support.utils.MobileNumberUtils;
import net.linlan.emis.support.validation.ValidationMobile;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.constant.SmsType;
import net.linlan.emis.uext.entity.MessageNoticeChannel;
import net.linlan.emis.uext.entity.MsgVariable;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.param.UserInfoParam;
import net.linlan.emis.user.service.UserExtService;
import net.linlan.emis.user.service.UserGroupRelationService;
import net.linlan.emis.user.service.UserInfoService;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on 2020-07-31
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * 获取用户（分页）
     *
     * @param pageModel {@link PageModel}
     * @return {@link OapiV1UserListQuery}
     */
    @Override
    public Page<UserListResult> getUserList(PageModel pageModel, OapiV1UserListQuery query) {
        UserInfoParam param = new UserInfoParam();
        if (Objects.nonNull(query)) {
            param.setUsername(query.getUsername());
            param.setFullName(query.getFullName());
            param.setEmail(query.getEmail());
            param.setMobile(query.getMobile());
            param.setCreateType(query.getDataOrigin());
            param.setInclSubOrganization(query.getInclSubOrganization());
            if (Objects.nonNull(query.getStatus())) {
                param.setStatus(query.getStatus().getKey());
            }
        }
        param.setPage(pageModel.getCurrent() + 1);
        param.setLimit(pageModel.getPageSize());
        com.github.pagehelper.Page<net.linlan.emis.user.dto.UserInfoDto> page = userInfoService
            .getPageDto(param);
        return userConverter.userDtoConvertToUserListResult(page, page.getTotal(), page.getPageNum(),
            page.getPages());
    }

    /**
     * 更改用户状态
     *
     * @param id     {@link Long}
     * @param status {@link UserStatus}
     */
    @Override
    public void changeUserStatus(String id, UserStatus status) {
        UserInfo user = userInfoService.findById(id);
        //用户不存在
        if (Objects.isNull(user)) {
            AuditContext.setContent("操作失败，用户不存在");
            log.warn(AuditContext.getContent());
            throw new OpenApiException(OpenApiStatus.USER_NOT_EXIST);
        }
        AuditContext.setTarget(
            Target.builder().id(id).name(user.getUsername()).type(TargetType.USER).build());
        userInfoService.updateUserStatus(id, status);
    }

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserCreateParam param) {
        if (StringUtils.isBlank(param.getMobile()) && StringUtils.isBlank(param.getEmail())) {
            throw new OpenApiException(OpenApiStatus.NO_MOBILE_OR_EMAIL);
        }
        //手机号
        if (StringUtils.isNotEmpty(param.getMobile())) {
            if (!MobileNumberUtils.getMobileNumber(param.getMobile())
                .matches(ValidationMobile.PHONE_REGEXP)) {
                throw new OpenApiException(OpenApiStatus.MOBILE_NOT_VALID);
            }
            Boolean validityMobile = userParamCheck(CheckValidityType.PHONE, param.getMobile(),
                null);
            if (!validityMobile) {
                throw new OpenApiException(OpenApiStatus.MOBILE_ALREADY_EXIST);
            }
        }
        //邮箱
        if (StringUtils.isNotEmpty(param.getEmail())) {
            Boolean validityEmail = userParamCheck(CheckValidityType.EMAIL, param.getEmail(), null);
            if (!validityEmail) {
                throw new OpenApiException(OpenApiStatus.EMAIL_ALREADY_EXIST);
            }
        }
        Boolean validityUsername = userParamCheck(CheckValidityType.USERNAME, param.getUsername(),
            null);
        if (!validityUsername) {
            throw new OpenApiException(OpenApiStatus.USERNAME_ALREADY_EXIST);
        }
        //用户信息
        UserInfo user = userConverter.userCreateParamConvertToUserEntity(param);
        // 校验密码
        passwordPolicyManager.validate(user, param.getPassword());
        // 保存用户信息
        userInfoService.save(user);
        //用户详情
        UserExt detail = userConverter.userCreateParamConvertToUserDetailEntity(param);
        detail.setId(user.getId());
        userExtService.save(detail);
        //用户组织关联关系
        UserOrganRelation member = new UserOrganRelation();
        member.setOrganId(param.getOrganId());
        member.setUserId(user.getId());
        userOrganRelationService.save(member);
        AuditContext.setTarget(
            Target.builder().id(user.getId()).name(user.getUsername()).type(TargetType.USER)
                .build(),
            Target.builder().id(detail.getId()).name(user.getUsername())
                .type(TargetType.USER_DETAIL).build());
        // 发送短信和邮件的欢迎信息（密码通知）
        UserCreateParam.PasswordInitializeConfig passwordInitializeConfig = param
            .getPasswordInitializeConfig();
        if (Objects.nonNull(passwordInitializeConfig)
            && Objects.nonNull(passwordInitializeConfig.getEnableNotice())
            && org.apache.commons.collections4.CollectionUtils
                .isNotEmpty(passwordInitializeConfig.getNoticeChannels())) {
            if (passwordInitializeConfig.getEnableNotice()) {
                List<MessageNoticeChannel> channels = passwordInitializeConfig.getNoticeChannels();
                if (channels.contains(MessageNoticeChannel.MAIL)) {
                    Map<String, Object> parameter = new HashMap<>(16);
                    parameter.put(MsgVariable.PASSWORD, param.getPassword());
                    mailMsgEventPublish.publish(MailType.WELCOME_MAIL, user.getEmail(), parameter);
                }
                if (channels.contains(MessageNoticeChannel.SMS)) {
                    LinkedHashMap<String, String> parameter = new LinkedHashMap<>();
                    parameter.put(SmsMsgEventPublish.USERNAME, user.getUsername());
                    parameter.put(MsgVariable.PASSWORD, param.getPassword());
                    smsMsgEventPublish.publish(SmsType.WELCOME_SMS, user.getMobile(), parameter);
                }
            }
        }
    }

    /**
     * 根据ID查询用户
     *
     * @param id {@link String}
     * @return {@link UserResult}
     */
    @Override
    public UserResult getUser(String id) {
        //查询
        UserInfo userEntity = userInfoService.findById(id);
        UserExt detail = userExtService.findById(id);
        //映射
        UserResult userResult = userConverter.entityConvertToUserResult(userEntity, detail);
        if (Objects.nonNull(userEntity) && StringUtils.isNotEmpty(userEntity.getMobile())) {
            StringBuilder mobileAreaCode = new StringBuilder(
                userEntity.getMobileAreaCode().replace(MobileNumberUtils.PLUS_SIGN, ""));
            mobileAreaCode.insert(0, MobileNumberUtils.PLUS_SIGN);
            userResult.setMobile(mobileAreaCode + userEntity.getMobile());
        }
        return userResult;
    }

    /**
     * 更新用户
     *
     * @param param {@link UserUpdateParam}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateParam param) {
        if (StringUtils.isNotBlank(param.getMobile())) {
            String mobileNumber = MobileNumberUtils.getMobileNumber(param.getMobile());
            if (!mobileNumber.matches(ValidationMobile.PHONE_REGEXP)) {
                throw new OpenApiException(OpenApiStatus.MOBILE_NOT_VALID);
            }
            Boolean validityMobile = userParamCheck(CheckValidityType.PHONE, param.getMobile(),
                param.getId());
            if (!validityMobile) {
                throw new OpenApiException(OpenApiStatus.MOBILE_ALREADY_EXIST);
            }
        }
        if (StringUtils.isNotBlank(param.getEmail())) {
            Boolean validityEmail = userParamCheck(CheckValidityType.EMAIL, param.getEmail(),
                param.getId());
            if (!validityEmail) {
                throw new OpenApiException(OpenApiStatus.EMAIL_ALREADY_EXIST);
            }
        }
        //用户信息
        UserInfo toUserEntity = userConverter.userUpdateParamConvertToUserEntity(param);
        UserInfo user = getUserEntity(param.getId());
        BeanUtils.merge(toUserEntity, user, BaseEntity.LAST_MODIFIED_BY,
            BaseEntity.LAST_MODIFIED_TIME);
        //如果更改密码到期时间，修改为启用
        if (UserStatus.EXPIRED_LOCKED.getKey().equals(user.getStatus())) {
            if (Objects.nonNull(toUserEntity.getExpireDate())
                && toUserEntity.getExpireDate().isAfter(LocalDate.now())) {
                user.setStatus(UserStatus.ENABLED.getKey());
            }
        }
        userInfoService.update(user);
        //用户详情
        UserExt detail = userExtService.findById(param.getId());
        boolean newDetail = Objects.isNull(detail);
        if (newDetail) {
            detail = new UserExt();
            detail.setId(user.getId());
        }
        UserExt toUserDetailsEntity = userConverter
            .userUpdateParamConvertToUserDetailsEntity(param);
        toUserDetailsEntity.setId(detail.getId());
        BeanUtils.merge(toUserDetailsEntity, detail, BaseEntity.LAST_MODIFIED_BY,
            BaseEntity.LAST_MODIFIED_TIME);
        if (newDetail) {
            userExtService.save(detail);
        } else {
            userExtService.update(detail);
        }
        AuditContext.setTarget(
            Target.builder().id(user.getId()).name(user.getUsername()).type(TargetType.USER)
                .build(),
            Target.builder().id(detail.getId()).name(user.getUsername())
                .type(TargetType.USER_DETAIL).build());
    }

    /**
     * 删除用户
     *
     * @param id {@link java.io.Serializable}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(String id) {
        UserInfo user = userInfoService.findById(id);
        //管理员不存在
        if (Objects.isNull(user)) {
            AuditContext.setContent("删除失败，用户不存在");
            log.warn(AuditContext.getContent());
            throw new OpenApiException(OpenApiStatus.USER_NOT_EXIST);
        }
        //删除
        userInfoService.deleteById(id);
        //删除用户详情
        userExtService.deleteById(id);
        //删除组织用户关联关系
        userOrganRelationService.deleteByUserIds(new String[] { id });
        //删除用户组用户详情
        userGroupRelationService.deleteAllByUserIdsIn(new String[] { id });
    }

    @Override
    public String getUserIdByParams(String foreignId, String mobileNumber, String email,
                                    String username) {
        net.linlan.emis.user.dto.UserInfoDto user;
        if (Objects.nonNull(foreignId)) {
            user = userInfoService.getByForeignId(foreignId);
        } else if (Objects.nonNull(mobileNumber)) {
            user = userInfoService.getByMobile(mobileNumber);
        } else if (Objects.nonNull(email)) {
            user = userInfoService.getByEmail(email);
        } else if (Objects.nonNull(username)) {
            user = userInfoService.getByUsername(username);
        } else {
            throw new OpenApiException(OpenApiStatus.INVALID_PARAMETER);
        }
        if (Objects.nonNull(user)) {
            return user.getId();
        }
        throw new OpenApiException(OpenApiStatus.USER_NOT_EXIST);
    }

    /**
     * 获取用户信息
     *
     * @param id {@link String}
     * @return {@link UserInfo}
     */
    private UserInfo getUserEntity(String id) {
        UserInfo user = userInfoService.findById(id);
        if (Objects.isNull(user)) {
            throw new OpenApiException(OpenApiStatus.USER_NOT_EXIST);
        }
        return user;
    }

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean}
     */
    public Boolean userParamCheck(CheckValidityType type, String value, String id) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        UserInfo entity = new UserInfo();
        boolean result = false;
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            entity = userInfoService.findById(id);
            if (Objects.isNull(entity)) {
                entity = new UserInfo();
            }
        }
        //手机号
        if (CheckValidityType.PHONE.equals(type)) {
            try {
                //手机号未修改
                if (StringUtils.equals(value.replace(MobileNumberUtils.PLUS_SIGN, ""),
                    entity.getMobileAreaCode() + entity.getMobile())) {
                    return true;
                }
                Phonenumber.PhoneNumber mobileNumber = PhoneNumberUtil.getInstance().parse(value,
                    "CN");

                result = Objects.isNull(userInfoService
                    .getByMobile(String.valueOf(mobileNumber.getNationalNumber())));
            } catch (NumberParseException e) {
                log.error("校验手机号发生异常", e);
                throw new OpenApiException(OpenApiStatus.MOBILE_NOT_VALID);
            }
        }
        //邮箱
        if (CheckValidityType.EMAIL.equals(type)) {
            if (StringUtils.equals(entity.getEmail(), value)) {
                return true;
            }
            result = Objects.isNull(userInfoService.getByEmail(value));
        }
        //用户名
        if (CheckValidityType.USERNAME.equals(type)) {
            if (StringUtils.equals(entity.getUsername(), value)) {
                return true;
            }
            result = Objects.isNull(userInfoService.getByUsername(value));
        }
        return result;
    }

    /**
     * 用户数据映射器
     */
    private final UserConverter                userConverter;

    /**
     * 用户信息Service
     */
    private final UserInfoService              userInfoService;

    /**
     * 用户详情Service
     */
    private final UserExtService               userExtService;

    /**
     * 组织成员Service
     */
    private final UserOrganRelationService     userOrganRelationService;

    /**
     * 用户组成员Service
     */
    private final UserGroupRelationService     userGroupRelationService;

    /**
     * 邮件消息发布
     */
    private final MailMsgEventPublish          mailMsgEventPublish;

    /**
     * 短信消息发送
     */
    private final SmsMsgEventPublish           smsMsgEventPublish;

    /**
     * PasswordPolicyManager
     */
    private final PasswordPolicyManager<UserInfo> passwordPolicyManager;

    public UserServiceImpl(UserConverter userConverter, UserInfoService userInfoService,
                           UserExtService userExtService,
                           UserOrganRelationService userOrganRelationService,
                           UserGroupRelationService userGroupRelationService,
                           MailMsgEventPublish mailMsgEventPublish,
                           SmsMsgEventPublish smsMsgEventPublish,
                           PasswordPolicyManager<UserInfo> passwordPolicyManager) {
        this.userConverter = userConverter;
        this.userInfoService = userInfoService;
        this.userExtService = userExtService;
        this.userOrganRelationService = userOrganRelationService;
        this.userGroupRelationService = userGroupRelationService;
        this.mailMsgEventPublish = mailMsgEventPublish;
        this.smsMsgEventPublish = smsMsgEventPublish;
        this.passwordPolicyManager = passwordPolicyManager;
    }
}
