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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.google.common.collect.Lists;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.manage.utils.AuthenticationUtils;
import net.linlan.emis.support.manage.enums.SecretType;
import net.linlan.emis.support.security.authentication.CustomWebAuthenticationDetails;
import net.linlan.emis.support.security.constant.SecurityConstants;
import net.linlan.emis.support.security.password.authentication.NeedChangePasswordAuthenticationToken;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.constant.EmisConstants.CAPTCHA_CODE_SESSION;

/**
 * 认证成功处理程序
 *
 * @author Linlan
 * Created on  2020/9/2 22:11
 */
public class ConsoleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Called when a user has been successfully authenticated.
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        ResponseResult<String> result = ResponseResult.ok("success");
        request.getSession().removeAttribute(SecretType.LOGIN.getPath());
        request.getSession().removeAttribute(CAPTCHA_CODE_SESSION);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        //更新 principal
        fillPrincipal(authentication, request, response);
        //更新认证次数
        updateAuthSuccessCount(authentication);
        //记录审计日志
        List<Target> targets = Lists.newArrayList(Target.builder().type(TargetType.CONSOLE)
            .id(principal.getId()).name(principal.getUsername()).build());
        auditEventPublish.publish(EventType.LOGIN_CONSOLE, authentication, EventStatus.SUCCESS,
            targets);

        if (principal.getNeedChangePassword()) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext
                .setAuthentication(new NeedChangePasswordAuthenticationToken(authentication));
            securityContextRepository.saveContext(securityContext, request, response);
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                ResponseResult.ok(SecurityConstants.REQUIRE_RESET_PASSWORD));
            return;
        }

        HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(), result);
    }

    private void fillPrincipal(Authentication authentication, HttpServletRequest request,
                               HttpServletResponse response) {
        //        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication
            .getDetails();
        //认证类型
        details.setAuthenticationProvider(AuthenticationUtils.geAuthType(authentication));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        securityContextRepository.saveContext(securityContext, request, response);
    }

    /**
     * 更新认证次数
     *
     * @param authentication {@link Authentication}
     */
    private void updateAuthSuccessCount(Authentication authentication) {
        //认证次数+1
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        //        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        //        administratorRepository.updateAuthSucceedInfo(principal.getId(), details.getRemoteAddress(),
        //            LocalDateTime.now());
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication
            .getDetails();
        administratorRepository.updateAuthSucceedInfo(principal.getId(),
            details.getGeoLocation().getIp(), details.getAuthenticationTime());
    }

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final AdministratorRepository   administratorRepository;
    private final AuditEventPublish         auditEventPublish;

    public ConsoleAuthenticationSuccessHandler(AdministratorRepository administratorRepository,
                                               AuditEventPublish auditEventPublish) {
        this.administratorRepository = administratorRepository;
        this.auditEventPublish = auditEventPublish;
    }
}
