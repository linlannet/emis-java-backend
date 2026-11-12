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
package net.linlan.emis.console.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class RedisUserCache implements UserCache {

    private final RedisTemplate<String, net.linlan.emis.support.userdetails.UserDetails> redisTemplate;

    /**
     * 安全认证缓存前缀，Redis Key 前缀
     */
    @Value("${spring.cache.redis.key-prefix:keyPrefix}")
    String                                                                               keyPrefix;
    /**
     * authenticate内userDetails的缓存名称
     */
    private static final String                                                          CACHE_NAME = ":userDetails:";

    public RedisUserCache(RedisTemplate<String, net.linlan.emis.support.userdetails.UserDetails> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDetails getUserFromCache(String username) {
        return redisTemplate.opsForValue().get(keyPrefix + CACHE_NAME + username);
    }

    @Override
    public void putUserInCache(UserDetails user) {
        redisTemplate.opsForValue().set(keyPrefix + CACHE_NAME + user.getUsername(),
            (net.linlan.emis.support.userdetails.UserDetails) user, 1, // 过期时间（单位：小时）
            TimeUnit.HOURS);
    }

    @Override
    public void removeUserFromCache(String username) {
        redisTemplate.delete(keyPrefix + CACHE_NAME + username);
    }

    //    @PostConstruct
    //    public void clearOldCache() {
    //        redisTemplate.getConnectionFactory().getConnection().flushDb();
    //    }
}
