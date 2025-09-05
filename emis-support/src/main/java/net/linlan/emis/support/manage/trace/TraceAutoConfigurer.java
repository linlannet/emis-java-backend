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
package net.linlan.emis.support.manage.trace;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 链路追踪自动配置
 * @author Linlan
 */
@Configuration
public class TraceAutoConfigurer implements Serializable {
    private static final long serialVersionUID = -8631010419770301118L;

    /**
     * taskExecutor
     * @return {@link AsyncTaskExecutor}
     */
    @Bean
    public AsyncTaskExecutor mdcThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = MdcThreadPoolTaskExecutor.newWithInheritedMdc(8, 32, 1,
            TimeUnit.MINUTES, 1000);
        executor.setMaxPoolSize(10);
        return executor;
    }
}
