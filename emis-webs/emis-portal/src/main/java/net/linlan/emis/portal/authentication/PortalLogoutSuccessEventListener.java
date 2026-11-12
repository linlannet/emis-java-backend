/*
 * emis-portal - portal门户平台
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
package net.linlan.emis.portal.authentication;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

/**
 * 退出成功
 *
 * @author Linlan
 * Created on  2020/9/3 22:42
 */
public class PortalLogoutSuccessEventListener implements
                                              ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    //    @Override
    //    public void onApplicationEvent(@NonNull LogoutSuccessEvent event) {
    //        AuditEventPublish auditEventPublish = ApplicationContextService
    //            .getBean(AuditEventPublish.class);
    //        // 审计事件
//        //@formatter:off
//        UserDetails principal = (UserDetails) event.getAuthentication().getPrincipal();
//        List<Target> targets= Lists.newArrayList(Target.builder().id(principal.getId()).name(principal.getUsername()).type(TargetType.PORTAL).build());
//        auditEventPublish.publish(EventType.LOGOUT_PORTAL, event.getAuthentication(), EventStatus.SUCCESS,targets);
//        //@formatter:on
    //    }
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {

    }
}
