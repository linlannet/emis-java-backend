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
package net.linlan.emis.console.pojo.update.app;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分组修改入参
 *
 * @author Linlan
 * Created on  2021/7/18 23:26
 */
@Data
@Schema(description = "分组修改入参")
public class AppGroupUpdateParam implements Serializable {

    /**
     * id
     */
    @Schema(description = "分组id")
    @NotNull(message = "ID不能为空")
    private String id;

    /**
     * 分组名称
     */
    @Schema(description = "分组名称")
    private String name;

    /**
     * 分组排序
     */
    @Schema(description = "分组编码")
    private String code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
