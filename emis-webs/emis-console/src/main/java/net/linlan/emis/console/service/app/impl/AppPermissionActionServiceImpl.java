/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.service.app.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.entity.AppPermissionResource;
import net.linlan.emis.console.converter.app.AppPermissionActionConverter;
import net.linlan.emis.console.pojo.query.app.AppPermissionActionListQuery;
import net.linlan.emis.console.pojo.result.app.AppPermissionActionListResult;
import net.linlan.emis.console.service.app.AppPermissionActionService;

/**
 * <p>
 * 资源权限 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-10
 */
@Service
@RequiredArgsConstructor
public class AppPermissionActionServiceImpl implements AppPermissionActionService {

    /**
     * 获取资源列表
     *
     * @param query {@link AppPermissionActionListQuery}
     * @return {@link AppPermissionActionListResult}
     */
    @Override
    public List<AppPermissionActionListResult> getPermissionActionList(AppPermissionActionListQuery query) {
        StringMap map = new StringMap();
        if (StringUtils.isNotBlank(query.getName())) {
            map.put("name", query.getName());
        }
        if (StringUtils.isNotBlank(query.getId())) {
            map.put("id", query.getId());
        }
        if (StringUtils.isNotBlank(query.getAppId())) {
            map.put("appId", query.getAppId());
        }
        List<AppPermissionResource> list = appPermissionResourceService.getList(map.map());
        return appPermissionActionConverter.entityConvertToResourceActionListResult(list);
    }

    private final net.linlan.emis.app.service.AppPermissionResourceService appPermissionResourceService;

    private final AppPermissionActionConverter    appPermissionActionConverter;
}
