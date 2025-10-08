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
package net.linlan.emis.audit.param;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.commons.core.DateUtils;
import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.utils.param.BaseParam;

/**
 *
 * AuditInfo数据域:行为审计查询参数实体类
 * @author Linlan
 * 
 * CreateTime 2025-09-06 15:55:04
 *
 */
@Data
@Schema(description = "查询审计日志列表入参")
public class AuditInfoParam extends BaseParam {

    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String          username;
    /**
     * 审计类型
     */
    @Parameter(description = "审计类型")
    private List<EventType> eventType;

    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    @Parameter(description = "用户类型")
    private String          userType;

    /**
     * 事件状态
     */
    @Parameter(description = "事件状态")
    private EventStatus     eventStatus;

    /**
     * 事件开始时间
     */
    @Parameter(description = "事件开始时间")
    @DateTimeFormat(pattern = DateUtils.yyyyMMddHHmmss)
    private LocalDateTime   startEventTime;

    /**
     * 事件结束时间
     */
    @Parameter(description = "事件结束时间")
    @DateTimeFormat(pattern = DateUtils.yyyyMMddHHmmss)
    private LocalDateTime   endEventTime;

}
