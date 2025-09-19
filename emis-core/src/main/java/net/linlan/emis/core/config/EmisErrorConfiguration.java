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

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;

import net.linlan.emis.support.exception.SupportErrorAttributes;
import net.linlan.emis.support.exception.SupportErrorStaticView;

/**
 *
 * ErrorConfiguration
 *
 * @author Linlan
 */
@Configuration
@AutoConfigureBefore(value = { ErrorMvcAutoConfiguration.class })
public class EmisErrorConfiguration {

    /**
     * defaultErrorView
     *
     * @return {@link  View}
     */
    @Bean(name = "error")
    public View defaultErrorView() {
        return new SupportErrorStaticView();
    }

    /**
     * errorAttributes
     *
     * @return {@link  DefaultErrorAttributes}
     */
    @Bean
    public SupportErrorAttributes errorAttributes() {
        return new SupportErrorAttributes();
    }
}
