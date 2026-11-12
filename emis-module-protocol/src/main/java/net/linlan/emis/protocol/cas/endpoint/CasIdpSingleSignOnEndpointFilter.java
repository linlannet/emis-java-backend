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
package net.linlan.emis.protocol.cas.endpoint;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import net.linlan.emis.core.constant.AuthnConstants;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.core.security.savedredirect.HttpSessionRedirectCache;
import net.linlan.emis.core.security.savedredirect.RedirectCache;
import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.cas.CasApplicationService;
import net.linlan.emis.protocol.cas.auth.CentralAuthenticationService;
import net.linlan.emis.protocol.cas.constant.CasProtocolConstants;
import net.linlan.emis.protocol.cas.tickets.ServiceTicket;
import net.linlan.emis.protocol.cas.tickets.TicketGrantingTicket;
import net.linlan.emis.protocol.constant.CasSsoModel;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.support.security.util.SecurityUtils;

/**
 * CAS 单点登录
 *
 * @author Linlan
 * Created on  2022/12/29 17:38
 */
@SuppressWarnings("DuplicatedCode")
public class CasIdpSingleSignOnEndpointFilter extends OncePerRequestFilter
                                              implements OrderedFilter {
    private static final Logger                logger                  = LoggerFactory
        .getLogger(CasIdpSingleSignOnEndpointFilter.class);
    private static final RequestMatcher        CAS_SSO_REQUEST_MATCHER = new AntPathRequestMatcher(
        ProtocolConstants.CasEndpointConstants.CAS_LOGIN_PATH, HttpMethod.GET.name());
    private final RedirectCache                redirectCache           = new HttpSessionRedirectCache();

    /**
     * 应用配置
     */
    private final ApplicationServiceLoader     applicationServiceLoader;

    private final CentralAuthenticationService centralAuthenticationService;

    public CasIdpSingleSignOnEndpointFilter(ApplicationServiceLoader applicationServiceLoader,
                                            CentralAuthenticationService centralAuthenticationService) {
        this.applicationServiceLoader = applicationServiceLoader;
        this.centralAuthenticationService = centralAuthenticationService;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        if (CAS_SSO_REQUEST_MATCHER.matches(request)) {
            if (!SecurityUtils.isAuthenticated()) {
                //Saved Redirect
                if (!CollectionUtils.isEmpty(request.getParameterMap())) {
                    redirectCache.saveRedirect(request, response,
                        RedirectCache.RedirectType.REQUEST);
                }
                //跳转登录
                response.sendRedirect(
                    PropertiesContextHelper.getPortalPublicBaseUrl() + AuthnConstants.FE_LOGIN);
                return;
            }
            String sessionId = request.getSession(false).getId();
            //获取应用配置
            ApplicationContext applicationContext = ApplicationContextHolder
                .getApplicationContext();
            ApplicationService applicationService = applicationServiceLoader
                .getApplicationService(applicationContext.getAppTemplate());
            CasSsoModel ssoModel = ((CasApplicationService) applicationService)
                .getSsoModel(applicationContext.getAppId());

            String service = request.getParameter(CasProtocolConstants.SERVICE);

            TicketGrantingTicket ticketGrantingTicket = centralAuthenticationService
                .getTicket(sessionId, TicketGrantingTicket.class);

            if (ticketGrantingTicket == null) {
                ticketGrantingTicket = centralAuthenticationService
                    .createTicketGrantingTicket(SecurityUtils.getCurrentUser(), sessionId);
            }
            ServiceTicket serviceTicket = centralAuthenticationService
                .grantServiceTicket(ticketGrantingTicket.getId(), service);

            response.sendRedirect(UriComponentsBuilder.fromHttpUrl(ssoModel.getSsoCallbackUrl())
                .queryParam(CasProtocolConstants.TICKET, serviceTicket.getId()).build().toString());
        }
        filterChain.doFilter(request, response);
    }

    public static RequestMatcher getRequestMatcher() {
        return CAS_SSO_REQUEST_MATCHER;
    }
}
