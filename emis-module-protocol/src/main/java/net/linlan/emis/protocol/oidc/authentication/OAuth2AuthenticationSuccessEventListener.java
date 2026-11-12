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
package net.linlan.emis.protocol.oidc.authentication;

import java.security.Principal;
import java.util.Objects;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;

/**
 * 监听登录成功事件
 *
 * @author Linlan
 * Created on 2020/9/3
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthenticationSuccessEventListener implements
                                                      ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
        if (event.getAuthentication() instanceof OAuth2AccessTokenAuthenticationToken){
            OAuth2AccessTokenAuthenticationToken authorization = (OAuth2AccessTokenAuthenticationToken) event.getAuthentication();
            OAuth2Authorization token = authorizationService.findByToken(authorization.getAccessToken().getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
            if (!Objects.isNull(token) && !Objects.isNull(token.getAttribute(Principal.class.getName()))){
                Authentication principal = token.getAttribute(Principal.class.getName());
                if (!Objects.isNull(principal)) {
                    Target target = Target.builder().id(authorization.getRegisteredClient().getId()).name(authorization.getRegisteredClient().getClientName()).type(TargetType.APPLICATION).build();
                    auditEventPublish.publish(EventType.APP_SSO, principal, EventStatus.SUCCESS, Lists.newArrayList(target));
                }
            }
        }
        //@formatter:on
    }

    private final AuditEventPublish          auditEventPublish;

    private final OAuth2AuthorizationService authorizationService;

    public OAuth2AuthenticationSuccessEventListener(AuditEventPublish auditEventPublish,
                                                    OAuth2AuthorizationService authorizationService) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        Assert.notNull(authorizationService, "authorizationService must not be null ");
        this.auditEventPublish = auditEventPublish;
        this.authorizationService = authorizationService;
    }
}
