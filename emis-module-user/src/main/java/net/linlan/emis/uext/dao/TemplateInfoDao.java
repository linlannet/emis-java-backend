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
package net.linlan.emis.uext.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.uext.dto.TemplateInfoDto;
import net.linlan.emis.uext.entity.TemplateInfo;
import net.linlan.emis.uext.param.TemplateInfoParam;

/**
*
* TemplateInfo数据域:模板信息配置持久层Dao
* @author Linlan
* CreateTime 2025-09-07 18:33:53
*
*/
@Mapper
public interface TemplateInfoDao extends MybatisBaseDao<TemplateInfo> {

    /** get the page of dto TemplateInfoDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的模板信息配置数据 {@link Page} 对象，包含 {@link TemplateInfo} 列表
     */
    Page<TemplateInfoDto> getPageDto(TemplateInfoParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link TemplateInfoDto}
     * @param id 模板ID|1
     * @return {@link TemplateInfoDto}
     */
    TemplateInfoDto getDtoById(String id);

    void deleteByType(@Param("type") String type);
}
