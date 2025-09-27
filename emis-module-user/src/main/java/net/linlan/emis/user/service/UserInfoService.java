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

import java.util.Collection;
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
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.dao.UserInfoDao;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.param.UserInfoParam;

/**
 *
 * UserInfo数据域:用户信息服务类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-06 15:46:35<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Service
public class UserInfoService {

    @Resource
    private UserInfoDao dao;

    /** get the list of entity UserInfo
     * 列表方法，返回{@link UserInfo} 列表
     * @param map the input select conditions
     * @return 列表的用户信息数据 {@link List} 对象，包含 {@link UserInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<UserInfo> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link UserInfo}
     * @param id 主键ID|1
     * @return {@link UserInfo}
     */
    public UserInfo findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param userInfo the input userInfo
     */
    //@Transactional
    public void save(UserInfo userInfo) {
        userInfo.init();
        dao.save(userInfo);
    }

    /** batch save the entity with input list
     * 保存对象用户信息，批量保存list
     * @param list {@link UserInfo}
     */
    public void batchSave(List<UserInfo> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param userInfo the input userInfo
     */
    //@Transactional
    public void update(UserInfo userInfo) {
        dao.update(userInfo);
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

    /** get the page of dto UserInfoDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户信息数据 {@link Page} 对象，包含 {@link UserInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<UserInfoDto> getPageDto(UserInfoParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserInfoDto}
     * @param id 主键ID|1
     * @return {@link UserInfoDto}
     */
    public UserInfoDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username {@link String}
     * @return {@link UserInfoDto}
     */
    public UserInfoDto getByUsername(String username) {
        return dao.getByUsername(username);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param mobile {@link String}
     * @return {@link UserInfoDto}
     */
    public UserInfoDto getByMobile(String mobile) {
        return dao.getByMobile(mobile);
    }

    /**
     * 根据邮件查询用户信息
     *
     * @param email {@link String}
     * @return {@link UserInfoDto}
     */
    public UserInfoDto getByEmail(String email) {
        return dao.getByEmail(email);
    }

    /**
     * 根据扩展ID查询用户信息
     *
     * @param foreignId {@link String}
     * @return {@link UserInfo}
     */
    public UserInfoDto getByForeignId(String foreignId) {
        return dao.getByForeignId(foreignId);
    }

    /**
     * 根据扩展ID查询用户信息
     *
     * @param foreignIds {@link Collection}
     * @return {@link UserInfo}
     */
    public List<UserInfoDto> getListByForeignIds(String[] foreignIds) {
        return dao.getListByForeignIds(foreignIds);
    }

    /**
     * 更新用户密码
     *
     * @param id                     {@link  String}
     * @param password               {@link  String}
     * @return {@link  Integer}
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePassword(String id, String password) {
        return dao.updatePassword(id, password);
    }

    /**
     * 更新用户邮箱
     *
     * @param id    {@link  String}
     * @param email {@link  String}
     * @return {@link  Integer}
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateEmail(String id, String email) {
        UserInfo entity = findById(id);
        if (ObjectUtils.isNotEmpty(entity)) {
            entity.setEmail(email);
            update(entity);
        }
        return 0;
    }

    /**
     * 更新用户手机号
     *
     * @param id    {@link  String}
     * @param mobile {@link  String}
     * @return {@link  Integer}
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateByIdAndMobile(String id, String mobile) {
        UserInfo entity = findById(id);
        if (ObjectUtils.isNotEmpty(entity)) {
            entity.setMobile(mobile);
            update(entity);
        }
        return 0;
    }

    /**
     * 更新用户状态
     *
     * @param id     {@link  String}
     * @param status {@link  UserStatus}
     * @return {@link  Integer}
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserStatus(String id, UserStatus status) {
        UserInfo entity = findById(id);
        if (ObjectUtils.isNotEmpty(entity)) {
            entity.setMobile(status.getKey());
            update(entity);
        }
        return 0;
    }

    /**
     * 查询已到期用户
     *
     * @return {@link UserInfo}
     */
    //    @Query(value = "FROM UserInfo WHERE expireDate <= CURRENT_DATE and status != 'expired_locked'")
    public List<UserInfo> findExpireUser() {
        return dao.findExpireUser();
    }

    /**
     * 根据用户名查询全部
     *
     * @param usernames {@link String}
     * @return {@link List}
     */
    public List<UserInfo> findAllByUsernameIn(String[] usernames) {
        return dao.findAllByUsernamesIn(usernames);
    }

    /**
     * 根据手机号查询全部
     *
     * @param mobiles {@link String}
     * @return {@link List}
     */
    public List<UserInfo> findAllByMobileIn(String[] mobiles) {
        return dao.findAllByMobilesIn(mobiles);
    }

    /**
     * 根据email模糊查询
     *
     * @param emails {@link String}
     * @return {@link List}
     */
    public List<UserInfo> findAllByEmailIn(String[] emails) {
        return dao.findAllByEmailsIn(emails);
    }

    /**
     * 根据status模糊查询
     *
     * @param status {@link UserStatus}
     * @return {@link List}
     */
    public List<UserInfo> findAllByStatusIn(UserStatus[] status) {
        return dao.findAllByStatusIn(status);
    }

    /**
     * 根据status查询
     *
     * @param status {@link UserStatus}
     * @return {@link List}
     */
    public List<UserInfo> findAllByStatus(UserStatus status) {
        return getList((new StringMap().put("status", status).map()));
    }

    /**
     * 按Id查找不在Id中
     *
     * @param ids {@link Collection}
     * @param dataOrigin {@link String}
     * @return {@link List}
     */
    public List<UserInfo> findAllByIdNotInAndDataOrigin(String[] ids, String dataOrigin) {
        return getList((new StringMap().put("ids", ids).put("dataOrigin", dataOrigin).map()));
    }

    /**
     * 根据身份源ID查询用户列表
     *
     * @param identitySourceId {@link String}
     * @return {@link List}
     */
    public List<UserInfo> findAllByIdentitySourceId(String identitySourceId) {
        return getList(new StringMap().put("identitySourceId", identitySourceId).map());
    }

    /**
     * 更新认证成功信息
     *
     * @param id {@link String}
     * @param ip {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginCount(@Param("id") String id, @Param("ip") String ip) {
        dao.updateLoginCount(id, ip);
    }

    /**
     * 根据状态擦护心用户数
     *
     * @param status {@link UserStatus}
     * @return {@link Long}
     */
    public int countByStatus(UserStatus status) {
        return getCount(new StringMap().put("status", status.getKey()).map());
    }

    /**
     * 根据姓名模糊查询
     *
     * @param fullName {@link String}
     * @return {@link UserInfo}
     */
    public List<UserInfo> findByFullNameLike(String fullName) {
        return getList(new StringMap().put("fullName", fullName).map());
    }

}
