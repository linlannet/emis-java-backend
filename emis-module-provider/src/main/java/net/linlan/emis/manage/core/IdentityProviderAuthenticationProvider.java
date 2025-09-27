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
package net.linlan.emis.manage.core;

import java.io.Serializable;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import lombok.Data;

import net.linlan.emis.manage.IdentityProviderAuthenticationService;
import net.linlan.emis.manage.exception.IdentityProviderUserExistBindException;
import net.linlan.emis.manage.exception.UserBindIdentityProviderException;
import net.linlan.emis.support.manage.enums.SecretType;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.utils.AesUtils;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.utils.http.HttpContextUtils;

/**
 *
 * @author Linlan
 * Created on  2023/8/6 23:45
 */
public class IdentityProviderAuthenticationProvider implements AuthenticationProvider {

    private final Logger                                logger   = LoggerFactory
        .getLogger(IdentityProviderAuthenticationProvider.class);
    protected MessageSourceAccessor                     messages = SpringSecurityMessageSource
        .getAccessor();

    private final UserDetailsService                    userDetailsService;

    private final IdentityProviderAuthenticationService identityProviderAuthenticationService;

    private final PasswordEncoder                       passwordEncoder;

    public IdentityProviderAuthenticationProvider(UserDetailsService userDetailsService,
                                                  IdentityProviderAuthenticationService identityProviderAuthenticationService,
                                                  PasswordEncoder passwordEncoder) {
        Assert.notNull(userDetailsService, "userDetailsService must not be null");
        Assert.notNull(passwordEncoder, "passwordEncoder must not be null");
        Assert.notNull(identityProviderAuthenticationService, "userIdpService must not be null");
        this.identityProviderAuthenticationService = identityProviderAuthenticationService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * nullcore.encrypt.EncryptContextHelper if the AuthenticationProvidercore.encrypt.EncryptContextHelper is unable to support
     * authentication of the passed Authenticationcore.encrypt.EncryptContextHelper object. In such a case,
     * the next AuthenticationProvidercore.encrypt.EncryptContextHelper that supports the presented
     * Authenticationcore.encrypt.EncryptContextHelper class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        IdentityProviderBindAuthentication identityProviderBindAuthentication = (IdentityProviderBindAuthentication) authentication;
        String username = identityProviderBindAuthentication.getUsername();
        String password = identityProviderBindAuthentication.getPassword();
        ValidationUtils.ValidationResult<UsernamePasswordBindIdpRequest> requestValidationResult = ValidationUtils
            .validateEntity(new UsernamePasswordBindIdpRequest(username, password));
        if (requestValidationResult.isHasErrors()) {
            throw new ConstraintViolationException(
                requestValidationResult.getConstraintViolations());
        }
        //拿到秘钥，解密
        try {
            String secret = (String) HttpContextUtils.getSession()
                .getAttribute(SecretType.LOGIN.getPath());
            password = AesUtils.decrypt(password, secret);
        } catch (Exception exception) {
            String content = "用户 [" + username + "] 绑定 IDP 失败, 密码解密异常";
            logger.error(content, exception);
            throw new UserBindIdentityProviderException();
        }
        //进行绑定逻辑
        IdentityProviderNotBindAuthentication identityProviderNotBindAuthentication = (IdentityProviderNotBindAuthentication) SecurityUtils
            .getAuthentication();
        IdentityProviderUserDetails identityProviderUserDetails = (IdentityProviderUserDetails) identityProviderNotBindAuthentication
            .getPrincipal();
        try {
            UserDetails userDetails = retrieveUser(username);
            if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
                logger.debug("Failed to authenticate since password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
            //绑定
            Boolean isExistBind = identityProviderAuthenticationService.checkIdpUserIsExistBind(
                identityProviderUserDetails.getOpenId(),
                identityProviderUserDetails.getProviderId());
            if (isExistBind) {
                logger.error("身份提供商【{}】用户【{}】已存在系统用户绑定",
                    identityProviderUserDetails.getProviderId(),
                    identityProviderUserDetails.getOpenId());
                throw new IdentityProviderUserExistBindException();
            }
            if (identityProviderAuthenticationService.bindUserIdp(userDetails.getAdminId(),
                identityProviderUserDetails)) {
                //构建 IdpAuthentication
                return new IdentityProviderAuthentication(userDetails,
                    identityProviderUserDetails.getProviderType(),
                    identityProviderUserDetails.getProviderId(), userDetails.getAuthorities());
            }
            throw new UserBindIdentityProviderException();
        } catch (UsernameNotFoundException ex) {
            logger.debug("Failed to find user '{}'", username);
            throw new BadCredentialsException(this.messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return IdentityProviderBindAuthentication.class.isAssignableFrom(authentication);
    }

    protected final UserDetails retrieveUser(String username) throws AuthenticationException {
        try {
            UserDetails loadedUser = (UserDetails) this.userDetailsService
                .loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException | InternalAuthenticationServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    @Data
    public static final class UsernamePasswordBindIdpRequest implements Serializable {

        private static final long serialVersionUID = -8631010419770301789L;
        @NotBlank(message = "用户名不能为空")
        private final String      username;
        @NotBlank(message = "密码不能为空")
        private final String      password;

        /**
         * @param username 用户名
         * @param password 密码
         */
        public UsernamePasswordBindIdpRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
