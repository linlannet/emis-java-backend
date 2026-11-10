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
import net.linlan.emis.app.dto.AppPermissionPolicyDto;
import net.linlan.emis.app.entity.AppPermissionPolicy;
import net.linlan.emis.app.param.AppPermissionPolicyParam;

/**
*
* AppPermissionPolicy数据域:应用权限策略持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:07:57
*
*/
@Mapper
public interface AppPermissionPolicyDao extends MybatisBaseDao<AppPermissionPolicy> {

    /** get the page of dto AppPermissionPolicyDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用权限策略数据 {@link Page} 对象，包含 {@link AppPermissionPolicy} 列表
     */
    Page<AppPermissionPolicyDto> getPageDto(AppPermissionPolicyParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppPermissionPolicyDto}
     * @param id 主键ID|1
     * @return {@link AppPermissionPolicyDto}
     */
    AppPermissionPolicyDto getDtoById(String id);

    void deleteAllBySubjectIdIn(String[] subjectIds);

    void deleteAllByObjectIdIn(String[] objectIds);

    void deleteAllByObjectId(String objectId);
}
