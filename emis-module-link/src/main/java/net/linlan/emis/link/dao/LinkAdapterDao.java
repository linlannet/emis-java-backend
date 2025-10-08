/*
 * emis-module-link - link连接适配扩展
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
package net.linlan.emis.link.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.link.dto.LinkAdapterDto;
import net.linlan.emis.link.entity.LinkAdapter;
import net.linlan.emis.link.param.LinkAdapterParam;

/**
*
* LinkAdapter数据域:接入适配器持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:35:13
*
*/
@Mapper
public interface LinkAdapterDao extends MybatisBaseDao<LinkAdapter> {

    /** get the page of dto LinkAdapterDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的接入适配器数据 {@link Page} 对象，包含 {@link LinkAdapter} 列表
     */
    Page<LinkAdapterDto> getPageDto(LinkAdapterParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link LinkAdapterDto}
     * @param id 主键ID|1
     * @return {@link LinkAdapterDto}
     */
    LinkAdapterDto getDtoById(String id);

}
