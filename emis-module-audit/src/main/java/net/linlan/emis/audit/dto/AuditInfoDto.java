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
package net.linlan.emis.audit.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.commons.core.DateUtils;
import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.entity.GeoLocation;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.entity.UserAgent;

/**
 *
 * AuditInfo数据域:行为审计Dto对象
 * @author Linlan
 *
 * CreateTime 2025-09-06 15:55:04
 *
 */
@Data
@Schema(description = "审计日志列表结果")
public class AuditInfoDto implements Serializable {
    /**
     * ID
     */
    @Schema(description = "ID")
    private String        id;

    /**
     * 用户
     */
    @Schema(description = "用户名")
    private String        username;

    /**
     * 用户 ID
     */
    @Schema(description = "用户ID")
    private String        userId;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型")
    private String        userType;

    /**
     * 用户代理
     */
    @Schema(description = "用户代理")
    private UserAgent     userAgent;

    /**
     * 地理IP
     */
    @Schema(description = "地理位置")
    private GeoLocation   geoLocation;

    /**
     * 事件类型
     */
    @Schema(description = "事件类型")
    private String        eventType;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = DateUtils.yyyyMMddHHmmss)
    @Schema(description = "事件时间")
    private LocalDateTime eventTime;

    /**
     * 事件状态
     */
    @Schema(description = "事件状态")
    private EventStatus   eventStatus;

    /**
     * 目标
     */
    @Schema(description = "目标")
    private List<Target>  target;
}
