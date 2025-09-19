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
package net.linlan.emis.support.event;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import net.linlan.utils.SpringContextUtils;

/**
 * 刷新配置listener
 *
 * @author Linlan
 */
@Component
@RequiredArgsConstructor
public class ConfigRefreshListenerRunner implements CommandLineRunner, Ordered {
    private final Logger         logger         = LoggerFactory
        .getLogger(ConfigRefreshListenerRunner.class);
    private final RedissonClient redissonClient;
    public static final String   CONFIG_REFRESH = "config-refresh";
    public static final String   REFRESH_ALL    = "__REFRESH_ALL__";

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        RTopic topic = redissonClient.getTopic(CONFIG_REFRESH);
        //ALL
        topic.addListener(String.class, (channel, msg) -> {
            logger.info("Refresh EMIS Configuration !");
            RefreshScope bean = SpringContextUtils.getBean(RefreshScope.class);
            if (REFRESH_ALL.equals(msg)) {
                bean.refreshAll();
                return;
            }
            //根据Bean名称刷新指定Bean
            bean.refresh(msg);
        });
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    /**
     * RefreshConfig
     *
     * @author Linlan
     */
    public static class RefreshConfig {

    }
}
