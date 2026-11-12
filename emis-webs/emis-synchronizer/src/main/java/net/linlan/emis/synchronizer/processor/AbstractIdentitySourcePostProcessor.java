/*
 * emis-synchronizer - synchronizer同步执行器
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
package net.linlan.emis.synchronizer.processor;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import lombok.Builder;
import lombok.Cleanup;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.core.constant.SyncStatus;
import net.linlan.emis.core.constant.ViewContentType;
import net.linlan.emis.core.storage.Storage;
import net.linlan.emis.idsource.config.StrategyConfig;
import net.linlan.emis.idsource.constant.IdentitySourceActionType;
import net.linlan.emis.idsource.constant.IdentitySourceProvider;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.idsource.service.IdentitySourceService;
import net.linlan.emis.idsource.service.IdentitySourceSyncHistoryService;
import net.linlan.emis.idsource.service.IdentitySourceSyncRecordService;
import net.linlan.emis.manage.exception.IdentitySourceNotExistException;
import net.linlan.emis.manage.mail.MailMsgEventPublish;
import net.linlan.emis.manage.vo.VerifyUser;
import net.linlan.emis.manage.vo.VerifyUserDetail;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.support.security.password.PasswordGenerator;
import net.linlan.emis.support.userdetails.DataOrigin;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.entity.MsgVariable;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.param.UserInfoParam;
import static net.linlan.emis.idsource.constant.IdentitySourceProvider.*;
import static net.linlan.emis.support.constant.EmisConstants.SYSTEM_DEFAULT_USER_NAME;

/**
 *
 * @author Linlan
 * Created on 2022/9/25 00:32
 */
@Slf4j
public class AbstractIdentitySourcePostProcessor {

    /**
     * 对比用户
     *
     * @param user {@link VerifyUser}
     * @param entity {@link UserInfoParam}
     * @return {@link Boolean}
     */
    protected Boolean equalsUser(VerifyUser user, UserInfo entity, String identitySourceId) {
        //@formatter:off
        if (user == null || user.getUserDetail() == null) {
            return false;
        }
        VerifyUserDetail userDetail = user.getUserDetail();
        return StringUtils.equals(entity.getForeignId(), user.getUserId())
                && StringUtils.equals(Objects.isNull(entity.getIdsourceId()) ? null:entity.getIdsourceId(), identitySourceId)
                && StringUtils.equals(entity.getMobileAreaCode(), user.getMobileAreaCode())
                //上游返回""，数据库有唯一索引，所以""在保存的时候被变成null，这里匹配需要注意
                && StringUtils.equals(StringUtils.isBlank(entity.getEmail()) ? null : entity.getEmail(), StringUtils.isBlank(user.getEmail()) ? null : user.getEmail())
                //上游返回""，数据库有唯一索引，所以""在保存的时候被变成null，这里匹配需要注意
                && StringUtils.equals(StringUtils.isBlank(entity.getMobile()) ? null : entity.getMobile(), StringUtils.isBlank(user.getMobile()) ? null : user.getMobile())
                && Objects.equals(UserStatus.ENABLED.getKey().equals(entity.getStatus()), user.getActive())
                && StringUtils.equals(entity.getFullName(),userDetail.getName())
                && StringUtils.equals(entity.getNickName(),userDetail.getNickName());
        //@formatter:on
    }

    /**
     * 设置用户最新值
     *
     * @param user {@link VerifyUser}
     * @param entity {@link UserInfo}
     */
    protected void setUpdateUser(VerifyUser user, UserInfo entity, IdentitySource identitySource) {
        if (StringUtils.isEmpty(entity.getAvatarUrl())) {
            try {
                convertAvatar(user.getAvatarUrl(), entity);
            } catch (Exception e) {
                log.error("修改用户, 同步头像发生异常: 用户: [{}], 异常: [{}]", user.getUserId(), e.getMessage());
            }
        }

        StrategyConfig.User strategyConfig = identitySource.getStrategyConfig().getUser();
        //@formatter:off
        boolean enabled = false;
        if (!Objects.isNull(strategyConfig)) {
            enabled = Boolean.TRUE.equals(strategyConfig.getEnabled());
        }
        entity.setStatus(enabled || user.getActive() ? UserStatus.ENABLED.getKey() : UserStatus.DISABLED.getKey());
//        entity.setMobile(StringUtils.isEmpty(user.getMobile()) ? null : user.getMobile());
//        entity.setMobileAreaCode(user.getMobileAreaCode());
//        entity.setEmail(StringUtils.isEmpty(user.getEmail()) ? null : user.getEmail());
        if (StringUtils.isNotBlank(user.getEmail())){
            entity.setEmail(user.getEmail());
            entity.setEmailVerified(true);
        }
        if (StringUtils.isNotBlank(user.getMobile())){
            entity.setMobile(user.getMobile());
            entity.setMobileVerified(true);
            entity.setMobileAreaCode(user.getMobileAreaCode());
        }
        entity.setNickName(user.getUserDetail().getNickName());
        entity.setFullName(user.getUserDetail().getName());
        entity.setIdsourceId(identitySource.getId());
    }

