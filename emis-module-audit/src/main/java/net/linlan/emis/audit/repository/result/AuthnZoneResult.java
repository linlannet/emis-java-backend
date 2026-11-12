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
package net.linlan.emis.audit.repository.result;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录区域结果
 *
 * @author Linlan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录区域响应")
public class AuthnZoneResult implements Serializable {

    /**
     * 省份code
     */
    @Schema(description = "省份code")
    private String name;

    /**
     * 登录次数
     */
    @Schema(description = "登录次数")
    private Long   count;
}
