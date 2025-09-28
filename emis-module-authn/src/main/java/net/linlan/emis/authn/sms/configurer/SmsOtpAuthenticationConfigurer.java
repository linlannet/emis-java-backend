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
package net.linlan.emis.authn.sms.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import lombok.NonNull;
import lombok.Setter;

import net.linlan.emis.authn.sms.filter.SendSmsOtpFilter;
import net.linlan.emis.authn.sms.filter.SmsOtpAuthenticationFilter;
import net.linlan.emis.uext.OtpContextHelper;
import net.linlan.emis.user.service.UserInfoService;
import static net.linlan.emis.support.security.util.HttpSecurityFilterOrderRegistrationUtils.putFilterAfter;

/**
 * 认证配置
 *
 * @author Linlan
 */
public class SmsOtpAuthenticationConfigurer extends
                                            AbstractAuthenticationFilterConfigurer<HttpSecurity, SmsOtpAuthenticationConfigurer, SmsOtpAuthenticationFilter> {
    @Setter
    @NonNull
    private String loginProcessingUrl = SmsOtpAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.addFilterBefore(new SendSmsOtpFilter(userRepository, otpContextHelp),
            OAuth2LoginAuthenticationFilter.class);

        //OTP
        this.setAuthenticationFilter(
            new SmsOtpAuthenticationFilter(userDetailsService, otpContextHelp));
        putFilterAfter(http, this.getAuthenticationFilter(), SendSmsOtpFilter.class);

        //登录处理地址
        super.loginProcessingUrl(this.loginProcessingUrl);
        super.init(http);
    }

    public RequestMatcher getRequestMatcher() {
        return SendSmsOtpFilter.getRequestMatcher();
    }

    private final UserInfoService    userRepository;
    private final UserDetailsService userDetailsService;

    private final OtpContextHelper   otpContextHelp;

    public SmsOtpAuthenticationConfigurer(UserInfoService userRepository,
                                          UserDetailsService userDetailsService,
                                          OtpContextHelper otpContextHelp) {
        Assert.notNull(userDetailsService, "userRepository must not be null");
        Assert.notNull(userDetailsService, "userDetailsService must not be null");
        Assert.notNull(otpContextHelp, "otpContextHelp must not be null");
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.otpContextHelp = otpContextHelp;
    }

    /**
     * Create the {@link RequestMatcher} given a loginProcessingUrl
     *
     * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the
     *                           loginProcessingUrl
     * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
     */
    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    public static SmsOtpAuthenticationConfigurer smsOtp(UserInfoService userRepository,
                                                        UserDetailsService userDetailsService,
                                                        OtpContextHelper otpContextHelp) {
        return new SmsOtpAuthenticationConfigurer(userRepository, userDetailsService,
            otpContextHelp);
    }
}
