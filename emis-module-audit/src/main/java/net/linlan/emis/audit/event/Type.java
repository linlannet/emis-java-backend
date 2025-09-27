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
package net.linlan.emis.audit.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import net.linlan.frame.comm.constant.AdminType;

/**
 * 类型
 *
 * @author Linlan
 */
@Data
@AllArgsConstructor
public class Type {
    /**
     * 编码
     */
    private String          code;
    /**
     * 名称
     */
    private String          name;
    /**
     * 资源
     */
    private Resource        resource;
    /**
     * 用户类型
     */
    private List<AdminType> userTypes;

    @Override
    public String toString() {
        return String.format("[%s](%s) %s", name, code, resource);
    }

}