    /**
     * 上游用户转系统用户Entity
     *
     * @param user {@link VerifyUser} 上游用户
     * @return {@link UserExt}
     */
    protected UserInfo thirdPartyUserToUserEntity(VerifyUser user,
                                                    IdentitySource identitySource) {
        DataOrigin dataOrigin = getDataOrigin(identitySource.getProvider());
        StrategyConfig.User strategyConfig = identitySource.getStrategyConfig().getUser();
        //@formatter:off
        boolean enabled = false;
        String defaultPassword = null;
        if (!Objects.isNull(strategyConfig)) {
            enabled = Boolean.TRUE.equals(strategyConfig.getEnabled());
            defaultPassword = Objects.toString(strategyConfig.getDefaultPassword());
        }
        if (StringUtils.isBlank(defaultPassword)) {
            defaultPassword = passwordGenerator.generatePassword();
        }
        //封装
        UserInfo entity = new UserInfo();
        entity.setId(UUID.randomUUID().toString());
        entity.setForeignId(user.getUserId());
        String avatar = user.getAvatarUrl();
        if (StringUtils.isNotEmpty(avatar)) {
            try {
                convertAvatar(user.getAvatarUrl(), entity);
            }
            catch (Exception e) {
                log.error("创建用户, 同步头像发生异常: 用户: [{}], 异常: [{}]", user.getUserId(), e.getMessage());
            }
        }
//        entity.setEmail(StringUtils.isEmpty(user.getEmail()) ? null : user.getEmail());
//        entity.setMobile(StringUtils.isEmpty(user.getMobile()) ? null : user.getMobile());
        if (StringUtils.isNotBlank(user.getMobile())){
            entity.setMobile(user.getMobile());
            entity.setMobileVerified(true);
            entity.setMobileAreaCode(user.getMobileAreaCode());
        }
        if (StringUtils.isNotBlank(user.getEmail())){
            entity.setEmail(user.getEmail());
            entity.setEmailVerified(true);
        }
        entity.setFullName(user.getUserDetail().getName());
        entity.setNickName(user.getUserDetail().getNickName());
        entity.setExpireDate(LocalDate.of(2116,12,31));
        entity.setMobileAreaCode(user.getMobileAreaCode());
        //配置为启用，上游未启用，用户为禁用
        entity.setStatus(enabled || user.getActive() ? UserStatus.ENABLED.getKey() : UserStatus.DISABLED.getKey());
        entity.setUsername(user.getUserId());
        entity.setCreateType(dataOrigin.getType());
        entity.setIdsourceId(identitySource.getId());
        entity.setPassword(passwordEncoder.encode(defaultPassword));
        entity.setPasswordPlainText(defaultPassword);

        //必须字段
        entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
        entity.setCreateTime(new Date());
        return entity;
        //@formatter:on
    }

    /**
     * 上游头像转换为系统头像地址
     *
     * @param avatar {@link String}
     * @param entity {@link UserInfo}
     */
    private void convertAvatar(String avatar, UserInfo entity) throws Exception {
        // 上传至对象存储空间
        URL url = new URL(avatar);
        URLConnection urlConnection = url.openConnection();
        String contentType = urlConnection.getContentType();
        String name = FilenameUtils.getBaseName(avatar);
        String suffix = ViewContentType.getSuffix(contentType);
        @Cleanup
        InputStream inputStream = urlConnection.getInputStream();
        if (Objects.nonNull(inputStream)) {
            entity.setAvatarUrl(storage.upload(name + "." + suffix, inputStream));
        }
    }

    /**
     * 创建组织用户关系
     *
     * @param createOrganizationMembers {@link Set}
     * @param currentUser {@link UserInfo}
     * @param organization {@link EmisOrganVo}
     */
    protected static void createOrganizationMember(Set<UserOrganRelation> createOrganizationMembers,
                                                   UserInfo currentUser, EmisOrganVo organization) {
        UserOrganRelation entity = new UserOrganRelation(UUID.randomUUID().toString(),
            String.valueOf(organization.getId()), currentUser.getId(), organization.getId());
        entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
        entity.setCreateTime(new Date());
        createOrganizationMembers.add(entity);
    }

    /**
     * 获取身份源策略配置
     *
     * @param id {@link String}
     * @return {@link IdentitySource}
     */
    protected IdentitySource getIdentitySource(String id) {
        IdentitySource identitySource = identitySourceService.findById(id);
        if (identitySource == null) {
            throw new IdentitySourceNotExistException();
        }
        return identitySource;
    }

