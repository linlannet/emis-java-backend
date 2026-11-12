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
package net.linlan.emis.console.service.account.impl;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.service.AppAccessPolicyService;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.AuditInfo;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.service.AuditInfoService;
import net.linlan.emis.console.converter.account.UserConverter;
import net.linlan.emis.console.pojo.query.account.UserListNotInGroupQuery;
import net.linlan.emis.console.pojo.query.account.UserListQuery;
import net.linlan.emis.console.pojo.result.account.BatchUserResult;
import net.linlan.emis.console.pojo.result.account.UserListResult;
import net.linlan.emis.console.pojo.result.account.UserLoginAuditListResult;
import net.linlan.emis.console.pojo.result.account.UserResult;
import net.linlan.emis.console.pojo.save.account.UserCreateParam;
import net.linlan.emis.console.pojo.update.account.ResetPasswordParam;
import net.linlan.emis.console.pojo.update.account.UserUpdateParam;
import net.linlan.emis.console.service.account.UserService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.manage.mail.MailMsgEventPublish;
import net.linlan.emis.manage.password.PasswordPolicyManager;
import net.linlan.emis.manage.sms.SmsMsgEventPublish;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.service.UserOrganRelationService;
import net.linlan.emis.support.exception.BadParamsException;
import net.linlan.emis.support.exception.InfoValidityFailException;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.support.utils.MobileNumberUtils;
import net.linlan.emis.support.validation.ValidationMobile;
import net.linlan.emis.ubind.service.UserIdpBindService;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.constant.SmsType;
import net.linlan.emis.uext.entity.MessageNoticeChannel;
import net.linlan.emis.uext.entity.MsgVariable;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserHistoryPassword;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.param.UserInfoParam;
import net.linlan.emis.user.service.UserExtService;
import net.linlan.emis.user.service.UserGroupRelationService;
import net.linlan.emis.user.service.UserHistoryPasswordService;
import net.linlan.emis.user.service.UserInfoService;
import static net.linlan.emis.manage.sms.SmsMsgEventPublish.USERNAME;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileNumber;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-07-31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * 获取用户（分页）
     *
     * @param pageModel {@link PageModel}
     * @return {@link UserListQuery}
     */
    @Override
    public Page<UserListResult> getUserList(PageModel pageModel, UserListQuery query) {
        UserInfoParam param = new UserInfoParam();
        param.setUsername(query.getUsername());
        param.setFullName(query.getFullName());
        param.setEmail(query.getEmail());
        param.setMobile(query.getMobile());
        param.setStatus(query.getStatus() == null ? null : query.getStatus().getKey());
        param.setCreateType(query.getDataOrigin());
        param.setInclSubOrganization(query.getInclSubOrganization());
        param.setPage(pageModel.getCurrent());
        param.setLimit(pageModel.getPageSize());
        com.github.pagehelper.Page<UserInfoDto> list = userInfoService.getPageDto(param);
        return userConverter.userPoConvertToUserListResult(list);
    }

    /**
     * 获取用户列表不在当前组
     *
     * @param model {@link  PageModel}
     * @param query {@link UserListNotInGroupQuery }
     * @return {@link  }
     */
    @Override
    public Page<UserListResult> getUserListNotInGroup(PageModel model,
                                                      UserListNotInGroupQuery query) {
        UserInfoParam param = new UserInfoParam();
        param.setFullName(query.getKeyword());
        param.setPage(model.getCurrent());
        param.setLimit(model.getPageSize());
        com.github.pagehelper.Page<UserInfoDto> list = userInfoService.getPageDto(param);
        return userConverter.userPoConvertToUserListResult(list);
    }

    /**
     * 重置密码
     *
     * @param param       {@link ResetPasswordParam} 用户修改入参
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetUserPassword(ResetPasswordParam param) {
        //additionalContent
        UserInfo userEntity = userInfoService.findById(param.getId());
        //用户不存在
        if (userEntity == null) {
            AuditContext.setContent("操作失败，用户不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        // 重置用户密码
        String password = new String(
            Base64.getUrlDecoder().decode(param.getPassword().getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);
        // 校验密码
        passwordPolicyManager.validate(userEntity, password);
        String encryptionPassword = passwordEncoder.encode(password);
        userInfoService.updatePassword(param.getId(), encryptionPassword);
        //保存历史密码
        UserHistoryPassword userHistoryPassword = new UserHistoryPassword();
        userHistoryPassword.setUserId(String.valueOf(param.getId()));
        userHistoryPassword.setPassword(encryptionPassword);
        userHistoryPassword.setChangeTime(new java.util.Date());
        userHistoryPasswordService.save(userHistoryPassword);
        AuditContext.setTarget(Target.builder().id(param.getId()).name(userEntity.getUsername())
            .type(TargetType.USER).build());

        ResetPasswordParam.PasswordResetConfig passwordResetConfig = param.getPasswordResetConfig();
        if (Objects.nonNull(passwordResetConfig) && passwordResetConfig.getEnableNotice()
            && org.apache.commons.collections4.CollectionUtils
                .isNotEmpty(passwordResetConfig.getNoticeChannels())) {
            // 重置密码成功通知
            if (passwordResetConfig.getNoticeChannels().contains(MessageNoticeChannel.MAIL)) {
                Map<String, Object> parameter = new HashMap<>(16);
                parameter.put(MsgVariable.PASSWORD, password);
                mailMsgEventPublish.publish(MailType.RESET_PASSWORD_CONFIRM, userEntity.getEmail(),
                    parameter);
            }
            if (passwordResetConfig.getNoticeChannels().contains(MessageNoticeChannel.SMS)) {
                LinkedHashMap<String, String> parameter = new LinkedHashMap<>();
                parameter.put(USERNAME, userEntity.getUsername());
                parameter.put(MsgVariable.PASSWORD, password);
                smsMsgEventPublish.publish(SmsType.RESET_PASSWORD_SUCCESS, userEntity.getMobile(),
                    parameter);
            }
        }

        return true;
    }

    /**
     * 更改用户状态
     *
     * @param id     {@link Long}
     * @param status {@link UserStatus}
     * @return {@link Boolean}
     */
    @Override
    public boolean changeUserStatus(String id, UserStatus status) {
        UserInfo optional = userInfoService.findById(id);
        //用户不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，用户不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        AuditContext.setTarget(Target.builder().id(id).name(optional.getUsername())
            .type(TargetType.USER).build());
        return userInfoService.updateUserStatus(id, status) > 0;
    }

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createUser(UserCreateParam param) {
        if (StringUtils.isBlank(param.getMobile()) && StringUtils.isBlank(param.getEmail())) {
            throw new SupportException("手机号或邮箱至少填写一个", HttpStatus.BAD_REQUEST);
        }
        //手机号
        if (StringUtils.isNotEmpty(param.getMobile())) {
            if (!getMobileNumber(param.getMobile()).matches(ValidationMobile.PHONE_REGEXP)) {
                throw new InfoValidityFailException("手机号格式错误");
            }
            Boolean validityMobile = userParamCheck(CheckValidityType.PHONE, param.getMobile(),
                null);
            if (!validityMobile) {
                throw new InfoValidityFailException("手机号已存在");
            }
        }
        //邮箱
        if (StringUtils.isNotEmpty(param.getEmail())) {
            Boolean validityEmail = userParamCheck(CheckValidityType.EMAIL, param.getEmail(), null);
            if (!validityEmail) {
                throw new InfoValidityFailException("邮箱已存在");
            }
        }
        Boolean validityUsername = userParamCheck(CheckValidityType.USERNAME, param.getUsername(),
            null);
        if (!validityUsername) {
            throw new InfoValidityFailException("用户名已存在");
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
                    parameter.put(USERNAME, user.getUsername());
                    parameter.put(MsgVariable.PASSWORD, param.getPassword());
                    smsMsgEventPublish.publish(SmsType.WELCOME_SMS, user.getMobile(), parameter);
                }
            }
        }
        return true;
    }

    /**
     * 通过外部ID获取用户
     *
     * @param id {@link String}
     * @return {@link UserInfo}
     */
    @Override
    public UserInfo getByForeignId(String id) {
        return userInfoService.getByForeignId(id);
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
        UserExt detail = getUserExt(id);
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
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserUpdateParam param) {
        if (StringUtils.isNotBlank(param.getMobile())) {
            String mobileNumber = getMobileNumber(param.getMobile());
            if (!mobileNumber.matches(ValidationMobile.PHONE_REGEXP)) {
                throw new InfoValidityFailException("手机号格式错误");
            }
            Boolean validityMobile = userParamCheck(CheckValidityType.PHONE, param.getMobile(),
                param.getId());
            if (!validityMobile) {
                throw new InfoValidityFailException("手机号已存在");
            }
        }
        if (StringUtils.isNotBlank(param.getEmail())) {
            Boolean validityEmail = userParamCheck(CheckValidityType.EMAIL, param.getEmail(),
                param.getId());
            if (!validityEmail) {
                throw new InfoValidityFailException("邮箱已存在");
            }
        }
        //用户信息
        UserInfo toUserEntity = userConverter.userUpdateParamConvertToUserEntity(param);
        UserInfo user = getUserEntity(param.getId());
        BeanUtils.merge(toUserEntity, user, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        //如果更改密码到期时间，修改为启用
        if (UserStatus.EXPIRED_LOCKED.getKey().equals(user.getStatus())) {
            if (toUserEntity.getExpireDate().isAfter(LocalDate.now())) {
                user.setStatus(UserStatus.ENABLED.getKey());
            }
        }
        userInfoService.update(user);
        //用户详情
        UserExt detail = getUserExt(param.getId());
        if (detail == null) {
            detail = new UserExt();
            detail.setId(user.getId());
        }
        UserExt toUserDetailsEntity = userConverter
            .userUpdateParamConvertToUserDetailsEntity(param);
        toUserDetailsEntity.setId(detail.getId());
        BeanUtils.merge(toUserDetailsEntity, detail, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        if (detail.getId() == null) {
            userExtService.save(detail);
        } else {
            userExtService.update(detail);
        }
        AuditContext.setTarget(
            Target.builder().id(user.getId()).name(user.getUsername()).type(TargetType.USER)
                .build(),
            Target.builder().id(detail.getId()).name(user.getUsername())
                .type(TargetType.USER_DETAIL).build());
        return true;
    }

    /**
     * 删除用户
     *
     * @param id {@link Serializable}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(String id) {
        UserInfo optional = userInfoService.findById(id);
        //管理员不存在
        if (optional == null) {
            AuditContext.setContent("删除失败，用户不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        //删除
        userInfoService.deleteById(id);
        //删除用户详情
        deleteUserExt(new String[]{id});
        //删除应用访问授权
        appAccessPolicyService.deleteAllBySubjectId(id);
        //删除组织用户关联关系
        userOrganRelationService.deleteByUserIds(new String[]{id});
        //删除用户组用户详情
        userGroupRelationService.deleteByUserId(id);
        return true;
    }

    /**
     * 批量删除用户
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchDeleteUser(String[] ids) {
        //删除用户
        userInfoService.deleteByIds(ids);
        //用户&身份提供商绑定
        userIdpBindService.deleteAllByUserIdsIn(ids);
        //删除用户详情
        deleteUserExt(ids);
        //删除组织用户关系
        userOrganRelationService.deleteByUserIds(ids);
        //删除用户组关系
        for (String id : ids) {
            userGroupRelationService.deleteByUserId(id);
        }
        return true;
    }

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean}
     */
    @Override
    public Boolean userParamCheck(CheckValidityType type, String value, String id) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        UserInfo entity = new UserInfo();
        boolean result = false;
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            entity = userInfoService.findById(id);
            if (entity == null) {
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
                List<UserInfo> list = userInfoService.getList(new StringMap()
                    .put("mobile", String.valueOf(mobileNumber.getNationalNumber()))
                    .put("mobileAreaCode", String.valueOf(mobileNumber.getCountryCode())).map());
                result = list.isEmpty();
            } catch (NumberParseException e) {
                log.error("校验手机号发生异常", e);
                throw new SupportException("校验手机号发生异常");
            }
        }
        //邮箱
        if (CheckValidityType.EMAIL.equals(type)) {
            if (StringUtils.equals(entity.getEmail(), value)) {
                return true;
            }
            result = userInfoService.getByEmail(value) == null;
        }
        //用户名
        if (CheckValidityType.USERNAME.equals(type)) {
            if (StringUtils.equals(entity.getUsername(), value)) {
                return true;
            }
            result = userInfoService.getByUsername(value) == null;
        }
        return result;
    }

    /**
     * 查看用户登录日志
     *
     * @param id {@link Long}
     * @param pageModel {@link PageModel}
     * @return {@link   List}
     */
    @Override
    public Page<UserLoginAuditListResult> findUserLoginAuditList(String id, PageModel pageModel) {
        PageHelper.startPage(pageModel.getCurrent(), pageModel.getPageSize());
        List<AuditInfo> auditList = auditInfoService
            .getList(new StringMap().put("creatorId", id).map());
        long total = auditList instanceof com.github.pagehelper.Page
                     ? ((com.github.pagehelper.Page<?>) auditList).getTotal() : auditList.size();
        return userConverter.entityConvertToAuditListResult(auditList, total, pageModel);

        //        //查询入参转查询条件，全文检索查询方法
        //        NativeSearchQuery nsq = userConverter.auditListRequestConvertToNativeSearchQuery(id, pageModel);
        //        //查询列表
        //        SearchHits<AuditElasticSearchEntity> search = elasticsearchRestTemplate.search(nsq,
        //                AuditElasticSearchEntity.class, IndexCoordinates.of(getAuditIndexPrefix(supportProperties.getDemo().isOpen()) + "*"));
        //        //结果转返回结果
        //        return userConverter.searchHitsConvertToAuditListResult(search, pageModel);
    }

    /**
     * 批量获取用户信息
     *
     * @param ids {@link List}
     * @return {@link List}
     */
    @Override
    public List<BatchUserResult> batchGetUser(List<String> ids) {
        List<UserInfo> list = new ArrayList<>();
        for (String id : ids) {
            UserInfo user = userInfoService.findById(id);
            if (user != null) {
                list.add(user);
            }
        }
        return userConverter.entityConvertToBatchGetUserResult(list);
    }

    /**
     * 获取用户详情
     *
     * @param userId {@link String}
     * @return {@link UserExt}
     */
    private UserExt getUserExt(String userId) {
        List<UserExt> list = userExtService.getList(new StringMap().put("userId", userId).map());
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 删除用户详情
     *
     * @param userIds {@link String[]}
     */
    private void deleteUserExt(String[] userIds) {
        List<UserExt> list = userExtService.findAllByUserIdsIn(userIds);
        if (!list.isEmpty()) {
            userExtService.deleteByIds(list.stream().map(UserExt::getId).toArray(String[]::new));
        }
    }

    /**
     * 获取用户信息
     *
     * @param id {@link String}
     * @return {@link UserInfo}
     */
    private UserInfo getUserEntity(String id) {
        UserInfo user = userInfoService.findById(id);
        if (user == null) {
            throw new BadParamsException("用户不存在");
        }
        return user;
    }

    /**
     * 用户数据映射器
     */
    private final UserConverter                     userConverter;

    /**
     * 用户信息
     */
    private final UserInfoService                   userInfoService;

    /**
     * 用户&身份提供商绑定
     */
    private final UserIdpBindService                userIdpBindService;

    /**
     * 应用访问授权
     */
    private final AppAccessPolicyService            appAccessPolicyService;

    /**
     * password encoder
     */
    private final PasswordEncoder                   passwordEncoder;

    /**
     * 组织成员
     */
    private final UserOrganRelationService          userOrganRelationService;

    /**
     * 用户组关系
     */
    private final UserGroupRelationService          userGroupRelationService;

    /**
     * 用户详情
     */
    private final UserExtService                    userExtService;

    /**
     * 修改密码历史
     */
    private final UserHistoryPasswordService        userHistoryPasswordService;

    /**
     * 邮件消息发布
     */
    private final MailMsgEventPublish               mailMsgEventPublish;

    /**
     * 短信消息发送
     */
    private final SmsMsgEventPublish                smsMsgEventPublish;

    /**
     * PasswordPolicyManager
     */
    private final PasswordPolicyManager<UserInfo> passwordPolicyManager;

    /**
     * 审计日志
     */
    private final AuditInfoService                  auditInfoService;

}
