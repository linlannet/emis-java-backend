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
package net.linlan.emis.protocol.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.protocol.dao.ProtocolCasConfigDao;
import net.linlan.emis.protocol.dto.ProtocolCasConfigDto;
import net.linlan.emis.protocol.entity.ProtocolCasConfig;
import net.linlan.emis.protocol.param.ProtocolCasConfigParam;

/**
 *
 * ProtocolCasConfig数据域:协议CAS配置服务类
 * @author Linlan
 * 
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Service
public class ProtocolCasConfigService {

    @Resource
    private ProtocolCasConfigDao dao;

    /** get the list of entity ProtocolCasConfig
     * 列表方法，返回{@link ProtocolCasConfig} 列表
     * @param map the input select conditions
     * @return 列表的协议CAS配置数据 {@link List} 对象，包含 {@link ProtocolCasConfig} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<ProtocolCasConfig> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link ProtocolCasConfig}
     * @param id 主键ID|1
     * @return {@link ProtocolCasConfig}
     */
    public ProtocolCasConfig findById(String id) {
        if (StringUtils.isBlank(id)){
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param protocolCasConfig the input protocolCasConfig
     */
    //@Transactional
    public void save(ProtocolCasConfig protocolCasConfig) {
        protocolCasConfig.init();
        dao.save(protocolCasConfig);
    }

    /** batch save the entity with input list
     * 保存对象协议CAS配置，批量保存list
     * @param list {@link ProtocolCasConfig}
     */
    public void batchSave(List<ProtocolCasConfig> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param protocolCasConfig the input protocolCasConfig
     */
    //@Transactional
    public void update(ProtocolCasConfig protocolCasConfig) {
        dao.update(protocolCasConfig);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id 主键ID|1
     */
    public void deleteById(String id) {
        if (StringUtils.isBlank(id)){
            return;
        }
        dao.deleteById(id);
    }

    /** batch delete the entity by input ids
     * 批量删除方法，通过ids删除对象
     * @param ids the input ids
     */
    //@Transactional
    public void deleteByIds(String[] ids) {
        if(ObjectUtils.isEmpty(ids)){
            return;
        }
        dao.deleteByIds(ids);
    }

    /** query the total count by input select conditions
     * 通过输入的条件查询记录总数
     * @param map the input select conditions
     * @return total count
     */
    public int getCount(Map<String, Object> map) {
        return dao.getCount(map);
    }

    /** get the page of dto ProtocolCasConfigDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的协议CAS配置数据 {@link Page} 对象，包含 {@link ProtocolCasConfig} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<ProtocolCasConfigDto> getPageDto(ProtocolCasConfigParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link ProtocolCasConfigDto}
     * @param id 主键ID|1
     * @return {@link ProtocolCasConfigDto}
     */
    public ProtocolCasConfigDto getDtoById(String id) {
        if (StringUtils.isBlank(id)){
            return null;
        }
        return dao.getDtoById(id);
    }
    /**
     * 按应用 ID 删除
     *
     * @param appId 应用ID
     */
    @CacheEvict(allEntries = true)
    public void deleteByAppId(String appId) {
        dao.deleteByAppId(appId);
    }

    /**
     * 根据应用ID获取配置
     *
     * @param appId 应用ID
     * @return {@link ProtocolCasConfig}
     */
    public ProtocolCasConfigDto getByAppId(String appId) {
        return dao.getByAppId(appId);
    }

    /**
     * 根据应用code获取应用
     *
     * @param appCode {@link String}
     * @return {@link ProtocolCasConfig}
     */
    public ProtocolCasConfigDto getByAppCode(String appCode) {
        return dao.getByAppCode(appCode);
    }
}
