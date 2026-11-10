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
package net.linlan.emis.audit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.event.type.EventType;

/**
 * Event
 *
 * @author Linlan
 */
@Data
@Builder
public class Event implements Serializable {

    private static final long  serialVersionUID = -8631010419770301126L;

    public static final String EVENT_TYPE       = "event.type.keyword";

    public static final String EVENT_TIME       = "event.time";

    public static final String EVENT_STATUS     = "event.status.keyword";

    /**
     * 审计事件类型
     */
    private EventType          type;

    /**
     * 参数
     */
    private String             param;

    /**
     * 事件内容
     */
    private String             content;

    /**
     * 事件结果
     */
    private String             result;

    /**
     * 事件时间
     */
    private LocalDateTime      time;

    /**
     * 事件状态
     */
    private EventStatus        status;

}
