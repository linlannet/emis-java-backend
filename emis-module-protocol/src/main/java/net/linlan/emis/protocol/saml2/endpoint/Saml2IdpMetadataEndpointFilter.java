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

import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.constant.ProtocolConstants;
import net.linlan.emis.protocol.constant.Saml2ProtocolConfig;
import net.linlan.emis.protocol.saml2.Saml2ApplicationService;
import net.linlan.emis.protocol.saml2.util.Saml2Utils;
import net.linlan.emis.protocol.util.SamlUtils;

/**
 * Saml Idp 元数据端点
 *
 * @author Linlan
 * Created on  2022/5/7 22:46
 */
public class Saml2IdpMetadataEndpointFilter extends OncePerRequestFilter implements OrderedFilter {

    private final Logger               logger                             = LoggerFactory
        .getLogger(Saml2IdpMetadataEndpointFilter.class);
    public static final RequestMatcher SAML2_IDP_METADATA_REQUEST_MATCHER = new AntPathRequestMatcher(
        ProtocolConstants.Saml2EndpointConstants.SAML_METADATA_PATH, HttpMethod.GET.name());

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
                                    @NonNull FilterChain filterChain) throws IOException,
                                                                      ServletException {
        if (SAML2_IDP_METADATA_REQUEST_MATCHER.matches(request)) {
            ApplicationContext applicationContext = ApplicationContextHolder
                .getApplicationContext();
            ApplicationService applicationService = applicationServiceLoader
                .getApplicationService(applicationContext.getAppTemplate());
            Saml2ProtocolConfig config = ((Saml2ApplicationService) applicationService)
                .getProtocolConfig(String.valueOf(applicationContext.getAppId()));
            // Generate MetadataXml
            EntityDescriptor entityDescriptor = Saml2Utils.getEntityDescriptor(config);
            String metadataXml = SamlUtils.transformSamlObject2String(entityDescriptor);
            // Response
            response.setContentType(MediaType.APPLICATION_XML_VALUE);
            response.setContentLength(metadataXml.length());
            response.getWriter().write(metadataXml);
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * SAML2应用配置
     */
    private final ApplicationServiceLoader applicationServiceLoader;

    public Saml2IdpMetadataEndpointFilter(ApplicationServiceLoader applicationServiceLoader) {
        this.applicationServiceLoader = applicationServiceLoader;
        // 初始化 OpenSAML
        SamlUtils.initOpenSaml();
        // BouncyCastleProvider
        java.security.Security
            .addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
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

    public static RequestMatcher getRequestMatcher() {
        return SAML2_IDP_METADATA_REQUEST_MATCHER;
    }
}
