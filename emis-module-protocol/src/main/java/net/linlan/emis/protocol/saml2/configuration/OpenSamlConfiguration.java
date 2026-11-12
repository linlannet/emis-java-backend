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
package net.linlan.emis.protocol.saml2.configuration;

import org.springframework.context.annotation.Configuration;

import net.linlan.emis.protocol.util.SamlUtils;

/**
 * @author Linlan
 * Created on  2022/10/17 21:28
 */
@Configuration
public class OpenSamlConfiguration {
    public OpenSamlConfiguration() {
        // 初始化 OpenSAML
        SamlUtils.initOpenSaml();
        // BouncyCastleProvider
        java.security.Security
            .addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
}
