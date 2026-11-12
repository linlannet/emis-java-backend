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
package net.linlan.emis.protocol.saml2.model;

import net.linlan.emis.core.context.PropertiesContextHelper;
import static net.linlan.emis.protocol.constant.ProtocolConstants.*;

/**
 * Saml2ConverterUtils
 *
 * @author Linlan
 * Created on  2022/8/23 22:57
 */
public class Saml2ConverterUtils {
    /**
     * 应用ID
     *
     * @param appCode {@link String}
     * @return {@link Saml2ProtocolEndpoint}
     */
    public static Saml2ProtocolEndpoint getProtocolEndpointDomain(String appCode) {
        Saml2ProtocolEndpoint domain = new Saml2ProtocolEndpoint();
        //IDP
        String baseUrl = PropertiesContextHelper.getPortalPublicBaseUrl();
        //元数据端点
        domain.setIdpMetaEndpoint(baseUrl + Saml2EndpointConstants.SAML_METADATA_PATH
            .replace(APP_CODE_VARIABLE, appCode));
        //EntityId端点
        domain.setIdpEntityIdEndpoint(baseUrl + Saml2EndpointConstants.SAML_METADATA_PATH
            .replace(APP_CODE_VARIABLE, appCode));
        //Sso端点
        domain.setIdpSsoEndpoint(
            baseUrl + Saml2EndpointConstants.SAML_SSO_PATH.replace(APP_CODE_VARIABLE, appCode));
        //Slo端点
        domain.setIdpSloEndpoint(
            baseUrl + Saml2EndpointConstants.SAML_LOGOUT_PATH.replace(APP_CODE_VARIABLE, appCode));
        return domain;
    }
}
