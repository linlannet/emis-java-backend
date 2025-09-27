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
package net.linlan.emis.user.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.linlan.emis.core.constant.ConfigBeanNameConstants;
import net.linlan.emis.manage.password.PasswordPolicyManager;
import net.linlan.emis.manage.password.manager.DefaultPasswordPolicyManager;
import net.linlan.emis.support.security.password.generator.DefaultPasswordGenerator;
import net.linlan.emis.support.security.password.weak.DefaultPasswordWeakLibImpl;
import net.linlan.emis.support.security.password.weak.PasswordWeakLib;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserHistoryPasswordService;
import net.linlan.emis.user.service.UserInfoService;

/**
 * SecurityConfiguration
 *
 * @author Linlan
 */
@Configuration
public class EmisPasswordConfiguration {

    /**
     * 密码策略管理器
     *
     * @param userRepository {@link UserInfoService} UserInfoService
     * @param userHistoryPasswordService {@link UserHistoryPasswordService} 用户历史密码repository
     * @param passwordWeakLib               {@link PasswordWeakLib} PasswordWeakLib
     * @param passwordEncoder               {@link PasswordEncoder} PasswordEncoder
     * @return {@link  DefaultPasswordPolicyManager}
     */
    @Bean(ConfigBeanNameConstants.DEFAULT_PASSWORD_POLICY_MANAGER)
    @RefreshScope
    public PasswordPolicyManager<UserInfo> passwordPolicyManager(UserInfoService userRepository,
                                                                 UserHistoryPasswordService userHistoryPasswordService,
                                                                 PasswordWeakLib passwordWeakLib,
                                                                 PasswordEncoder passwordEncoder) {
        return new DefaultPasswordPolicyManager(userRepository, userHistoryPasswordService,
            passwordWeakLib, passwordEncoder);
    }

    /**
     * 默认密码生成
     *
     * @return {@link DefaultPasswordGenerator}
     */
    @Bean
    public DefaultPasswordGenerator defaultPasswordGenerator() {
        return new DefaultPasswordGenerator();
    }

    /**
     * 弱密码库
     *
     * @return {@link PasswordWeakLib}
     */
    @Bean
    public PasswordWeakLib passwordWeakLib() {
        return new DefaultPasswordWeakLibImpl();
    }

}
