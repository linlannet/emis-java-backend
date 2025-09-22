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
package net.linlan.emis.audit.dto;

import java.util.Set;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 审计字典结果
 *
 * @author Linlan
 */
@Data
@Schema(description = "审计字典结果")
public class DictResult {

    @Parameter(description = "分组名")
    private String         name;
    @Parameter(description = "分组编码")
    private String         code;
    @Parameter(description = "类型")
    private Set<AuditType> types;

    @Data
    @Schema(description = "审计类型")
    public static class AuditType {
        /**
         * 名称
         */
        @Parameter(description = "名称")
        private String name;

        /**
         * 代码
         */
        @Parameter(description = "代码")
        private String code;
    }

}
