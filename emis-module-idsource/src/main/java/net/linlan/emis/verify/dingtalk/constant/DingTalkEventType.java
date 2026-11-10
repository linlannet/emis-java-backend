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
package net.linlan.emis.verify.dingtalk.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.utils.enums.BaseEnumString;

/**
 * 钉钉事件类型
 * @author Linlan
 */
public enum DingTalkEventType implements BaseEnumString {
                                                         /**
                                                          * 测试
                                                          */
                                                         CHECK_URL("check_url", "测试url"),
                                                         /**
                                                          * 用户变更-通讯录用户增加
                                                          */
                                                         USER_ADD_ORG("user_add_org", "通讯录用户增加"),
                                                         /**
                                                          * 通讯录用户更改
                                                          */
                                                         USER_MODIFY_ORG("user_modify_org",
                                                                         "通讯录用户更改"),
                                                         /**
                                                          * 通讯录用户离职
                                                          */
                                                         USER_LEAVE_ORG("user_leave_org",
                                                                        "通讯录用户离职"),
                                                         /**
                                                          * 加入企业后用户激活
                                                          */
                                                         USER_ACTIVE_ORG("user_active_org",
                                                                         "加入企业后用户激活"),
                                                         /**
                                                          * 部门变更-通讯录企业部门创建
                                                          */
                                                         ORG_DEPT_CREATE("org_dept_create",
                                                                         "通讯录企业部门创建"),
                                                         /**
                                                          * 通讯录企业部门修改
                                                          */
                                                         ORG_DEPT_MODIFY("org_dept_modify",
                                                                         "通讯录企业部门修改"),
                                                         /**
                                                          * 通讯录企业部门删除
                                                          */
                                                         ORG_DEPT_REMOVE("org_dept_remove",
                                                                         "通讯录企业部门删除");

    /**
     * key
     */
    @JsonValue
    private final String key;
    /**
     * 名称
     */
    private final String value;

    DingTalkEventType(String key, String value) {
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

    public static DingTalkEventType getType(String key) {
        DingTalkEventType[] values = values();
        for (DingTalkEventType status : values) {
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
