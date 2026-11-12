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
package net.linlan.emis.console.service.user.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import net.linlan.emis.console.converter.user.UserProfileConverter;
import net.linlan.emis.console.pojo.update.user.*;
import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.console.service.user.UserProfileService;
import net.linlan.emis.core.exception.user.PasswordValidatedFailException;
import net.linlan.emis.core.exception.user.UserNotFoundException;
import net.linlan.emis.manage.mail.MailUtils;
import net.linlan.emis.manage.sms.SmsMsgEventPublish;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.support.exception.BadParamsException;
import net.linlan.emis.support.exception.InfoValidityFailException;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.support.utils.MobileNumberUtils;
import net.linlan.emis.uext.OtpContextHelper;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.constant.SmsType;
import net.linlan.emis.uext.entity.MessageNoticeChannel;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.utils.http.HttpContextUtils;
import static net.linlan.emis.manage.sms.SmsMsgEventPublish.USERNAME;
import static net.linlan.emis.support.constant.EmisConstants.FORGET_PASSWORD_TOKEN_ID;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.EX000102;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;
import static net.linlan.emis.support.utils.MobileNumberUtils.isMobileValidate;

/**
 *
 * @author Linlan
 * Created on  2022/10/3 22:20
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeInfo(UpdateUserInfoRequest param) {
        //用户信息
        EmisAdminVo administrator = userProfileConverter
            .userUpdateParamConvertToAdministratorEntity(param);
        EmisAdminVo user = administratorRepository
            .findById(SecurityUtils.getCurrentUser().getId())
            .orElseThrow(() -> new RuntimeException());
        BeanUtils.merge(administrator, user, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
        administratorRepository.save(user);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changePassword(ChangePasswordRequest param) {
        //获取用户
        EmisAdminVo administrator = getCurrentUser();
        //校验旧密码
        if (!passwordEncoder.matches(param.getOldPassword(), administrator.getPassword())) {
            logger.error("用户ID: [{}] 用户名: [{}] 修改密码失败，原密码错误", administrator.getAdminId(),
                administrator.getUsername());
            throw new PasswordValidatedFailException("旧密码错误");
        }
        //修改密码
        administratorRepository.updatePassword(SecurityUtils.getCurrentUser().getId(),
            passwordEncoder.encode(param.getNewPassword()), LocalDateTime.now());
        logger.info("用户ID: [{}] 用户名: [{}] 修改密码成功", administrator.getAdminId(),
            administrator.getUsername());
        //异步下线所有用户
        removeSession(SecurityUtils.getCurrentUserName());
        //@formatter:on
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean prepareChangeMobile(PrepareChangeMobileRequest param) {
        EmisAdminVo user = validatedPassword(param.getPassword());
        // 发送短信验证码
        if (StringUtils.isNotBlank(user.getMobile())) {
            otpContextHelp.sendOtp(param.getMobile(), SmsType.UPDATE_MOBILE.getKey(),
                MessageNoticeChannel.SMS);
        } else {
            otpContextHelp.sendOtp(param.getMobile(), SmsType.BIND_MOBILE.getKey(),
                MessageNoticeChannel.SMS);
        }
        return true;
    }

    /**
     * 修改手机
     *
     * @param param {@link ChangeMobileRequest}
     * @return Boolean
     */
    @Override
    public Boolean changeMobile(ChangeMobileRequest param) {
        EmisAdminVo user = getCurrentUser();
        Boolean checkOtp;
        if (StringUtils.isNotBlank(user.getMobile())) {
            checkOtp = otpContextHelp.checkOtp(SmsType.UPDATE_MOBILE.getKey(),
                MessageNoticeChannel.SMS, param.getMobile(), param.getOtp());
        } else {
            checkOtp = otpContextHelp.checkOtp(SmsType.BIND_MOBILE.getKey(),
                MessageNoticeChannel.SMS, param.getMobile(), param.getOtp());
        }
        if (!checkOtp) {
            throw new InfoValidityFailException(EX000102.getValue());
        }
        // 校验是否已经存在
        Optional<EmisAdminVo> optionalAdministrator = administratorRepository
            .findByMobile(param.getMobile());
        if (optionalAdministrator.isPresent()
            && !user.getAdminId().equals(optionalAdministrator.get().getAdminId())) {
            throw new SupportException("系统中已存在[" + param.getMobile() + "]手机号, 请先解绑");
        }
        String id = SecurityUtils.getCurrentUser().getId();
        administratorRepository.updateByIdAndMobile(id, param.getMobile());
        // 修改手机号成功发送短信
        LinkedHashMap<String, String> parameter = Maps.newLinkedHashMap();
        parameter.put(USERNAME, user.getUsername());
        smsMsgEventPublish.publish(SmsType.BIND_MOBILE_SUCCESS, param.getMobile(), parameter);
        return true;
    }

    /**
     * 准备修改邮箱
     *
     * @param param {@link PrepareChangeEmailRequest}
     * @return {@link Boolean}
     */
    @Override
    public Boolean prepareChangeEmail(PrepareChangeEmailRequest param) {
        EmisAdminVo user = validatedPassword(param.getPassword());
        // 发送邮箱验证码
        if (StringUtils.isNotBlank(user.getEmail())) {
            otpContextHelp.sendOtp(param.getEmail(), MailType.UPDATE_BIND_MAIL.getKey(),
                MessageNoticeChannel.MAIL);
        } else {
            otpContextHelp.sendOtp(param.getEmail(), MailType.BIND_EMAIL.getKey(),
                MessageNoticeChannel.MAIL);
        }
        return true;
    }

    /**
     * 更改邮箱
     *
     * @param param {@link ChangeEmailRequest}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeEmail(ChangeEmailRequest param) {
        EmisAdminVo administrator = getCurrentUser();
        Boolean checkOtp;
        if (StringUtils.isNotBlank(administrator.getEmail())) {
            checkOtp = otpContextHelp.checkOtp(MailType.UPDATE_BIND_MAIL.getKey(),
                MessageNoticeChannel.MAIL, param.getEmail(), param.getOtp());
        } else {
            checkOtp = otpContextHelp.checkOtp(MailType.BIND_EMAIL.getKey(),
                MessageNoticeChannel.MAIL, param.getEmail(), param.getOtp());
        }
        if (!checkOtp) {
            throw new InfoValidityFailException(EX000102.getValue());
        }
        // 校验是否已经存在
        Optional<EmisAdminVo> optionalAdministrator = administratorRepository
            .findByEmail(param.getEmail());
        if (optionalAdministrator.isPresent()
            && !administrator.getAdminId().equals(optionalAdministrator.get().getAdminId())) {
            throw new SupportException("系统中已存在[" + param.getEmail() + "]邮箱, 请先解绑");
        }
        administratorRepository.updateByIdAndEmail(SecurityUtils.getCurrentUser().getId(),
            param.getEmail());
        return true;
    }

    @Override
    public Boolean forgetPasswordCode(String recipient) {
        if (MailUtils.isEmailValidate(recipient)) {
            // 验证在库中是否有邮箱
            Optional<EmisAdminVo> byEmail = administratorRepository.findByEmail(recipient);
            if (byEmail.isPresent()) {
                otpContextHelp.sendOtp(recipient, MailType.FORGET_PASSWORD.getKey(),
                    MessageNoticeChannel.MAIL);
                return true;
            }
            logger.warn("忘记密码: 邮箱: [{}] 不存在", recipient);
        } else if (isMobileValidate(recipient)) {
            // 验证在库中是否有手机号
            Optional<EmisAdminVo> byMobile = administratorRepository
                .findByMobile(MobileNumberUtils.getMobileNumber(recipient));
            if (byMobile.isPresent()) {
                otpContextHelp.sendOtp(recipient, SmsType.FORGET_PASSWORD.getKey(),
                    MessageNoticeChannel.SMS);
                return true;
            }
            logger.warn("忘记密码: 手机号: [{}] 不存在", recipient);
        }
        logger.error("忘记密码: 接受者: [{}] 格式错误", recipient);
        throw new BadParamsException("请输入正确的手机号或邮箱");
    }

    @Override
    public Boolean prepareForgetPassword(String recipient, String code) {
        // 校验验证码
        Boolean checkOtp = false;
        Optional<EmisAdminVo> user = Optional.empty();
        if (MailUtils.isEmailValidate(recipient)) {
            user = administratorRepository.findByEmail(recipient);
            if (user.isPresent()) {
                checkOtp = otpContextHelp.checkOtp(MailType.FORGET_PASSWORD.getKey(),
                    MessageNoticeChannel.MAIL, recipient, code);
            }
        } else if (isMobileValidate(recipient)) {
            user = administratorRepository
                .findByMobile(MobileNumberUtils.getMobileNumber(recipient));
            if (user.isPresent()) {
                checkOtp = otpContextHelp.checkOtp(SmsType.FORGET_PASSWORD.getKey(),
                    MessageNoticeChannel.SMS, recipient, code);
            }
        }
        if (!checkOtp) {
            throw new InfoValidityFailException(EX000102.getValue());
        }

        // 生成忘记密码TOKEN ID
        String tokenId = UUID.randomUUID().toString();
        HttpSession session = HttpContextUtils.getSession();
        // 保存用户ID到Redis, 有效期10分钟
        stringRedisTemplate.opsForValue().set(session.getId() + tokenId,
            String.valueOf(user.get().getAdminId()), 10, TimeUnit.MINUTES);
        // 保存TOKEN ID到会话
        session.setAttribute(FORGET_PASSWORD_TOKEN_ID, tokenId);
        return true;
    }

    @Override
    public Boolean forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        // 验证TOKEN
        HttpSession session = HttpContextUtils.getSession();
        String redisTokenId = session.getId() + session.getAttribute(FORGET_PASSWORD_TOKEN_ID);
        String userId = stringRedisTemplate.opsForValue().get(redisTokenId);
        if (Objects.isNull(userId)) {
            // 清除tokenId
            session.removeAttribute(FORGET_PASSWORD_TOKEN_ID);
            return false;
        }
        //修改密码
        Optional<EmisAdminVo> user = administratorRepository.findById(userId);
        if (user.isPresent()) {
            EmisAdminVo administratorEntity = user.get();
            administratorRepository.updatePassword(administratorEntity.getAdminId(),
                passwordEncoder.encode(forgetPasswordRequest.getNewPassword()),
                LocalDateTime.now());
            logger.info("忘记密码: 用户ID: [{}] 用户名: [{}] 修改密码成功", administratorEntity.getAdminId(),
                administratorEntity.getUsername());
            removeSession(administratorEntity.getUsername());
            stringRedisTemplate.delete(redisTokenId);
            return true;
        }
        return false;
    }

    /**
     * 异步下线所有用户
     *
     * @param username {@link String}
     */
    private void removeSession(String username) {
        executor.execute(() -> {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(username, false);
            sessions.forEach(SessionInformation::expireNow);
            //@formatter:on
        });
        SecurityContextHolder.clearContext();
    }

    /**
     * 获取用户信息
     * 为保证安全，从数据库获取最新信息使用，并更新到当前上下文中
     *
     * @return {@link UserInfo}
     */
    public EmisAdminVo getCurrentUser() {
        String userId = SecurityUtils.getCurrentUserId();
        Optional<EmisAdminVo> optional = administratorRepository.findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        }
        SecurityContextHolder.clearContext();
        logger.error("根据用户ID: [{}] 未查询到用户信息", userId);
        throw new UserNotFoundException();
    }

    /**
     * 验证密码
     *
     * @param password {@link String}
     * @return {@link UserInfo}
     */
    public EmisAdminVo validatedPassword(String password) {
        EmisAdminVo user = getCurrentUser();
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            logger.error("用户ID: [{}] 用户名: [{}] 密码匹配失败", user.getAdminId(), user.getUsername());
            throw new PasswordValidatedFailException();
        }
        return user;
    }

    /**
     * Executor
     */
    private final Executor                executor;

    /**
     *  AccountConverter
     */
    private final UserProfileConverter    userProfileConverter;

    /**
     *  PasswordEncoder
     */
    private final PasswordEncoder         passwordEncoder;

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    /**
     * SessionRegistry
     */
    private final SessionRegistry         sessionRegistry;

    /**
     * OtpContextHelp
     */
    private final OtpContextHelper        otpContextHelp;

    /**
     * SmsMsgEventPublish
     */
    private final SmsMsgEventPublish      smsMsgEventPublish;

    /**
     * StringRedisTemplate
     */
    private final StringRedisTemplate     stringRedisTemplate;

    public UserProfileServiceImpl(AsyncConfigurer asyncConfigurer,
                                  UserProfileConverter userProfileConverter,
                                  PasswordEncoder passwordEncoder,
                                  AdministratorRepository administratorRepository,
                                  SessionRegistry sessionRegistry, OtpContextHelper otpContextHelp,
                                  SmsMsgEventPublish smsMsgEventPublish,
                                  StringRedisTemplate stringRedisTemplate) {
        this.executor = asyncConfigurer.getAsyncExecutor();
        this.userProfileConverter = userProfileConverter;
        this.passwordEncoder = passwordEncoder;
        this.administratorRepository = administratorRepository;
        this.sessionRegistry = sessionRegistry;
        this.otpContextHelp = otpContextHelp;
        this.smsMsgEventPublish = smsMsgEventPublish;
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
