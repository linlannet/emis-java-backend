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
package net.linlan.emis.protocol.form.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import net.linlan.emis.core.encrypt.EncryptContextHelper;
import net.linlan.emis.core.exception.app.AppAccountNotExistException;
import net.linlan.emis.manage.vo.LoginAccount;
import net.linlan.emis.protocol.constant.FormEncryptType;
import net.linlan.emis.protocol.form.FormAuthorizationService;
import net.linlan.emis.protocol.form.client.FormRegisteredClient;
import net.linlan.emis.protocol.form.constant.FormProtocolConstants;
import net.linlan.emis.protocol.form.exception.FormAuthenticationException;
import net.linlan.emis.protocol.form.exception.FormError;
import net.linlan.emis.protocol.form.exception.FormErrorCodes;
import net.linlan.emis.protocol.form.model.FormProtocolConfig;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.utils.AesUtils;
import static net.linlan.emis.support.security.util.SecurityUtils.isPrincipalAuthenticated;

/**
 *
 * @author Linlan
 * Created on 2023/7/8 00:11
 */
public final class FormAuthenticationTokenProvider implements AuthenticationProvider {
    private final Log logger = LogFactory.getLog(FormAuthenticationTokenProvider.class);

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            FormRequestAuthenticationToken requestAuthenticationToken = (FormRequestAuthenticationToken) authentication;
            Authentication principal = (Authentication) requestAuthenticationToken.getPrincipal();
            if (!isPrincipalAuthenticated(principal)) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace(
                        "Did not authenticate form request since principal not authenticated");
                }
                return authentication;
            }
            FormProtocolConfig config = requestAuthenticationToken.getConfig();
            UserDetails user = ((UserDetails) principal.getPrincipal());
            //@formatter:off
            FormRegisteredClient registeredClient = FormRegisteredClient.builder()
                    .id(config.getAppId())
                    .code(config.getAppCode())
                    .clientId(config.getClientId())
                    .clientName(config.getAppName())
                    .build();
            LoginAccount account = authorizationService.getDefaultLoginAccount(registeredClient, user);
            //@formatter:on
            //密码加密
            String password = getEncryptionField(
                EncryptContextHelper.decrypt(account.getPassword()),
                config.getPasswordEncryptType(), config.getPasswordEncryptKey());
            // 用户加密
            String username = getEncryptionField(account.getUsername(),
                config.getUsernameEncryptType(), config.getUsernameEncryptKey());
            return new FormAuthenticationToken(principal, username, password, config);
        } catch (AppAccountNotExistException exception) {
            FormError error = new FormError(FormErrorCodes.APP_ACCOUNT_NOT_EXIST,
                "App account not exist", FormProtocolConstants.FORM_ERROR_URI);
            throw new FormAuthenticationException(error);
        } catch (Exception exception) {
            FormError error = new FormError(FormErrorCodes.SERVER_ERROR, exception.getMessage(),
                FormProtocolConstants.FORM_ERROR_URI);
            throw new FormAuthenticationException(error);
        }
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * instance of the <code>Authentication</code> class. It simply indicates it can
     * support closer evaluation of it. An <code>AuthenticationProvider</code> can still
     * return <code>null</code> from the {@link #authenticate(Authentication)} method to
     * indicate another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication {@link FormAuthenticationToken}
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(FormRequestAuthenticationToken.class);
    }

    private String getEncryptionField(String fieldValue, FormEncryptType encryptionType,
                                      String encryptionKey) {
        if (encryptionType == null) {
            return fieldValue;
        }
        switch (encryptionType) {
            case BASE64: {
                return Base64.getEncoder()
                    .encodeToString(fieldValue.getBytes(StandardCharsets.UTF_8));
            }
            case AES: {
                return new AesUtils(encryptionKey).encrypt(fieldValue);
            }
            case MD5: {
                return DigestUtils.md5Hex(fieldValue);
            }
        }
        return fieldValue;
    }

    private final FormAuthorizationService authorizationService;

    public FormAuthenticationTokenProvider(FormAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
}
