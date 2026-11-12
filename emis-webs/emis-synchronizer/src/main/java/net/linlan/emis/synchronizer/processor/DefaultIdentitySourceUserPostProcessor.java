/*
 * emis-synchronizer - synchronizer同步执行器
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
package net.linlan.emis.synchronizer.processor;

// import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.core.constant.SyncStatus;
import net.linlan.emis.core.constant.TriggerType;
import net.linlan.emis.core.storage.Storage;
import net.linlan.emis.idsource.config.StrategyConfig;
import net.linlan.emis.idsource.constant.IdentitySourceActionType;
import net.linlan.emis.idsource.constant.IdentitySourceObjectType;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.idsource.entity.IdentitySourceSyncHistory;
import net.linlan.emis.idsource.entity.IdentitySourceSyncRecord;
import net.linlan.emis.idsource.service.IdentitySourceService;
import net.linlan.emis.idsource.service.IdentitySourceSyncHistoryService;
import net.linlan.emis.idsource.service.IdentitySourceSyncRecordService;
import net.linlan.emis.manage.mail.MailMsgEventPublish;
import net.linlan.emis.manage.processor.IdentitySourceSyncUserPostProcessor;
import net.linlan.emis.manage.vo.VerifyUser;
import net.linlan.emis.organ.dto.UserOrganRelationDto;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.service.EmisOrganService;
import net.linlan.emis.organ.service.UserOrganRelationService;
import net.linlan.emis.support.security.password.PasswordGenerator;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserExtService;
import net.linlan.emis.user.service.UserGroupRelationService;
import net.linlan.emis.user.service.UserInfoService;
import static java.util.stream.Collectors.toSet;

import static net.linlan.emis.support.constant.EmisConstants.SYSTEM_DEFAULT_USER_NAME;

/**
 * 身份源数据 pull post 处理器
 *
 * @author Linlan
 * Created on 2022/3/1 22:04
 */
