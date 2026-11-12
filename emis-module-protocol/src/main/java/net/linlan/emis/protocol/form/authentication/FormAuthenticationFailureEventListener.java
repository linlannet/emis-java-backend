/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.form.authentication;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.protocol.form.exception.FormAuthenticationException;
import net.linlan.emis.protocol.form.model.FormProtocolConfig;

/**
 * 认证失败监听
 *
 * @author Linlan
 * Created on 2023/7/8 21:25
 */
public class FormAuthenticationFailureEventListener implements
                                                    ApplicationListener<AbstractAuthenticationFailureEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AbstractAuthenticationFailureEvent event) {
        if (event.getException() instanceof FormAuthenticationException) {
            FormRequestAuthenticationToken authentication = (FormRequestAuthenticationToken) event
                .getAuthentication();
            FormProtocolConfig config = authentication.getConfig();
            auditEventPublish.publish(EventType.APP_SSO, authentication, EventStatus.FAIL,
                Lists.newArrayList(Target.builder().id(config.getAppId()).name(config.getAppName())
                    .type(TargetType.APPLICATION).build()));
        }
    }

    private final AuditEventPublish auditEventPublish;

    public FormAuthenticationFailureEventListener(AuditEventPublish auditEventPublish) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        this.auditEventPublish = auditEventPublish;
    }
}
