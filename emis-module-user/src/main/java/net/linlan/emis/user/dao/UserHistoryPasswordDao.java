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
import net.linlan.emis.user.dto.UserHistoryPasswordDto;
import net.linlan.emis.user.entity.UserHistoryPassword;
import net.linlan.emis.user.param.UserHistoryPasswordParam;

/**
*
* UserHistoryPassword数据域:用户历史密码持久层Dao
* <br>
* @author Linlan
* CreateTime 2025-09-06 15:46:35<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@Mapper
public interface UserHistoryPasswordDao extends MybatisBaseDao<UserHistoryPassword> {

    /** get the page of dto UserHistoryPasswordDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户历史密码数据 {@link Page} 对象，包含 {@link UserHistoryPassword} 列表
     */
    Page<UserHistoryPasswordDto> getPageDto(UserHistoryPasswordParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserHistoryPasswordDto}
     * @param id 主键ID|1
     * @return {@link UserHistoryPasswordDto}
     */
    UserHistoryPasswordDto getDtoById(String id);

}
