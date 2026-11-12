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
package net.linlan.emis.protocol.form;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import freemarker.template.Configuration;
import net.linlan.emis.manage.config.AuthenticationConfigUtils;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;
import net.linlan.emis.protocol.form.endpoint.FormInitSingleSignOnEndpointFilter;
import net.linlan.emis.protocol.form.endpoint.FormSingleSignOnEndpointFilter;
import net.linlan.utils.SpringContextUtils;

/**
 * 认证配置
 *
 * @author Linlan
 * Created by  2021/9/10 22:58
 */
public final class FormProtocolConfigurer<B extends HttpSecurityBuilder<B>> extends
                                         AbstractHttpConfigurer<FormProtocolConfigurer<B>, B> {

    @Override
    public void configure(B http) {
        ApplicationServiceLoader applicationServiceLoader = AuthenticationConfigUtils
            .getApplicationServiceLoader(http);
        Configuration configuration = SpringContextUtils.getBean(Configuration.class);
        //Form 单点登录地址
        http.addFilterAfter(
            new FormSingleSignOnEndpointFilter(applicationServiceLoader, configuration),
            UsernamePasswordAuthenticationFilter.class);
        //发起Form表单登录过滤器
        http.addFilterAfter(
            new FormInitSingleSignOnEndpointFilter(applicationServiceLoader, configuration),
            FormSingleSignOnEndpointFilter.class);
    }

    public RequestMatcher getEndpointsMatcher() {
        List<RequestMatcher> requestMatchers = new ArrayList<>();
        //Form 门户端发起登录
        requestMatchers.add(FormSingleSignOnEndpointFilter.getRequestMatcher());
        //Form 服务端发起登录
        requestMatchers.add(FormInitSingleSignOnEndpointFilter.getRequestMatcher());
        return new OrRequestMatcher(requestMatchers);
    }

}
