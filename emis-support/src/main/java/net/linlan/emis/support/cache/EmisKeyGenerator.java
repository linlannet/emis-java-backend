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
package net.linlan.emis.support.cache;

import java.lang.reflect.Method;

import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;

import com.alibaba.fastjson2.JSON;

/**
 * 系统自定义缓存生成
 *
 * @author Linlan
 */
public class EmisKeyGenerator implements KeyGenerator {
    /**
     * Generate a key for the given method and its parameters.
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    @Override
    @NonNull
    public Object generate(@NonNull Object target, @NonNull Method method,
                           @NonNull Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append(method.getName());
        for (Object obj : params) {
            // 由于参数可能不同, hashCode肯定不一样, 缓存的key也需要不一样
            sb.append(JSON.toJSONString(obj).hashCode());
        }
        return Md5Crypt.md5Crypt(sb.toString().getBytes());
    }
}
