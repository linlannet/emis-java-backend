/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.manage.event;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import net.linlan.utils.SpringContextUtils;

/**
 * 身份源配置事件工具
 *
 * @author Linlan
 *
 */
public class IdentitySourceEventUtils {
    public static final String IDENTITY_SOURCE_EVENT = "identity-source-event";

    /**
     * refresh
     * 利用redis订阅分发，完成集群参数刷新加载
     * @param id 主键ID
     */
    public static void destroy(String id) {
        RedissonClient redisson = SpringContextUtils.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(IDENTITY_SOURCE_EVENT);
        topic.publish(new IdentitySourceEvent(id, IdentitySourceEventType.DESTROY));
    }

    /**
     * refresh
     * 利用redis订阅分发，完成集群参数刷新加载
     * @param id 主键ID
     */
    public static void register(String id) {
        RedissonClient redisson = SpringContextUtils.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(IDENTITY_SOURCE_EVENT);
        topic.publish(new IdentitySourceEvent(id, IdentitySourceEventType.REGISTER));
    }

    /**
     * sync
     * 利用redis订阅分发，发送同步任务
     * @param id 主键ID
     */
    public static void sync(String id) {
        RedissonClient redisson = SpringContextUtils.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(IDENTITY_SOURCE_EVENT);
        topic.publish(new IdentitySourceEvent(id, IdentitySourceEventType.SYNC));
    }

}
