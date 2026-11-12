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

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import lombok.AllArgsConstructor;

/**
 * 监听登录成功事件
 *
 * @author Linlan
 * Created on  2020/9/3
 */
@AllArgsConstructor
public class ConsoleAuthenticationSuccessEventListener implements
                                                       ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
//        AuditEventPublish auditEventPublish = ApplicationContextService.getBean(AuditEventPublish.class);
//        AdministratorService administratorService = ApplicationContextService.getBean(AdministratorService.class);
//        Authentication authentication = event.getAuthentication();
//        Object principal = authentication.getPrincipal();
//        //@formatter:on
        //        if (principal instanceof UserDetails) {
        //            //认证类型
        //            ((UserDetails) principal)
        //                .setAuthType(AuthenticationUtils.geAuthTypeString(authentication));
        //            //登录事件
        //            ((UserDetails) principal).setLoginTime(LocalDateTime.now());
        //            //区域
        //            ((UserDetails) principal).setGeoLocation(geoLocationParser
        //                .getGeoLocation(IpUtils.getIpAddr(ServletContextHelper.getRequest())));
        //            //认证次数+1
        //            administratorService.updateAuthSucceedInfo(((UserDetails) principal).getId(),
        //                ((UserDetails) principal).getGeoLocation().getIp(),
        //                ((UserDetails) principal).getLoginTime());
        //            // 审计事件
//            //@formatter:off
//            List<Target> targets= Lists.newArrayList(Target.builder().id(((UserDetails) principal).getId()).name(((UserDetails) principal).getUsername()).type(TargetType.CONSOLE).build());
//            auditEventPublish.publish(LOGIN_CONSOLE, event.getAuthentication(), EventStatus.SUCCESS, targets);
//            //@formatter:on
        //        }
    }

    //    private final GeoLocationParser geoLocationParser;

}
