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

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.messaging.context.MessageContext;
import org.opensaml.saml.saml2.core.LogoutRequest;
import org.opensaml.security.credential.CredentialResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;

import net.linlan.emis.audit.constant.EventStatus;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.audit.event.AuditEventPublish;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.core.constant.AuthnConstants;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.constant.Saml2ProtocolConfig;
import net.linlan.emis.protocol.saml2.Saml2ApplicationService;
import net.linlan.emis.protocol.saml2.endpoint.xml.Saml2ValidatorSuite;
import net.linlan.emis.protocol.util.SamlKeyStoreProvider;
import net.linlan.emis.protocol.util.SamlUtils;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.utils.SpringContextUtils;

/**
 * 单点登出
 *
 * @author Linlan
 * Created on  2022/5/10 23:55
 */
@SuppressWarnings("DuplicatedCode")
@RequiredArgsConstructor
public class Saml2IdpSingleSignOutEndpointFilter extends OncePerRequestFilter
                                                 implements OrderedFilter {

    private final Logger                logger          = LoggerFactory
        .getLogger(Saml2IdpSingleSignOutEndpointFilter.class);
    private final static RequestMatcher REQUEST_MATCHER = new AntPathRequestMatcher(
        ProtocolConstants.Saml2EndpointConstants.SAML_LOGOUT_PATH);

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        boolean success = true;
        String result = null;
        //@formatter:off
        if (REQUEST_MATCHER.matches(request)) {
            if (HttpMethod.GET.matches(request.getMethod()) || HttpMethod.POST.matches(request.getMethod())) {
                //获取应用配置
                ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
                ApplicationService applicationService = applicationServiceLoader.getApplicationService(applicationContext.getAppTemplate());
                Saml2ProtocolConfig protocolConfig = ((Saml2ApplicationService) applicationService).getProtocolConfig(String.valueOf(applicationContext.getAppId()));
                try {
                    //获取消息内容
                    MessageContext messageContext = SamlUtils.getMessageContext(request);
                    LogoutRequest logoutRequest = (LogoutRequest) messageContext.getMessage();
                    logger.info("LogoutRequest: ");
                    SamlUtils.logSamlObject(logoutRequest);
                    CredentialResolver credentialResolver = SamlKeyStoreProvider.getKeyStoreCredentialResolver(protocolConfig.getSpEntityId(), protocolConfig.getSpSignCert());
                    Saml2ValidatorSuite.verifySignatureUsingSignatureValidator(logoutRequest.getSignature(), credentialResolver, protocolConfig.getSpEntityId());
                    Saml2ValidatorSuite.verifySignatureUsingMessageHandler(messageContext, credentialResolver, protocolConfig.getSpEntityId());
                    //根据 SessionIndexes 清除会话
                    logoutRequest.getSessionIndexes().forEach((i) -> sessionRegistry.removeSessionInformation(i.getDOM().getNodeValue()));
                    //跳转登录
                    response.sendRedirect(PropertiesContextHelper.getPortalPublicBaseUrl() + AuthnConstants.FE_LOGIN);
                } catch (Exception e) {
                    success=false;
                    result=e.getMessage();
                    throw new SupportException(e.getMessage());
                }
                finally {
                    AuditEventPublish publish = SpringContextUtils.getBean(AuditEventPublish.class);
                    publish.publish(EventType.SIGN_OUT_APP, Lists.newArrayList(Target.builder().id(protocolConfig.getAppId()).name(protocolConfig.getAppCode()).type(TargetType.APPLICATION).build()),result,success? EventStatus.SUCCESS:EventStatus.FAIL);
                }
            }
        }
    }

    /**
     * SAML2应用配置
     */
    private final ApplicationServiceLoader applicationServiceLoader;

    private final SessionRegistry sessionRegistry;
}
