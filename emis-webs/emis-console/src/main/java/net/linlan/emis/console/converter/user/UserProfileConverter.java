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
package net.linlan.emis.console.converter.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import net.linlan.emis.console.pojo.update.user.UpdateUserInfoRequest;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.user.entity.UserInfo;

/**
 * AccountConverter
 *
 * @author Linlan
 * Created on  2022/3/25 21:52
 */
@Mapper(componentModel = "spring")
public interface UserProfileConverter {

    /**
     * 用户更新参数转换为用户实体类
     *
     * @param param {@link UpdateUserInfoRequest} 更新参数
     * @return {@link UserInfo} 用户实体
     */
    @Mapping(target = "needChangePassword", ignore = true)
    @Mapping(target = "mobileVerified", ignore = true)
    @Mapping(target = "mobileAreaCode", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "mobile", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastUpdatePasswordTime", ignore = true)
    @Mapping(target = "lastAuthTime", ignore = true)
    @Mapping(target = "lastAuthIp", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expandParam", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "authTotal", ignore = true)
    EmisAdminVo userUpdateParamConvertToAdministratorEntity(UpdateUserInfoRequest param);
}
