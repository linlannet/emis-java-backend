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
package net.linlan.emis.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.user.constant.UserStatus;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.param.UserInfoParam;

/**
*
* UserInfo数据域:用户信息持久层Dao
* <br>
* @author Linlan
* CreateTime 2025-09-06 15:46:35<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@Mapper
public interface UserInfoDao extends MybatisBaseDao<UserInfo> {

    /** get the page of dto UserInfoDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的用户信息数据 {@link Page} 对象，包含 {@link UserInfo} 列表
     */
    Page<UserInfoDto> getPageDto(UserInfoParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link UserInfoDto}
     * @param id 主键ID|1
     * @return {@link UserInfoDto}
     */
    UserInfoDto getDtoById(String id);

    UserInfoDto getByUsername(String username);

    UserInfoDto getByMobile(String mobile);

    UserInfoDto getByEmail(String email);

    UserInfoDto getByForeignId(String foreignId);

    List<UserInfoDto> getListByForeignIds(String[] foreignIds);

    Integer updatePassword(@Param("id") String id, @Param("password") String password);

    List<UserInfo> findExpireUser();

    List<UserInfo> findAllByUsernamesIn(String[] usernames);

    List<UserInfo> findAllByMobilesIn(String[] mobiles);

    List<UserInfo> findAllByEmailsIn(String[] emails);

    List<UserInfo> findAllByStatusIn(UserStatus[] status);

    void updateLoginCount(@Param("id") String id, @Param("ip") String ip);
}
