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
package net.linlan.emis.protocol.jwt.authentication;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.protocol.jwt.model.JwtProtocolConfig;

/**
 * 监听登录成功事件
 *
 * @author Linlan
 * Created on 2020/9/3
 */
public class JwtAuthenticationSuccessEventListener implements
                                                   ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
        if (event.getAuthentication() instanceof JwtAuthenticationToken){
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) event.getAuthentication();
            JwtProtocolConfig config = authentication.getConfig();
            auditEventPublish.publish(EventType.APP_SSO, (Authentication) authentication.getPrincipal(), EventStatus.SUCCESS,Lists.newArrayList(Target.builder().id(config.getAppId()).type(TargetType.APPLICATION).name(config.getAppName()).build()));
        }
        //@formatter:on
    }

    private final AuditEventPublish auditEventPublish;

    public JwtAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        this.auditEventPublish = auditEventPublish;
    }
}
