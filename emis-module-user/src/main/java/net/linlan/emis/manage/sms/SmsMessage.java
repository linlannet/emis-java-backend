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
package net.linlan.emis.manage.sms;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

import net.linlan.emis.uext.constant.SmsType;

/**
 * 短信消息事件
 *
 * @author Linlan
 */
@Data
@NoArgsConstructor
public class SmsMessage implements Serializable {
    /**
     * 消息类型
     */
    private SmsType             type;
    /**
     * 参数
     */
    private Map<String, String> parameter;

    public SmsMessage(SmsType type, Map<String, String> parameter) {
        this.type = type;
        this.parameter = parameter;
    }
}
