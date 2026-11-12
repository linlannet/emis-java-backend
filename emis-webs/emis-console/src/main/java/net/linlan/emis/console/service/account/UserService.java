/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.service.account;

import java.io.Serializable;
import java.util.List;

import net.linlan.emis.console.pojo.query.account.UserListNotInGroupQuery;
import net.linlan.emis.console.pojo.query.account.UserListQuery;
import net.linlan.emis.console.pojo.result.account.BatchUserResult;
import net.linlan.emis.console.pojo.result.account.UserListResult;
import net.linlan.emis.console.pojo.result.account.UserLoginAuditListResult;
import net.linlan.emis.console.pojo.result.account.UserResult;
import net.linlan.emis.console.pojo.save.account.UserCreateParam;
import net.linlan.emis.console.pojo.update.account.ResetPasswordParam;
import net.linlan.emis.console.pojo.update.account.UserUpdateParam;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.entity.UserInfo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-07-31
 */
public interface UserService {

    /**
     * 获取用户（分页）
     *
     * @param page  {@link PageModel}
     * @param query {@link UserListQuery}
     * @return {@link UserListQuery}
     */
    Page<UserListResult> getUserList(PageModel page, UserListQuery query);

    /**
     * 获取用户列表不在当前组
     *
     * @param page  {@link  PageModel}
     * @param query {@link UserListNotInGroupQuery }
     * @return {@link  }
     */
    Page<UserListResult> getUserListNotInGroup(PageModel page, UserListNotInGroupQuery query);

    /**
     * 重置密码
     *
     * @param param       {@link ResetPasswordParam} 用户入参
     * @return {@link Boolean} 是否成功
     */
    Boolean resetUserPassword(ResetPasswordParam param);

    /**
     * 更改用户状态
     *
     * @param id     {@link Long}
     * @param status {@link UserStatus}
     * @return {@link Boolean}
     */
    boolean changeUserStatus(String id, UserStatus status);

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    Boolean createUser(UserCreateParam param);

    /**
     * 通过外部ID获取用户
     *
     * @param id {@link String}
     * @return {@link UserInfo}
     */
    UserInfo getByForeignId(String id);

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
     * @return {@link Boolean}
     */
    boolean updateUser(UserUpdateParam param);

    /**
     * 删除用户
     *
     * @param id {@link Serializable}
     * @return {@link Boolean}
     */
    boolean deleteUser(String id);

    /**
     * 批量删除用户
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    Boolean batchDeleteUser(String[] ids);

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean}
     */
    Boolean userParamCheck(CheckValidityType type, String value, String id);

    /**
     * 查看用户登录日志
     *
     * @param id {@link Long}
     * @param pageModel {@link PageModel}
     * @return {@link   List}
     */
    Page<UserLoginAuditListResult> findUserLoginAuditList(String id, PageModel pageModel);

    /**
     * 批量获取用户信息
     *
     * @param ids {@link List}
     * @return {@link List}
     */
    List<BatchUserResult> batchGetUser(List<String> ids);
}
