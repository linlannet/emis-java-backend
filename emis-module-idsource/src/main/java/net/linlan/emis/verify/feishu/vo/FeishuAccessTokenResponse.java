/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.verify.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * access token响应
 *
 * @author Linlan
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class FeishuAccessTokenResponse extends FeishuBaseResponse {
    /**
     * 访问 token
     */
    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;

    /**
     * token 过期时间，单位: 秒
     */
    private int    expire;
}
