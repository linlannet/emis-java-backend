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

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.audit.entity.*;
import net.linlan.emis.audit.service.AuditInfoService;

/**
 * 事件监听
 *
 * @author Linlan
 */
@Component
@Async
@Slf4j
public class AuditEventListener implements ApplicationListener<AuditEvent> {

    @Resource
    private AuditInfoService auditInfoService;

    /**
     * onApplicationEvent
     *
     * @param auditEvent {@link AuditEvent}
     */
    @Override
    public void onApplicationEvent(@NonNull AuditEvent auditEvent) {
        Event event = auditEvent.getEvent();
        Actor actor = auditEvent.getActor();
        List<Target> target = auditEvent.getTargets();
        GeoLocation geoLocation = auditEvent.getGeoLocation();
        UserAgent userAgent = auditEvent.getUserAgent();
        //保存数据库
        AuditInfo entity = new AuditInfo();
        try {
            entity.setRequestId(auditEvent.getRequestId());
            entity.setSessionId(auditEvent.getSessionId());
            //事件
            entity.setEventType(event.getType());
            entity.setEventContent(event.getContent());
            entity.setEventParam(event.getParam());
            entity.setEventStatus(event.getStatus());
            entity.setEventResult(event.getResult());
            entity.setEventTime(event.getTime());
            //操作目标
            entity.setTarget(target);
            entity.setGeoLocation(geoLocation);
            entity.setUserAgent(userAgent);
            entity.setActorId(actor.getId());
            entity.setActorType(actor.getType());
            entity.setActorAuthType(actor.getAuthType());
            auditInfoService.save(entity);
        } catch (Exception e) {
            log.error("Audit record saving failed: {}", JSONObject.toJSONString(entity), e);
        }

    }

}
