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

import net.linlan.emis.protocol.dto.ProtocolCasConfigDto;
import net.linlan.emis.protocol.entity.ProtocolCasConfig;
import net.linlan.emis.protocol.service.ProtocolCasConfigService;

/**
 * CAS 应用配置数据访问适配器。
 *
 * @author Linlan
 */
@Component
public class AppCasConfigRepository {

    @Resource
    private ProtocolCasConfigService protocolCasConfigService;

    public ProtocolCasConfig save(ProtocolCasConfig entity) {
        protocolCasConfigService.save(entity);
        return entity;
    }

    public void deleteById(String id) {
        protocolCasConfigService.deleteById(id);
    }

    public void deleteByAppId(String appId) {
        protocolCasConfigService.deleteByAppId(appId);
    }

    public Optional<ProtocolCasConfig> findByAppId(String appId) {
        return Optional.ofNullable(protocolCasConfigService.getByAppId(appId));
    }

    public ProtocolCasConfigDto getByAppId(String appId) {
        return protocolCasConfigService.getByAppId(appId);
    }

    public ProtocolCasConfigDto findByAppCode(String appCode) {
        return protocolCasConfigService.getByAppCode(appCode);
    }
}
