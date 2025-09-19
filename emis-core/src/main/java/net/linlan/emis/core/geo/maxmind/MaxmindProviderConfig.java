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
package net.linlan.emis.core.geo.maxmind;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.core.encrypt.JsonPropertyEncrypt;
import net.linlan.emis.core.geo.GeoLocationProviderConfig;

/**
 *
 * @author Linlan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaxmindProviderConfig extends GeoLocationProviderConfig.GeoLocationConfig {
    /**
     * 密码
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "密码不能为空")
    private String sessionKey;
}
