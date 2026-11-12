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

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.linlan.emis.app.entity.AppGroupAssociation;
import net.linlan.emis.app.service.AppGroupAssociationService;

/**
 * 应用分组关联数据访问适配器，桥接新的 AppGroupAssociationService。
 *
 * @author Linlan
 */
@Component
public class AppGroupAssociationRepository {

    @Resource
    private AppGroupAssociationService appGroupAssociationService;

    public List<AppGroupAssociation> saveAll(List<AppGroupAssociation> list) {
        appGroupAssociationService.batchSave(list);
        return list;
    }

    public void deleteByGroupIdAndAppId(String groupId, String appId) {
        appGroupAssociationService.deleteByGroupIdAndAppId(groupId, appId);
    }

    public void deleteAllByGroupId(String groupId) {
        appGroupAssociationService.deleteAllByGroupId(groupId);
    }
}
