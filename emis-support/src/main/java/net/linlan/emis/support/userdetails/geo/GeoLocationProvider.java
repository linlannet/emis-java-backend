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
package net.linlan.emis.support.userdetails.geo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 地理位置提供商配置
 *
 * @author Linlan
 */
public class GeoLocationProvider implements Serializable {

    private static final long               serialVersionUID = -8631010419770301122L;

    public static final GeoLocationProvider NONE             = new GeoLocationProvider("none",
        "none");

    public static final GeoLocationProvider IP2REGION        = new GeoLocationProvider("ip2region",
        "ip2region");

    public static final GeoLocationProvider MAXMIND          = new GeoLocationProvider("maxmind",
        "MAXMIND");

    /**
     * code
     */
    @JsonValue
    private final String                    code;
    /**
     * 名称
     */
    private final String                    name;
    /**
     * 库文件下载地址
     */
    private final String                    downloadUrl;
    /**
     * sha256校验文件下载地址
     */
    private final String                    sha256Url;

    public GeoLocationProvider(String code, String name) {
        this.code = code;
        this.name = name;
        this.downloadUrl = null;
        this.sha256Url = null;
    }

    GeoLocationProvider(String code, String name, String downloadUrl, String sha256Url) {
        this.code = code;
        this.name = name;
        this.downloadUrl = downloadUrl;
        this.sha256Url = sha256Url;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getSha256Url() {
        return sha256Url;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static GeoLocationProvider fromString(String code) {
        if (code == null || code.trim().isEmpty()) {
            return GeoLocationProvider.NONE;
        }

        // 处理已知的 provider 代码
        switch (code) {
            case "none":
                return NONE;
            case "ip2region":
                return IP2REGION;
            case "maxmind":
                return MAXMIND;
            // 可以添加其他已知的 provider
            default:
                return IP2REGION;
        }
    }

}
