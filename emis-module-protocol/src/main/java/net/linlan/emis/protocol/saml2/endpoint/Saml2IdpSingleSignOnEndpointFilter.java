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
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.encoder.MessageEncodingException;
import org.opensaml.saml.common.messaging.context.SAMLBindingContext;
import org.opensaml.saml.common.messaging.context.SAMLEndpointContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPPostEncoder;
import org.opensaml.saml.saml2.binding.encoding.impl.HTTPRedirectDeflateEncoder;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.metadata.Endpoint;
import org.opensaml.saml.saml2.metadata.impl.AssertionConsumerServiceBuilder;
import org.opensaml.security.credential.CredentialResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
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
import net.linlan.emis.protocol.saml2.endpoint.xml.ResponseGenerator;
import net.linlan.emis.protocol.saml2.endpoint.xml.Saml2ValidatorSuite;
import net.linlan.emis.protocol.util.SamlKeyStoreProvider;
import net.linlan.emis.protocol.util.SamlUtils;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.utils.SpringContextUtils;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import static org.opensaml.saml.common.xml.SAMLConstants.SAML2_POST_BINDING_URI;
import static org.opensaml.saml.common.xml.SAMLConstants.SAML2_REDIRECT_BINDING_URI;
import static org.springframework.util.StringUtils.hasText;

/**
 * Saml 接受SP发起登录端点
 *
 * @author Linlan
 * Created on  2022/5/7 22:46
 */
