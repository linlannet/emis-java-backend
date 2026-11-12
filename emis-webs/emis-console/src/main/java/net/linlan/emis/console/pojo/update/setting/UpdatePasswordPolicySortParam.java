/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.pojo.update.setting;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *更新密码策略排序入参
 *
 * @author Linlan
 * Created on  2023/6/11 22:03
 */
@Data
@Schema(description = "更新密码策略排序入参")
public class UpdatePasswordPolicySortParam implements Serializable {

    @Parameter(description = "ID")
    @NotNull(message = "ID不能为空")
    private String id;

    @Parameter(description = "优先级")
    @NotNull(message = "优先级不能为空")
    private Long   priority;

}
