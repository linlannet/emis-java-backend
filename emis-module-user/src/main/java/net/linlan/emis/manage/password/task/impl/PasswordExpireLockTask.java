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

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.manage.password.task.PasswordExpireTask;
import net.linlan.emis.support.manage.trace.Trace;
import net.linlan.emis.uext.SettingsContextHelper;
import net.linlan.emis.uext.constant.PasswordPolicySettingConstants;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserInfoService;

/**
 * 密码过期锁定任务
 *
 * @author Linlan
 */
@RequiredArgsConstructor
public class PasswordExpireLockTask implements PasswordExpireTask {

    private final Logger logger = LoggerFactory.getLogger(PasswordExpireLockTask.class);

    /**
     * 每天凌晨两点锁定密码过期用户
     */
    @Trace
    @Scheduled(cron = "0 0 2 * * ?")
    @Override
    public void execute() {
        logger.info("密码过期锁定用户任务开始");
        // 查询非密码过期锁定和过期锁定用户信息
        List<UserInfo> list = userInfoService.findAllByStatusIn(
            new UserStatus[] { UserStatus.PASSWORD_EXPIRED_LOCKED, UserStatus.EXPIRED_LOCKED });
        Iterator<UserInfo> iterator = list.iterator();
        while (!list.isEmpty()) {
            UserInfo user = iterator.next();
            //获取到期日期
            int expireDays = getExpireDays();
            if (user.getLastResetTime().plusDays(expireDays).isBefore(LocalDateTime.now())) {
                user.setStatus(UserStatus.PASSWORD_EXPIRED_LOCKED.getKey());
                userInfoService.save(user);
                logger.info("锁定密码过期用户:{}", user.getUsername());
                iterator.remove();
            }
            iterator = list.iterator();
        }
        logger.info("密码过期锁定用户任务结束");
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
     * UserInfoService
     */
    private final UserInfoService userInfoService;

}
