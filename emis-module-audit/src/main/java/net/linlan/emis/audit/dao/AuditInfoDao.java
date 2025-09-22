/*
 * emis-module-audit - audit安全审计
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
package net.linlan.emis.audit.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.audit.dto.AuditInfoDto;
import net.linlan.emis.audit.entity.AuditInfo;
import net.linlan.emis.audit.param.AuditInfoParam;

/**
*
* AuditInfo数据域:行为审计持久层Dao
* <br>
* @author Linlan
* CreateTime 2025-09-06 15:55:04<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@Mapper
public interface AuditInfoDao extends MybatisBaseDao<AuditInfo> {

    /** get the page of dto AuditInfoDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的行为审计数据 {@link Page} 对象，包含 {@link AuditInfo} 列表
     */
    Page<AuditInfoDto> getPageDto(AuditInfoParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AuditInfoDto}
     * @param id 主键ID|1
     * @return {@link AuditInfoDto}
     */
    AuditInfoDto getDtoById(String id);

}
