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
import net.linlan.emis.app.dao.AppGroupAssociationDao;
import net.linlan.emis.app.dto.AppGroupAssociationDto;
import net.linlan.emis.app.entity.AppGroupAssociation;
import net.linlan.emis.app.param.AppGroupAssociationParam;

/**
 *
 * AppGroupAssociation数据域:应用分组组关联服务类
 * @author Linlan
 *
 *
 */
@Service
public class AppGroupAssociationService {

    @Resource
    private AppGroupAssociationDao dao;

    /** get the list of entity AppGroupAssociation
     * 列表方法，返回{@link AppGroupAssociation} 列表
     * @param map the input select conditions
     * @return 列表的应用分组组关联数据 {@link List} 对象，包含 {@link AppGroupAssociation} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AppGroupAssociation> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AppGroupAssociation}
     * @param id 主键ID|1
     * @return {@link AppGroupAssociation}
     */
    public AppGroupAssociation findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param appGroupAssociation the input appGroupAssociation
     */
    //@Transactional
    public void save(AppGroupAssociation appGroupAssociation) {
        appGroupAssociation.init();
        dao.save(appGroupAssociation);
    }

    /** batch save the entity with input list
     * 保存对象应用分组组关联，批量保存list
     * @param list {@link AppGroupAssociation}
     */
    public void batchSave(List<AppGroupAssociation> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param appGroupAssociation the input appGroupAssociation
     */
    //@Transactional
    public void update(AppGroupAssociation appGroupAssociation) {
        dao.update(appGroupAssociation);
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

    /** get the page of dto AppGroupAssociationDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用分组组关联数据 {@link Page} 对象，包含 {@link AppGroupAssociation} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AppGroupAssociationDto> getPageDto(AppGroupAssociationParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppGroupAssociationDto}
     * @param id 主键ID|1
     * @return {@link AppGroupAssociationDto}
     */
    public AppGroupAssociationDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 根据应用组ID和应用ID删除
     *
     * @param groupId 组编号ID
     * @param appId  应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByGroupIdAndAppId(@Param("groupId") String groupId, @Param("appId") String appId) {
        dao.deleteByGroupIdAndAppId(groupId, appId);
    }

    /**
     * 根据应用ID删除关联
     *
     * @param appId {@link  String}
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteByAppId(String appId) {
        dao.deleteByAppId(appId);
    }

    /**
     * 按应用 ID
     *
     * @param appId 应用ID
     * @return    结果列表
     */
    List<AppGroupAssociation> findByAppId(String appId) {
        return dao.findByAppId(appId);
    }

    /**
     * 根据应用组ID删除关联
     *
     * @param groupId 分组ID
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByGroupId(@Param("groupId") String groupId) {
        dao.deleteByGroupId(groupId);
    }

    /**
     * 根据应用ID 查询关联信息
     *
     * @param appId 应用ID
     * @return    结果列表
     */
    List<String> findGroupIdsByAppId(@Param("appId") String appId) {
        return dao.findGroupIdsByAppId(appId);
    }

}
