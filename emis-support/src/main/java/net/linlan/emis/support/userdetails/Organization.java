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
package net.linlan.emis.support.userdetails;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) // 存储类名用于反序列化
public class Organization {

    /**
     * 主键ID
     */
    private String           id;
    /**
     * key
     */
    private String           name;

    /**
     * 编码
     */
    private String           code;

    /**
     * 组织机构类型
     */
    private OrganizationType type;

    /**
     * 父级
     */
    private String           parentId;

    /**
     * path
     */
    private String           path;

    public Organization(String id, String path) {
        this.id = id;
        this.path = path;
    }

    public Organization(String id, String path, OrganizationType type) {
        this.id = id;
        this.path = path;
        this.type = type;

    }
}
