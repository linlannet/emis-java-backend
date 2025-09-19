/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.repository.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分页结果
 *
 * @author Linlan
 */
@Data
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -8631010419770301111L;
    /**
     * list
     */
    @Schema(description = "数据集合")
    private List<T>           list;
    /**
     * 分页数据
     */
    @Schema(description = "页数数据")
    private Pagination        pagination;

    /**
     * 分页参数数据
     */
    @Data
    @Builder
    public static class Pagination implements Serializable {
        private static final long serialVersionUID = -8631010419770301112L;
        @Schema(description = "总条数")
        private Long              total;
        @Schema(description = "总页数")
        private Integer           totalPages;
        @Schema(description = "当前页")
        private Integer           current;
    }
}
