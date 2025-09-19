/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 同步状态
 *
 * @author Linlan
 */
public enum SyncStatus implements BaseEnumString {
                                                  /**
                                                   * 成功
                                                   */
                                                  SUCCESS("success", "成功"),
                                                  /**
                                                   * 失败
                                                   */
                                                  FAIL("fail", "失败"),
                                                  /**
                                                   * 跳过
                                                   */
                                                  SKIP("skip", "跳过"),
                                                  /**
                                                   * 同步中
                                                   */
                                                  PENDING("pending", "同步中");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;

    SyncStatus(String key, String value) {
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

    @EnumConvert
    public static SyncStatus getStatus(String key) {
        SyncStatus[] values = values();
        for (SyncStatus status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return key;
    }
}
