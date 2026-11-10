/*
 * emis-module-audit - audit安全审计
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
package net.linlan.emis.audit.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserAgent用户代理
 * @author Linlan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAgent implements Serializable {

    public static final String USER_AGENT_BROWSER = "user_agent.browser.keyword";
    /**
     * 设备类型
     */
    private String             deviceType;
    /**
     * 系统平台
     */
    private String             platform;
    /**
     * 平台版本
     */
    private String             platformVersion;
    /**
     * 浏览器
     */
    private String             browser;
    /**
     * 浏览器类型
     */
    private String             browserType;
    /**
     * 浏览器主版本
     */
    private String             browserMajorVersion;
}
