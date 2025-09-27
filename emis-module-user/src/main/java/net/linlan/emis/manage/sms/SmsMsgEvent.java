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

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

import net.linlan.emis.uext.constant.SmsType;

/**
 * 短信消息事件
 *
 * @author Linlan
 */
@Getter
public class SmsMsgEvent extends ApplicationEvent {
    /**
     * 消息类型
     */
    private final SmsType             type;
    /**
     * 参数
     */
    private final Map<String, String> parameter;

    public SmsMsgEvent(SmsType type, Map<String, String> parameter) {
        super(parameter);
        this.type = type;
        this.parameter = parameter;
    }
}
