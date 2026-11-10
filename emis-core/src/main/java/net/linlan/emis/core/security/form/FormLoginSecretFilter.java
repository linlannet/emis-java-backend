/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.form;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson2.JSONObject;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.support.exception.enums.ExceptionStatus;
import net.linlan.emis.support.manage.ParameterRequestWrapper;
import net.linlan.emis.support.manage.enums.SecretType;
import net.linlan.emis.support.security.constant.SecurityConstants;
import net.linlan.emis.support.utils.AesUtils;
import net.linlan.utils.http.HttpResponseUtils;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;

/**
 * 秘钥过滤器
 *
 * @author Linlan
 */
public class FormLoginSecretFilter extends OncePerRequestFilter {
    /**
     * 匹配路径
     */
    private final RequestMatcher requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(
        SecurityConstants.FORM_LOGIN, HttpMethod.POST.name());

    /**
     * 需要认证
     *
     * @param request {@link HttpServletRequest}
     * @return {@link Boolean}
     */
    protected boolean requiresAuthentication(HttpServletRequest request) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        if (requiresAuthentication(request)) {
            // 获取加密密码
            String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
            //拿到秘钥，解密
            try {
                String secret = (String) request.getSession()
                    .getAttribute(SecretType.LOGIN.getPath());
                password = AesUtils.decrypt(password, secret);
            } catch (Exception exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                HttpResponseUtils.flushResponse(response,
                    JSONObject.toJSONString(ResponseResult.error(ExceptionStatus.EX900005.getKey(),
                        ExceptionStatus.EX900005.getValue())));
                return;
            }
            ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request);
            wrapper.addParameter(SPRING_SECURITY_FORM_PASSWORD_KEY, password);
            filterChain.doFilter(wrapper, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
