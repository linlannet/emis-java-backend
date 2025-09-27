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

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.user.dto.UserThirdPartyDto;
import net.linlan.emis.user.entity.UserThirdParty;
import net.linlan.emis.user.param.UserThirdPartyParam;

/**
*
* UserThirdParty数据域:三方用户持久层Dao
* <br>
* @author Linlan
* CreateTime 2025-09-06 15:46:35<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@Mapper
public interface UserThirdPartyDao extends MybatisBaseDao<UserThirdParty> {

    /** get the page of dto UserThirdPartyDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的三方用户数据 {@link Page} 对象，包含 {@link UserThirdParty} 列表
     */
    Page<UserThirdPartyDto> getPageDto(UserThirdPartyParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserThirdPartyDto}
     * @param id 主键ID|1
     * @return {@link UserThirdPartyDto}
     */
    UserThirdPartyDto getDtoById(String id);

    List<UserThirdPartyDto> getByParams(UserThirdPartyParam param);
}
