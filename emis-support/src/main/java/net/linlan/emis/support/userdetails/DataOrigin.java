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

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.utils.AesUtils;

/**
 * 数据来源
 *
 * @author Linlan
 */
public class DataOrigin implements Serializable {

    /**
     * 系统添加
     */
    public static final DataOrigin INPUT       = new DataOrigin(AesUtils.encrypt("input"), "自建");

    /**
     * 钉钉
     */
    public static final DataOrigin DING_TALK   = new DataOrigin(AesUtils.encrypt("dingtalk"),
        "钉钉导入");

    /**
     * 企业微信
     */
    public static final DataOrigin WECHAT_WORK = new DataOrigin(AesUtils.encrypt("wechat"),
        "企业微信导入");

    /**
     * 飞书
     */
    public static final DataOrigin FEI_SHU     = new DataOrigin(AesUtils.encrypt("feishu"), "飞书导入");

    /**
     * code
     */
    @JsonValue
    @JSONField
    private final String           type;
    /**
     * desc
     */
    private final String           name;

    DataOrigin(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
