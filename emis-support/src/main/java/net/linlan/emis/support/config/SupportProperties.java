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
package net.linlan.emis.support.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import static net.linlan.emis.support.config.SupportProperties.DEFAULT_PREFIX;

/**
 * emis Support Properties
 *
 * @author Linlan
 */
@Data
@ConfigurationProperties(value = DEFAULT_PREFIX)
public class SupportProperties {
    /**
     * linlan
     */
    public static final String DEFAULT_PREFIX = "linlan";

    /**
     * 演示环境
     */
    @NestedConfigurationProperty
    private final Demo         demo           = new Demo();

    /**
     * 安全相关
     */
    @NestedConfigurationProperty
    private final Security     security       = new Security();

    /**
     * 异步配置
     */
    @NestedConfigurationProperty
    private final Async        async          = new Async();

    /**
     * 服务器配置
     */
    @NestedConfigurationProperty
    private final Server       server         = new Server();

    /**
     * 演示环境
     */
    @Getter
    @Setter
    public static class Demo {
        /**
         * 是否开启
         */
        boolean open = false;
    }

    /**
     * 安全配置
     */
    @Getter
    @Setter
    public static class Security {

    }

    /**
     * 异步
     */
    @Getter
    @Setter
    public static class Async {
        /**
         *核心池大小
         */
        private int corePoolSize  = 2;
        /**
         * 连接池最大连接数
         */
        private int maxPoolSize   = 50;
        /**
         * 队列
         */
        private int queueCapacity = 10000;
    }

    /**
     * Server
     *
     * @author Linlan
    *
     */
    @Getter
    @Setter
    public static class Server {
        /**
         * 最终用户访问控制台的公开 URL，此设置不能以斜杠 (/) 结尾。
         */
        private String consolePublicBaseUrl;

        /**
         * 最终用户访问门户端 的公开 URL，此设置不能以斜杠 (/) 结尾。
         */
        private String portalPublicBaseUrl;

        /**
         *  最终用户访问同步器 的公开 URL，此设置不能以斜杠 (/) 结尾。
         */
        private String synchronizerPublicBaseUrl;

        /**
         *  最终用户访问OpenAPI 的公开 URL，此设置不能以斜杠 (/) 结尾。
         */
        private String openApiPublicBaseUrl;
    }
}
