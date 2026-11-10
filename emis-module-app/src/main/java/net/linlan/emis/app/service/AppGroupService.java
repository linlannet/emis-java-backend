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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.app.dao.AppGroupDao;
import net.linlan.emis.app.dto.AppGroupDto;
import net.linlan.emis.app.entity.AppGroup;
import net.linlan.emis.app.param.AppGroupParam;

/**
 *
 * AppGroup数据域:应用分组服务类
 * @author Linlan
 *
 *
 */
@Service
public class AppGroupService {

    @Resource
    private AppGroupDao dao;

    /** get the list of entity AppGroup
     * 列表方法，返回{@link AppGroup} 列表
     * @param map the input select conditions
     * @return 列表的应用分组数据 {@link List} 对象，包含 {@link AppGroup} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AppGroup> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AppGroup}
     * @param id 主键ID|1
     * @return {@link AppGroup}
     */
    public AppGroup findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param appGroup the input appGroup
     */
    //@Transactional
    public void save(AppGroup appGroup) {
        appGroup.init();
        dao.save(appGroup);
    }

    /** batch save the entity with input list
     * 保存对象应用分组，批量保存list
     * @param list {@link AppGroup}
     */
    public void batchSave(List<AppGroup> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param appGroup the input appGroup
     */
    //@Transactional
    public void update(AppGroup appGroup) {
        dao.update(appGroup);
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

    /** get the page of dto AppGroupDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用分组数据 {@link Page} 对象，包含 {@link AppGroup} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AppGroupDto> getPageDto(AppGroupParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppGroupDto}
     * @param id 主键ID|1
     * @return {@link AppGroupDto}
     */
    public AppGroupDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 根据code查询
     *
     * @param code 代码编码
     * @return {@link AppGroup}
     */
    public AppGroup findByCode(@Param("code") String code) {
        return dao.findByCode(code);
    }

    /**
     * 编码存在校验
     *
     * @param code 代码编码
     * @return {@link boolean}
     */
    public boolean existsByCode(String code) {
        return dao.existsByCode(code);
    }

    /**
     * 查询应用组列表
     *
     * @param subjectIds 授权主体Ids
     * @param param 查询条件
     * @return    结果列表
     */
    public List<AppGroupDto> getListBySubjectIdsAndParam(List<String> subjectIds,
                                                         AppGroupParam param) {
        return dao.getListBySubjectIdsAndParam(subjectIds, param);
    }

    /**
     * 根据当前用户和分组获取应用数量
     *
     * @param groupId 组编号ID
     * @param subjectIds 授权主体Ids
     * @return    结果
     */
    public int getAppCount(List<String> subjectIds, String groupId) {
        return dao.getAppCount(subjectIds, groupId);
    }
}
