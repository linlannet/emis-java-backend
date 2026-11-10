/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.linlan.emis.support.config.SupportProperties;
import net.linlan.emis.support.manage.preview.DemoEnvironmentAspect;

/**
 * 演示环境
 *
 * @author Linlan
 */
@Configuration
@EnableConfigurationProperties(SupportProperties.class)
public class EmisDemoEnvConfiguration {

    public EmisDemoEnvConfiguration(SupportProperties properties) {
        this.demo = properties.getDemo();
    }

    /**
     * 演示环境
     *
     * @return {@link DemoEnvironmentAspect}
     */
    @Bean
    @ConditionalOnMissingBean
    public DemoEnvironmentAspect demoEnvironmentAspect() {
        return new DemoEnvironmentAspect(demo.isOpen());
    }

    /**
     * properties
     */
    private final SupportProperties.Demo demo;
}
