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
package net.linlan.emis.openapi.pojo.response.app;

// import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资源分页查询结果
 *
 * @author Linlan
 * Created on 2020/8/11 23:08
 */
@Data
@Accessors(chain = true)
@Schema(description = "分页查询资源结果")
public class AppPermissionResourceListResult implements Serializable {

    //@Serial
    private static final long serialVersionUID = 3320953184046791392L;
    /**
     * ID
     */
    @Parameter(description = "id")
    private String            id;

    /**
     * 资源名称
     */
    @Parameter(description = "资源名称")
    private String            name;

    /**
     * code
     */
    @Parameter(description = "资源编码")
    private String            code;

    /**
     * 所属应用
     */
    @Parameter(description = "所属应用")
    private String            appId;

    /**
     * desc
     */
    @Parameter(description = "描述")
    private String            desc;
}
