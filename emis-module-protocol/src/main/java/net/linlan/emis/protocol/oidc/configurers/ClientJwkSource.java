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
package net.linlan.emis.protocol.oidc.configurers;

import java.util.List;
import java.util.Map;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import net.linlan.emis.protocol.base.context.ApplicationContext;
import net.linlan.emis.protocol.base.context.ApplicationContextHolder;
import net.linlan.emis.protocol.oidc.model.OidcProtocolConfig;

/**
 * 客户端JWK
 *
 * @author Linlan
 * Created on 2023/6/27 22:50
 */
public class ClientJwkSource implements JWKSource<SecurityContext> {

    @Override
    public List<JWK> get(JWKSelector jwkSelector,
                         SecurityContext context) throws KeySourceException {
        //根据应用编码获取应用证书
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        Map<String, Object> config = applicationContext.getConfig();
        OidcProtocolConfig protocolConfig = (OidcProtocolConfig) config
            .get(OidcProtocolConfig.class.getName());
        return jwkSelector.select(new JWKSet(protocolConfig.getJwks()));
    }
}
