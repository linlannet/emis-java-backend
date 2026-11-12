/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.pojo.save;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AppAccountCreateParam 应用账户新增入参
 *
 * @author Linlan
 * Created on 2022/5/24 22:13
 */
@Data
@Schema(description = "应用账户新增入参")
public class AppAccountCreateParam {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @NotNull(message = "应用ID不能为空")
    private String  appId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private String  userId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    @NotBlank(message = "账户名称不能为空")
    private String  account;

    /**
     * 是否默认
     */
    @Schema(description = "是否默认")
    private Boolean defaulted;
}
