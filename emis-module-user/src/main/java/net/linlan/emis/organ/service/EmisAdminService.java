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

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.emis.organ.dao.EmisAdminDao;
import net.linlan.emis.user.constant.UserStatusInt;
import net.linlan.frame.admin.dto.AdminUserDto;
import net.linlan.frame.admin.entity.AdminUser;
import net.linlan.frame.admin.service.AdminUserService;

/**
 *
 * UserOrganRelation数据域:用户机构关系服务类
 * @author Linlan
 *
 * CreateTime 2025-09-22 00:29:01
 *
 */
@Service
public class EmisAdminService {

    @Resource
    private EmisAdminDao     dao;
    @Resource
    private AdminUserService adminUserService;

    /**
     * findById
     *
     * @param id must not be {@literal null}.
     * @return {@link AdminUserDto}
     */
    public AdminUser findById(String id) {
        AdminUser adminUser = adminUserService.findById(Long.valueOf(id));
        return adminUser;
    }

    /**
     * findByUserId
     *
     * @param userId must not be {@literal null}.
     * @return {@link AdminUserDto}
     */
    public AdminUser findByUserId(String userId) {
        return adminUserService.findByUserId(userId);
    }

    /**
     * deleteById，通过id删除管理用户
     *
     * @param id must not be {@literal null}.
     */
    public void deleteById(String id) {
        adminUserService.deleteById(Long.valueOf(id));
    }

    /**
     * deleteByUserId，通过userId删除管理用户
     *
     * @param userId must not be {@literal null}.
     */
    public void deleteByUserId(String userId) {
        adminUserService.deleteByUserId(userId);
    }

    /**
     * deleteAllById
     *
     * @param ids must not be {@literal null}.
     */
    public void deleteAllById(String[] ids) {
        Long[] idlongs = Arrays.stream(ids).map(Long::valueOf).toArray(Long[]::new);
        adminUserService.deleteByIds(idlongs);
    }

    /**
     * save
     *
     * @param entity must not be null
     */
    public void save(AdminUser entity) {
        adminUserService.save(entity);
    }

    /**
     * 根据用户名查询
     *
     * @param username {@link String}
     * @return {@link AdminUserDto}
     */
    public AdminUserDto getByUsername(String username) {
        return adminUserService.getByUsername(username);
    }

    /**
     * 根据手机号查询
     *
     * @param mobile {@link String}
     * @return {@link AdminUserDto}
     */
    public AdminUserDto getByMobile(String mobile) {
        return adminUserService.getByMobile(mobile);
    }

    /**
     * 根据邮箱查询
     *
     * @param email {@link String}
     * @return {@link AdminUserDto}
     */
    public AdminUserDto getByEmail(String email) {
        return adminUserService.getByEmail(email);
    }

    /**
     * 更新管理员状态
     *
     * @param id     主键ID
     * @param status {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, UserStatusInt status) {
        adminUserService.updateUserStatus(id, status.getKey());
    }

    /**
     * 更新用户密码
     *
     * @param adminId                {@link  String}
     * @param password               {@link  String}
     * @return 操作结果
     */
    public Integer updateUserPassword(String adminId, String password) {
        return adminUserService.updateUserPwd(Long.valueOf(adminId), password);
    }

    /**
     * 更新用户密码
     *
     * @param username                     {@link  String}
     * @param password               {@link  String}
     * @return 操作结果
     */
    public Integer resetUserPassword(String username, String password) {
        return adminUserService.resetUserPwd(username, password);
    }

    /**
     * 更新认证成功信息
     *
     * @param id {@link String}
     * @param ip {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginCount(String id, String ip) {
        adminUserService.updateLoginCount(Long.valueOf(id), ip);
    }

    /**
     * 更新邮箱
     *
     * @param adminId {@link String}
     * @param email {@link String}
     */
    public void updateEmail(String adminId, String email) {
        AdminUser entity = adminUserService.findById(Long.valueOf(adminId));
        if (ObjectUtils.isNotEmpty(entity)) {
            entity.setEmail(email);
            adminUserService.update(entity);
        }
    }

    /**
     * 更新用户手机号
     *
     * @param adminId   {@link  String}
     * @param mobile    {@link  String}
     */
    public void updateMobile(String adminId, String mobile) {
        AdminUser entity = adminUserService.findById(Long.valueOf(adminId));
        if (ObjectUtils.isNotEmpty(entity)) {
            entity.setMobile(mobile);
            adminUserService.update(entity);
        }
    }
}
