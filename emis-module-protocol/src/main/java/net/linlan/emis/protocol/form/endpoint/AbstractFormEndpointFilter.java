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
package net.linlan.emis.protocol.form.endpoint;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import freemarker.template.Configuration;
import freemarker.template.Template;
import net.linlan.emis.app.constant.AppProtocol;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.core.encrypt.EncryptContextHelper;
import net.linlan.emis.core.security.savedredirect.HttpSessionRedirectCache;
import net.linlan.emis.core.security.savedredirect.RedirectCache;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.protocol.entity.ProtocolFormConfig;
import net.linlan.emis.protocol.form.FormApplicationService;
import net.linlan.emis.protocol.form.model.FormProtocolConfig;
import net.linlan.emis.support.security.util.SecurityUtils;
import static net.linlan.emis.core.constant.AuthnConstants.FE_LOGIN;
import static net.linlan.emis.protocol.constant.ProtocolConstants.APP_CODE;
import static net.linlan.emis.support.security.util.SecurityUtils.isAuthenticated;

/**
 * IDP 发起单点登录端点
 *
 * @author Linlan
 * Created by  2022/5/7 22:46
 */
@SuppressWarnings("DuplicatedCode")
@AllArgsConstructor
public abstract class AbstractFormEndpointFilter extends OncePerRequestFilter
                                                 implements OrderedFilter {

    private final RedirectCache redirectCache = new HttpSessionRedirectCache();

    /**
     *
     *
     * @param requestMatcher     {@link RequestMatcher}
     * @param request     {@link HttpServletRequest}
     * @param response    {@link HttpServletResponse}
     * @param filterChain {@link FilterChain}
     */
    @SneakyThrows
    protected void doFilter(@NonNull RequestMatcher requestMatcher,
                            @NonNull HttpServletRequest request,
                            @NonNull HttpServletResponse response,
                            @NonNull FilterChain filterChain) {
        if (!isAuthenticated()) {
            //Saved Redirect
            redirectCache.saveRedirect(request, response, RedirectCache.RedirectType.REQUEST);
            //跳转登录
            response.sendRedirect(PropertiesContextHelper.getPortalPublicBaseUrl() + FE_LOGIN);
            return;
        }
        //@formatter:off
        if (requestMatcher.matches(request)) {
            //获取应用编码
            Map<String, String> variables = requestMatcher.matcher(request).getVariables();
            String appCode = variables.get(APP_CODE);
            //获取应用配置
            FormApplicationService applicationService = (FormApplicationService) applicationServiceLoader.getApplicationService(AppProtocol.FORM.getKey());
            FormProtocolConfig config = applicationService.getProtocolConfig(appCode);
            if (Objects.isNull(config)) {
                throw new AppNotExistException();
            }
            AppAccount appAccount = applicationService.getAppAccount(config.getAppId(), SecurityUtils.getCurrentUserId());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(ContentType.TEXT_HTML.getMimeType());
            Template template = cfg.getTemplate("form_redirect.ftlh");
            Map<String, Object> data = new HashMap<>(16);
            data.put("nonce", System.currentTimeMillis());
            data.put("loginUrl", config.getLoginUrl());
            data.put("submitType", config.getSubmitType());
            data.put("usernameField", config.getUsernameField());
            data.put("passwordField", config.getPasswordField());
            data.put("account", appAccount.getAccount());
            data.put("password", EncryptContextHelper.decrypt(appAccount.getPassword()));
            List<ProtocolFormConfig.OtherField> otherField = config.getOtherField();
            data.put("otherFields", otherField);
            template.process(data, response.getWriter());
            return;
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
     * Form 应用配置
     */
    private final ApplicationServiceLoader applicationServiceLoader;

    private final Configuration            cfg;
}
