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

import net.linlan.emis.app.constant.AppCertUsingType;
import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.app.service.AppCertificationService;

/**
 * 应用证书数据访问适配器，桥接新的 AppCertificationService。
 *
 * @author Linlan
 */
@Component
public class AppCertRepository {

    @Resource
    private AppCertificationService appCertificationService;

    public AppCertification save(AppCertification entity) {
        appCertificationService.save(entity);
        return entity;
    }

    public void deleteById(String id) {
        appCertificationService.deleteById(id);
    }

    public void deleteByAppId(String appId) {
        appCertificationService.deleteAllByAppId(appId);
    }

    public Optional<AppCertification> findById(String id) {
        return Optional.ofNullable(appCertificationService.findById(id));
    }

    public Optional<AppCertification> findByAppIdAndUsingType(String appId,
                                                              AppCertUsingType usingType) {
        return Optional
            .ofNullable(appCertificationService.findByAppIdAndUsingType(appId, usingType.getKey()));
    }
}
