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
package net.linlan.emis.audit.entity;

import java.io.Serializable;

import lombok.*;

import net.linlan.emis.audit.constant.TargetType;

/**
 * Target目标
 *
 * @author Linlan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Target implements Serializable {

    private static final long  serialVersionUID  = -8631010419770301127L;

    public static final String TARGET_ID_KEYWORD = "target.id.keyword";

    /**
     * 目标 ID
     */
    @NonNull
    private String             id;

    /**
     * 目标名称
     */
    @NonNull
    private String             name;
    /**
     *
     * 目标类型
     */
    @NonNull
    private TargetType         type;

    /**
     * 目标类型名称
     */
    private String             typeName;

    public String getTypeName() {
        return type.getValue();
    }
}
