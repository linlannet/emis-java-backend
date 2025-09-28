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

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dao.AppPermissionActionDao;
import net.linlan.emis.app.dto.AppPermissionActionDto;
import net.linlan.emis.app.entity.AppPermissionAction;
import net.linlan.emis.app.param.AppPermissionActionParam;

/**
 *
 * AppPermissionAction数据域:应用权限项服务类
 * @author Linlan
 *
 *
 */
@Service
public class AppPermissionActionService {

    @Resource
    private AppPermissionActionDao dao;

    /** get the list of entity AppPermissionAction
     * 列表方法，返回{@link AppPermissionAction} 列表
     * @param map the input select conditions
     * @return 列表的应用权限项数据 {@link List} 对象，包含 {@link AppPermissionAction} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AppPermissionAction> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AppPermissionAction}
     * @param id 主键ID|1
     * @return {@link AppPermissionAction}
     */
    public AppPermissionAction findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param appPermissionAction the input appPermissionAction
     */
    //@Transactional
    public void save(AppPermissionAction appPermissionAction) {
        appPermissionAction.init();
        dao.save(appPermissionAction);
    }

    /** batch save the entity with input list
     * 保存对象应用权限项，批量保存list
     * @param list {@link AppPermissionAction}
     */
    public void batchSave(List<AppPermissionAction> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param appPermissionAction the input appPermissionAction
     */
    //@Transactional
    public void update(AppPermissionAction appPermissionAction) {
        dao.update(appPermissionAction);
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

    /** get the page of dto AppPermissionActionDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用权限项数据 {@link Page} 对象，包含 {@link AppPermissionAction} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AppPermissionActionDto> getPageDto(AppPermissionActionParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppPermissionActionDto}
     * @param id 主键ID|1
     * @return {@link AppPermissionActionDto}
     */
    public AppPermissionActionDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * findAllByResourceId
     *
     * @param resourceId 资源ID
     * @return    结果列表
     */
    public List<AppPermissionAction> findAllByResourceId(String resourceId) {
        List<AppPermissionAction> list = getList(
            new StringMap().put("resourceId", resourceId).map());
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

}
