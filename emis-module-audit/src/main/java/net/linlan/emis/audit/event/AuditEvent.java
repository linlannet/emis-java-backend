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
package net.linlan.emis.audit.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

import net.linlan.emis.audit.entity.*;

/**
 * 审计事件
 *
 * @author Linlan
 */
@Getter
public class AuditEvent extends ApplicationEvent {
    private static final long  serialVersionUID = -8631010419770301128L;
    /**
     * 请求ID
     */
    private final String       requestId;
    /**
     * 会话ID
     */
    private final String       sessionId;
    /**
     * 行动者
     */
    private final Actor        actor;
    /**
     * 事件
     */
    private final Event        event;
    /**
     * 用户代理
     */
    private final UserAgent    userAgent;
    /**
     * 用户所在地理位置
     */
    private final GeoLocation  geoLocation;
    /**
     * 扩展对象
     */
    private final List<Target> targets;

    public AuditEvent(String requestId, String sessionId, Actor actor, Event event,
                      UserAgent userAgent, GeoLocation geoLocation, List<Target> targets) {
        super(requestId);
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.actor = actor;
        this.event = event;
        this.targets = targets;
        this.userAgent = userAgent;
        this.geoLocation = geoLocation;
    }
}
