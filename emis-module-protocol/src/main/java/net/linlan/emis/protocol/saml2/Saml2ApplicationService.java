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
package net.linlan.emis.protocol.saml2;

import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.constant.Saml2ProtocolConfig;
import net.linlan.emis.protocol.constant.Saml2SsoModel;

/**
 * 应用接口
 *
 * @author Linlan
 * Created on  2022/8/20 23:20
 */
public interface Saml2ApplicationService extends ApplicationService {

    /**
     * 获取SSO Modal
     *
     * @param appId {@link String}
     * @return {@link Saml2SsoModel}
     */
    Saml2SsoModel getSsoModel(String appId);

    /**
     * 获取协议配置
     *
     * @param appId {@link String}
     * @return {@link Saml2ProtocolConfig}
     */
    Saml2ProtocolConfig getProtocolConfig(String appId);
}
