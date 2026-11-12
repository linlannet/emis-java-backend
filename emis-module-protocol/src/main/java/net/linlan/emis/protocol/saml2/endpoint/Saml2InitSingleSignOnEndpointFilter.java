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
package net.linlan.emis.protocol.saml2.endpoint;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.core.constant.AuthnConstants;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.core.security.savedredirect.HttpSessionRedirectCache;
import net.linlan.emis.core.security.savedredirect.RedirectCache;
import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.constant.Saml2SsoModel;
import net.linlan.emis.protocol.saml2.Saml2ApplicationService;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.utils.SpringContextUtils;

/**
 * IDP 发起单点登录端点
 *
 * @author Linlan
 * Created on  2022/5/7 22:46
 */
@SuppressWarnings("DuplicatedCode")
@AllArgsConstructor
public class Saml2InitSingleSignOnEndpointFilter extends OncePerRequestFilter
                                                 implements OrderedFilter {

    private static final RequestMatcher SAML2_INIT_SINGLE_SIGN_MATCHER = new AntPathRequestMatcher(
        ProtocolConstants.IDP_SAML2_SSO_INITIATOR, HttpMethod.POST.name());
    private final RedirectCache         redirectCache                  = new HttpSessionRedirectCache();

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request     {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     * @param filterChain {@link FilterChain}
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        boolean success = true;
        String result = null;
        if (!SecurityUtils.isAuthenticated()) {
            //Saved Redirect
            redirectCache.saveRedirect(request, response, RedirectCache.RedirectType.REQUEST);
            //跳转登录
            response.sendRedirect(
                PropertiesContextHelper.getPortalPublicBaseUrl() + AuthnConstants.FE_LOGIN);
            return;
        }
        //@formatter:off
        if (SAML2_INIT_SINGLE_SIGN_MATCHER.matches(request)) {
            //获取应用配置
            ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
            ApplicationService applicationService = applicationServiceLoader.getApplicationService(applicationContext.getAppTemplate());
            Saml2SsoModel modal = ((Saml2ApplicationService) applicationService).getSsoModel(String.valueOf(applicationContext.getAppId()));
            try{
                //获取 RelayState
                String relayState = StringUtils.defaultString(modal.getRelayState());
                Saml2IdpSingleSignOnEndpointFilter.httpBinding(response, modal, relayState);
                return;
            }catch (Exception e){
                success=false;
                result=e.getMessage();
                throw new SupportException(e.getMessage());
            }finally {
                AuditEventPublish publish = SpringContextUtils.getBean(AuditEventPublish.class);
                publish.publish(EventType.APP_SSO, Lists.newArrayList(Target.builder().id(modal.getAppId()).name(modal.getAppCode()).type(TargetType.APPLICATION).build()),result,success? EventStatus.SUCCESS:EventStatus.FAIL);
            }
        }
        filterChain.doFilter(request, response);
        //@formatter:on
    }

    /**
     * Get the order value of this object.
     * <p>Higher values are interpreted as lower priority. As a consequence,
     * the object with the lowest value has the highest priority (somewhat
     * analogous to Servlet {@code load-on-startup} values).
     * <p>Same order values will result in arbitrary sort positions for the
     * affected objects.
     *
     * @return the order value
     * @see #HIGHEST_PRECEDENCE
     * @see #LOWEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * SAML2应用配置
     */
    private final ApplicationServiceLoader applicationServiceLoader;

    public static RequestMatcher getRequestMatcher() {
        return SAML2_INIT_SINGLE_SIGN_MATCHER;
    }
}
