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
package net.linlan.emis.organ.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.organ.dao.UserOrganRelationDao;
import net.linlan.emis.organ.dto.UserOrganRelationDto;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.param.UserOrganRelationParam;

/**
 *
 * UserOrganRelation数据域:用户机构关系服务类
 * 
 * @author Linlan
 *
 * CreateTime 2025-09-28 18:01:50
 *
 */
@Service
public class UserOrganRelationService {

    @Resource
    private UserOrganRelationDao dao;

    /** get the list of entity UserOrganRelation
     * 列表方法，返回{@link UserOrganRelation} 列表
     * @param map the input select conditions
     * @return 列表的用户机构关系数据 {@link List} 对象，包含 {@link UserOrganRelation} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<UserOrganRelation> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link UserOrganRelation}
     * @param id 主键ID|1
     * @return {@link UserOrganRelation}
     */
    public UserOrganRelation findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param userOrganRelation the input userOrganRelation
     */
    //@Transactional
    public void save(UserOrganRelation userOrganRelation) {
        userOrganRelation.init();
        dao.save(userOrganRelation);
    }

    /** batch save the entity with input list
     * 保存对象用户机构关系，批量保存list
     * @param list {@link UserOrganRelation}
     */
    public void batchSave(List<UserOrganRelation> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param userOrganRelation the input userOrganRelation
     */
    //@Transactional
    public void update(UserOrganRelation userOrganRelation) {
        dao.update(userOrganRelation);
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

    /** get the page of dto UserOrganRelationDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户机构关系数据 {@link Page} 对象，包含 {@link UserOrganRelation} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<UserOrganRelationDto> getPageDto(UserOrganRelationParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserOrganRelationDto}
     * @param id 主键ID|1
     * @return {@link UserOrganRelationDto}
     */
    public UserOrganRelationDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /** get list by input organId, return dto list
     * 对象详情方法，通过id查询对象{@link UserOrganRelationDto}
     * @param organId 机构ID|1
     * @return {@link UserOrganRelationDto}
     */
    public List<UserOrganRelationDto> getByOrganId(String organId) {
        if (StringUtils.isBlank(organId)) {
            return null;
        }
        return dao.getByOrganId(organId);
    }

    /** get list by input userId, return dto list
     * 对象详情方法，通过id查询对象{@link UserOrganRelationDto}
     * @param userId 用户ID|1
     * @return {@link UserOrganRelationDto}
     */
    public List<UserOrganRelationDto> getByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        return dao.getByUserId(userId);
    }

    /** delete the entity by input organId, userId
     * 删除方法，通过id删除对象
     * @param organId 机构ID|1
     * @param userId 用户ID|1
     */
    public void deleteByOrganIdAndUserId(String organId, String userId) {
        if (StringUtils.isBlank(organId) && StringUtils.isBlank(userId)) {
            return;
        }
        dao.deleteByOrganIdAndUserId(organId, userId);
    }

    /** delete the entity by input userIds
     * 删除方法，通过ids删除对象
     * @param userIds 用户IDs|1
     */
    public void deleteByUserIds(String[] userIds) {
        if (ObjectUtils.isEmpty(userIds)) {
            return;
        }
        dao.deleteByUserIds(userIds);
    }

}
