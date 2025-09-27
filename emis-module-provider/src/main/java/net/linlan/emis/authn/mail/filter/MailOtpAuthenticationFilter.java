/*
 * emis-module-provider - provider身份提供商
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
package net.linlan.emis.authn.mail.filter;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.authn.mail.constant.MailOtpAuthenticationConstants;
import net.linlan.emis.authn.mail.exception.CaptchaNotExistException;
import net.linlan.emis.authn.mail.exception.MailNotExistException;
import net.linlan.emis.manage.core.OtpAuthentication;
import net.linlan.emis.support.exception.InfoValidityFailException;
import net.linlan.emis.uext.OtpContextHelper;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.entity.MessageNoticeChannel;
import net.linlan.emis.user.constant.IdentityProviderType;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.EX000102;

/**
 * AbstractOTPAuthenticationFilter
 *
 * @author Linlan
 */
public class MailOtpAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger               logger                       = LoggerFactory
        .getLogger(MailOtpAuthenticationFilter.class);
    /**
     * 请求方法
     */
    public static final String         METHOD                       = "POST";

    private String                     recipientParameter           = MailOtpAuthenticationConstants.RECIPIENT_KEY;
    private String                     codeParameter                = MailOtpAuthenticationConstants.CODE_KEY;

    public final static String         DEFAULT_FILTER_PROCESSES_URI = MailOtpAuthenticationConstants.OTP_LOGIN
                                                                      + "/mail";

    public static final RequestMatcher MAIL_LOGIN_MATCHER           = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.POST.name());

    /**
     * 是否值处理POST请求
     */
    private boolean                    postOnly                     = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            if (postOnly && !METHOD.equalsIgnoreCase(request.getMethod())) {
                throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
            }
            // 获取手机号/邮箱
            String recipient = Objects.toString(obtainUsername(request), "").trim();
            if (StringUtils.isBlank(recipient)) {
                throw new MailNotExistException();
            }
            String code = Objects.toString(obtainCode(request), "").trim();
            if (StringUtils.isBlank(code)) {
                throw new CaptchaNotExistException();
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(recipient);
            IdentityProviderType type = checkOtp(recipient, code);
            OtpAuthentication authentication = new OtpAuthentication(userDetails, recipient,
                type.value(), userDetails.getAuthorities());
            // Allow subclasses to set the "details" property
            setDetails(request, authentication);
            return authentication;
        } catch (Exception e) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.BAD_REQUEST.value(),
                ResponseResult.error(EX000102.getKey(), EX000102.getValue()));
            return null;
        }
    }

    public IdentityProviderType checkOtp(String recipient, String code) {
        Boolean checkOtp = otpContextHelp.checkOtp(MailType.LOGIN.getKey(),
            MessageNoticeChannel.MAIL, recipient, code);
        if (!checkOtp) {
            logger.error("用户邮箱: [{}], 验证码: [{}] 认证失败", recipient, code);
            throw new InfoValidityFailException(EX000102.getValue());
        }
        return IdentityProviderType.MAIL;
    }

    public String getFilterProcessesUri() {
        return DEFAULT_FILTER_PROCESSES_URI;
    }

    /**
     * Enables subclasses to override the composition of the username, such as
     * by including additional values and a separator.
     *
     * @param request so that request attributes can be retrieved
     * @return the username that will be presented in the
     * Authenticationcore.encrypt.EncryptContextHelper request token to the
     * AuthenticationManagercore.encrypt.EncryptContextHelper
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(recipientParameter);
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }

    /**
     * Provided so that subclasses may configure what is put into the
     * authentication request's details property.
     *
     * @param request     that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     *                    set
     */
    protected void setDetails(HttpServletRequest request, OtpAuthentication authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    /**
     * Defines whether only HTTP POST requests will be allowed by this filter.
     * If set to true, and an authentication request is received which is not a
     * POST request, an exception will be raised immediately and authentication
     * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
     * will be called as if handling a failed authentication.
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     * @param postOnly  仅仅提交方式
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getRecipientParameter() {
        return recipientParameter;
    }

    public final String getCodeParameter() {
        return codeParameter;
    }

    public void setRecipientParameter(String codeParameter) {
        Assert.hasText(recipientParameter, "Email parameter must not be empty or null");
        this.recipientParameter = codeParameter;
    }

    public void setCodeParameter(String codeParameter) {
        Assert.hasText(codeParameter, "Code parameter must not be empty or null");
        this.codeParameter = codeParameter;
    }

    public static RequestMatcher getRequestMatcher() {
        return MAIL_LOGIN_MATCHER;
    }

    private final OtpContextHelper   otpContextHelp;

    private final UserDetailsService userDetailsService;

    public MailOtpAuthenticationFilter(UserDetailsService userDetailsService,
                                       OtpContextHelper otpContextHelp) {
        super(MAIL_LOGIN_MATCHER);
        this.userDetailsService = userDetailsService;
        this.otpContextHelp = otpContextHelp;
    }
}
