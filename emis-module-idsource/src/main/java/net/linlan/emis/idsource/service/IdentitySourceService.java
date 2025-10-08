/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.idsource.service;

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
import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.idsource.config.StrategyConfig;
import net.linlan.emis.idsource.dao.IdentitySourceDao;
import net.linlan.emis.idsource.dto.IdentitySourceDto;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.idsource.param.IdentitySourceParam;

/**
 *
 * IdentitySource数据域:身份源配置服务类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:09:58
 *
 */
@Service
public class IdentitySourceService {

    @Resource
    private IdentitySourceDao dao;

    /** get the list of entity IdentitySource
     * 列表方法，返回{@link IdentitySource} 列表
     * @param map the input select conditions
     * @return 列表的身份源配置数据 {@link List} 对象，包含 {@link IdentitySource} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<IdentitySource> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link IdentitySource}
     * @param id 主键ID|1
     * @return {@link IdentitySource}
     */
    public IdentitySource findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param identitySource the input identitySource
     */
    //@Transactional
    public void save(IdentitySource identitySource) {
        identitySource.init();
        dao.save(identitySource);
    }

    /** batch save the entity with input list
     * 保存对象身份源配置，批量保存list
     * @param list {@link IdentitySource}
     */
    public void batchSave(List<IdentitySource> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param identitySource the input identitySource
     */
    //@Transactional
    public void update(IdentitySource identitySource) {
        dao.update(identitySource);
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

    /** get the page of dto IdentitySourceDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的身份源配置数据 {@link Page} 对象，包含 {@link IdentitySource} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<IdentitySourceDto> getPageDto(IdentitySourceParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link IdentitySourceDto}
     * @param id 主键ID|1
     * @return {@link IdentitySourceDto}
     */
    public IdentitySourceDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 查询启用的身份源
     *
     * @return    结果列表
     */
    public List<IdentitySource> findByEnabledIsTrue() {
        List<IdentitySource> list = getList(new StringMap().put("isEnabled", true).map());
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * 更新身份源状态
     *
     * @param id      {@link  Long}
     * @param enabled {@link  Boolean}
     * @return {@link  Boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateIdentitySourceStatus(@Param(value = "id") String id,
                                              @Param(value = "enabled") Boolean enabled) {
        IdentitySource accessPolicy = findById(id);
        if (ObjectUtils.isNotEmpty(accessPolicy)) {
            accessPolicy.setIsEnabled(enabled);
            update(accessPolicy);
        }
        return 0;
    }

    /**
     * 更新身份源策略
     *
     * @param id             {@link Long} 主键
     * @param strategyConfig {@link String} 策略
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStrategyConfig(@Param(value = "id") String id,
                                     @Param(value = "strategyConfig") StrategyConfig strategyConfig) {
        IdentitySource accessPolicy = findById(id);
        if (ObjectUtils.isNotEmpty(accessPolicy)) {
            accessPolicy.setStrategyConfig(strategyConfig);
            update(accessPolicy);
        }
    }

    /**
     * 根据code查询
     *
     * @param code 代码编码
     * @return {@link IdentitySource}
     */
    public IdentitySource findByCode(String code) {
        return dao.findByCode(code);
    }

}
