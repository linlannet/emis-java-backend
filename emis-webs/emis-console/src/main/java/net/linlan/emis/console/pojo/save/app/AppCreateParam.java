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
package net.linlan.emis.console.pojo.save.app;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用保存入参
 *
 * @author Linlan
 * Created on  2021/7/18 23:26
 */
@Data
@Schema(description = "应用保存入参")
public class AppCreateParam implements Serializable {

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    @Schema(description = "应用名称")
    private String       name;

    /**
     * 应用模版
     */
    @NotNull(message = "应用模版不能为空")
    @Schema(description = "应用模版")
    private String       template;

    /**
     * 应用图标
     */
    @Schema(description = "应用图标")
    private String       icon;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String       remark;

    /**
     * 应用分组id
     */
    @Schema(description = "应用分组")
    private List<String> groupIds;
}
