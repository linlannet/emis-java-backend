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
package net.linlan.emis.organ.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.organ.dto.UserOrganRelationDto;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.param.UserOrganRelationParam;

/**
*
* UserOrganRelation数据域:用户机构关系持久层Dao
* 
* @author Linlan
* CreateTime 2025-09-28 18:01:50
* @version 1.0
* @since 1.0
*
*/
@Mapper
public interface UserOrganRelationDao extends MybatisBaseDao<UserOrganRelation> {

    /** get the page of dto UserOrganRelationDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户机构关系数据 {@link Page} 对象，包含 {@link UserOrganRelation} 列表
     */
    Page<UserOrganRelationDto> getPageDto(UserOrganRelationParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserOrganRelationDto}
     * @param id 主键ID|1
     * @return {@link UserOrganRelationDto}
     */
    UserOrganRelationDto getDtoById(String id);

    List<UserOrganRelationDto> getByOrganId(String organId);

    List<UserOrganRelationDto> getByUserId(String userId);

    void deleteByOrganIdAndUserId(@Param("organId") String organId, @Param("userId") String userId);

    void deleteByUserIds(@Param("userIds") String[] userIds);
}
