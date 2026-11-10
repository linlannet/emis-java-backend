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
package net.linlan.emis.manage.password.task.impl;

import java.time.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

import net.linlan.commons.core.StringUtils;
import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.manage.mail.MailMsgEventPublish;
import net.linlan.emis.manage.password.task.PasswordExpireTask;
import net.linlan.emis.manage.sms.SmsMsgEventPublish;
import net.linlan.emis.support.manage.trace.Trace;
import net.linlan.emis.uext.SettingsContextHelper;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.constant.PasswordPolicySettingConstants;
import net.linlan.emis.uext.constant.SmsType;
import net.linlan.emis.uext.entity.MsgVariable;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserInfoService;

/**
 * 密码过期前提醒任务
 *
 * @author Linlan
 */
@RequiredArgsConstructor
public class PasswordExpireWarnTask implements PasswordExpireTask {
    private final Logger logger = LoggerFactory.getLogger(PasswordExpireWarnTask.class);

    /**
     * 提醒 每天凌晨两点扫描，发送邮件及短信提醒
     */
    @Trace
    @Scheduled(cron = "0 0 2 * * ?")
    @Override
    public void execute() {
        logger.info("密码过期前提醒任务开始");
        int expireDays = getExpireDays();
        int expireWarnDays = getWarnExpireDays();
        //1、根据提醒时间，分页查询即将要过期的密码
        List<UserInfo> list = userInfoService
            .getList(new StringMap().put("status", UserStatus.ENABLED).map());
        //2、发送通知提醒
        for (UserInfo user : list) {
            // 获取到期日期
            LocalDate expiredDate = user.getLastResetTime().atOffset(ZoneOffset.MAX)
                .plusDays(expireDays).toLocalDate();
            // 到了提醒时间
            if (user.getLastResetTime().plusDays(expireWarnDays).isBefore(LocalDateTime.now())) {
                //如果邮箱不为空，发送邮件通知
                if (StringUtils.isNotBlank(user.getEmail())) {
                    logger.info("邮件通知密码过期前提醒用户:{}", user.getUsername());
                    HashMap<String, Object> map = Maps.newHashMap();
                    //查询两个时间的间距
                    map.put(MsgVariable.EXPIRE_DAYS,
                        Duration.between(LocalDate.now(), expiredDate).toDays());
                    mailMsgEventPublish.publish(MailType.PASSWORD_SOON_EXPIRED_REMIND,
                        user.getEmail(), map);
                }
                // 短信通知
                if (StringUtils.isNotBlank(user.getMobile())) {
                    logger.info("短信通知密码过期前提醒用户:{}", user.getUsername());
                    LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
                    // 查询两个时间的间距
                    map.put(MsgVariable.EXPIRE_DAYS,
                        String.valueOf(Duration.between(LocalDate.now(), expiredDate).toDays()));
                    smsMsgEventPublish.publish(SmsType.PASSWORD_SOON_EXPIRED_REMIND,
                        user.getMobile(), map);
                }
            }

        }
        logger.info("密码过期前提醒任务结束");
    }

    /**
     * 获取密码过期前警告日
     *
     * @return 过期天数
     */
    private Integer getWarnExpireDays() {
        EmisSettingVo beforeExpireDaysSetting = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_VALID_WARN_BEFORE_DAYS);
        String beforeExpireDays = Objects.isNull(beforeExpireDaysSetting)
            ? PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_VALID_WARN_BEFORE_DAYS)
            : beforeExpireDaysSetting.getContent();
        return Integer.valueOf(beforeExpireDays);
    }

    /**
     * 获取密码过期日
     *
     * @return 过期天数
     */
    private Integer getExpireDays() {
        EmisSettingVo expireDaysSetting = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_VALID_DAYS);
        String expireDays = Objects.isNull(expireDaysSetting)
            ? PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_VALID_DAYS)
            : expireDaysSetting.getContent();
        return Integer.parseInt(expireDays);
    }

    /**
     * 用户Repository
     */
    private final UserInfoService     userInfoService;

    /**
     * 邮件消息发布
     */
    private final MailMsgEventPublish mailMsgEventPublish;

    /**
     * 短信消息发送
     */
    private final SmsMsgEventPublish  smsMsgEventPublish;
}
