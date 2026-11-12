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
package net.linlan.emis.console.service.admin.impl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import com.google.common.collect.Lists;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.admin.AdministratorConverter;
import net.linlan.emis.console.pojo.query.admin.AdministratorListQuery;
import net.linlan.emis.console.pojo.result.admin.AdministratorListResult;
import net.linlan.emis.console.pojo.result.admin.AdministratorResult;
import net.linlan.emis.console.pojo.save.admin.AdministratorCreateParam;
import net.linlan.emis.console.pojo.update.admin.AdministratorUpdateParam;
import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.console.service.admin.AdministratorService;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.core.exception.user.PasswordValidatedFailException;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.support.exception.InfoValidityFailException;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.userdetails.UserType;
import net.linlan.emis.support.utils.MobileNumberUtils;
import net.linlan.emis.support.validation.ValidationMobile;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserInfo;
import static net.linlan.emis.support.constant.EmisConstants.DEFAULT_ADMIN_USERNAME;
import static net.linlan.emis.support.utils.MobileNumberUtils.getMobileNumber;

/**
 * @author Linlan
 * Created on  2021/11/13 23:13
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {

    private final Logger logger = LoggerFactory.getLogger(AdministratorServiceImpl.class);

    /**
     * 查询平台管理员列表
     *
     * @param model {@link PageModel}
     * @param query {@link AdministratorListQuery}
     * @return {@link List}
     */
    @Override
    public Page<AdministratorListResult> getAdministratorList(PageModel model,
                                                              AdministratorListQuery query) {
        com.github.pagehelper.Page<EmisAdminVo> page = administratorRepository.findPage(query, model);
        return administratorConverter.entityConvertToAdministratorPaginationResult(page);
    }

    /**
     * 创建管理员
     *
     * @param param {@link AdministratorCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAdministrator(AdministratorCreateParam param) {
        //@formatter:off
        if (StringUtils.isBlank(param.getMobile()) && StringUtils.isBlank(param.getEmail())) {
            throw new SupportException("手机号或邮箱至少填写一个", HttpStatus.BAD_REQUEST);
        }
        //手机号
        if (StringUtils.isNotEmpty(param.getMobile())) {
            if (!getMobileNumber(param.getMobile()).matches(ValidationMobile.PHONE_REGEXP)) {
                throw new InfoValidityFailException("手机号格式错误");
            }
            Boolean validityMobile = administratorParamCheck(CheckValidityType.PHONE, param.getMobile(), null);
            if (!validityMobile) {
                throw new InfoValidityFailException("手机号已存在");
            }
        }
        //邮箱
        if (StringUtils.isNotEmpty(param.getEmail())) {
            Boolean validityEmail = administratorParamCheck(CheckValidityType.EMAIL, param.getEmail(), null);
            if (!validityEmail) {
                throw new InfoValidityFailException("邮箱已存在");
            }
        }
        Boolean validityUsername = administratorParamCheck(CheckValidityType.USERNAME, param.getUsername(), null);
        if (!validityUsername) {
            throw new InfoValidityFailException("用户名已存在");
        }
        EmisAdminVo entity = administratorConverter.administratorCreateParamConvertToEntity(param);
        administratorRepository.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getAdminId())
            .name(entity.getUsername()).type(TargetType.ADMINISTRATOR)
            .typeName(TargetType.ADMINISTRATOR.getValue()).build());
        return true;
        //@formatter:on
    }

    /**
     * 修改管理员
     *
     * @param param {@link AdministratorUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAdministrator(AdministratorUpdateParam param) {
        //@formatter:off
        EmisAdminVo entity = administratorRepository.findById(param.getId()).orElseThrow(() -> new SupportException("管理员信息不存在"));
        AuditContext.setContent(entity.getUsername());
        administratorRepository.save(administratorConverter.administratorUpdateParamConvertToEntity(param, entity));
        AuditContext.setTarget(Target.builder().id(entity.getAdminId())
            .name(entity.getUsername()).type(TargetType.ADMINISTRATOR)
            .typeName(TargetType.ADMINISTRATOR.getValue()).build());
        return true;
        //@formatter:on
    }

    /**
     * 删除管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAdministrator(String id) {
        EmisAdminVo administratorEntity = getAdministrator(id, "删除失败，管理员不存在");
        if (administratorEntity.getUsername().equals(DEFAULT_ADMIN_USERNAME)) {
            AuditContext.setContent("默认超级管理员禁止删除");
            logger.warn(AuditContext.getContent());
            throw new SupportException("操作失败");
        }
        //执行删除
        administratorRepository.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name(administratorEntity.getUsername())
            .type(TargetType.ADMINISTRATOR).build());
        // 下线登录中已删除的管理员
        removeSession(administratorEntity.getUsername());
        return true;
    }

    /**
     * 更改管理员状态
     *
     * @param id     {@link String}
     * @param status {@link UserStatus}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateAdministratorStatus(String id, UserStatus status) {
        EmisAdminVo administratorEntity = getAdministrator(id,
            status.getValue() + "失败，管理员不存在");
        AuditContext.setContent(administratorEntity.getUsername());
        Optional<EmisAdminVo> optional = administratorRepository.findById(id);
        if (optional.isPresent() && optional.get().getUsername().equals(DEFAULT_ADMIN_USERNAME)) {
            logger.warn("默认超级管理员禁止禁用");
            throw new SupportException("操作失败");
        }
        administratorRepository.updateStatus(id, status);
        AuditContext.setTarget(Target.builder().id(id).name(administratorEntity.getUsername())
            .type(TargetType.ADMINISTRATOR).build());
        if (UserStatus.DISABLED == status) {
            // 下线登录中已禁用的管理员
            removeSession(administratorEntity.getUsername());
        }
        return true;
    }

    @NotNull
    private EmisAdminVo getAdministrator(String id, String message) {
        Optional<EmisAdminVo> optional = administratorRepository.findById(id);
        //管理员不存在
        if (!optional.isPresent()) {
            AuditContext.setContent(message);
            logger.warn(AuditContext.getContent());
            throw new SupportException("操作失败");
        }
        return optional.get();
    }

    /**
     * 重置管理员密码
     *
     * @param id       {@link String}
     * @param password {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean resetAdministratorPassword(String id, String password) {
        EmisAdminVo entity = getAdministrator(id, "重置密码失败，管理员不存在");
        password = new String(
            Base64.getUrlDecoder().decode(password.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);
        password = passwordEncoder.encode(password);
        administratorRepository.updatePassword(id, password, LocalDateTime.now());
        AuditContext.setTarget(Target.builder().id(id).name(entity.getUsername())
            .type(TargetType.ADMINISTRATOR).build());
        // 下线登录中已重置密码的管理员
        removeSession(entity.getUsername());
        return true;
    }

    /**
     * 更新认证成功信息
     *
     * @param id {@link String}
     * @param ip {@link String}
     * @param loginTime {@link LocalDateTime}
     */
    public Boolean updateAuthSucceedInfo(String id, String ip, LocalDateTime loginTime) {
        administratorRepository.updateAuthSucceedInfo(id, ip, loginTime);
        return true;
    }

    /**
     * 强制重置当前登录管理员密码
     *
     * @param password {@link String}
     */
    @Override
    public void forceResetAdministratorPassword(String username, String password) {
        EmisAdminVo adminEntity = getAdministratorByUsername(username);
        forceResetAdministratorPassword(adminEntity, password);
    }

    @Override
    public void forceResetAdministratorPassword(EmisAdminVo adminEntity, String password) {
        boolean matches = passwordEncoder.matches(password, adminEntity.getPassword());
        if (matches) {
            logger.error("用户ID: [{}] 用户名: [{}] 新密码与旧密码相同", adminEntity.getAdminId(),
                adminEntity.getUsername());
            throw new PasswordValidatedFailException("新密码不允许与旧密码相同");
        }
        password = passwordEncoder.encode(password);
        adminEntity.setPassword(password);
        adminEntity.setLastUpdatePasswordTime(LocalDateTime.now());
        adminEntity.setNeedChangePassword(false);
        // 更新密码
        administratorRepository.save(adminEntity);
        AuditContext.setTarget(Target.builder().id(adminEntity.getAdminId())
            .name(adminEntity.getUsername()).type(TargetType.ADMINISTRATOR).build());
        // 下线登录中已重置密码的管理员
        removeSession(adminEntity.getUsername());
    }

    @Override
    public EmisAdminVo getAdministratorByUsername(String username) {
        return administratorRepository.findByUsername(username).orElseThrow(() -> {
            AuditContext.setContent("重置密码失败，管理员不存在");
            logger.warn(AuditContext.getContent());
            return new SupportException("操作失败");
        });
    }

    /**
     * 下线管理员
     *
     * @param username {@link String}
     */
    private void removeSession(String username) {
        //异步下线所有用户
        executor.execute(() -> {
            //@formatter:off
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(username,false);
            sessions.forEach(SessionInformation::expireNow);
            //@formatter:on
        });
    }

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean} false 不可用 true 可用
     */
    @Override
    public Boolean administratorParamCheck(CheckValidityType type, String value, String id) {
        EmisAdminVo entity = new EmisAdminVo();
        boolean result = false;
        // ID存在说明是修改操作，查询一下当前数据
        if (Objects.nonNull(id)) {
            entity = administratorRepository.findById(id).orElse(new EmisAdminVo());
        }
        //邮箱
        if (CheckValidityType.EMAIL.equals(type)) {
            if (StringUtils.equals(entity.getEmail(), value)) {
                return true;
            }
            result = !administratorRepository.existsByEmail(value);
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
                result = !administratorRepository
                    .existsByMobile(String.valueOf(mobileNumber.getNationalNumber()));
            } catch (NumberParseException e) {
                logger.error("校验手机号发生异常", e);
                throw new SupportException("校验手机号发生异常");
            }
        }
        //用户名
        if (CheckValidityType.USERNAME.equals(type)) {
            if (StringUtils.equals(entity.getUsername(), value)) {
                return true;
            }
            result = !administratorRepository.existsByUsername(value);
        }
        return result;
    }

    /**
     * 根据用户名、手机号、邮箱查询用户
     *
     * @param keyword {@link String}
     * @return {@link UserInfo}
     */
    @Override
    public Optional<EmisAdminVo> findByUsernameOrMobileOrEmail(String keyword) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 异步执行查询
        CompletableFuture<Optional<EmisAdminVo>> findByUsernameFuture = CompletableFuture
            .supplyAsync(() -> administratorRepository.findByUsername(keyword));

        CompletableFuture<Optional<EmisAdminVo>> findByMobileFuture = CompletableFuture
            .supplyAsync(() -> administratorRepository.findByMobile(keyword));

        CompletableFuture<Optional<EmisAdminVo>> findByEmailFuture = CompletableFuture
            .supplyAsync(() -> administratorRepository.findByEmail(keyword));

        // 等待所有查询完成，并处理结果
        CompletableFuture<Optional<EmisAdminVo>> combinedFuture = CompletableFuture
            .allOf(findByUsernameFuture, findByMobileFuture, findByEmailFuture)
            .thenApply(voided -> {
                try {
                    if (findByUsernameFuture.get().isPresent()) {
                        return findByUsernameFuture.get();
                    } else if (findByMobileFuture.get().isPresent()) {
                        return findByMobileFuture.get();
                    } else {
                        return findByEmailFuture.get();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    return Optional.empty();
                }
            });

        try {
            // 等待最终结果
            return combinedFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            // 处理异常
            return Optional.empty();
        } finally {
            // 结束计时
            stopWatch.stop();
            logger.info("根据用户名、手机号、邮箱查询管理员耗时:{}ms", stopWatch.getTotalTimeMillis());
        }
    }

    /**
     * 获取用户详情
     *
     * @param userId {@link String}
     * @return {@link UserDetails}
     */
    @Override
    public UserDetails getUserDetails(String userId) {
        Optional<EmisAdminVo> optional = administratorRepository.findById(userId);
        return optional.map(this::getUserDetails).orElse(null);
    }

    /**
     * 获取用户详情
     *
     * @param administrator {@link EmisAdminVo}
     * @return {@link UserDetails}
     */
    @Override
    public UserDetails getUserDetails(EmisAdminVo administrator) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(UserType.ADMIN.getType()));
        return administrator.toUserDetails(Lists.newArrayList(authorities));
    }

    /**
     * 查询管理员详情
     *
     * @param id {@link String}
     * @return {@link AdministratorResult}
     */
    @Override
    public AdministratorResult getAdministrator(String id) {
        EmisAdminVo administrator = administratorRepository.findById(id).orElse(null);
        AdministratorResult result = administratorConverter
            .entityConvertToAdministratorDetailsResult(administrator);
        if (Objects.nonNull(administrator) && StringUtils.isNotEmpty(administrator.getMobile())) {
            StringBuilder mobileAreaCode = new StringBuilder(
                administrator.getMobileAreaCode().replace(MobileNumberUtils.PLUS_SIGN, ""));
            mobileAreaCode.insert(0, MobileNumberUtils.PLUS_SIGN);
            result.setMobile(mobileAreaCode + administrator.getMobile());
        }
        return result;
    }

    /**
     * Executor
     */
    private final Executor                executor;

    /**
     * AdministratorConverter
     */
    private final AdministratorConverter  administratorConverter;

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    /**
     * PasswordEncoder
     */
    private final PasswordEncoder         passwordEncoder;

    /**
     * SessionRegistry
     */
    private final SessionRegistry         sessionRegistry;

    public AdministratorServiceImpl(AdministratorConverter administratorConverter,
                                    AdministratorRepository administratorRepository,
                                    PasswordEncoder passwordEncoder,
                                    AsyncConfigurer asyncConfigurer,
                                    SessionRegistry sessionRegistry) {
        this.administratorConverter = administratorConverter;
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
        this.executor = asyncConfigurer.getAsyncExecutor();
        this.sessionRegistry = sessionRegistry;
    }
}
