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

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.app.dto.AppGroupAssociationDto;
import net.linlan.emis.app.entity.AppGroupAssociation;
import net.linlan.emis.app.param.AppGroupAssociationParam;

/**
*
* AppGroupAssociation数据域:应用分组组关联持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:07:57
*
*/
@Mapper
public interface AppGroupAssociationDao extends MybatisBaseDao<AppGroupAssociation> {

    /** get the page of dto AppGroupAssociationDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用分组组关联数据 {@link Page} 对象，包含 {@link AppGroupAssociation} 列表
     */
    Page<AppGroupAssociationDto> getPageDto(AppGroupAssociationParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppGroupAssociationDto}
     * @param id 主键ID|1
     * @return {@link AppGroupAssociationDto}
     */
    AppGroupAssociationDto getDtoById(String id);

    void deleteByGroupIdAndAppId(@Param("groupId") String groupId, @Param("appId") String appId);

    void deleteByAppId(String appId);

    List<AppGroupAssociation> findByAppId(String appId);

    void deleteByGroupId(String groupId);

    List<String> findGroupIdsByAppId(String appId);

}
