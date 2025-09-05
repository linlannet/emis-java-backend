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
package net.linlan.emis.support.utils;

import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

/**
 *  HttpUrl Utils
 *
 * @author Linlan
 */
public class UrlUtils {

    public static String integrate(String basicUrl, Map<String, String> parameters) {
        StringBuilder url = new StringBuilder(basicUrl).append("?");
        if (MapUtils.isNotEmpty(parameters)) {
            parameters
                .forEach((key, value) -> url.append("&").append(key).append("=").append(value));
        }
        url.deleteCharAt(url.indexOf("?&") + 1);
        return url.toString();
    }

    public static String format(String url) {
        return url.replaceAll("(?<!(http:|https:))/+", "/");
    }

    public static boolean testUrlWithTimeOut(String urlString, int timeOutMillSeconds) {
        try {
            URL url = new URL(urlString);
            URLConnection co = url.openConnection();
            co.setConnectTimeout(timeOutMillSeconds);
            co.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
