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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.service.AppAccountService;

/**
 * 应用账户数据访问适配器，桥接新的 AppAccountService。
 *
 * @author Linlan
 */
@Component
public class AppAccountRepository {

    @Resource
    private AppAccountService appAccountService;

    public AppAccount save(AppAccount entity) {
        appAccountService.save(entity);
        return entity;
    }

    public void deleteById(String id) {
        appAccountService.deleteById(id);
    }

    public List<AppAccount> findByAppIdAndUserId(String appId, String userId) {
        return appAccountService.findByAppIdAndUserId(appId, userId);
    }

    public Optional<AppAccount> findByAppIdAndUserIdAndDefaultedIsTrue(String appId,
                                                                       String userId) {
        List<AppAccount> list = appAccountService.findByAppIdAndUserId(appId, userId);
        if (list == null) {
            return Optional.empty();
        }
        List<AppAccount> defaulted = list.stream()
            .filter(item -> Boolean.TRUE.equals(item.getIsDefault())).collect(Collectors.toList());
        if (!defaulted.isEmpty()) {
            return Optional.of(defaulted.get(0));
        }
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public Optional<AppAccount> findByAppIdAndUserIdAndAccount(String appId, String userId,
                                                               String account) {
        return Optional.ofNullable(appAccountService.findByParams(appId, userId, account));
    }

    public void deleteAllByAppId(String appId) {
        appAccountService.deleteAllByAppId(appId);
    }

    public void deleteByUserId(String userId) {
        appAccountService.deleteByUserId(userId);
    }
}
