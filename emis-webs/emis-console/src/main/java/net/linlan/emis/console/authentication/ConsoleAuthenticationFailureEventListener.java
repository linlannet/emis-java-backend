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

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.entity.Actor;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.userdetails.UserType;
import net.linlan.utils.SpringContextUtils;
import static net.linlan.emis.support.security.util.SecurityUtils.getFailureMessage;

/**
 * 认证失败
 *
 * @author Linlan
 * Created on  2020/9/3 22:42
 */
public class ConsoleAuthenticationFailureEventListener implements
                                                       ApplicationListener<AbstractAuthenticationFailureEvent> {
    private final Logger logger = LoggerFactory
        .getLogger(ConsoleAuthenticationFailureEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AbstractAuthenticationFailureEvent event) {
        //@formatter:off
        AuditEventPublish publish = SpringContextUtils.getBean(AuditEventPublish.class);
        String content = getFailureMessage(event);
        logger.error("Administrator account authentication failed :{}", event.getException().getMessage());
        String principal = getPrincipal(event);
        if (StringUtils.isNotBlank(principal)){
            Optional<EmisAdminVo> optional = getAdministratorRepository().findByUsername(principal);
            if (!optional.isPresent()) {
                // 手机号
                optional = getAdministratorRepository().findByMobile(principal);
                if (!optional.isPresent()) {
                    // 邮箱
                    optional = getAdministratorRepository().findByEmail(principal);
                }
            }
            if (!optional.isPresent()) {
                logger.error("The administrator account does not exist: [{}]", principal);
                return;
            }
            EmisAdminVo administrator = optional.get();
            Actor actor = Actor.builder().id(administrator.getAdminId()).type(UserType.ADMIN.getType()).build();
            publish.publish(EventType.LOGIN_CONSOLE, content+"："+administrator.getUsername(), actor, EventStatus.FAIL);
        }
        //@formatter:on
    }

    private static String getPrincipal(AbstractAuthenticationFailureEvent event) {
        String principal = (String) event.getAuthentication().getPrincipal();
        if (event.getAuthentication().getPrincipal() instanceof String) {
            principal = (String) event.getAuthentication().getPrincipal();
        }
        if (event.getAuthentication().getPrincipal() instanceof UserDetails || event
            .getAuthentication()
            .getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            principal = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        }
        return principal;
    }

    private AdministratorRepository getAdministratorRepository() {
        return SpringContextUtils.getBean(AdministratorRepository.class);
    }

}
