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
package net.linlan.emis.manage.task;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.trace.Trace;
import net.linlan.emis.uext.SettingsContextHelper;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserInfoService;

/**
 * 用户锁定自动解锁任务
 *
 * @author Linlan
 */
@RequiredArgsConstructor
public class UserUnlockTask {

    private final Logger logger = LoggerFactory.getLogger(UserUnlockTask.class);

    /**
     * 每十分钟扫描已锁定的用户解锁
     */
    @Trace
    @Lock(throwException = false)
    @Scheduled(cron = "0 */10 * * * ?")
    public void execute() {
        logger.info("用户自动解锁任务开始");
        List<UserInfo> list = userRepository.findAllByStatus(UserStatus.LOCKED);
        logger.info("待解锁用户数量: [{}] ", list.size());
        for (UserInfo entity : list) {
            logger.info("开始解锁用户: {}", entity.getUsername());
            LocalDateTime updateTime = LocalDateTime.parse(entity.getLastTime().toString());
            Integer unlockTime = SettingsContextHelper.getAutoUnlockTime();
            if (updateTime.plusMinutes(unlockTime).isBefore(LocalDateTime.now())) {
                entity.setStatus(UserStatus.ENABLED.getKey());
                userRepository.save(entity);
                logger.info("成功解锁用户: {}", entity.getUsername());
            }
        }
        logger.info("用户自动解锁任务结束");
    }

    /**
     * UserInfoService
     *
     */
    private final UserInfoService userRepository;
}
