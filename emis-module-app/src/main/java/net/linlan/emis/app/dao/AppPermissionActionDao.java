/*
 * emis-module-app - app应用管理
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
package net.linlan.emis.app.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.app.dto.AppPermissionActionDto;
import net.linlan.emis.app.entity.AppPermissionAction;
import net.linlan.emis.app.param.AppPermissionActionParam;

/**
*
* AppPermissionAction数据域:应用权限项持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:07:57
*
*/
@Mapper
public interface AppPermissionActionDao extends MybatisBaseDao<AppPermissionAction> {

    /** get the page of dto AppPermissionActionDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用权限项数据 {@link Page} 对象，包含 {@link AppPermissionAction} 列表
     */
    Page<AppPermissionActionDto> getPageDto(AppPermissionActionParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppPermissionActionDto}
     * @param id 主键ID|1
     * @return {@link AppPermissionActionDto}
     */
    AppPermissionActionDto getDtoById(String id);

}
