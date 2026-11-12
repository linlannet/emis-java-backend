/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.service;

import java.io.Serializable;

import net.linlan.emis.openapi.pojo.query.OapiV1UserListQuery;
import net.linlan.emis.openapi.pojo.result.UserListResult;
import net.linlan.emis.openapi.pojo.result.UserResult;
import net.linlan.emis.openapi.pojo.save.UserCreateParam;
import net.linlan.emis.openapi.pojo.update.UserUpdateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.user.constant.UserStatus;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Linlan
 * Created on 2020-07-31
 */
public interface UserService {

    /**
     * 获取用户（分页）
     *
     * @param page  {@link PageModel}
     * @param query {@link OapiV1UserListQuery}
     * @return {@link OapiV1UserListQuery}
     */
    Page<UserListResult> getUserList(PageModel page, OapiV1UserListQuery query);

    /**
     * 更改用户状态
     *
     * @param id     {@link Long}
     * @param status {@link UserStatus}
     */
    void changeUserStatus(String id, UserStatus status);

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     */
    void createUser(UserCreateParam param);

    /**
     * 根据ID查询用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    UserResult getUser(String id);

    /**
     * 更新用户
     *
     * @param param {@link UserUpdateParam}
     */
    void updateUser(UserUpdateParam param);

    /**
     * 删除用户
     *
     * @param id {@link Serializable}
     */
    void deleteUser(String id);

    /**
     * 获取用户id
     *
     * @param foreignId {@link String}
     * @param mobileNumber {@link String}
     * @param email {@link String}
     * @param username {@link String}
     * @return {@link String}
     */
    String getUserIdByParams(String foreignId, String mobileNumber, String email, String username);
}