@Slf4j
@Component
@SuppressWarnings({ "AlibabaMethodTooLong", "DuplicatedCode" })
public class DefaultIdentitySourceUserPostProcessor extends AbstractIdentitySourcePostProcessor
                                                    implements IdentitySourceSyncUserPostProcessor {

    @Override
    public void process(String batch, String identitySourceId, List<VerifyUser> thirdPartyUserList,
                        LocalDateTime startTime, TriggerType triggerType) {
        ProcessDataResult processData = null;
        //保存同步历史
        IdentitySourceSyncHistory history = saveSyncHistory(batch, identitySourceId, startTime,
            triggerType);
        //手动开启事务
        TransactionStatus transactionStatus = platformTransactionManager
            .getTransaction(transactionDefinition);
        IdentitySource identitySource = getIdentitySource(identitySourceId);
        try {
            //初始化数据
            InitDataResult initData = initData(identitySource, thirdPartyUserList);
            //处理数据
            processData = processData(identitySource, initData);
            //校验数据
            validateData(processData);
            //批量保存用户
            if (!CollectionUtils.isEmpty(processData.getCreateUsers())) {
                List<UserInfo> createUserList = Lists.newArrayList(processData.getCreateUsers());
                userInfoService.batchSave(createUserList);
            }
            //批量更新用户
            if (!CollectionUtils.isEmpty(processData.getUpdateUsers())) {
                processData.getUpdateUsers().forEach(userInfoService::update);
            }
            //保存组织成员关系
            if (!CollectionUtils.isEmpty(processData.getCreateOrganizationMembers())) {
                List<UserOrganRelation> organizationMemberEntityList = Lists
                    .newArrayList(processData.getCreateOrganizationMembers());
                userOrganRelationService.batchSave(organizationMemberEntityList);
            }
            //删除相关数据
            List<String> deleteUserIds;
            if (!CollectionUtils.isEmpty(processData.getDeleteUsers())) {
                deleteUserIds = processData.getDeleteUsers().stream().map(UserInfo::getId)
                    .collect(Collectors.toList());
                //删除用户
                userInfoService.deleteByIds(deleteUserIds.toArray(new String[0]));
                //删除用户详情
                List<UserExt> userExtList = userExtService
                    .findAllByUserIdsIn(deleteUserIds.toArray(new String[0]));
                if (!CollectionUtils.isEmpty(userExtList)) {
                    userExtService.deleteByIds(
                        userExtList.stream().map(UserExt::getId).toArray(String[]::new));
                }
                //删除组织用户关联关系
                userOrganRelationService.deleteByUserIds(deleteUserIds.toArray(new String[0]));
                //删除用户组关系
                userGroupRelationService.deleteAllByUserIdsIn(deleteUserIds.toArray(new String[0]));
            }
            //删除组织关系
            if (!CollectionUtils.isEmpty(processData.getDeleteOrganizationMembers())) {
                for (String userId : processData.getDeleteOrganizationMembers().keySet()) {
                    for (String organId : processData.getDeleteOrganizationMembers().get(userId)) {
                        userOrganRelationService.deleteByOrganIdAndUserId(organId, userId);
                    }
                }
            }
            //更新同步历史
            updateSyncHistory(processData, history, SyncStatus.SUCCESS);
            //新增同步记录
            saveSyncHistoryRecord(history.getId(), processData);
            //提交事务
            platformTransactionManager.commit(transactionStatus);
            if (!CollectionUtils.isEmpty(processData.getCreateUsers()) && Boolean.TRUE
                .equals(identitySource.getStrategyConfig().getUser().getEmailNotify())) {
                // 发送密码通知
                publishMessage(Lists.newArrayList(processData.getCreateUsers()));
            }
        } catch (Exception e) {
            log.error("处理用户数据发生异常", e);
            platformTransactionManager.rollback(transactionStatus);
            updateSyncHistory(processData, history, SyncStatus.FAIL);
        }
    }

    /**
     * 新增同步记录
     *
     * @param historyId  {@link String}
     * @param processData {@link ProcessDataResult}
     */
    private void saveSyncHistoryRecord(String historyId, ProcessDataResult processData) {
        //创建用户
        saveCreateUsersRecord(historyId, processData.getCreateUsers());
        //更新用户记录
        saveUpdateUsersRecord(historyId, processData.getUpdateUsers());
        //删除用户记录
        saveDeleteUsersRecord(historyId, processData.getDeleteUsers());
        //跳过用户记录
        saveSkipUsersRecord(historyId, processData.getSkipUsers());
    }

    /**
     * 保存创建用户记录
     *
     * @param historyId {@link String}
     * @param createUsers {@link Set}
     */
    private void saveCreateUsersRecord(String historyId, Set<UserInfo> createUsers) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        createUsers.forEach(user -> {
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(SyncStatus.SUCCESS.getKey());
            entity.setObjectType(IdentitySourceObjectType.USER.getKey());
            entity.setObjectId(user.getId());
            entity.setObjectName(user.getFullName());
            entity.setActionType(IdentitySourceActionType.INSERT.getKey());
            //其他信息
            entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
            entity.setCreateTime(new Date());
            list.add(entity);
        });
        //保存
        identitySourceSyncRecordService.batchSave(list);
    }

    /**
     * 保存更新用户记录
     *
     * @param historyId {@link String}
     * @param updateUsers {@link Set}
     */
    private void saveUpdateUsersRecord(String historyId, Set<UserInfo> updateUsers) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        updateUsers.forEach(user -> {
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(SyncStatus.SUCCESS.getKey());
            entity.setObjectType(IdentitySourceObjectType.USER.getKey());
            entity.setObjectId(user.getId());
            entity.setObjectName(user.getFullName());
            entity.setActionType(IdentitySourceActionType.UPDATE.getKey());
            //其他信息
            entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
            entity.setCreateTime(new Date());
            list.add(entity);
        });
        //保存
        identitySourceSyncRecordService.batchSave(list);
    }

    /**
     * 保存删除用户记录
     *
     * @param historyId {@link String}
     * @param deleteUsers {@link Set}
     */
    private void saveDeleteUsersRecord(String historyId, Set<UserInfo> deleteUsers) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        deleteUsers.forEach(user -> {
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(SyncStatus.SUCCESS.getKey());
            entity.setObjectType(IdentitySourceObjectType.USER.getKey());
            entity.setObjectId(user.getId());
            entity.setObjectName(user.getFullName());
            entity.setActionType(IdentitySourceActionType.DELETE.getKey());
            //其他信息
            entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
            entity.setCreateTime(new Date());
            list.add(entity);
        });
        //保存
        identitySourceSyncRecordService.batchSave(list);
    }

    /**
     * 保存跳过用户记录
     *
     * @param historyId {@link String}
     * @param skipUsers {@link Set}
     */
    private void saveSkipUsersRecord(String historyId, Set<SkipUser> skipUsers) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        skipUsers.forEach(skipUser -> {
            UserInfo user = skipUser.getUser();
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(skipUser.getStatus().getKey());
            entity.setObjectType(IdentitySourceObjectType.USER.getKey());
            entity.setObjectId(user.getId());
            entity.setObjectName(user.getFullName());
            entity.setActionType(skipUser.getActionType().getKey());
            //其他信息
            entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
            entity.setCreateTime(new Date());
            entity.setDescription(skipUser.getReason());
            list.add(entity);
        });
        //保存
        identitySourceSyncRecordService.batchSave(list);
    }

    /**
     * 更新同步历史
     *
     * @param processData {@link ProcessDataResult} 数据
     * @param history {@link IdentitySourceSyncHistory} 身份源同步历史
     * @param status {@link SyncStatus} 同步状态
     */
    private void updateSyncHistory(ProcessDataResult processData, IdentitySourceSyncHistory history,
                                   SyncStatus status) {
        if (!Objects.isNull(processData)) {
            history.setSkippedCount((long) processData.getSkipUsers().size());
            history.setDeletedCount((long) processData.getDeleteUsers().size());
            history.setUpdatedCount((long) processData.getUpdateUsers().size());
            history.setCreatedCount((long) processData.getCreateUsers().size());
        }
        history.setEndTime(new Date());
        history.setStatus(status.getKey());
        identitySourceSyncHistoryService.save(history);
    }

    /**
     * 保存同步历史
     *
     * @param batch {@link String} 批号
     * @param identitySourceId {@link String} 身份源ID
     * @param startTime {@link String} 开始时间
     * @param triggerType {@link String} 任务方式
     * @return {@link IdentitySourceSyncHistory}
     */
    @NotNull
    private IdentitySourceSyncHistory saveSyncHistory(String batch, String identitySourceId,
                                                      LocalDateTime startTime,
                                                      TriggerType triggerType) {
        IdentitySourceSyncHistory history = new IdentitySourceSyncHistory();
        history.setBatch(batch);
        history.setIdsourceId(identitySourceId);
        history.setStartTime(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()));
        history.setObjectType(IdentitySourceObjectType.USER.getKey());
        history.setTriggerType(triggerType.getKey());
        history.setStatus(SyncStatus.PENDING.getKey());
        identitySourceSyncHistoryService.save(history);
        return history;
    }

    /**
     * 初始化数据
     *
     * @param identitySource {@link IdentitySource} 身份源信息
     * @param thirdPartyUserList {@link List} 上游用户信息
     */
    private InitDataResult initData(IdentitySource identitySource,
                                    List<VerifyUser> thirdPartyUserList) {
        StrategyConfig strategyConfig = identitySource.getStrategyConfig();
        Long targetId = strategyConfig.getOrganization().getTargetId();
        //@formatter:off
        //封装三方组织及用户，为上游最新数据结构，Key: 用户，Value: 部门列表
        Map<VerifyUser, Set<EmisOrganVo>> thirdPartyUserOrganizations = Maps.newHashMap();
        //封装当前系统组织及用户， KEY: 用户，Value: 部门列表
        Map<UserInfo, Set<EmisOrganVo>> currentUserOrganizations = Maps.newHashMap();
        //查询身份源组织
        List<EmisOrganVo> identitySourceOrgList = emisOrganService
            .getVosByIdentitySourceId(identitySource.getId());
        if (identitySourceOrgList == null) {
            identitySourceOrgList = Lists.newArrayList();
        }
        Optional<EmisOrganVo> identitySourceRootOrg = Optional.ofNullable(
            emisOrganService.getVoById(targetId));
        //添加根组织
        identitySourceRootOrg.ifPresent(identitySourceOrgList::add);
        //封装三方用户
        for (VerifyUser thirdPartyUser : thirdPartyUserList) {
            List<String> deptIdList = thirdPartyUser.getDeptIdList();
            final List<EmisOrganVo> orgList = identitySourceOrgList;
            List<EmisOrganVo> list = orgList.stream().filter(dept -> {
                String rootForeignId = getRootForeignId(identitySource.getProvider());
                if (deptIdList.contains(rootForeignId) && Objects.equals(targetId, dept.getId())) {
                    return true;
                }
                return deptIdList.contains(dept.getForeignId());
            }).collect(Collectors.toList());
            thirdPartyUserOrganizations.put(thirdPartyUser, Sets.newHashSet(list));
        }

        //封装当前用户
        List<UserInfo> users = userInfoService.findAllByIdentitySourceId(identitySource.getId());
        Map<Long,EmisOrganVo> identitySourceOrgMap = identitySourceOrgList.stream().collect(Collectors.toMap(EmisOrganVo::getId, Function.identity()));
        users.forEach(user -> {
            List<UserOrganRelationDto> organizationMembers = userOrganRelationService.getByUserId(user.getId());
            List<String> organIdList = organizationMembers.stream().map(UserOrganRelation::getOrganId).collect(Collectors.toList());
            organIdList.forEach(organId -> {
                EmisOrganVo organization = identitySourceOrgMap.get(Long.valueOf(organId));
                if (CollectionUtils.isEmpty(currentUserOrganizations.get(user))) {
                    currentUserOrganizations.put(user, Sets.newHashSet(organization));
                } else {
                    Set<EmisOrganVo> set = currentUserOrganizations.get(user);
                    set.add(organization);
                    currentUserOrganizations.put(user, set);
                }
            });
            //会存在同一来源下组织未关联用户情况
            if (CollectionUtils.isEmpty(organIdList)) {
                currentUserOrganizations.put(user, Sets.newHashSet());
            }
        });
        return InitDataResult.builder().currentUsers(currentUserOrganizations).thirdPartyUsers(thirdPartyUserOrganizations)
                .build();
        //@formatter:on
    }

    @Data
    @Builder
    private static class InitDataResult implements Serializable {

        //@Serial
        private static final long                 serialVersionUID = 5558133274263199406L;

        /**
         * 封装三方部门及用户，为上游最新数据结构，Key: 用户，Value: 当前部门
         */
        private Map<VerifyUser, Set<EmisOrganVo>> thirdPartyUsers;
        /**
         * 封装当前系统部门及用户， KEY: 用户，Value: 用户所有部门
         */
        private Map<UserInfo, Set<EmisOrganVo>>   currentUsers;
    }

    /**
     * 处理数据
     *
     * @param initData {@link InitDataResult}
     */
    private ProcessDataResult processData(IdentitySource identitySource, InitDataResult initData) {
        //@formatter:off
        // 创建、修改用户等 Set集合，Key： 组织ID，Value： 用户信息
        Set<UserInfo> createUsers = new TreeSet<>(Comparator.comparing(UserInfo::getUsername)),
            updateUsers = new TreeSet<>(Comparator.comparing(UserInfo::getUsername));
        //跳过用户集合，Key：用户，Value：跳过原因
        Set<SkipUser> skipUsers = Sets.newLinkedHashSet();
        //当前系统用户对应系统部门
        Map<UserInfo, Set<EmisOrganVo>> currentUsers = initData.getCurrentUsers();
        //上游用户对应上游部门
        Map<VerifyUser, Set<EmisOrganVo>> thirdPartyUsers = initData.getThirdPartyUsers();
        //需要创建的组织成员关系
        Set<UserOrganRelation> createOrganizationMembers = Sets.newLinkedHashSet();
        //需要删除组织成员关系集合 KEY：用户ID，value：组织ID
        Map< String,Set<String>> deleteOrganizationMembers = Maps.newHashMap();
        //上游用户在当前系统存在
        currentUsers.keySet().stream().filter(i -> thirdPartyUsers.keySet().stream().map(VerifyUser::getUserId).collect(toSet()).contains(i.getForeignId())).collect(toSet())
                .forEach(currentUser -> thirdPartyUsers.keySet().forEach(thirdPartyUser -> {
            if (currentUser.getForeignId().equals(thirdPartyUser.getUserId())) {
                log.info("处理上游用户:[{}]开始", thirdPartyUser.getUserId());
                //处理用户信息
                if (!equalsUser(thirdPartyUser, currentUser,identitySource.getId())) {
                    //设置更新用户数据
                    setUpdateUser(thirdPartyUser, currentUser,identitySource);
                    log.info("上游用户:[{}]对应系统用户:[{}]({})存在,用户信息不一致,修改用户信息：{}", thirdPartyUser.getUserId(), currentUser.getUsername(), currentUser.getId(), JSONObject.toJSONString(currentUser));
                    updateUsers.add(currentUser);
                } else {
                    skipUsers.add(SkipUser.builder().user(currentUser).actionType(IdentitySourceActionType.UPDATE).status(SyncStatus.SKIP).reason( "用户信息一致").build());
                    log.info("上游用户:[{}]对应系统用户:[{}]({})存在,用户信息一致", thirdPartyUser.getUserId(), currentUser.getUsername(), currentUser.getId());
                }
                //处理组织机构关系
                Set<EmisOrganVo> thirdPartyOrganizations = thirdPartyUsers.get(thirdPartyUser);
                Set<EmisOrganVo> currentOrganizations = currentUsers.get(currentUser);
                //需要创建的新关系
                thirdPartyOrganizations.stream().filter(item -> !currentOrganizations.contains(item)).collect(toSet()).forEach(organization -> {
                    createOrganizationMember(createOrganizationMembers, currentUser, organization);
                    log.info("上游用户:[{}]对应当前系统用户:[{}]({})存在,需要创建组织[{}]({})与成员[{}]({})关系", thirdPartyUser.getUserId(), currentUser.getUsername(), currentUser.getId(), organization.getName(), organization.getId(), currentUser.getUsername(), currentUser.getId());
                });
                //需要删除的旧关系
                currentOrganizations.stream().filter(item -> Objects.nonNull(item) && !thirdPartyOrganizations.contains(item)).collect(toSet()).forEach(organization -> {
                    Set<String> ids = deleteOrganizationMembers.get(currentUser.getId());
                    log.info("上游用户:[{}]对应当前系统用户:[{}]({})存在,需要删除组织[{}]({})与成员[{}]({})关系", thirdPartyUser.getUserId(), currentUser.getUsername(), currentUser.getId(), organization.getName(), organization.getId(), currentUser.getUsername(), currentUser.getId());
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(ids)) {
                        ids.add(String.valueOf(organization.getId()));
                        deleteOrganizationMembers.put(currentUser.getId(), ids);
                        return;
                    }
                    deleteOrganizationMembers.put(currentUser.getId(), Sets.newHashSet(String.valueOf(organization.getId())));
                });
                log.info("处理上游用户:[{}]结束", thirdPartyUser.getUserId());
            }
        }));
        //上游用户在当前系统不存在
        thirdPartyUsers.keySet().stream().filter(i -> !currentUsers.keySet().stream().map(UserInfo::getForeignId).collect(toSet()).contains(i.getUserId())).collect(toSet())
                .forEach(thirdPartyUser -> {
            log.info("处理上游用户:[{}]开始", thirdPartyUser.getUserId());
            //新增用户信息
            UserInfo userEntity = thirdPartyUserToUserEntity(thirdPartyUser, identitySource);
            log.info("上游用户:[{}]在当前系统不存在,创建用户: {}", thirdPartyUser.getUserId(), JSONObject.toJSONString(userEntity));
            createUsers.add(userEntity);
            //处理组织机构关系
            thirdPartyUsers.get(thirdPartyUser).forEach(organization -> {
                createOrganizationMember(createOrganizationMembers, userEntity, organization);
                log.info("上游用户:[{}]在当前系统不存在,创建组织[{}]({})与成员[{}]({})关系", thirdPartyUser.getUserId(), organization.getName(), organization.getId(), userEntity.getUsername(), userEntity.getId());
            });
            log.info("处理上游用户:[{}]结束", thirdPartyUser.getUserId());
        });

        //过滤需要删除的用户
        Set<UserInfo> deleteUsers = currentUsers.keySet().stream().filter(currentUser -> !thirdPartyUsers.keySet().stream().map(VerifyUser::getUserId).collect(toSet()).contains(currentUser.getForeignId())).collect(toSet());
        for (UserInfo user : deleteUsers) {
            log.info("上游用户不存在,本地存在用户:[{}]({})", user.getUsername(),user.getId());
        }
        return ProcessDataResult.builder()
                //需要创建的用户
                .createUsers(createUsers)
                //需要更新的用户
                .updateUsers(updateUsers)
                //需要跳过的用户
                .skipUsers(skipUsers)
                //需要的删除用户ID
                .deleteUsers(deleteUsers)
                //需要创建的组织成员关系
                .createOrganizationMembers(createOrganizationMembers)
                //需要删除的组织成员关系
                .deleteOrganizationMembers(deleteOrganizationMembers).build();
        //@formatter:on
    }

    /**
     * 校验数据
     *
     * @param processData {@link ProcessDataResult}
     */
    private void validateData(ProcessDataResult processData) {
        //处理创建用户
        validateCreateUserData(processData);
        //处理修改用户
        validateUpdateUserData(processData);
    }

    /**
     * 验证创建用户数据
     *
     * @param processData {@link ProcessDataResult}
     */
    private void validateCreateUserData(ProcessDataResult processData) {
        Set<UserInfo> createUsers = processData.getCreateUsers();
        Set<SkipUser> skipUsers = processData.getSkipUsers();
        Set<UserOrganRelation> createOrganizationMembers = processData
            .getCreateOrganizationMembers();
        if (!CollectionUtils.isEmpty(createUsers)) {
            //根据用户名查询
            List<UserInfo> existUsernames = getByUsernames(createUsers);
            //根据邮箱查询
            List<UserInfo> existEmails = getEmails(createUsers);
            //根据手机号查询
            List<UserInfo> existMobile = getMobiles(createUsers);
            existUsernames.forEach(user -> {
                Iterator<UserInfo> userIterator = createUsers.iterator();
                while (userIterator.hasNext()) {
                    UserInfo createUser = userIterator.next();
                    //校验用户名
                    if (createUser.getUsername().equals(user.getUsername())) {
                        log.info("跳过上游用户:[{{}}]创建,用户名:[{}]在系统已存在", createUser.getForeignId(),
                            user.getUsername());
                        skipUsers.add(SkipUser.builder().user(createUser)
                            .actionType(IdentitySourceActionType.INSERT).status(SyncStatus.FAIL)
                            .reason("用户名 [" + createUser.getUsername() + "] 已存在").build());
                        userIterator.remove();
                        //移除创建关系
                        createOrganizationMembers
                            .removeIf(member -> member.getUserId().equals(createUser.getId()));
                    }
                }
            });
            existEmails.forEach(user -> {
                Iterator<UserInfo> userIterator = createUsers.iterator();
                while (userIterator.hasNext()) {
                    UserInfo createUser = userIterator.next();
                    //校验邮箱
                    if (StringUtils.isNotBlank(createUser.getEmail())
                        && createUser.getEmail().equals(user.getEmail())) {
                        log.info("跳过上游用户:[{{}}]创建,邮箱:[{}]在系统已存在", createUser.getForeignId(),
                            user.getEmail());
                        skipUsers.add(SkipUser.builder().user(createUser)
                            .actionType(IdentitySourceActionType.INSERT).status(SyncStatus.FAIL)
                            .reason("邮箱 [" + createUser.getEmail() + "] 已存在").build());
                        userIterator.remove();
                        //移除创建关系
                        createOrganizationMembers
                            .removeIf(member -> member.getUserId().equals(createUser.getId()));
                    }
                }
            });
            existMobile.forEach(user -> {
                Iterator<UserInfo> userIterator = createUsers.iterator();
                while (userIterator.hasNext()) {
                    UserInfo createUser = userIterator.next();
                    //校验手机号
                    if (StringUtils.isNotBlank(createUser.getMobile())
                        && createUser.getMobile().equals(user.getMobile())) {
                        log.info("跳过上游用户:[{{}}]创建,手机号:[{}]在系统已存在", createUser.getForeignId(),
                            user.getMobile());
                        skipUsers.add(SkipUser.builder().user(createUser)
                            .actionType(IdentitySourceActionType.INSERT).status(SyncStatus.FAIL)
                            .reason("手机号 [" + createUser.getMobile() + "] 已存在").build());
                        userIterator.remove();
                        //移除创建关系
                        createOrganizationMembers
                            .removeIf(member -> member.getUserId().equals(createUser.getId()));
                    }
                }
            });
        }
    }

    /**
     * 验证修改用户数据
     *
     * @param processData {@link ProcessDataResult}
     */
    private void validateUpdateUserData(ProcessDataResult processData) {
        Set<UserInfo> updateUsers = processData.getUpdateUsers();
        Set<SkipUser> skipUsers = processData.getSkipUsers();
        if (!CollectionUtils.isEmpty(updateUsers)) {
            //根据用户名查询
            List<UserInfo> existUsernames = getByUsernames(updateUsers);
            //根据邮箱查询
            List<UserInfo> existEmails = getEmails(updateUsers);
            //根据手机号查询
            List<UserInfo> existMobile = getMobiles(updateUsers);
            existUsernames.forEach(user -> {
                Iterator<UserInfo> userIterator = updateUsers.iterator();
                while (userIterator.hasNext()) {
                    UserInfo updateUser = userIterator.next();
                    //校验用户名
                    if (updateUser.getUsername().equals(user.getUsername())
                        && !updateUser.getId().equals(user.getId())) {
                        log.info("跳过上游用户:[{{}}]修改,用户名:[{}]在系统已存在", updateUser.getForeignId(),
                            user.getUsername());
                        skipUsers.add(SkipUser.builder().user(updateUser)
                            .actionType(IdentitySourceActionType.UPDATE).status(SyncStatus.FAIL)
                            .reason("用户名 [" + updateUser.getUsername() + "] 已存在").build());
                        userIterator.remove();
                    }
                }
            });
            existEmails.forEach(user -> {
                Iterator<UserInfo> userIterator = updateUsers.iterator();
                while (userIterator.hasNext()) {
                    UserInfo updateUser = userIterator.next();
                    //校验邮箱
                    if (StringUtils.isNotBlank(updateUser.getEmail())
                        && updateUser.getEmail().equals(user.getEmail())
                        && !updateUser.getId().equals(user.getId())) {
                        log.info("跳过上游用户:[{{}}]修改,邮箱:[{}]在系统已存在", updateUser.getForeignId(),
                            user.getEmail());
                        skipUsers.add(SkipUser.builder().user(updateUser)
                            .actionType(IdentitySourceActionType.UPDATE).status(SyncStatus.FAIL)
                            .reason("邮箱 [" + updateUser.getEmail() + "] 已存在").build());
                        userIterator.remove();
                    }
                }
            });
            existMobile.forEach(user -> {
                Iterator<UserInfo> userIterator = updateUsers.iterator();
                while (userIterator.hasNext()) {
                    UserInfo updateUser = userIterator.next();
                    //校验手机号
                    if (StringUtils.isNotBlank(updateUser.getMobile())
                        && updateUser.getMobile().equals(user.getMobile())
                        && !updateUser.getId().equals(user.getId())) {
                        log.info("跳过上游用户:[{{}}]修改,手机号:[{}]在系统已存在", updateUser.getForeignId(),
                            user.getMobile());
                        skipUsers.add(SkipUser.builder().user(updateUser)
                            .actionType(IdentitySourceActionType.UPDATE).status(SyncStatus.FAIL)
                            .reason("手机号 [" + updateUser.getMobile() + "] 已存在").build());
                        userIterator.remove();
                    }
                }
            });
        }
    }

    private List<UserInfo> getMobiles(Set<UserInfo> list) {
        List<String> mobiles = list.stream().map(UserInfo::getMobile).collect(Collectors.toList());
        return userInfoService.findAllByMobileIn(mobiles.toArray(new String[0]));
    }

    private List<UserInfo> getEmails(Set<UserInfo> userList) {
        List<String> emails = userList.stream().map(UserInfo::getEmail)
            .collect(Collectors.toList());
        return userInfoService.findAllByEmailIn(emails.toArray(new String[0]));
    }

    private List<UserInfo> getByUsernames(Set<UserInfo> userList) {
        List<String> usernames = userList.stream().map(UserInfo::getUsername)
            .collect(Collectors.toList());
        return userInfoService.findAllByUsernameIn(usernames.toArray(new String[0]));
    }

    @Data
    @Builder
    private static class ProcessDataResult implements Serializable {

        //@Serial
        private static final long        serialVersionUID = 5995750190105246430L;

        /**
         * 创建用户集合
         */
        private Set<UserInfo>            createUsers;

        /**
         * 更新用户集合
         */
        private Set<UserInfo>            updateUsers;

        /**
         * 跳过用户集合
         */
        private Set<SkipUser>            skipUsers;

        /**
         * 删除用户
         */
        private Set<UserInfo>            deleteUsers;

        /**
         * 创建组织成员关系
         */
        private Set<UserOrganRelation>   createOrganizationMembers;

        /**
         * 删除组织成员关系 KEY：用户ID，value： 组织ID集合
         */
        private Map<String, Set<String>> deleteOrganizationMembers;
    }

    /**
     * 用户 Service
     */
    private final UserInfoService          userInfoService;

    /**
     * 用户详情 Service
     */
    private final UserExtService           userExtService;

    /**
     * 组织机构 Service
     */
    private final EmisOrganService         emisOrganService;

    /**
     * 组织成员 Service
     */
    private final UserOrganRelationService userOrganRelationService;

    /**
     * 删除用户组关联关系
     */
    private final UserGroupRelationService userGroupRelationService;

    public DefaultIdentitySourceUserPostProcessor(MailMsgEventPublish mailMsgEventPublish,
                                                  TransactionDefinition transactionDefinition,
                                                  PlatformTransactionManager platformTransactionManager,
                                                  IdentitySourceService identitySourceService,
                                                  IdentitySourceSyncHistoryService identitySourceSyncHistoryService,
                                                  IdentitySourceSyncRecordService identitySourceSyncRecordService,
                                                  PasswordEncoder passwordEncoder,
                                                  PasswordGenerator passwordGenerator,
                                                  UserInfoService userInfoService,
                                                  UserExtService userExtService,
                                                  UserOrganRelationService userOrganRelationService,
                                                  UserGroupRelationService userGroupRelationService,
                                                  EmisOrganService emisOrganService,
                                                  Storage storage) {
        super(mailMsgEventPublish, passwordEncoder, passwordGenerator, transactionDefinition,
            platformTransactionManager, identitySourceService, identitySourceSyncHistoryService,
            identitySourceSyncRecordService, storage);
        this.userInfoService = userInfoService;
        this.userExtService = userExtService;
        this.userOrganRelationService = userOrganRelationService;
        this.userGroupRelationService = userGroupRelationService;
        this.emisOrganService = emisOrganService;
    }
}
