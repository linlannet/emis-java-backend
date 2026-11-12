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
package net.linlan.emis.console.converter.account;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.google.common.collect.Lists;

import net.linlan.emis.console.pojo.result.account.UserGroupListResult;
import net.linlan.emis.console.pojo.result.account.UserGroupMemberListResult;
import net.linlan.emis.console.pojo.result.account.UserGroupResult;
import net.linlan.emis.console.pojo.save.account.UserGroupCreateParam;
import net.linlan.emis.console.pojo.update.account.UserGroupUpdateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.user.dto.UserGroupDto;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.entity.UserGroup;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.utils.SpringContextUtils;

/**
 * 用户映射
 *
 * @author Linlan
 * Created on  2020/8/14 22:45
 */
@Mapper(componentModel = "spring")
public interface UserGroupConverter {

    /**
     * 用户实体转换为用户分页结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<UserGroupListResult> userGroupEntityConvertToUserGroupResult(com.github.pagehelper.Page<UserGroupDto> page) {
        Page<UserGroupListResult> result = new Page<>();
        List<UserGroupListResult> list = new ArrayList<>();
        for (UserGroup user : page) {
            UserGroupConverter bean = SpringContextUtils.getBean(UserGroupConverter.class);
            list.add(bean.entityConvertToUserGroupPaginationResult(user));
        }
        //@formatter:off
        result.setPagination(Page.Pagination.builder()
                .total(page.getTotal())
                .totalPages(page.getPages())
                .current(page.getPageNum())
                .build());
        //@formatter:on
        result.setList(list);
        return result;
    }

    /**
     * 实体转换为群组成员列表结果
     *
     * @param users {@link List}
     * @return {@link List}
     */
    default Page<UserGroupMemberListResult> userPoConvertToGroupMemberListResult(com.github.pagehelper.Page<UserInfoDto> users) {
        Page<UserGroupMemberListResult> page = new Page<>();
        List<UserGroupMemberListResult> list = Lists.newArrayList();
        for (UserInfoDto entity : users) {
            list.add(userPoConvertToGroupMemberListResult(entity));
        }
        page.setList(list);
        //@formatter:off
        page.setPagination(Page.Pagination.builder()
                .total(users.getTotal())
                .totalPages(users.getPages())
                .current(users.getPageNum())
                .build());
        //@formatter:on
        return page;
    }

    /**
     * 用户实体转换为用户分页结果
     *
     * @param page {@link UserGroup}
     * @return {@link UserGroupListResult}
     */
    UserGroupListResult entityConvertToUserGroupPaginationResult(UserGroup page);

    /**
     * 用户创建参数转换为用户实体
     *
     * @param param {@link UserGroupCreateParam}
     * @return {@link UserInfo}
     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    UserGroup userGroupCreateParamConvertToEntity(UserGroupCreateParam param);

    /**
     * 用户更新参数转换为用户实体类
     *
     * @param param {@link UserGroupUpdateParam} 更新参数
     * @return {@link UserInfo} 用户实体
     */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    UserGroup userGroupUpdateParamConvertToEntity(UserGroupUpdateParam param);

    /**
     * 实体转用户群组列表
     *
     * @param user {@link UserInfoParam}
     * @return {@link UserGroupMemberListResult}
     */
    UserGroupMemberListResult userPoConvertToGroupMemberListResult(UserInfoDto user);

    /**
     * 实体转用户详情
     *
     * @param details {@link UserGroup}
     * @return {@link UserGroupResult}
     */
    UserGroupResult entityConvertToUserGroupResult(UserGroup details);
}
