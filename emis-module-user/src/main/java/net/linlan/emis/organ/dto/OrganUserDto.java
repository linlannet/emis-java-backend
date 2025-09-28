/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.organ.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 组织用户Dto对象
 *
 * @author Linlan
 */
@Getter
@Setter
public class OrganUserDto implements Serializable {
    private static final long serialVersionUID = -8631010419770301741L;

    /**
     * 主键ID
     */
    private String            id;

    /**
     * 用户ID
     */
    private String            userId;

    /**
     * 组织ID
     */
    private String            organId;

    /**
     * 组织名称
     */
    private String            organName;

    /**
     * 显示路径
     */
    private String            displayPath;

    public OrganUserDto(String id, String userId, String organId, String organName,
                        String displayPath) {
        this.id = String.valueOf(id);
        this.userId = String.valueOf(userId);
        this.organId = organId;
        this.organName = organName;
        this.displayPath = displayPath;
    }
}
