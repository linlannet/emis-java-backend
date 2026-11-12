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
package net.linlan.emis.console.pojo.save.setting;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 角色创建参数
 *
 * @author Linlan
 * Created on  2020/8/26 21:46
 */
@Data
@Schema(description = "创建角色入参")
public class RoleCreateParam implements Serializable {
    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 编码
     */
    @NotEmpty(message = "编码不能为空")
    @Schema(description = "编码")
    private String code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
