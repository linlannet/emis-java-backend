/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.form;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;

import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.form.jackson.FormAuthorizationModule;
import net.linlan.emis.support.security.jackjson.SupportJackson2Modules;

/**
 *
 * @author Linlan
 * Created on 2023/7/8 23:30
 */
public class RedisFormAuthorizationService extends AbstractFormAuthorizationService {

    public RedisFormAuthorizationService(RedisOperations<String, String> redisOperations,
                                         AutowireCapableBeanFactory beanFactory,
                                         ApplicationServiceLoader applicationServiceLoader) {
        super(applicationServiceLoader);
        Assert.notNull(redisOperations, "redisOperations mut not be null");
        this.redisOperations = redisOperations;
        ClassLoader classLoader = this.getClass().getClassLoader();
        objectMapper.registerModules(SupportJackson2Modules.getModules(classLoader));
        objectMapper.registerModule(new FormAuthorizationModule());
        objectMapper.setHandlerInstantiator(new SpringHandlerInstantiator(beanFactory));
    }

    private final RedisOperations<String, String> redisOperations;

    @Setter
    private ObjectMapper                          objectMapper = new ObjectMapper();
}
