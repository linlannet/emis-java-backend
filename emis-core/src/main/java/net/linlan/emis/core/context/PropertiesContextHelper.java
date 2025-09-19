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
package net.linlan.emis.core.context;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.core.constant.AuthnConstants;
import net.linlan.emis.support.config.SupportProperties;
import net.linlan.utils.SpringContextUtils;

/**
 * ServerContextHelp
 *
 * @author Linlan
 */
@Slf4j
public final class PropertiesContextHelper {

    public static SupportProperties getSupportProperties() {
        return SpringContextUtils.getBean(SupportProperties.class);
    }

    /**
     * 获取控制台基础网址
     *
     * @return {@link  String}
     */
    public static String getConsolePublicBaseUrl() {
        return getSupportProperties().getServer().getConsolePublicBaseUrl();
    }

    /**
     * 获取门户基础网址
     *
     * @return {@link  String}
     */
    public static String getPortalPublicBaseUrl() {
        return getSupportProperties().getServer().getPortalPublicBaseUrl();
    }

    /**
     * 获取同步基础网址
     *
     * @return {@link  String}
     */
    public static String getSynchronizerPublicBaseUrl() {
        return getSupportProperties().getServer().getSynchronizerPublicBaseUrl();
    }

    /**
     * 获取OpenAPI基础网址
     *
     * @return {@link  String}
     */
    public static String getOpenMailSendRecordPublicBaseUrl() {
        return getSupportProperties().getServer().getOpenApiPublicBaseUrl();
    }

    /**
     * 获取门户登录网址
     *
     * @return {@link  String}
     */
    public static String getPortalLoginUrl() {
        return getSupportProperties().getServer().getPortalPublicBaseUrl()
               + AuthnConstants.FE_LOGIN;
    }

}
