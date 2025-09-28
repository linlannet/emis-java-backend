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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
     * GeoLocationResponse
     *
     * @author Linlan
 *
     */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation implements Serializable {

    private static final long serialVersionUID = -8631010419770301104L;

    /**
     * IP
     */
    private String            ip;

    /**
     * continent code
     */
    private String            continentCode;

    /**
     * continent Name
     */
    private String            continentName;

    /**
     * 国家/地区code
     */
    private String            countryCode;

    /**
     * 国家
     */
    private String            countryName;

    /**
     * 城市code
     */
    private String            cityCode;

    /**
     * 城市
     */
    private String            cityName;

    /**
     * 省份code
     */
    private String            provinceCode;

    /**
     * 省份
     */
    private String            provinceName;

    /**
     * 经度 WGS84坐标
     */
    private Double            latitude;

    /**
     * 维度 WGS84坐标
     */
    private Double            longitude;

    /**
     * 提供商
     */
    private String            provider;

}
