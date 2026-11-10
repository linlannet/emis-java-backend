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
package net.linlan.emis.protocol.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.protocol.dao.ProtocolJwtConfigDao;
import net.linlan.emis.protocol.dto.ProtocolJwtConfigDto;
import net.linlan.emis.protocol.entity.ProtocolFormConfig;
import net.linlan.emis.protocol.entity.ProtocolJwtConfig;
import net.linlan.emis.protocol.param.ProtocolJwtConfigParam;

/**
 *
 * ProtocolJwtConfig数据域:应用JWT配置服务类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Service
public class ProtocolJwtConfigService {

    @Resource
    private ProtocolJwtConfigDao dao;

    /** get the list of entity ProtocolJwtConfig
     * 列表方法，返回{@link ProtocolJwtConfig} 列表
     * @param map the input select conditions
     * @return 列表的应用JWT配置数据 {@link List} 对象，包含 {@link ProtocolJwtConfig} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<ProtocolJwtConfig> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link ProtocolJwtConfig}
     * @param id 主键ID|1
     * @return {@link ProtocolJwtConfig}
     */
    public ProtocolJwtConfig findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param protocolJwtConfig the input protocolJwtConfig
     */
    //@Transactional
    public void save(ProtocolJwtConfig protocolJwtConfig) {
        protocolJwtConfig.init();
        dao.save(protocolJwtConfig);
    }

    /** batch save the entity with input list
     * 保存对象应用JWT配置，批量保存list
     * @param list {@link ProtocolJwtConfig}
     */
    public void batchSave(List<ProtocolJwtConfig> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param protocolJwtConfig the input protocolJwtConfig
     */
    //@Transactional
    public void update(ProtocolJwtConfig protocolJwtConfig) {
        dao.update(protocolJwtConfig);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id 主键ID|1
     */
    public void deleteById(String id) {
        if (StringUtils.isBlank(id)) {
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
        if (ObjectUtils.isEmpty(ids)) {
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

    /** get the page of dto ProtocolJwtConfigDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用JWT配置数据 {@link Page} 对象，包含 {@link ProtocolJwtConfig} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<ProtocolJwtConfigDto> getPageDto(ProtocolJwtConfigParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link ProtocolJwtConfigDto}
     * @param id 主键ID|1
     * @return {@link ProtocolJwtConfigDto}
     */
    public ProtocolJwtConfigDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 按应用 ID 删除
     *
     * @param appId 应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByAppId(@Param("appId") String appId) {
        dao.deleteByAppId(appId);
    }

    /**
     * 根据应用ID获取配置
     *
     * @param appId 应用ID
     * @return {@link ProtocolFormConfig}
     */
    public ProtocolJwtConfig findByAppId(String appId) {
        return dao.findByAppId(appId);
    }

    /**
     * 根据应用ID获取
     *
     * @param appId 应用ID
     * @return {@link ProtocolJwtConfigDto}
     */
    public ProtocolJwtConfigDto getByAppId(@Param("appId") String appId) {
        return dao.getByAppId(appId);
    }

    /**
     * 根据应用编码查询应用配置
     *
     * @param appCode {@link String}
     * @return {@link ProtocolJwtConfigDto}
     */
    public ProtocolJwtConfigDto getByAppCode(@Param("appCode") String appCode) {
        return dao.getByAppCode(appCode);
    }

    /**
     * 根据应用 Client 获取
     *
     * @param clientId {@link String}
     * @return {@link ProtocolJwtConfigDto}
     */
    public ProtocolJwtConfigDto getByClientId(@Param("clientId") String clientId) {
        return dao.getByClientId(clientId);
    }

}
