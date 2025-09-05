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
package net.linlan.emis.support.manage.lock;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;

import net.linlan.emis.support.constant.EmisConstants;
import net.linlan.emis.support.utils.Md5Utils;
import net.linlan.emis.support.utils.SpelUtils;
import net.linlan.utils.SpringContextUtils;

/**
 * 通过AOP实现锁功能和解锁功能
 *
 * @author Linlan
 */
@Aspect
@Component
public class LockAspect {
    private final RedissonClient redis;

    public LockAspect(RedissonClient redis) {
        this.redis = redis;
    }

    /**
     * 返回结果之前
     * @param pjp   切面入口
     * @param lock  锁标记
     * @return      对象
     * @throws Throwable    异常
     */
    @Around(value = "@annotation(lock)", argNames = "pjp,lock")
    public Object around(ProceedingJoinPoint pjp, Lock lock) throws Throwable {
        RLock rLock = null;
        try {
            // 根据请求参数、方法进行加锁
            Object[] args = pjp.getArgs();
            String suffix;
            if (!StringUtils.isBlank(lock.spEL())) {
                MethodSignature signature = (MethodSignature) pjp.getSignature();
                suffix = SpelUtils.parser(pjp.getTarget(), lock.spEL(), signature.getMethod(),
                    args);
            } else {
                suffix = Md5Utils
                    .md532(String.join(String.valueOf(pjp.getTarget()), Arrays.toString(args)));
            }
            String namespaces = StringUtils.isNotBlank(lock.namespaces())
                ? lock.namespaces() + EmisConstants.COLON
                : "";
            String key = getLockKeyPrefix() + namespaces;
            rLock = redis.getLock(key + suffix);
            boolean tryLock = rLock.tryLock();
            if (tryLock) {
                Object proceed = pjp.proceed();
                //释放锁
                rLock.unlock();
                return proceed;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (!Objects.isNull(rLock) && rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
        throw new SupportLockException();
    }

    /**
     * 获取Lock key 前缀
     *
     * @return {@link String}
     */
    public static String getLockKeyPrefix() {
        return SpringContextUtils.getBean(CacheProperties.class).getRedis().getKeyPrefix()
               + EmisConstants.COLON + "lock" + EmisConstants.COLON + "keys";
    }
}
