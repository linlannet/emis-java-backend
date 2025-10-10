/*
 * emis-java-backend - 基于JAVA开发的企业级多身份管理系统后端工程，依托EFRAME框架开发，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。
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
package net.linlan.emis.manage.endpoint;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.core.security.savedredirect.HttpSessionRedirectCache;
import net.linlan.emis.core.security.savedredirect.RedirectCache;
import net.linlan.emis.support.security.web.AbstractAuthenticationEntryPoint;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import net.linlan.utils.http.HttpResponseUtils;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import static net.linlan.utils.http.HttpContextUtils.isHtmlRequest;

/**
 *
 * @author Linlan
 *
 */
public class UnauthorizedAuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {

    private final RedirectCache redirectCache = new HttpSessionRedirectCache();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //记录
        redirectCache.saveRedirect(request, response, RedirectCache.RedirectType.REQUEST);
        //判断请求
        boolean isHtmlRequest = isHtmlRequest(request);
        // HTML
        if (isHtmlRequest) {
            //跳转前端SESSION过期路由
            response.sendRedirect(PropertiesContextHelper.getPortalLoginUrl());
            return;
        }
        // JSON
        ResponseResult<Object> result = new ResponseResult(String.valueOf(UNAUTHORIZED.value()),
            Objects.toString(authException.getMessage(), UNAUTHORIZED.getReasonPhrase()));
        HttpResponseUtils.flushResponseJson(response, UNAUTHORIZED.value(), result);
    }

    public UnauthorizedAuthenticationEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }

}
