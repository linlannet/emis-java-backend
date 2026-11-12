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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户类型
 *
 * @author Linlan
 */
public class UserType implements Serializable {
    /**
     * 用户
     */
    public static final UserType USER      = new UserType("user", "用户");
    /**
     * 管理员
     */
    public static final UserType ADMIN     = new UserType("admin", "管理员");
    /**
     * 开发人员
     */
    public static final UserType DEVELOPER = new UserType("developer", "开发人员");
    /**
     * 未知
     */
    public static final UserType UNKNOWN   = new UserType("unknown", "未知");

    /**
     * 用户类型，如admin，user
     */
    private String               type;

    /**
     * 用户类型名称，如管理员，用户
     */
    private String               name;

    UserType() {

    }

    UserType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @JsonProperty("type")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
