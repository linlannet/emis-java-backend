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
package net.linlan.emis.console.pojo.save.identitysource;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 身份源创建返回结果
 *
 * @author Linlan
 * Created on  2022/2/21 21:34
 */
@Data
@Schema(description = "身份源创建返回响应")
public class IdentitySourceCreateResult implements Serializable {
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String id;

    public IdentitySourceCreateResult() {
    }

    public IdentitySourceCreateResult(String id) {
        this.id = id;
    }
}
