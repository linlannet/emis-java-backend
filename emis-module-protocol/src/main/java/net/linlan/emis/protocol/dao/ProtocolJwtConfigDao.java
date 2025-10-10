/*
 * emis-java-backend - 基于JAVA开发的企业级多身份管理系统后端工程，依托EFRAME框架开发，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。
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
import net.linlan.emis.protocol.dto.ProtocolJwtConfigDto;
import net.linlan.emis.protocol.entity.ProtocolJwtConfig;
import net.linlan.emis.protocol.param.ProtocolJwtConfigParam;

/**
*
* ProtocolJwtConfig数据域:应用JWT配置持久层Dao
* @author Linlan
* CreateTime 2025-09-06 19:12:33
*
*/
@Mapper
public interface ProtocolJwtConfigDao extends MybatisBaseDao<ProtocolJwtConfig> {

    /** get the page of dto ProtocolJwtConfigDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用JWT配置数据 {@link Page} 对象，包含 {@link ProtocolJwtConfig} 列表
     */
    Page<ProtocolJwtConfigDto> getPageDto(ProtocolJwtConfigParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link ProtocolJwtConfigDto}
     * @param id 主键ID|1
     * @return {@link ProtocolJwtConfigDto}
     */
    ProtocolJwtConfigDto getDtoById(String id);
    void deleteByAppId(String appId);

    ProtocolJwtConfig findByAppId(String appId);

    ProtocolJwtConfigDto getByAppId(String appId);

    ProtocolJwtConfigDto getByAppCode(String appCode);

    ProtocolJwtConfigDto getByClientId(String clientId);

}
