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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.linlan.emis.support.manage.enums.EnumConverterFactory;

/**
 * WebConfiguration
 *
 * @author Linlan
 */
@Configuration
public class EmisWebConfiguration implements WebMvcConfigurer {

    /**
     * FormContentFilter
     *
     * @return {@link FormContentFilter}
     */
    @Bean
    public FormContentFilter formContentFilter() {
        return new FormContentFilter();
    }

    /**
     * 添加格式化转换器
     *
     * @param registry {@link FormatterRegistry}
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverterFactory());
    }
}
