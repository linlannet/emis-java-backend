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
package net.linlan.emis.app.service;

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
import net.linlan.emis.app.dao.AppAccessPolicyDao;
import net.linlan.emis.app.dto.AppAccessPolicyDto;
import net.linlan.emis.app.entity.AppAccessPolicy;
import net.linlan.emis.app.param.AppAccessPolicyParam;

/**
 *
 * AppAccessPolicy数据域:应用访问授权策略服务类
 * @author Linlan
 *
 *
 */
@Service
public class AppAccessPolicyService {

    @Resource
    private AppAccessPolicyDao dao;

    /** get the list of entity AppAccessPolicy
     * 列表方法，返回{@link AppAccessPolicy} 列表
     * @param map the input select conditions
     * @return 列表的应用访问授权策略数据 {@link List} 对象，包含 {@link AppAccessPolicy} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AppAccessPolicy> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AppAccessPolicy}
     * @param id 主键ID|1
     * @return {@link AppAccessPolicy}
     */
    public AppAccessPolicy findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param appAccessPolicy the input appAccessPolicy
     */
    //@Transactional
    public void save(AppAccessPolicy appAccessPolicy) {
        appAccessPolicy.init();
        dao.save(appAccessPolicy);
    }

    /** batch save the entity with input list
     * 保存对象应用访问授权策略，批量保存list
     * @param list {@link AppAccessPolicy}
     */
    public void batchSave(List<AppAccessPolicy> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param appAccessPolicy the input appAccessPolicy
     */
    //@Transactional
    public void update(AppAccessPolicy appAccessPolicy) {
        dao.update(appAccessPolicy);
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

    /** get the page of dto AppAccessPolicyDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用访问授权策略数据 {@link Page} 对象，包含 {@link AppAccessPolicy} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AppAccessPolicyDto> getPageDto(AppAccessPolicyParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppAccessPolicyDto}
     * @param id 主键ID|1
     * @return {@link AppAccessPolicyDto}
     */
    public AppAccessPolicyDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 根据应用ID删除所有数据
     *
     * @param appId 应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByAppId(String appId) {
        dao.deleteAllByAppId(appId);
    }

    /**
     * 根据主体ID删除所有数据
     *
     * @param subjectId 授权主体Id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllBySubjectId(String subjectId) {
        dao.deleteAllBySubjectId(subjectId);
    }

    /**
     * 根据主体ID和应用列表删除所有数据
     *
     * @param subjectId 授权主体Id
     * @param appIds 应用IDs
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllBySubjectIdAndAppIdIn(@Param("subjectId") String subjectId,
                                               @Param("appIds") String[] appIds) {
        dao.deleteAllBySubjectIdAndAppIdIn(subjectId, appIds);
    }

    /**
     * 根据应用ID、主体ID，主体类型查询
     *
     * @param appId 应用ID
     * @param subjectId 授权主体Id
     * @param subjectType 授权主体类型
     * @return {@link AppAccessPolicy}
     */
    public AppAccessPolicy findByParams(String appId, String subjectId, String subjectType) {
        List<AppAccessPolicy> list = getList(new StringMap().put("appId", appId)
            .put("subjectId", subjectId).put("subjectType", subjectType).map());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 修改应用访问授权状态
     *
     * @param id     主键ID
     * @param enabled 状态
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateStatus(@Param(value = "id") String id,
                                @Param(value = "enabled") Boolean enabled) {
        AppAccessPolicy accessPolicy = findById(id);
        if (ObjectUtils.isNotEmpty(accessPolicy)) {
            accessPolicy.setIsEnabled(enabled);
            update(accessPolicy);
        }
        return 0;
    }

}
