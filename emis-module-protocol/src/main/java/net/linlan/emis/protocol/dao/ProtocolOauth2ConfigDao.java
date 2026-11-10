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
import net.linlan.emis.protocol.dto.ProtocolOauth2ConfigDto;
import net.linlan.emis.protocol.entity.ProtocolOauth2Config;
import net.linlan.emis.protocol.param.ProtocolOauth2ConfigParam;

/**
*
* ProtocolOauth2Config数据域:应用OAUTH2配置持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:12:33
*
*/
@Mapper
public interface ProtocolOauth2ConfigDao extends MybatisBaseDao<ProtocolOauth2Config> {

    /** get the page of dto ProtocolOauth2ConfigDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用OAUTH2配置数据 {@link Page} 对象，包含 {@link ProtocolOauth2Config} 列表
     */
    Page<ProtocolOauth2ConfigDto> getPageDto(ProtocolOauth2ConfigParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link ProtocolOauth2ConfigDto}
     * @param id 主键ID|1
     * @return {@link ProtocolOauth2ConfigDto}
     */
    ProtocolOauth2ConfigDto getDtoById(String id);

}
