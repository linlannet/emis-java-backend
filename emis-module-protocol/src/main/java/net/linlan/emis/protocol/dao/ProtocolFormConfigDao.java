/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.protocol.dto.ProtocolFormConfigDto;
import net.linlan.emis.protocol.entity.ProtocolFormConfig;
import net.linlan.emis.protocol.param.ProtocolFormConfigParam;

/**
*
* ProtocolFormConfig数据域:表单代填应用配置持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:12:33
*
*/
@Mapper
public interface ProtocolFormConfigDao extends MybatisBaseDao<ProtocolFormConfig> {

    /** get the page of dto ProtocolFormConfigDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的表单代填应用配置数据 {@link Page} 对象，包含 {@link ProtocolFormConfig} 列表
     */
    Page<ProtocolFormConfigDto> getPageDto(ProtocolFormConfigParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link ProtocolFormConfigDto}
     * @param id 主键ID|1
     * @return {@link ProtocolFormConfigDto}
     */
    ProtocolFormConfigDto getDtoById(String id);

    void deleteByAppId(String appId);

    ProtocolFormConfig findByAppId(String appId);

    ProtocolFormConfigDto getByAppId(String appId);

    ProtocolFormConfigDto getByAppCode(String appCode);

    ProtocolFormConfigDto getByClientId(String clientId);

}
