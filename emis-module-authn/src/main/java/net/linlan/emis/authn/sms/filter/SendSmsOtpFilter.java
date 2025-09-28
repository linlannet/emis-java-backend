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
package net.linlan.emis.authn.sms.filter;

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
import net.linlan.emis.authn.sms.constant.SmsOtpAuthenticationConstants;
import net.linlan.emis.authn.sms.exception.MobileNotExistException;
import net.linlan.emis.support.utils.MobileNumberUtils;
import net.linlan.emis.uext.OtpContextHelper;
import net.linlan.emis.uext.constant.SmsType;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.service.UserInfoService;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.security.constant.SecurityConstants.LOGIN_PATH;
import static net.linlan.emis.support.utils.MobileNumberUtils.isMobileValidate;
import static net.linlan.emis.uext.entity.MessageNoticeChannel.SMS;

/**
 * 发送OTP
 *
 * @author Linlan
 */
public class SendSmsOtpFilter extends OncePerRequestFilter {
    /**
     * 短信验证码登录路径
     */
    public static final String         LOGIN_SMS_SEND       = LOGIN_PATH + "/sms/send";

    public static final RequestMatcher SMS_SEND_OPT_MATCHER = new AntPathRequestMatcher(
        LOGIN_SMS_SEND, HttpMethod.POST.name());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!getRequestMatcher().matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String recipient = request.getParameter(SmsOtpAuthenticationConstants.RECIPIENT_KEY);
        if (StringUtils.isBlank(recipient)) {
            throw new MobileNotExistException();
        }
        sendOtp(response, recipient);
    }

    public static RequestMatcher getRequestMatcher() {
        return SMS_SEND_OPT_MATCHER;
    }

    public void sendOtp(HttpServletResponse response, String recipient) {
        if (isMobileValidate(recipient)) {
            //判断是否存在用户
            UserInfoDto user = userRepository
                .getByMobile(MobileNumberUtils.getMobileNumber(recipient));
            if (ObjectUtils.isNotEmpty(user)) {
                otpContextHelp.sendOtp(recipient, SmsType.LOGIN.getKey(), SMS);
            } else {
                logger.warn("发送验证码登录失败, 手机号不存在: [{" + recipient + "}]");
            }
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                ResponseResult.ok());
        } else {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                ResponseResult.error("请输入正确的手机号"));
        }
    }

    private final UserInfoService  userRepository;
    private final OtpContextHelper otpContextHelp;

    public SendSmsOtpFilter(UserInfoService userRepository, OtpContextHelper otpContextHelp) {
        this.userRepository = userRepository;
        this.otpContextHelp = otpContextHelp;
    }
}
