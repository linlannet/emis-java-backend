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

import java.io.Serializable;

import lombok.NoArgsConstructor;

/**
 * 组织机构类型
 *
 * @author Linlan
 */
@NoArgsConstructor
public class OrganizationType implements Serializable {
    /**
     * 集团
     */
    public static final OrganizationType GROUP      = new OrganizationType("group", "集团");
    /**
     * 公司
     */
    public static final OrganizationType COMPANY    = new OrganizationType("company", "公司");
    /**
     * 部门
     */
    public static final OrganizationType DEPARTMENT = new OrganizationType("department", "部门");
    /**
     * 单位
     */
    public static final OrganizationType UNIT       = new OrganizationType("unit", "单位");
    /**
     * 机构类型
     */
    private String                       type;

    public String getType() {
        return this.type;
    }

    // 添加静态方法用于反序列化
    public static OrganizationType fromString(String type) {
        switch (type) {
            case "group":
                return GROUP;
            case "company":
                return COMPANY;
            case "department":
                return DEPARTMENT;
            case "unit":
                return UNIT;
            default:
                throw new IllegalArgumentException("未知类型: " + type);
        }
    }

    /**
     * 机构类型名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    OrganizationType(String type, String name) {
        this.type = type;
        this.name = name;
    }

}
