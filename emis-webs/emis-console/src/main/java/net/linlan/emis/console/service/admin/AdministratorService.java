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
package net.linlan.emis.console.service.admin;

import java.time.LocalDateTime;
import java.util.Optional;

import net.linlan.emis.console.pojo.query.admin.AdministratorListQuery;
import net.linlan.emis.console.pojo.result.admin.AdministratorListResult;
import net.linlan.emis.console.pojo.result.admin.AdministratorResult;
import net.linlan.emis.console.pojo.save.admin.AdministratorCreateParam;
import net.linlan.emis.console.pojo.update.admin.AdministratorUpdateParam;
import net.linlan.emis.core.constant.CheckValidityType;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.user.constant.UserStatus;

/**
 * 管理员
 *
 * @author Linlan
 * Created on  2021/11/13 23:12
 */
public interface AdministratorService {
    /**
     * 查询平台管理员列表
     *
     * @param model {@link PageModel}
     * @param query {@link AdministratorListQuery}
     * @return {@link Page}
     */
    Page<AdministratorListResult> getAdministratorList(PageModel model,
                                                       AdministratorListQuery query);

    /**
     * 创建管理员
     *
     * @param param {@link AdministratorCreateParam}
     * @return {@link Boolean}
     */
    Boolean createAdministrator(AdministratorCreateParam param);

    /**
     * 修改管理员
     *
     * @param param {@link AdministratorUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updateAdministrator(AdministratorUpdateParam param);

    /**
     * 删除管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deleteAdministrator(String id);

    /**
     * 根据ID获取管理员
     *
     * @param id {@link String}
     * @return {@link AdministratorResult}
     */
    AdministratorResult getAdministrator(String id);

    /**
     * 更改管理员状态
     *
     * @param id     {@link String}
     * @param status {@link UserStatus}
     * @return {@link Boolean}
     */
    Boolean updateAdministratorStatus(String id, UserStatus status);

    /**
     * 重置管理员密码
     *
     * @param id       {@link String}
     * @param password {@link String}
     * @return {@link Boolean}
     */
    Boolean resetAdministratorPassword(String id, String password);

    /**
     * 更新认证成功信息
     *
     * @param id {@link String}
     * @param ip {@link String}
     * @param loginTime {@link LocalDateTime}
     */
    Boolean updateAuthSucceedInfo(String id, String ip, LocalDateTime loginTime);

    /**
     * 强制重置当前登录管理员密码
     *
     * @param username {@link String}
     * @param password {@link String}
     */
    void forceResetAdministratorPassword(String username, String password);

    /**
     * 强制重置当前登录管理员密码
     *
     * @param adminEntity {@link EmisAdminVo}
     * @param password {@link String}
     */
    void forceResetAdministratorPassword(EmisAdminVo adminEntity, String password);

    /**
     * 根据用户名查询管理员
     * @param username {@link String}
     * @return {@link EmisAdminVo}
     */
    EmisAdminVo getAdministratorByUsername(String username);

    /**
     * 参数有效性验证
     *
     * @param type  {@link CheckValidityType}
     * @param value {@link String}
     * @param id    {@link Long}
     * @return {@link Boolean}
     */
    Boolean administratorParamCheck(CheckValidityType type, String value, String id);

    /**
     * 根据用户名、手机号、邮箱查询用户
     *
     * @return {@link EmisAdminVo}
     */
    Optional<EmisAdminVo> findByUsernameOrMobileOrEmail(String keyword);

    /**
     * 获取用户详情
     *
     * @param userId {@link String}
     * @return {@link UserDetails}
     */
    UserDetails getUserDetails(String userId);

    /**
     * 获取用户详情
     *
     * @param user {@link EmisAdminVo}
     * @return {@link UserDetails}
     */
    UserDetails getUserDetails(EmisAdminVo user);
}
