/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.verify.feishu.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.utils.enums.BaseEnumString;

/**
 * 飞书事件类型
 * @author Linlan
 */
public enum FeiShuEventType implements BaseEnumString {
                                                       /**
                                                        * 测试url
                                                        */
                                                       URL_VERIFICATION("url_verification",
                                                                        "测试url"),
                                                       /**
                                                        * 用户变更-通讯录用户增加
                                                        */
                                                       USER_ADD_ORG("contact.user.created_v3",
                                                                    "通讯录用户增加"),
                                                       /**
                                                        * 通讯录用户更改
                                                        */
                                                       USER_MODIFY_ORG("contact.user.updated_v3",
                                                                       "通讯录用户更改"),
                                                       /**
                                                        * 通讯录用户离职
                                                        */
                                                       USER_LEAVE_ORG("contact.user.deleted_v3",
                                                                      "通讯录用户离职"),
                                                       /**
                                                        * 部门变更-通讯录企业部门创建
                                                        */
                                                       ORG_DEPT_CREATE("contact.department.created_v3",
                                                                       "通讯录企业部门创建"),
                                                       /**
                                                        * 通讯录企业部门修改
                                                        */
                                                       ORG_DEPT_MODIFY("contact.department.updated_v3",
                                                                       "通讯录企业部门修改"),
                                                       /**
                                                        * 通讯录企业部门删除
                                                        */
                                                       ORG_DEPT_REMOVE("contact.department.deleted_v3",
                                                                       "通讯录企业部门删除");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    FeiShuEventType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static FeiShuEventType getType(String key) {
        FeiShuEventType[] values = values();
        for (FeiShuEventType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该类型");
    }

    @Override
    public String toString() {
        return this.key;
    }
}
