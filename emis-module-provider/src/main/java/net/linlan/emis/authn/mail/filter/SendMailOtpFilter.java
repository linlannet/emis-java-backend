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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.authn.mail.constant.MailOtpAuthenticationConstants;
import net.linlan.emis.authn.mail.exception.MailNotExistException;
import net.linlan.emis.uext.OtpContextHelper;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.service.UserInfoService;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.security.constant.SecurityConstants.LOGIN_PATH;
import static net.linlan.emis.uext.entity.MessageNoticeChannel.MAIL;

/**
 * 发送OTP
 *
 * @author Linlan
 */
public class SendMailOtpFilter extends OncePerRequestFilter {

    /**
     * 邮件验证码登录路径
     */
    public static final String         LOGIN_MAIL_SEND       = LOGIN_PATH + "/mail/send";

    public static final RequestMatcher MAIL_SEND_OPT_MATCHER = new AntPathRequestMatcher(
        LOGIN_MAIL_SEND, HttpMethod.POST.name());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!getRequestMatcher().matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String recipient = request.getParameter(MailOtpAuthenticationConstants.RECIPIENT_KEY);
        if (StringUtils.isBlank(recipient)) {
            throw new MailNotExistException();
        }
        boolean isSend = sendOtp(recipient);
        //发送OTP
        if (isSend) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                ResponseResult.ok());
            return;
        }
        HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
            ResponseResult.error("请输入正确的邮箱"));
    }

    public RequestMatcher getRequestMatcher() {
        return MAIL_SEND_OPT_MATCHER;
    }

    public boolean sendOtp(String recipient) {
        //判断是否存在用户
        UserInfoDto user = userRepository.getByEmail(recipient);
        if (ObjectUtils.isNotEmpty(user)) {
            otpContextHelp.sendOtp(recipient, MailType.LOGIN.getKey(), MAIL);
            return true;
        } else {
            logger.warn("发送验证码登录失败, 手机号不存在: [{" + recipient + "}]");
        }
        return false;
    }

    private final UserInfoService  userRepository;
    private final OtpContextHelper otpContextHelp;

    public SendMailOtpFilter(UserInfoService userRepository, OtpContextHelper otpContextHelp) {
        this.userRepository = userRepository;
        this.otpContextHelp = otpContextHelp;
    }
}
