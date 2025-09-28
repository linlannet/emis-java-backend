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
import net.linlan.emis.app.dao.AppPermissionRoleDao;
import net.linlan.emis.app.dto.AppPermissionRoleDto;
import net.linlan.emis.app.entity.AppPermissionRole;
import net.linlan.emis.app.param.AppPermissionRoleParam;

/**
 *
 * AppPermissionRole数据域:应用权限角色服务类
 * @author Linlan
 *
 *
 */
@Service
public class AppPermissionRoleService {

    @Resource
    private AppPermissionRoleDao dao;

    /** get the list of entity AppPermissionRole
     * 列表方法，返回{@link AppPermissionRole} 列表
     * @param map the input select conditions
     * @return 列表的应用权限角色数据 {@link List} 对象，包含 {@link AppPermissionRole} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AppPermissionRole> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AppPermissionRole}
     * @param id 主键ID|1
     * @return {@link AppPermissionRole}
     */
    public AppPermissionRole findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param appPermissionRole the input appPermissionRole
     */
    //@Transactional
    public void save(AppPermissionRole appPermissionRole) {
        appPermissionRole.init();
        dao.save(appPermissionRole);
    }

    /** batch save the entity with input list
     * 保存对象应用权限角色，批量保存list
     * @param list {@link AppPermissionRole}
     */
    public void batchSave(List<AppPermissionRole> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param appPermissionRole the input appPermissionRole
     */
    //@Transactional
    public void update(AppPermissionRole appPermissionRole) {
        dao.update(appPermissionRole);
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

    /** get the page of dto AppPermissionRoleDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用权限角色数据 {@link Page} 对象，包含 {@link AppPermissionRole} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AppPermissionRoleDto> getPageDto(AppPermissionRoleParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppPermissionRoleDto}
     * @param id 主键ID|1
     * @return {@link AppPermissionRoleDto}
     */
    public AppPermissionRoleDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 更新角色状态
     *
     * @param id      {@link String}
     * @param enabled {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateStatus(@Param(value = "id") String id,
                         @Param(value = "enabled") Boolean enabled) {
        AppPermissionRole accessPolicy = findById(id);
        if (ObjectUtils.isNotEmpty(accessPolicy)) {
            accessPolicy.setIsEnabled(enabled);
            update(accessPolicy);
        }
        return 0;
    }

}
