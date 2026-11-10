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
package net.linlan.emis.core.security.captcha;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson2.JSONObject;

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.commons.core.ResponseResult;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.support.constant.EmisConstants;
import net.linlan.emis.support.security.constant.SecurityConstants;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.EX000102;

/**
 * 验证码过滤器
 *
 * @author Linlan
 */
public class CaptchaValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        if (requiresAuthentication(request)) {
            RequestIdUtils.put(UUID.randomUUID().toString());
            boolean validate = captchaValidator.validate(request, response);
            if (!validate) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                HttpResponseUtils.flushResponse(response, JSONObject
                    .toJSONString(ResponseResult.error(EX000102.getKey(), EX000102.getValue())));
                return;
            }
            filterChain.doFilter(request, response);
            RequestIdUtils.remove();
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码
     *
     * @param captcha {@link String}
     * @return boolean
     */
    public boolean validate(String captcha) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        String value = String
            .valueOf(request.getSession().getAttribute(EmisConstants.CAPTCHA_CODE_SESSION));
        return StringUtils.equals(value, captcha);
    }

    /**
     * 需要认证
     *
     * @param request {@link HttpServletRequest}
     * @return {@link Boolean}
     */
    protected boolean requiresAuthentication(HttpServletRequest request) {
        OrRequestMatcher requestMatcher = new OrRequestMatcher(
            //登录
            new AntPathRequestMatcher(SecurityConstants.FORM_LOGIN, HttpMethod.POST.name()),
            //发送OTP
            new AntPathRequestMatcher(SecurityConstants.LOGIN_OTP_SEND, HttpMethod.POST.name()));
        return requestMatcher.matches(request);
    }

    /**
     * CaptchaValidator
     */
    private final CaptchaValidator captchaValidator;

    /**
     *
     * @param captchaValidator {@link CaptchaValidator}
     */
    public CaptchaValidatorFilter(CaptchaValidator captchaValidator) {
        this.captchaValidator = captchaValidator;
    }

}
