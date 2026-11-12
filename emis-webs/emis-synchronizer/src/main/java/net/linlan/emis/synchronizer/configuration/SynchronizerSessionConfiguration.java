/*
 * emis-synchronizer - synchronizer同步执行器
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
package net.linlan.emis.synchronizer.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 控制台Session配置
 * @author Linlan
 * Created on 2023/7/22 21:43
 */
@Configuration
public class SynchronizerSessionConfiguration {

    /**
     * 配置密码加密
     *
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    @ConditionalOnMissingBean(value = BCryptPasswordEncoder.class)
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
