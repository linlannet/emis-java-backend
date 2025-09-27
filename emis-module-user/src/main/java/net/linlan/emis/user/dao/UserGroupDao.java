/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.user.dto.UserGroupDto;
import net.linlan.emis.user.entity.UserGroup;
import net.linlan.emis.user.param.UserGroupParam;

/**
*
* UserGroup数据域:用户分组持久层Dao
* <br>
* @author Linlan
* CreateTime 2025-09-06 15:46:35<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@Mapper
public interface UserGroupDao extends MybatisBaseDao<UserGroup> {

    /** get the page of dto UserGroupDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户分组数据 {@link Page} 对象，包含 {@link UserGroup} 列表
     */
    Page<UserGroupDto> getPageDto(UserGroupParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserGroupDto}
     * @param id 主键ID|1
     * @return {@link UserGroupDto}
     */
    UserGroupDto getDtoById(String id);

}
