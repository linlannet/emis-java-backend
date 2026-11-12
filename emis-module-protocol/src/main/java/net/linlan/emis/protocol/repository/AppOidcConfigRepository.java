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
package net.linlan.emis.protocol.repository;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.linlan.emis.protocol.dto.ProtocolOidcConfigDto;
import net.linlan.emis.protocol.entity.ProtocolOidcConfig;
import net.linlan.emis.protocol.service.ProtocolOidcConfigService;

/**
 * OIDC 应用配置数据访问适配器。
 *
 * @author Linlan
 */
@Component
public class AppOidcConfigRepository {

    @Resource
    private ProtocolOidcConfigService protocolOidcConfigService;

    public ProtocolOidcConfig save(ProtocolOidcConfig entity) {
        protocolOidcConfigService.save(entity);
        return entity;
    }

    public void deleteById(String id) {
        protocolOidcConfigService.deleteById(id);
    }

    public void deleteByAppId(String appId) {
        protocolOidcConfigService.deleteByAppId(appId);
    }

    public Optional<ProtocolOidcConfig> findByAppId(String appId) {
        ProtocolOidcConfigDto dto = protocolOidcConfigService.findByAppId(appId);
        return Optional.ofNullable(dto);
    }

    public ProtocolOidcConfigDto getByAppId(String appId) {
        return protocolOidcConfigService.getByAppId(appId);
    }

    public ProtocolOidcConfigDto getByClientId(String clientId) {
        return protocolOidcConfigService.getByClientId(clientId);
    }

    public ProtocolOidcConfigDto findByAppCode(String appCode) {
        return protocolOidcConfigService.getByAppCode(appCode);
    }
}
