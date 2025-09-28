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
package net.linlan.emis.ubind.service;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.ubind.dao.UserIdpBindDao;
import net.linlan.emis.ubind.dto.UserIdpBindDto;
import net.linlan.emis.ubind.entity.UserIdpBind;
import net.linlan.emis.ubind.entity.UserThirdParty;
import net.linlan.emis.ubind.param.UserIdpBindParam;

/**
 *
 * UserIdpBind数据域:用户身份提供商绑定服务类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-06 15:46:35<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Service
@Slf4j
public class UserIdpBindService {

    @Resource
    private UserIdpBindDao dao;

    /** get the list of entity UserIdpBind
     * 列表方法，返回{@link UserIdpBind} 列表
     * @param map the input select conditions
     * @return 列表的用户身份提供商绑定数据 {@link List} 对象，包含 {@link UserIdpBind} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<UserIdpBind> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link UserIdpBind}
     * @param id 主键ID|1
     * @return {@link UserIdpBind}
     */
    public UserIdpBind findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param userIdpBind the input userIdpBind
     */
    //@Transactional
    public void save(UserIdpBind userIdpBind) {
        userIdpBind.init();
        dao.save(userIdpBind);
    }

    /** batch save the entity with input list
     * 保存对象用户身份提供商绑定，批量保存list
     * @param list {@link UserIdpBind}
     */
    public void batchSave(List<UserIdpBind> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param userIdpBind the input userIdpBind
     */
    //@Transactional
    public void update(UserIdpBind userIdpBind) {
        dao.update(userIdpBind);
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

    /** get the page of dto UserIdpBindDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户身份提供商绑定数据 {@link Page} 对象，包含 {@link UserIdpBind} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<UserIdpBindDto> getPageDto(UserIdpBindParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserIdpBindDto}
     * @param id 主键ID|1
     * @return {@link UserIdpBindDto}
     */
    public UserIdpBindDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    public UserIdpBindDto getByProviderIdAndOpenId(String providerId, String openId) {
        if (StringUtils.isBlank(openId) || StringUtils.isBlank(providerId)) {
            return null;
        }
        UserIdpBindParam param = new UserIdpBindParam();
        param.setOpenId(openId);
        param.setProviderId(providerId);
        List<UserIdpBindDto> list = getPageDto(param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public UserIdpBindDto getByProviderIdAndUserId(String providerId, String userId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(providerId)) {
            return null;
        }
        UserIdpBindParam param = new UserIdpBindParam();
        param.setForeignId(userId);
        param.setProviderId(providerId);
        List<UserIdpBindDto> list = getPageDto(param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public int countByThirdParty(UserThirdParty userThirdParty) {
        return 0;
    }

    public void deleteAllByUserIdsIn(String[] userIds) {

    }

}
