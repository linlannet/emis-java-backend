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
import net.linlan.emis.app.dto.AppGroupDto;
import net.linlan.emis.app.entity.AppGroup;
import net.linlan.emis.app.param.AppGroupParam;

/**
*
* AppGroup数据域:应用分组持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:07:57
*
*/
@Mapper
public interface AppGroupDao extends MybatisBaseDao<AppGroup> {

    /** get the page of dto AppGroupDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用分组数据 {@link Page} 对象，包含 {@link AppGroup} 列表
     */
    Page<AppGroupDto> getPageDto(AppGroupParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppGroupDto}
     * @param id 主键ID|1
     * @return {@link AppGroupDto}
     */
    AppGroupDto getDtoById(String id);

    AppGroup findByCode(String code);

    boolean existsByCode(String code);

    List<AppGroupDto> getListBySubjectIdsAndParam(@Param("subjectIds") List<String> subjectIds,
                                                  AppGroupParam param);

    int getAppCount(@Param("subjectIds") List<String> subjectIds, @Param("groupId") String groupId);
}
