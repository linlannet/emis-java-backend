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

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.trace.Trace;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserInfoService;

/**
 * 用户过期锁定任务
 *
 * @author Linlan
 */
@RequiredArgsConstructor
public class UserExpireLockTask {

    private final Logger logger = LoggerFactory.getLogger(UserExpireLockTask.class);

    /**
     * 每十分钟扫描已过期的用户
     */
    @Trace
    @Lock(throwException = false)
    @Scheduled(cron = "0 */10 * * * ?")
    public void execute() {
        logger.info("扫描已过期待锁定用户任务开始");
        //1、根据提醒时间，分页查询即将要过期的密码
        List<UserInfo> list = userRepository.findExpireUser();
        Iterator<UserInfo> iterator = list.iterator();
        logger.info("已过期待锁定用户数量为:{}个", list.size());
        while (!list.isEmpty()) {
            UserInfo entity = iterator.next();
            entity.setStatus(UserStatus.EXPIRED_LOCKED.getKey());
            userRepository.save(entity);
            logger.info("锁定已过期用户：{}", entity.getUsername());
            iterator.remove();
            iterator = list.iterator();
        }
        logger.info("扫描已过期待锁定任务结束");
    }

    /**
     * UserInfoService
     */
    private final UserInfoService userRepository;

}
