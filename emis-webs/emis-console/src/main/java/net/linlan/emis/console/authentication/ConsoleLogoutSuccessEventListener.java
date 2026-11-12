/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.authentication;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.LogoutSuccessEvent;

import com.google.common.collect.Lists;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.utils.SpringContextUtils;

/**
 * 退出成功
 *
 * @author Linlan
 * Created on  2020/9/3 22:42
 */
public class ConsoleLogoutSuccessEventListener implements ApplicationListener<LogoutSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull LogoutSuccessEvent event) {
        AuditEventPublish auditEventPublish = SpringContextUtils.getBean(AuditEventPublish.class);
        // 审计事件
        //@formatter:off
        UserDetails principal = (UserDetails) event.getAuthentication().getPrincipal();
        List<Target> targets= Lists.newArrayList(Target.builder().id(principal.getId()).name(principal.getUsername()).type(TargetType.CONSOLE).build());
        auditEventPublish.publish(EventType.LOGOUT_CONSOLE, event.getAuthentication(), EventStatus.SUCCESS,targets);
        //@formatter:on
    }
}
