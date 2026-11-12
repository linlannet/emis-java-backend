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
package net.linlan.emis.protocol.form.endpoint.authentication;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;

import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.form.authentication.FormRequestAuthenticationToken;
import net.linlan.emis.protocol.form.constant.FormProtocolConstants;
import net.linlan.emis.protocol.form.exception.FormAuthenticationException;
import net.linlan.emis.protocol.form.exception.FormError;
import net.linlan.emis.protocol.form.exception.FormErrorCodes;
import net.linlan.emis.protocol.form.model.FormProtocolConfig;

/**
 * @author Linlan
 * Created on 2023/7/8 00:14
 */
public final class FormAuthenticationTokenConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        if (MapUtils.isEmpty(context.getConfig())
            | !context.getConfig().containsKey(FormProtocolConfig.class.getName())) {
            FormError error = new FormError(FormErrorCodes.SERVER_ERROR, null,
                FormProtocolConstants.FORM_ERROR_URI);
            throw new FormAuthenticationException(error);
        }
        FormProtocolConfig config = (FormProtocolConfig) context.getConfig()
            .get(FormProtocolConfig.class.getName());
        return new FormRequestAuthenticationToken(authentication, config);
    }

}
