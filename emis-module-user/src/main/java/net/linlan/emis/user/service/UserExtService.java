/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.user.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.user.dao.UserExtDao;
import net.linlan.emis.user.dto.UserExtDto;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.param.UserExtParam;

/**
 *
 * UserExt数据域:用户详情服务类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-06 15:46:35<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Service
public class UserExtService {

    @Resource
    private UserExtDao dao;

    /** get the list of entity UserExt
     * 列表方法，返回{@link UserExt} 列表
     * @param map the input select conditions
     * @return 列表的用户详情数据 {@link List} 对象，包含 {@link UserExt} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<UserExt> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link UserExt}
     * @param id 用户ID|1
     * @return {@link UserExt}
     */
    public UserExt findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param userExt the input userExt
     */
    //@Transactional
    public void save(UserExt userExt) {
        userExt.init();
        dao.save(userExt);
    }

    /** batch save the entity with input list
     * 保存对象用户详情，批量保存list
     * @param list {@link UserExt}
     */
    public void batchSave(List<UserExt> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param userExt the input userExt
     */
    //@Transactional
    public void update(UserExt userExt) {
        dao.update(userExt);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id 用户ID|1
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

    /** get the page of dto UserExtDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户详情数据 {@link Page} 对象，包含 {@link UserExt} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<UserExtDto> getPageDto(UserExtParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserExtDto}
     * @param id 用户ID|1
     * @return {@link UserExtDto}
     */
    public UserExtDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 根据用户ID查询用户详情
     *
     * @param userIds  {@link List}
     * @return {@link List}
     */
    public List<UserExt> findAllByUserIdsIn(String[] userIds) {
        return dao.findAllByUserIdsIn(userIds);
    }

}
