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
package net.linlan.emis.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.linlan.emis.app.entity.AppPermissionAction;
import net.linlan.emis.app.entity.AppPermissionResource;
import net.linlan.emis.console.pojo.result.app.AppPermissionActionListResult;
import net.linlan.emis.core.constant.PermissionActionType;

/**
 * 权限映射
 *
 * @author Linlan
 * Created on  2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface AppPermissionActionConverter {

    /**
     * 实体转资源权限结果返回
     *
     * @param list {@link AppPermissionResource}
     * @return {@link AppPermissionActionListResult}
     */
    default List<AppPermissionActionListResult> entityConvertToResourceActionListResult(List<AppPermissionResource> list) {
        List<AppPermissionActionListResult> results = new ArrayList<>();
        List<AppPermissionActionListResult.Action> menus = new ArrayList<>();
        List<AppPermissionActionListResult.Action> apis = new ArrayList<>();
        List<AppPermissionActionListResult.Action> buttons = new ArrayList<>();
        List<AppPermissionActionListResult.Action> others = new ArrayList<>();
        List<AppPermissionActionListResult.Action> datas = new ArrayList<>();
        for (AppPermissionResource resource : list) {
            for (AppPermissionAction action : resource.getActions()) {
                if (PermissionActionType.MENU.equals(action.getType())) {
                    menus.add(actionConvertToResourceActionResult(action));
                }
                if (PermissionActionType.API.equals(action.getType())) {
                    apis.add(actionConvertToResourceActionResult(action));
                }
                if (PermissionActionType.DATA.equals(action.getType())) {
                    datas.add(actionConvertToResourceActionResult(action));
                }
                if (PermissionActionType.BUTTON.equals(action.getType())) {
                    buttons.add(actionConvertToResourceActionResult(action));
                }
                if (PermissionActionType.OTHER.equals(action.getType())) {
                    others.add(actionConvertToResourceActionResult(action));
                }
            }
            AppPermissionActionListResult result = new AppPermissionActionListResult();
            //基本信息
            result.setAppId(resource.getAppId().toString());
            result.setId(resource.getId().toString());
            result.setName(resource.getName());
            result.setEnabled(resource.getIsEnabled());
            result.setDesc(resource.getDescription());
            //权限资源
            result.setButtons(buttons);
            result.setApis(apis);
            result.setDatas(datas);
            result.setMenus(menus);
            result.setOthers(others);
            results.add(result);
        }
        return results;
    }

    /**
     * actionConvertToResourceActionResult
     *
     * @param action {@link AppPermissionAction}
     * @return {@link AppPermissionActionListResult.Action}
     */
    @Mapping(target = "access", source = "content")
    AppPermissionActionListResult.Action actionConvertToResourceActionResult(AppPermissionAction action);

}
