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

import net.linlan.emis.protocol.dto.ProtocolSaml2ConfigDto;
import net.linlan.emis.protocol.entity.ProtocolSaml2Config;
import net.linlan.emis.protocol.service.ProtocolSaml2ConfigService;

/**
 * SAML2 应用配置数据访问适配器。
 *
 * @author Linlan
 */
@Component
public class AppSaml2ConfigRepository {

    @Resource
    private ProtocolSaml2ConfigService protocolSaml2ConfigService;

    public ProtocolSaml2Config save(ProtocolSaml2Config entity) {
        protocolSaml2ConfigService.save(entity);
        return entity;
    }

    public void deleteById(String id) {
        protocolSaml2ConfigService.deleteById(id);
    }

    public void deleteByAppId(String appId) {
        protocolSaml2ConfigService.deleteByAppId(appId);
    }

    public Optional<ProtocolSaml2Config> findByAppId(String id) {
        return Optional.ofNullable(protocolSaml2ConfigService.findByAppId(id));
    }

    public ProtocolSaml2ConfigDto getByAppId(String appId) {
        return protocolSaml2ConfigService.getByAppId(appId);
    }

    public ProtocolSaml2ConfigDto findByAppCode(String appCode) {
        return protocolSaml2ConfigService.getByAppCode(appCode);
    }
}
