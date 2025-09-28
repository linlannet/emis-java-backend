/*
 * emis-module-authn - authn认证模块
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
package net.linlan.emis.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.manage.core.IdentityProviderBindAuthentication;
import net.linlan.emis.manage.core.IdentityProviderNotBindAuthentication;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.EX000101;
import static net.linlan.emis.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 *
 * @author Linlan
 * Created on  2023/8/6 23:43
 */
@Slf4j
public class IdentityProviderBindUserAuthenticationFilter extends
                                                          AbstractAuthenticationProcessingFilter {

    public final static String         DEFAULT_FILTER_PROCESSES_URI = LOGIN_PATH + "/idp_bind_user";
    public static final RequestMatcher REQUEST_MATCHER              = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.POST.name());

    /**
     * -- SETTER --
     *  Defines whether only HTTP POST requests will be allowed by this filter. If set to
     *  true, and an authentication request is received which is not a POST request, an
     *  exception will be raised immediately and authentication will not be attempted. The
     *  <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
     *  authentication.
     *  <p>
     *  Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    @Setter
    private boolean                    postOnly                     = true;

    public static final String         USER_BIND_USERNAME_KEY       = "username";

    public static final String         USER_BIND_PASSWORD_KEY       = "password";

    private String                     usernameParameter            = USER_BIND_USERNAME_KEY;

    private String                     passwordParameter            = USER_BIND_PASSWORD_KEY;

    /**
     * Performs actual authentication.
     * The implementation should do one of the following:
     * <ol>
     * <li>Return a populated authentication token for the authenticated user, indicating
     * successful authentication</li>
     * <li>Return null, indicating that the authentication process is still in progress.
     * Before returning, the implementation should perform any additional work required to
     * complete the process.</li>
     * <li>Throw an <tt>AuthenticationException</tt> if the authentication process
     * fails</li>
     * </ol>
     *
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OpenID).
     * @return the authenticated user token, or null if authentication is incomplete.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                "Authentication method not supported: " + request.getMethod());
        }
        Authentication authentication = SecurityUtils.getAuthentication();
        if (!(authentication instanceof IdentityProviderNotBindAuthentication)) {
            return null;
        }
        String username = obtainUsername(request);
        username = (username != null) ? username.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        IdentityProviderBindAuthentication identityProviderBindAuthentication = new IdentityProviderBindAuthentication(
            username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, identityProviderBindAuthentication);
        try {
            return this.getAuthenticationManager().authenticate(identityProviderBindAuthentication);
        }
        //用户名/密码错误异常
        catch (BadCredentialsException e) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.BAD_REQUEST.value(),
                ResponseResult.error(e.getMessage(), EX000101.getKey()));
        }
        //自定义异常
        catch (SupportException e) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.BAD_REQUEST.value(),
                ResponseResult.error(e.getMessage(), e.getErrorCode()));
        }
        //return null 为了防止会话丢失。
        return null;
    }

    /**
     * Enables subclasses to override the composition of the password, such as by
     * including additional values and a separator.
     * This might be used for example if a postcode/zipcode was required in addition to
     * the password. A delimiter such as a pipe (|) should be used to separate the
     * password and extended value(s). The AuthenticationDao will need to
     * generate the expected password in a corresponding manner.
     * @param request so that request attributes can be retrieved
     * @return the password that will be presented in the Authentication
     * request token to the AuthenticationManager
     */
    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    /**
     * Enables subclasses to override the composition of the username, such as by
     * including additional values and a separator.
     * @param request so that request attributes can be retrieved
     * @return the username that will be presented in the Authentication
     * request token to the AuthenticationManager
     */
    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    /**
     * Sets the parameter name which will be used to obtain the username from the login
     * request.
     * @param usernameParameter the parameter name. Defaults to "username".
     */
    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    /**
     * Sets the parameter name which will be used to obtain the password from the login
     * request..
     * @param passwordParameter the parameter name. Defaults to "password".
     */
    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     * set
     */
    protected void setDetails(HttpServletRequest request,
                              IdentityProviderBindAuthentication authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public IdentityProviderBindUserAuthenticationFilter() {
        super(REQUEST_MATCHER);
    }

    public IdentityProviderBindUserAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }
}
