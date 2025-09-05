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
package net.linlan.emis.support.userdetails.useragent;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentService;

/**
 * 用户代理实用程序工具类
 *
 * @author Linlan
 */
@Component
public class DefaultUserAgentParser implements UserAgentParser {
    private static volatile com.blueconic.browscap.UserAgentParser userAgentParser;

    private DefaultUserAgentParser() {

    }

    /**
     * 获取用户代理对象
     *
     * @param request request
     * @return UserAgent
     */
    public UserAgent getUserAgent(HttpServletRequest request) {
        Capabilities parse = getUserAgentParser().parse(request.getHeader("User-Agent"));
        final String browser = parse.getBrowser();
        final String browserType = parse.getBrowserType();
        final String browserMajorVersion = parse.getBrowserMajorVersion();
        final String deviceType = parse.getDeviceType();
        final String platform = parse.getPlatform();
        final String platformVersion = parse.getPlatformVersion();
        // the custom defined fields are available
        final String renderingEngineMaker = parse.getValue(BrowsCapField.RENDERING_ENGINE_MAKER);
        return new UserAgent().setBrowser(browser).setBrowserType(browserType)
            .setBrowserMajorVersion(browserMajorVersion).setDeviceType(deviceType)
            .setPlatform(platform).setPlatformVersion(platformVersion)
            .setRenderingEngineMaker(renderingEngineMaker);
    }

    public static com.blueconic.browscap.UserAgentParser getUserAgentParser() {
        try {
            if (userAgentParser == null) {
                synchronized (UserAgentService.class) {
                    if (userAgentParser == null) {
                        userAgentParser = new UserAgentService().loadParser();
                    }
                }
            }
            return userAgentParser;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 加载用户代理解析器
     *
     * @author Linlan
     */
    @Component
    class LoadUserAgentParser implements ApplicationRunner, Ordered {
        /**
         * ThreadPoolExecutor
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), new CustomizableThreadFactory("load-user-agent-parser"));

        /**
         * Callback used to run the bean.
         *
         * @param args incoming application arguments
         * @throws Exception on error
         */
        @Override
        public void run(ApplicationArguments args) throws Exception {
            executor.execute(DefaultUserAgentParser::getUserAgentParser);
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
    }
}
