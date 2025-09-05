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

import java.io.Serializable;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户代理对象
 *
 * @author Linlan
 */
@Data
@Accessors(chain = true)
public class UserAgent implements Serializable {

    private static final long serialVersionUID = -8631010419770301123L;

    public UserAgent() {
    }

    /**
     * 浏览器
     */
    String browser;
    /**
     * 浏览器类型
     */
    String browserType;
    /**
     * 浏览器主版本
     */
    String browserMajorVersion;
    /**
     * 设备类型
     */
    @JsonProperty("deviceType")
    String deviceType;
    /**
     * 系统平台
     */
    String platform;
    /**
     * 平台版本
     */
    String platformVersion;
    /**
     * 自定义渲染引擎字段
     */
    String renderingEngineMaker;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public UserAgent(String browser, String browserType, String browserMajorVersion,
                     String deviceType, String platform, String platformVersion,
                     String renderingEngineMaker) {
        this.browser = browser;
        this.browserType = browserType;
        this.browserMajorVersion = browserMajorVersion;
        this.deviceType = deviceType;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.renderingEngineMaker = renderingEngineMaker;
    }

    public static UserAgent.UserAgentBuilder builder() {
        return new UserAgent.UserAgentBuilder();
    }

    public static class UserAgentBuilder {
        public UserAgentBuilder() {
        }

        public UserAgent build() {
            return new UserAgent();
        }

        public UserAgent.UserAgentBuilder browser(String browser) {
            this.browser(browser);
            return this;
        }

        public UserAgent.UserAgentBuilder browserType(String browserType) {
            this.browserType(browserType);
            return this;
        }

        public UserAgent.UserAgentBuilder browserMajorVersion(String browserMajorVersion) {
            this.browserMajorVersion(browserMajorVersion);
            return this;
        }

        public UserAgent.UserAgentBuilder deviceType(String deviceType) {
            this.deviceType(deviceType);
            return this;
        }

        public UserAgent.UserAgentBuilder platform(String platform) {
            this.platform(platform);
            return this;
        }

        public UserAgent.UserAgentBuilder platformVersion(String platformVersion) {
            this.platformVersion(platformVersion);
            return this;
        }

        public UserAgent.UserAgentBuilder renderingEngineMaker(String renderingEngineMaker) {
            this.renderingEngineMaker(renderingEngineMaker);
            return this;
        }

        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}
