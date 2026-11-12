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
package net.linlan.emis.console.pojo.query.app;

// import java.io.Serial;
import java.io.Serializable;

import org.springdoc.api.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询应用列表
 *
 * @author Linlan
 * Created on 2022/7/6 22:38
 */
@Data
@Schema(description = "查询应用列表")
@ParameterObject
public class GetAppListQuery implements Serializable {

    //@Serial
    private static final long serialVersionUID = -8631010419770301756L;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String            name;

    /**
     * 应用分组ID
     */
    @Parameter(description = "应用分组ID")
    private String            groupId;

}
