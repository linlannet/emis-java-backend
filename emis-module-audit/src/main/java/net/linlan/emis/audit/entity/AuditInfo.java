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

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * AuditInfo数据域:行为审计实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 15:55:04
 *
 */
@Data
public class AuditInfo extends BaseEntity {

    /**
     * 主键ID
     */
    private String        id;

    /**
     * 外键ID
     */
    private String        foreignId;

    /**
     * 请求ID
     */
    private String        requestId;

    /**
     * 会话ID
     */
    private String        sessionId;

    /**
     * 事件类型
     */
    private EventType     eventType;

    /**
     * 事件参数
     */
    private String        eventParam;

    /**
     * 事件内容
     */
    private String        eventContent;

    /**
     * 事件结果
     */
    private String        eventResult;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件状态
     */
    private EventStatus   eventStatus;

    /**
     * 操作者ID
     */
    private String        actorId;

    /**
     * 操作者类型
     */
    private String        actorType;

    /**
     * 操作者认证类型
     */
    private String        actorAuthType;

    /**
     * 操作目标
     */
    private List<Target>  target;

    /**
     * 用户代理
     */
    private UserAgent     userAgent;

    /**
     * 地理位置
     */
    private GeoLocation   geoLocation;

    /**
     * 删除标记，0未删除1已删除
     */
    private Integer       delFlag;

    /**
     * 备注
     */
    private String        description;

    /**
     * 备用1
     */
    private String        spare1;

    /**
     * 备用2
     */
    private String        spare2;

    /**
     * 初始化方法
     */
    public void init() {
        //添加对数据库或实体对象的默认值处理
        if (getId() == null) {
            setId(RandomUtils.randomSid());
        }

    }

}
