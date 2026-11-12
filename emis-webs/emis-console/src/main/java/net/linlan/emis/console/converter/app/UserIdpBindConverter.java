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
package net.linlan.emis.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import net.linlan.emis.console.pojo.result.app.UserIdpBindListResult;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.ubind.entity.UserIdpBind;

/**
 * 用户身份提供商绑定
 *
 * @author Linlan
 * Created on  2022/11/3 21:08
 */
@Mapper(componentModel = "spring")
public interface UserIdpBindConverter {

    /**
     * 用户身份提供商绑定关系分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default List<UserIdpBindListResult> userIdpBindEntityConvertToUserIdpBindListResult(Iterable<UserIdpBind> page) {
        List<UserIdpBindListResult> list = new ArrayList<>();
        for (UserIdpBind entity : page) {
            list.add(entityConvertToAppAccountResult(entity));
        }
        return list;
    }

    /**
     * 用户身份提供商绑定关系转换结果
     *
     * @param userIdpBindPo {@link UserIdpBind}
     * @return {@link UserIdpBindListResult}
     */
    UserIdpBindListResult entityConvertToAppAccountResult(UserIdpBind userIdpBindPo);
}
