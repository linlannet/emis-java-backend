/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.event;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import net.linlan.utils.SpringContextUtils;

/**
 * 获取Spring上下文对象
 *
 * @author Linlan
 */
@Component
public final class ConfigRefreshUtils {

    /**
     * refresh
     * @param beanName {@link  String}
     * 利用redis订阅分发，完成集群参数刷新加载
     */
    public static void refresh(String beanName) {
        RedissonClient redisson = SpringContextUtils.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(ConfigRefreshListenerRunner.CONFIG_REFRESH);
        topic.publish(beanName);
    }

}