    /**
     * 获取身份源策略配置
     *
     * @param id {@link String}
     * @return {@link StrategyConfig}
     */
    protected StrategyConfig getStrategyConfig(String id) {
        IdentitySource entity = getIdentitySource(id);
        return entity.getStrategyConfig();
    }

    /**
     * 获取根组织扩展ID
     *
     * @return {@link String}
     */
    protected String getRootForeignId(String providerKey) {
        IdentitySourceProvider provider = IdentitySourceProvider.getType(providerKey);
        if (DINGTALK.equals(provider)) {
            return "1";
        }
        if (FEISHU.equals(provider)) {
            return "0";
        }
        throw new IdentitySourceNotExistException();
    }

    /**
     * 获取数据来源
     *
     * @param provider {@link IdentitySourceProvider}
     * @return {@link String}
     */
    protected DataOrigin getDataOrigin(String providerKey) {
        IdentitySourceProvider provider = IdentitySourceProvider.getType(providerKey);
        if (DINGTALK.equals(provider)) {
            return DataOrigin.DING_TALK;
        }
        if (WECHAT_WORK.equals(provider)) {
            return DataOrigin.WECHAT_WORK;
        }
        if (FEISHU.equals(provider)) {
            return DataOrigin.FEI_SHU;
        }
        throw new IdentitySourceNotExistException();
    }

    /**
     * 批量发送短信邮件欢迎信息(密码通知)
     *
     * @param users {@link List<UserInfo>} 新增用户信息
     */
    protected void publishMessage(List<UserInfo> users) {
        try {
            users.forEach(thirdPartyUser -> {
                log.info("批量发送邮件欢迎信息:[{}]开始, email: {}, mobile: {}", thirdPartyUser.getUsername(),
                    thirdPartyUser.getEmail(), thirdPartyUser.getMobile());
                if (StringUtils.isNotEmpty(thirdPartyUser.getEmail())) {
                    Map<String, Object> parameter = new HashMap<>(16);
                    parameter.put(MsgVariable.PASSWORD, thirdPartyUser.getPasswordPlainText());
                    mailMsgEventPublish.publish(MailType.RESET_PASSWORD_CONFIRM,
                        thirdPartyUser.getEmail(), parameter);
                }
            });
        } catch (Exception e) {
            log.error("身份源同步用户发送邮件异常:[{}]", e.getMessage(), e);
        }
    }

    @Data
    @Builder
    protected static class SkipUser {

        /**
         * 用户
         */
        private UserInfo                 user;

        /**
         * 操作类型
         */
        private IdentitySourceActionType actionType;

        /**
         * 状态
         */
        private SyncStatus               status;

        /**
         * 原因
         */
        private String                   reason;
    }

    /**
     * PasswordEncoder
     */
    private final PasswordEncoder                    passwordEncoder;

    /**
     * 密码生成器
     */
    private final PasswordGenerator                  passwordGenerator;

    /**
     * 邮件消息事件发布
     */
    private final MailMsgEventPublish                mailMsgEventPublish;

    /**
     * Storage
     */
    private final Storage                            storage;

    /**
     * TransactionDefinition
     */
    protected final TransactionDefinition            transactionDefinition;

    /**
     * PlatformTransactionManager
     */
    protected final PlatformTransactionManager       platformTransactionManager;

    /**
     * 身份源 Service
     */
    protected final IdentitySourceService            identitySourceService;

    /**
     * IdentitySourceSyncHistoryService
     */
    protected final IdentitySourceSyncHistoryService identitySourceSyncHistoryService;

    /**
     * IdentitySourceSyncRecordService
     */
    protected final IdentitySourceSyncRecordService  identitySourceSyncRecordService;

    public AbstractIdentitySourcePostProcessor(MailMsgEventPublish mailMsgEventPublish,
                                               PasswordEncoder passwordEncoder,
                                               PasswordGenerator passwordGenerator,
                                               TransactionDefinition transactionDefinition,
                                               PlatformTransactionManager platformTransactionManager,
                                               IdentitySourceService identitySourceService,
                                               IdentitySourceSyncHistoryService identitySourceSyncHistoryService,
                                               IdentitySourceSyncRecordService identitySourceSyncRecordService,
                                               Storage storage) {
        this.mailMsgEventPublish = mailMsgEventPublish;
        this.passwordEncoder = passwordEncoder;
        this.passwordGenerator = passwordGenerator;
        this.transactionDefinition = transactionDefinition;
        this.platformTransactionManager = platformTransactionManager;
        this.identitySourceService = identitySourceService;
        this.identitySourceSyncHistoryService = identitySourceSyncHistoryService;
        this.identitySourceSyncRecordService = identitySourceSyncRecordService;
        this.storage = storage;
    }

}
