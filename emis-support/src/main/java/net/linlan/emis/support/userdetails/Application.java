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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) // 存储类名用于反序列化
public class Application {

    /**
     * 应用ID
     */
    private String                 id;
    /**
     * 应用CODE
     */
    private String                 code;
    /**
     * 应用名称
     */
    private String                 name;
    /**
     * 应用关联组
     */
    private List<ApplicationGroup> groups;

    public Application(String id, String code, String name, List<ApplicationGroup> groups) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.groups = groups;

    }
}