@SuppressWarnings("DuplicatedCode")
@AllArgsConstructor
public class Saml2IdpSingleSignOnEndpointFilter extends OncePerRequestFilter
                                                implements OrderedFilter {
    private static final Logger         logger          = LoggerFactory
        .getLogger(Saml2IdpSingleSignOnEndpointFilter.class);
    private final static RequestMatcher REQUEST_MATCHER = new AntPathRequestMatcher(
        ProtocolConstants.Saml2EndpointConstants.SAML_SSO_PATH);
    private final RedirectCache         redirectCache   = new HttpSessionRedirectCache();

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request      {@link HttpServletRequest}
     * @param httpResponse {@link HttpServletResponse}
     * @param filterChain  {@link FilterChain}
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse httpResponse,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        boolean success = true;
        String result = null;
        //@formatter:off
        if (REQUEST_MATCHER.matches(request)) {
            if (HttpMethod.GET.matches(request.getMethod()) || HttpMethod.POST.matches(request.getMethod())) {
                if (!SecurityUtils.isAuthenticated()) {
                    //Saved Redirect
                    if (!CollectionUtils.isEmpty(request.getParameterMap())) {
                        redirectCache.saveRedirect(request, httpResponse, RedirectCache.RedirectType.REQUEST);
                    }
                    //跳转登录
                    httpResponse.sendRedirect(PropertiesContextHelper.getPortalPublicBaseUrl() + AuthnConstants.FE_LOGIN);
                    return;
                }
                //获取应用配置
                ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
                ApplicationService applicationService = applicationServiceLoader.getApplicationService(applicationContext.getAppTemplate());
                Saml2SsoModel modal = ((Saml2ApplicationService) applicationService).getSsoModel(String.valueOf(applicationContext.getAppId()));

                //获取消息内容
                MessageContext messageContext = SamlUtils.getMessageContext(request);
                AuthnRequest authnRequest = (AuthnRequest) messageContext.getMessage();
                logger.info("AuthnRequest: ");
                SamlUtils.logSamlObject(authnRequest);
                //是否强制授权
                if (authnRequest.isForceAuthn()){
                    logger.info("SP 要求强制认证，清除当前会话状态，重定向到登录页面");
                    SecurityContextHolder.clearContext();
                    redirectCache.saveRedirect(request, httpResponse, RedirectCache.RedirectType.REQUEST);
                    return;
                }
                SAMLBindingContext subcontext = messageContext.getSubcontext(SAMLBindingContext.class);
                //获取 RelayState
                String relayState = StringUtils.defaultString(subcontext.getRelayState(), modal.getRelayState());
                try {
                    //是否对 SAML Request 签名进行验证
                    if (modal.getSpRequestsSigned()) {
                        CredentialResolver credentialResolver = SamlKeyStoreProvider.getKeyStoreCredentialResolver(modal.getSpEntityId(), modal.getSpSignCert());
                        Saml2ValidatorSuite.verifySignatureUsingSignatureValidator(authnRequest.getSignature(), credentialResolver, modal.getSpEntityId());
                        Saml2ValidatorSuite.verifySignatureUsingMessageHandler(messageContext, credentialResolver, modal.getSpEntityId());
                    }
                    String authnRequestId = authnRequest.getID();
                    httpBinding(httpResponse, modal, relayState, authnRequestId);
                    return;
                } catch (Exception e) {
                    success=false;
                    result=e.getMessage();
                    throw new SupportException(e.getMessage());
                }finally {
                    AuditEventPublish publish = SpringContextUtils.getBean(AuditEventPublish.class);
                    publish.publish(EventType.APP_SSO, Lists.newArrayList(Target.builder().id(modal.getAppId()).name(modal.getAppCode()).type(TargetType.APPLICATION).build()),result,success? EventStatus.SUCCESS:EventStatus.FAIL);
                }
            }
        }
        filterChain.doFilter(request, httpResponse);
    }
    //@formatter:on

    public static void httpBinding(HttpServletResponse httpResponse, Saml2SsoModel model,
                                   String relayState,
                                   String authnRequestId) throws ComponentInitializationException,
                                                          MessageEncodingException {
        //构建响应
        //@formatter:off
        ResponseGenerator responseGenerator = new ResponseGenerator(
                getIssuerName(model.getAppCode()),
                model.getSpAcsUrl(),
                model.getRecipient(),
                model.getAudience(),
                model.getNameIdValue(),
                model.getNameIdFormat(),
                //响应签名
                model.getResponseSigned(),
                model.getResponseSignAlgorithm(),
                //断言签名
                model.getAssertSigned(),
                model.getAssertSignAlgorithm(),
                //断言加密
                model.getAssertEncrypted(),
                model.getAssertEncryptAlgorithm(),
                //证书相关
                model.getIdpSignCert(),
                model.getIdpEncryptCert(),
                model.getAuthnContextClassRef(),
                model.getAttributeStatements());
        //AuthRequestId
        responseGenerator.setAuthnRequestId(authnRequestId);
        Response buildResponse = responseGenerator.generateResponse();
        logger.info("Response: ");
        //@formatter:on
        SamlUtils.logSamlObject(buildResponse);
        MessageContext context = new MessageContext();
        context.setMessage(buildResponse);
        if (hasText(relayState)) {
            context.getSubcontext(SAMLBindingContext.class, true).setRelayState(relayState);
        }
        SAMLEndpointContext samlEndpointContext = context
            .getSubcontext(SAMLPeerEntityContext.class, true)
            .getSubcontext(SAMLEndpointContext.class, true);
        if (!Objects.isNull(samlEndpointContext)) {
            Endpoint endpoint = new AssertionConsumerServiceBuilder().buildObject();
            endpoint.setLocation(model.getSpAcsUrl());
            samlEndpointContext.setEndpoint(endpoint);
        }
        // POST
        if (SAML2_POST_BINDING_URI.equals(model.getAcsBinding())) {
            HTTPPostEncoder httpPostEncoder = new HTTPPostEncoder();
            httpPostEncoder.setMessageContext(context);
            httpPostEncoder.setVelocityEngine(VELOCITY_ENGINE);
            httpPostEncoder.setHttpServletResponse(httpResponse);
            httpPostEncoder.initialize();
            httpPostEncoder.encode();
            return;
        }
        // HTTP-Redirect
        if (SAML2_REDIRECT_BINDING_URI.equals(model.getAcsBinding())) {
            HTTPRedirectDeflateEncoder httpRedirectDeflateEncoder = new HTTPRedirectDeflateEncoder();
            httpRedirectDeflateEncoder.setMessageContext(context);
            httpRedirectDeflateEncoder.setHttpServletResponse(httpResponse);
            httpRedirectDeflateEncoder.initialize();
            httpRedirectDeflateEncoder.encode();
        }
    }

    public static void httpBinding(HttpServletResponse httpResponse, Saml2SsoModel saml2,
                                   String relayState) throws ComponentInitializationException,
                                                      MessageEncodingException {
        httpBinding(httpResponse, saml2, relayState, null);
    }

    public static String getIssuerName(String appCode) {
        return PropertiesContextHelper.getPortalPublicBaseUrl()
               + ProtocolConstants.Saml2EndpointConstants.SAML_METADATA_PATH
                   .replace(ProtocolConstants.APP_CODE_VARIABLE, appCode);
    }

    /**
     * Velocity 引擎
     */
    public final static VelocityEngine VELOCITY_ENGINE;

    static {
        VELOCITY_ENGINE = new VelocityEngine();
        VELOCITY_ENGINE.setProperty(RuntimeConstants.ENCODING_DEFAULT, "UTF-8");
        VELOCITY_ENGINE.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        VELOCITY_ENGINE.setProperty("classpath.resource.loader.class",
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VELOCITY_ENGINE.init();
    }

    /**
     * SAML2应用配置
     */
    private final ApplicationServiceLoader applicationServiceLoader;

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

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
