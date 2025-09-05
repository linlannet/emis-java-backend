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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class GeoLocationMixin {
    @JsonCreator
    public GeoLocationMixin(@JsonProperty("ip") String ip,
                            @JsonProperty("continentCode") String continentCode,
                            @JsonProperty("continentName") String continentName,
                            @JsonProperty("countryCode") String countryCode,
                            @JsonProperty("countryName") String countryName,
                            @JsonProperty("cityCode") String cityCode,
                            @JsonProperty("cityName") String cityName,
                            @JsonProperty("provinceCode") String provinceCode,
                            @JsonProperty("provinceName") String provinceName,
                            @JsonProperty("latitude") Double latitude,
                            @JsonProperty("longitude") Double longitude,
                            @JsonProperty("provider") GeoLocationProvider provider

    ) {

    }
}