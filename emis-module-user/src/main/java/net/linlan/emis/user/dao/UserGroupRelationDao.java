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
import org.springframework.data.repository.query.Param;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.user.dto.UserGroupRelationDto;
import net.linlan.emis.user.entity.UserGroupRelation;
import net.linlan.emis.user.param.UserGroupRelationParam;

/**
*
* UserGroupRelation数据域:用户分组成员持久层Dao
* <br>
* @author Linlan
* CreateTime 2025-09-06 15:46:35<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@Mapper
public interface UserGroupRelationDao extends MybatisBaseDao<UserGroupRelation> {

    /** get the page of dto UserGroupRelationDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户分组成员数据 {@link Page} 对象，包含 {@link UserGroupRelation} 列表
     */
    Page<UserGroupRelationDto> getPageDto(UserGroupRelationParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserGroupRelationDto}
     * @param id 主键ID|1
     * @return {@link UserGroupRelationDto}
     */
    UserGroupRelationDto getDtoById(String id);

    List<UserGroupRelation> findByUserId(@Param("userId") String userId);

    List<UserGroupRelation> findByGroupId(@Param("groupId") String groupId);

    void deleteByUserId(@Param("userId") String userId);

    void deleteByGroupId(@Param("groupId") String groupId);

    void deleteAllByUserIdsIn(@Param("userIds") String[] userIds);

    void deleteByGroupIdAndUserId(@Param("groupId") String groupId, @Param("userId") String userId);

}
