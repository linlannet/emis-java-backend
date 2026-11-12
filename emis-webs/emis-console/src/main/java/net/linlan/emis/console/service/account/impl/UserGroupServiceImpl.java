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
package net.linlan.emis.console.service.account.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.converter.account.UserGroupConverter;
import net.linlan.emis.console.pojo.query.account.UserGroupListQuery;
import net.linlan.emis.console.pojo.query.account.UserGroupMemberListQuery;
import net.linlan.emis.console.pojo.result.account.UserGroupListResult;
import net.linlan.emis.console.pojo.result.account.UserGroupMemberListResult;
import net.linlan.emis.console.pojo.save.account.UserGroupCreateParam;
import net.linlan.emis.console.pojo.update.account.UserGroupUpdateParam;
import net.linlan.emis.console.service.account.UserGroupService;
import net.linlan.emis.core.exception.user.UserGroupExistUserException;
import net.linlan.emis.core.exception.user.UserGroupNotExistException;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.repository.domain.PageModel;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.emis.user.dto.UserGroupDto;
import net.linlan.emis.user.dto.UserGroupRelationDto;
import net.linlan.emis.user.dto.UserInfoDto;
import net.linlan.emis.user.entity.UserGroup;
import net.linlan.emis.user.entity.UserGroupRelation;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.param.UserGroupParam;
import net.linlan.emis.user.param.UserGroupRelationParam;
import net.linlan.emis.user.service.UserGroupRelationService;
import net.linlan.emis.user.service.UserInfoService;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 用户组实现service
 *
 * @author Linlan
 * Created on  2021/10/11 21:30
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
    /**
     * 查询用户分组列表
     *
     * @param page  {@link PageModel}
     * @param query {@link UserGroupListQuery}
     * @return {@link UserGroupListResult}
     */
    @Override
    public net.linlan.emis.support.repository.domain.Page<UserGroupListResult> getUserGroupList(PageModel page, UserGroupListQuery query) {
        //查询条件
        UserGroupParam param = new UserGroupParam();
        param.setName(query.getName());
        param.setCode(query.getCode());
        param.setPage(page.getCurrent());
        param.setLimit(page.getPageSize());
        Page<UserGroupDto> list = userGroupService.getPageDto(param);
        return userGroupConverter.userGroupEntityConvertToUserGroupResult(list);
    }

    /**
     * 创建用户组
     *
     * @param param {@link UserGroupCreateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean createUserGroup(UserGroupCreateParam param) {
        UserGroup entity = userGroupConverter.userGroupCreateParamConvertToEntity(param);
        userGroupService.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getName())
            .type(TargetType.USER_GROUP).build());
        return true;
    }

    /**
     * 更新用户组
     *
     * @param param {@link UserGroupUpdateParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateUserGroup(UserGroupUpdateParam param) {
        UserGroup entity = userGroupConverter.userGroupUpdateParamConvertToEntity(param);
        UserGroup details = getUserGroup(param.getId());
        BeanUtils.merge(entity, details, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
        userGroupService.update(details);
        AuditContext.setTarget(Target.builder().id(details.getId()).name(entity.getName())
            .type(TargetType.USER_GROUP).build());
        return true;
    }

    /**
     * 删除用户组
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteUserGroup(String id) {
        UserGroup optional = userGroupService.findById(id);
        //用户组不存在
        if (optional == null) {
            AuditContext.setContent("删除用户组失败，用户组不存在");
            log.warn(AuditContext.getContent());
            throw new UserGroupNotExistException(AuditContext.getContent());
        }
        //用户组存在用户
        Long count = getUserGroupMemberCount(id);
        if (count > 0) {
            AuditContext.setContent("删除用户组失败，当前用户组下存在用户");
            log.warn(AuditContext.getContent());
            throw new UserGroupExistUserException(AuditContext.getContent());
        }
        userGroupService.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name(optional.getName())
            .type(TargetType.USER_GROUP).build());
        return true;
    }

    /**
     * 获取用户组内分组列表
     *
     * @param query {@link UserGroupMemberListQuery}
     * @return {@link UserGroupMemberListResult}
     */
    @Override
    public net.linlan.emis.support.repository.domain.Page<UserGroupMemberListResult> getUserGroupMemberList(PageModel model,
                                                                  UserGroupMemberListQuery query) {
        UserGroupRelationParam param = new UserGroupRelationParam();
        param.setGroupId(query.getId());
        param.setPage(model.getCurrent());
        param.setLimit(model.getPageSize());
        Page<UserGroupRelationDto> relationPage = userGroupRelationService.getPageDto(param);
        Page<UserInfoDto> userPage = new Page<>(relationPage.getPageNum(),
            relationPage.getPageSize());
        userPage.setTotal(relationPage.getTotal());
        for (UserGroupRelationDto relation : relationPage) {
            UserInfoDto user = userInfoService.getDtoById(relation.getUserId());
            if (user != null) {
                userPage.add(user);
            }
        }
        return userGroupConverter.userPoConvertToGroupMemberListResult(userPage);
    }

    /**
     * 从用户组移除用户
     *
     * @param id     {@link String}
     * @param userId {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean removeMember(String id, String userId) {
        //查询关联关系
        userGroupRelationService.deleteByGroupIdAndUserId(id, userId);
        AuditContext.setTarget(
            Target.builder().id(userId).name("从用户组移除用户").type(TargetType.USER).build(),
            Target.builder().id(id).name("").type(TargetType.USER_GROUP).build());
        return true;
    }

    /**
     * 添加用户
     *
     * @param userIds {@link String}
     * @param groupId {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean addMember(String groupId, String[] userIds) {
        UserGroup optional = userGroupService.findById(groupId);
        //用户组不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，用户组不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        List<UserGroupRelation> list = new ArrayList<>();
        Lists.newArrayList(userIds).forEach(id -> {
            UserGroupRelation member = new UserGroupRelation();
            member.setGroupId(groupId);
            member.setUserId(id);
            list.add(member);
        });
        //添加
        userGroupRelationService.batchSave(list);
        List<Target> targets;
        CompletableFuture<List<Target>> future = CompletableFuture
            .supplyAsync(() -> new ArrayList<>(Arrays.stream(userIds).map(i -> {
                UserInfo userEntity = userInfoService.findById(i);
                return userEntity == null ? null : Target.builder().id(i)
                    .name(userEntity.getFullName()).type(TargetType.USER).build();
            }).collect(Collectors.toList())));
        try {
            // 等待最终结果
            targets = future.get();
        } catch (InterruptedException | ExecutionException e) {
            // 处理异常
            return false;
        }
        targets.add(Target.builder().id(groupId).name(optional.getName())
            .type(TargetType.USER_GROUP).build());
        AuditContext.setTarget(targets);
        return true;
    }

    /**
     * 根据ID查询用户分组
     *
     * @param id {@link Long}
     * @return {@link UserGroup}
     */
    @Override
    public UserGroup getUserGroup(String id) {
        return userGroupService.findById(id);
    }

    /**
     * 批量移除用户
     *
     * @param userIds {@link String}
     * @param id      {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean batchRemoveMember(String id, List<String> userIds) {
        UserGroup optional = userGroupService.findById(id);
        //用户组不存在
        if (optional == null) {
            AuditContext.setContent("操作失败，用户组不存在");
            log.warn(AuditContext.getContent());
            throw new SupportException(AuditContext.getContent());
        }
        userIds.forEach(userId -> userGroupRelationService.deleteByGroupIdAndUserId(id, userId));
        List<Target> targets;
        CompletableFuture<List<Target>> future = CompletableFuture
            .supplyAsync(() -> new ArrayList<>(userIds.stream().map(i -> {
                UserInfo userEntity = userInfoService.findById(i);
                return userEntity == null ? null : Target.builder().id(i)
                    .name(userEntity.getFullName()).type(TargetType.USER).build();
            }).collect(Collectors.toList())));
        try {
            // 等待最终结果
            targets = future.get();
        } catch (InterruptedException | ExecutionException e) {
            // 处理异常
            return false;
        }
        targets.add(Target.builder().id(id).name(optional.getName())
            .type(TargetType.USER_GROUP).build());
        AuditContext.setTarget(targets);
        return true;
    }

    @Override
    public Long getUserGroupMemberCount(String groupId) {
        int count = userGroupRelationService.getCount(new StringMap().put("groupId", groupId).map());
        return (long) count;
    }

    /**
     * 用户组数据映射
     */
    private final UserGroupConverter        userGroupConverter;

    /**
     * 用户组
     */
    private final net.linlan.emis.user.service.UserGroupService userGroupService;

    /**
     * 用户组关联关系
     */
    private final UserGroupRelationService  userGroupRelationService;

    /**
     * 用户信息
     */
    private final UserInfoService           userInfoService;
}
