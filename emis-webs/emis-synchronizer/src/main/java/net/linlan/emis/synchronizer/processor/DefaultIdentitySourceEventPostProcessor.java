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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.RandomUtils;
import net.linlan.emis.core.constant.SyncStatus;
import net.linlan.emis.core.storage.Storage;
import net.linlan.emis.idsource.constant.IdentitySourceActionType;
import net.linlan.emis.idsource.constant.IdentitySourceObjectType;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.idsource.entity.IdentitySourceEventRecord;
import net.linlan.emis.idsource.service.IdentitySourceEventRecordService;
import net.linlan.emis.idsource.service.IdentitySourceService;
import net.linlan.emis.idsource.service.IdentitySourceSyncHistoryService;
import net.linlan.emis.idsource.service.IdentitySourceSyncRecordService;
import net.linlan.emis.manage.constant.IdentitySourceEventReceiveType;
import net.linlan.emis.manage.mail.MailMsgEventPublish;
import net.linlan.emis.manage.processor.IdentitySourceEventPostProcessor;
import net.linlan.emis.manage.processor.modal.IdentitySourceEventProcessData;
import net.linlan.emis.manage.vo.VerifyDept;
import net.linlan.emis.manage.vo.VerifyUser;
import net.linlan.emis.organ.dto.UserOrganRelationDto;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.service.EmisOrganService;
import net.linlan.emis.organ.service.UserOrganRelationService;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.security.password.PasswordGenerator;
import net.linlan.emis.support.userdetails.OrganizationType;
import net.linlan.emis.user.entity.UserExt;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserExtService;
import net.linlan.emis.user.service.UserGroupRelationService;
import net.linlan.emis.user.service.UserInfoService;
import static java.util.stream.Collectors.toSet;

import static net.linlan.emis.support.constant.EmisConstants.SYSTEM_DEFAULT_USER_NAME;

/**
 * 身份源数据 event 处理器
 *
 * @author Linlan
 * Created on 2022/3/1 22:04
 */
@Slf4j
@Component
@SuppressWarnings({ "rawtypes", "unchecked", "DuplicatedCode" })
public class DefaultIdentitySourceEventPostProcessor extends AbstractIdentitySourcePostProcessor
                                                     implements IdentitySourceEventPostProcessor {
    private static final String PATH_SEPARATOR = "/";

    /**
     * 处理数据
     *
     * @param eventData {@link  IdentitySourceEventProcessData}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void process(IdentitySourceEventProcessData eventData) {
        log.info("处理数据上游回调数据开始: {}", eventData);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        IdentitySourceEventReceiveType eventType = eventData.getEventType();
        IdentitySource identitySource = getIdentitySource(eventData.getId());
        LocalDateTime eventTime = eventData.getEventTime();
        //用户添加
        if (IdentitySourceEventReceiveType.USER_ADD.equals(eventType)) {
            createUsers(eventData.getData(), eventTime, identitySource);
            //用户修改
        } else if (IdentitySourceEventReceiveType.USER_MODIFY.equals(eventType)) {
            modifyUsers(eventData.getData(), eventTime, identitySource);
            //用户离职
        } else if (IdentitySourceEventReceiveType.USER_LEAVE.equals(eventType)) {
            leaveUsers(eventData.getData(), eventTime, identitySource);
            //部门添加
        } else if (IdentitySourceEventReceiveType.DEPT_CREATE.equals(eventType)) {
            createOrganizations(eventData.getData(), eventTime, identitySource);
            //部门修改
        } else if (IdentitySourceEventReceiveType.DEPT_MODIFY.equals(eventType)) {
            modifyOrganizations(eventData.getData(), eventTime, identitySource);
            //部门移除
        } else if (IdentitySourceEventReceiveType.DEPT_REMOVE.equals(eventType)) {
            removeOrganizations(eventData.getData(), eventTime, identitySource);
        } else {
            throw new SupportException(eventData.getProvider().getName() + "身份提供商事件回调非法事件");
        }
        stopWatch.stop();
        log.info("处理数据上游回调数据结束, 执行时长: {} ms", stopWatch.getTotalTimeMillis());
    }

    /**
     * 创建用户
     *
     * @param thirdPartyUserList {@link List}
     */
    void createUsers(List<VerifyUser> thirdPartyUserList, LocalDateTime eventTime,
                     IdentitySource identitySource) {
        List<IdentitySourceEventRecord> records = new ArrayList<>();
        List<UserInfo> createUsers = new ArrayList<>();
        Set<UserOrganRelation> createOrganizationMembers = Sets.newHashSet();
        Long targetId = identitySource.getStrategyConfig().getOrganization().getTargetId();
        //根据用户名查询
        List<UserInfo> existUsernames = getByUsernames(
            thirdPartyUserList.stream().map(VerifyUser::getUserId).collect(Collectors.toList()));
        //根据邮箱查询
        List<UserInfo> existEmails = getEmails(
            thirdPartyUserList.stream().map(VerifyUser::getEmail).collect(Collectors.toList()));
        //根据手机号查询
        List<UserInfo> existMobile = getMobiles(
            thirdPartyUserList.stream().map(VerifyUser::getMobile).collect(Collectors.toList()));
        thirdPartyUserList.forEach(thirdPartyUser -> {
            log.info("处理上游用户新增事件:[{}]开始", thirdPartyUser.getUserId());
            UserInfo createUser = thirdPartyUserToUserEntity(thirdPartyUser, identitySource);
            log.info("根据上游用户:[{}],创建本地用户: {}", thirdPartyUser.getUserId(),
                JSONObject.toJSONString(createUser));
            if (existUsernames.stream()
                .anyMatch(user -> createUser.getUsername().equals(user.getUsername()))) {
                log.info("跳过上游用户:[{{}}]创建,用户名:[{}]在系统已存在", createUser.getForeignId(),
                    createUser.getUsername());
                addRecord(eventTime, identitySource, createUser.getId(), createUser.getFullName(),
                    records, SyncStatus.FAIL, IdentitySourceObjectType.USER,
                    IdentitySourceActionType.INSERT, "用户名 [" + createUser.getUsername() + "] 已存在");
                return;
            }
            if (StringUtils.isNotBlank(createUser.getEmail()) && existEmails.stream()
                .anyMatch(user -> createUser.getEmail().equals(user.getEmail()))) {
                log.info("跳过上游用户:[{{}}]创建,邮箱:[{}]在系统已存在", createUser.getForeignId(),
                    createUser.getEmail());
                addRecord(eventTime, identitySource, createUser.getId(), createUser.getFullName(),
                    records, SyncStatus.FAIL, IdentitySourceObjectType.USER,
                    IdentitySourceActionType.INSERT, "邮箱 [" + createUser.getEmail() + "] 已存在");
                return;
            }
            if (StringUtils.isNotBlank(createUser.getMobile()) && existMobile.stream()
                .anyMatch(user -> createUser.getMobile().equals(user.getMobile()))) {
                log.info("跳过上游用户:[{{}}]创建,手机号:[{}]在系统已存在", createUser.getForeignId(),
                    createUser.getMobile());
                addRecord(eventTime, identitySource, createUser.getId(), createUser.getFullName(),
                    records, SyncStatus.FAIL, IdentitySourceObjectType.USER,
                    IdentitySourceActionType.INSERT, "手机号 [" + createUser.getMobile() + "] 已存在");
                return;
            }
            createUsers.add(createUser);
            //组织关系
            List<EmisOrganVo> organizationList = emisOrganService
                .getVosByForeignIds(thirdPartyUser.getDeptIdList(), identitySource.getId());
            if (organizationList == null) {
                organizationList = Lists.newArrayList();
            }
            // 根组织
            if (thirdPartyUser.getDeptIdList().stream().anyMatch(
                deptId -> deptId.equals(getRootForeignId(identitySource.getProvider())))) {
                EmisOrganVo rootOrgan = emisOrganService.getVoById(Long.valueOf(targetId));
                if (rootOrgan != null) {
                    organizationList.add(rootOrgan);
                }
            }
            organizationList.forEach(organization -> {
                log.info("根据上游用户:[{}],创建组织[{}]({})与成员[{}]({})关系", thirdPartyUser.getUserId(),
                    organization.getName(), organization.getId(), createUser.getUsername(),
                    createUser.getId());
                createOrganizationMember(createOrganizationMembers, createUser, organization);
            });
            addRecord(eventTime, identitySource, createUser.getId(), createUser.getFullName(),
                records, SyncStatus.SUCCESS, IdentitySourceObjectType.USER,
                IdentitySourceActionType.INSERT, "新增成功");
            log.info("处理上游用户新增事件:[{}]结束", thirdPartyUser.getUserId());
        });
        //保存用户
        userInfoService.batchSave(createUsers);
        //保存组织关系
        userOrganRelationService.batchSave(Lists.newArrayList(createOrganizationMembers));
        //保存事件记录
        identitySourceEventRecordService.batchSave(records);
        // 批量发送短信邮件欢迎信息(密码通知)
        if (Boolean.TRUE.equals(identitySource.getStrategyConfig().getUser().getEmailNotify())) {
            publishMessage(createUsers);
        }
    }

    /**
     * 修改用户
     *
     * @param thirdPartyUserList {@link List}
     */
    void modifyUsers(List<VerifyUser> thirdPartyUserList, LocalDateTime eventTime,
                     IdentitySource identitySource) {
        //@formatter:off
        List<IdentitySourceEventRecord> records = new ArrayList<>();
        List<UserInfo> updateUsers = new ArrayList<>();
        Set<UserOrganRelation> createOrganizationMembers = new HashSet<>();
        //需要删除组织成员关系集合 KEY：用户ID，value：组织ID
        Map<String, Set<String>> deleteOrganizationMembers = Maps.newHashMap();
        Map<UserInfo, Set<EmisOrganVo>> currentUsers = Maps.newHashMap();
        Map<VerifyUser, Set<EmisOrganVo>> thirdPartyUsers = Maps.newHashMap();
        Long targetId = identitySource.getStrategyConfig().getOrganization().getTargetId();
        //根据用户名查询
        List<UserInfo> existUsernames = getByUsernames(thirdPartyUserList.stream().map(VerifyUser::getUserId).collect(Collectors.toList()));
        //根据邮箱查询
        List<UserInfo> existEmails = getEmails(thirdPartyUserList.stream().map(VerifyUser::getEmail).collect(Collectors.toList()));
        //根据手机号查询
        List<UserInfo> existMobile = getMobiles(thirdPartyUserList.stream().map(VerifyUser::getMobile).collect(Collectors.toList()));
        //根据上游用户封装数据，因为这里是修改操作，所以只有本地存在才会处理，如果上游用户在本地不存在，减少复杂度，这里不处理，可以走增量拉取
        thirdPartyUserList.forEach(thirdPartyUser -> {

            Optional<UserInfo> optional = Optional
                .ofNullable(userInfoService.getByForeignId(thirdPartyUser.getUserId()));
            if (optional.isPresent()) {
                UserInfo user = optional.get();
                //查询当前系统用户组织关联信息
                List<UserOrganRelationDto> organizationMembers = userOrganRelationService.getByUserId(user.getId());
                List<String> organIdList = organizationMembers.stream().map(UserOrganRelation::getOrganId).collect(Collectors.toList());
                //查询当前系统用户组织
                List<EmisOrganVo> currentUserOrgList = emisOrganService.getVosByOrganIds(identitySource.getId(), organIdList.toArray(new String[0]));
                if (currentUserOrgList == null) {
                    currentUserOrgList = Lists.newArrayList();
                }
                //添加根组织
                if (organIdList.contains(String.valueOf(targetId))) {
                    EmisOrganVo rootOrgan = emisOrganService.getVoById(targetId);
                    if (rootOrgan != null) {
                        currentUserOrgList.add(rootOrgan);
                    }
                }
                currentUsers.put(optional.get(), Sets.newHashSet(currentUserOrgList));
                //查询上游用户组织信息
                List<EmisOrganVo> organizationList = emisOrganService.getVosByForeignIds(thirdPartyUser.getDeptIdList(), identitySource.getId());
                if (organizationList == null) {
                    organizationList = Lists.newArrayList();
                }
                // 根组织
                if (thirdPartyUser.getDeptIdList().stream().anyMatch(deptId -> deptId.equals(getRootForeignId(identitySource.getProvider())))) {
                    EmisOrganVo rootOrgan = emisOrganService.getVoById(Long.valueOf(targetId));
                    if (rootOrgan != null) {
                        organizationList.add(rootOrgan);
                    }
                }
                thirdPartyUsers.put(thirdPartyUser, Sets.newHashSet(organizationList));
            }
        });
        thirdPartyUsers.keySet()
            .forEach(thirdPartyUser -> currentUsers.keySet().forEach(currentUser -> {
                if (thirdPartyUser.getUserId().equals(currentUser.getForeignId())) {
                    log.info("处理上游用户修改事件:[{}]开始", thirdPartyUser.getUserId());
                    //更新基本信息
                    setUpdateUser(thirdPartyUser, currentUser, identitySource);
                    log.info("上游用户:[{}]对应系统用户:[{}]({})存在,修改本地用户信息：{}", thirdPartyUser.getUserId(), currentUser.getUsername(), currentUser.getId(), JSONObject.toJSONString(currentUser));
                    if (existUsernames.stream().anyMatch(user -> !currentUser.getForeignId().equals(user.getForeignId()) && currentUser.getUsername().equals(user.getUsername()))) {
                        log.info("跳过上游用户:[{{}}]创建,用户名:[{}]在系统已存在", currentUser.getForeignId(),
                                currentUser.getUsername());
                        addRecord(eventTime, identitySource, currentUser.getId(), currentUser.getFullName(), records, SyncStatus.FAIL,
                                IdentitySourceObjectType.USER, IdentitySourceActionType.UPDATE, "用户名 [" + currentUser.getUsername() + "] 已存在");
                        return;
                    }
                    if (StringUtils.isNotBlank(currentUser.getEmail()) && existEmails.stream().anyMatch(user -> !currentUser.getForeignId().equals(user.getForeignId()) && currentUser.getEmail().equals(user.getEmail()))) {
                        log.info("跳过上游用户:[{{}}]创建,邮箱:[{}]在系统已存在", currentUser.getForeignId(),
                                currentUser.getEmail());
                        addRecord(eventTime, identitySource, currentUser.getId(), currentUser.getFullName(), records, SyncStatus.FAIL,
                                IdentitySourceObjectType.USER, IdentitySourceActionType.UPDATE, "邮箱 [" + currentUser.getEmail() + "] 已存在");
                        return;
                    }
                    if (StringUtils.isNotBlank(currentUser.getMobile()) && existMobile.stream().anyMatch(user -> !currentUser.getForeignId().equals(user.getForeignId()) && currentUser.getMobile().equals(user.getMobile()))) {
                        log.info("跳过上游用户:[{{}}]创建,手机号:[{}]在系统已存在", currentUser.getForeignId(),
                                currentUser.getMobile());
                        addRecord(eventTime, identitySource, currentUser.getId(), currentUser.getFullName(), records, SyncStatus.FAIL,
                                IdentitySourceObjectType.USER, IdentitySourceActionType.UPDATE, "手机号 [" + currentUser.getMobile() + "] 已存在");
                        return;
                    }
                    updateUsers.add(currentUser);
                    //处理组织机构关系
                    Set<EmisOrganVo> thirdPartyOrganizations = thirdPartyUsers.get(thirdPartyUser);
                    Set<EmisOrganVo> currentOrganizations = currentUsers.get(currentUser);
                    //需要创建的新关系
                    thirdPartyOrganizations.stream()
                        .filter(item -> !currentOrganizations.contains(item)).collect(toSet())
                        .forEach(organization -> {createOrganizationMember(createOrganizationMembers, currentUser, organization);
                            log.info("上游用户:[{}]对应当前系统用户:[{}]({}),需要创建本地组织[{}]({})与本地成员[{}]({})关系", thirdPartyUser.getUserId(), currentUser.getUsername(), currentUser.getId(), organization.getName(), organization.getId(), currentUser.getUsername(), currentUser.getId());
                        });
                    //需要删除的旧关系
                    currentOrganizations.stream()
                        .filter(item -> !thirdPartyOrganizations.contains(item)).collect(toSet())
                        .forEach(organization -> {
                            Set<String> ids = deleteOrganizationMembers.get(currentUser.getId());
                            log.info("上游用户:[{}]对应当前系统用户:[{}]({}),需要删除本地组织[{}]({})与本地成员[{}]({})关系", thirdPartyUser.getUserId(), currentUser.getUsername(), currentUser.getId(), organization.getName(), organization.getId(), currentUser.getUsername(), currentUser.getId());
                            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(ids)) {
                                ids.add(String.valueOf(organization.getId()));
                                deleteOrganizationMembers.put(currentUser.getId(), ids);
                                return;
                            }
                            deleteOrganizationMembers.put(currentUser.getId(), Sets.newHashSet(String.valueOf(organization.getId())));
                        });
                    //记录日志
                    addRecord(eventTime, identitySource, currentUser.getId(), currentUser.getFullName(), records, SyncStatus.SUCCESS,
                            IdentitySourceObjectType.USER, IdentitySourceActionType.UPDATE, "修改成功");
                    log.info("处理上游用户修改事件:[{}]结束", thirdPartyUser.getUserId());
                }
                else {
                    addRecord(eventTime, identitySource, thirdPartyUser.getUserId(), Objects.nonNull(thirdPartyUser.getUserDetail()) ? thirdPartyUser.getUserDetail().getName() : null, records, SyncStatus.SKIP,
                            IdentitySourceObjectType.ORGANIZATION, IdentitySourceActionType.UPDATE, "用户[" + (Objects.nonNull(thirdPartyUser.getUserDetail()) ? thirdPartyUser.getUserDetail().getName() : thirdPartyUser.getUserId()) + "]不存在");
                }
            }));
        //更新用户信息
        updateUsers.forEach(userInfoService::update);
        //保存组织关系
        userOrganRelationService.batchSave(Lists.newArrayList(createOrganizationMembers));
        //删除组织关系
        deleteOrganizationMembers.keySet()
            .forEach(user -> deleteOrganizationMembers.get(user)
                .forEach(organization -> userOrganRelationService
                    .deleteByOrganIdAndUserId(organization, user)));
        //保存事件记录
        identitySourceEventRecordService.batchSave(records);
        //@formatter:on
    }

    /**
     * 用户离职
     *
     * @param userIds {@link List}
     */
    void leaveUsers(List<String> userIds, LocalDateTime eventTime, IdentitySource identitySource) {
        List<IdentitySourceEventRecord> records = new ArrayList<>();
        List<UserInfo> list = Lists
            .newArrayList(userInfoService.getListByForeignIds(userIds.toArray(new String[0])));
        List<UserInfo> removeUserList = new ArrayList<>();
        list.forEach(user -> {
            log.info("处理上游用户离职事件:[{}]开始", user);
            //离职删除
            removeUserList.add(user);
            //记录日志
            addRecord(eventTime, identitySource, user.getId(), user.getFullName(), records,
                SyncStatus.SUCCESS, IdentitySourceObjectType.USER, IdentitySourceActionType.DELETE,
                "删除成功");
            log.info("处理上游用户离职事件:[{}]结束", user);
        });
        //保存事件记录
        identitySourceEventRecordService.batchSave(records);
        //删除用户信息
        if (!CollectionUtils.isEmpty(removeUserList)) {
            List<String> deleteUserIds = removeUserList.stream().map(UserInfo::getId)
                .collect(Collectors.toList());
            //删除用户
            userInfoService.deleteByIds(deleteUserIds.toArray(new String[0]));
            //删除用户详情
            List<UserExt> userExtList = userExtService
                .findAllByUserIdsIn(deleteUserIds.toArray(new String[0]));
            if (!CollectionUtils.isEmpty(userExtList)) {
                userExtService
                    .deleteByIds(userExtList.stream().map(UserExt::getId).toArray(String[]::new));
            }
            //删除组织用户关联关系
            userOrganRelationService.deleteByUserIds(deleteUserIds.toArray(new String[0]));
            //删除用户组关系
            userGroupRelationService.deleteAllByUserIdsIn(deleteUserIds.toArray(new String[0]));
        }
    }

    /**
     * 创建部门
     *
     * @param organizations {@link List}
     */
    void createOrganizations(List<VerifyDept> organizations, LocalDateTime eventTime,
                             IdentitySource identitySource) {
        List<IdentitySourceEventRecord> records = new ArrayList<>();
        List<EmisOrganVo> createOrganizations = new ArrayList<>();
        organizations.forEach(thirdPartyOrganization -> {
            log.info("处理上游部门新增事件:[{}]开始", thirdPartyOrganization.getDeptId());
            //查询上级部门
            //如果父部门是根节点，那需要查询一下EMIS中根节点目前配置的是多少
            Optional<EmisOrganVo> parentOptional;
            if (!thirdPartyOrganization.getParentId()
                .equals(getRootForeignId(identitySource.getProvider()))) {
                parentOptional = Optional.ofNullable(
                    emisOrganService.findByForeignId(thirdPartyOrganization.getParentId()));
            } else {
                Long targetId = identitySource.getStrategyConfig().getOrganization().getTargetId();
                parentOptional = Optional.ofNullable(emisOrganService.getVoById(targetId));
            }
            if (!parentOptional.isPresent()) {
                return;
            }
            EmisOrganVo parentOrganization = parentOptional.get();
            //更改 leaf=false
            if (parentOrganization.getLeaf()) {
                emisOrganService.updateIsLeaf(parentOrganization.getId(), false);
            }
            //封装数据
            EmisOrganVo entity = new EmisOrganVo();
            entity.setId(RandomUtils.randomLid());
            entity.setName(thirdPartyOrganization.getName());
            entity.setCode(RandomStringUtils.randomAlphanumeric(7));
            entity.setType(OrganizationType.DEPARTMENT);
            entity.setParentId(parentOrganization.getId());
            entity.setPath(parentOrganization.getPath() + PATH_SEPARATOR + entity.getId());
            entity.setDisplayPath(
                parentOrganization.getDisplayPath() + PATH_SEPARATOR + entity.getName());
            entity.setForeignId(thirdPartyOrganization.getDeptId());
            entity.setPriority(thirdPartyOrganization.getPriority() == null ? null
                : thirdPartyOrganization.getPriority().intValue());
            entity.setIdentitySourceId(identitySource.getId());
            entity.setDataOrigin(getDataOrigin(identitySource.getProvider()).getType());
            entity.setLeaf(true);
            entity.setEnabled(true);
            //额外信息
            entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
            entity.setCreateTime(new Date());
            createOrganizations.add(entity);
            //记录日志
            addRecord(eventTime, identitySource, String.valueOf(entity.getId()), entity.getName(),
                records, SyncStatus.SUCCESS, IdentitySourceObjectType.ORGANIZATION,
                IdentitySourceActionType.INSERT, "新增成功");
            log.info("处理上游部门新增事件:[{}]结束", thirdPartyOrganization.getDeptId());
        });
        //保存部门信息
        createOrganizations.forEach(emisOrganService::saveByVo);
        //保存事件记录
        identitySourceEventRecordService.batchSave(records);
    }

    /**
     * 修改部门
     *
     * @param organizations {@link List}
     */
    void modifyOrganizations(List<VerifyDept> organizations, LocalDateTime eventTime,
                             IdentitySource identitySource) {
        List<IdentitySourceEventRecord> records = new ArrayList<>();
        List<EmisOrganVo> updateOrganizations = new ArrayList<>();
        //根据外部ID查询
        List<EmisOrganVo> list = emisOrganService.getVosByForeignIds(
            organizations.stream().map(VerifyDept::getDeptId).collect(Collectors.toList()),
            identitySource.getId());
        if (list == null) {
            return;
        }
        list.forEach(current -> organizations.forEach(threeParty -> {
            //当前三方ID和上游科室对应
            if (current.getForeignId().equals(threeParty.getDeptId())) {

                //判断父部门是否变化，父部门变化，需要更改次部门的下级部门，判断名称是否更改，需要更改子路径显示名称
                //如果父部门是根节点，那需要查询一下EMIS中根节点目前配置的是多少
                Optional<EmisOrganVo> parentOptional;
                if (!threeParty.getParentId()
                    .equals(getRootForeignId(identitySource.getProvider()))) {
                    parentOptional = Optional
                        .ofNullable(emisOrganService.findByForeignId(threeParty.getParentId()));
                } else {
                    Long targetId = identitySource.getStrategyConfig().getOrganization()
                        .getTargetId();
                    parentOptional = Optional.ofNullable(emisOrganService.getVoById(targetId));
                }
                if (!parentOptional.isPresent()) {
                    return;
                }
                //上级部门不一致
                EmisOrganVo parentOrganization = parentOptional.get();
                if (!Objects.equals(parentOrganization.getId(), current.getParentId())
                    || !StringUtils.equals(current.getName(), threeParty.getName())) {
                    current.setParentId(parentOrganization.getId());
                    current
                        .setPath(parentOrganization.getPath() + PATH_SEPARATOR + current.getId());
                    current.setDisplayPath(parentOrganization.getDisplayPath() + PATH_SEPARATOR
                                           + threeParty.getName());
                    //递归组装子级数据
                    updateOrganizations.addAll(processSubOrganizations(current));
                    //当前组织的父组织是否具有子节点，已经不存在子节点，更改 leaf=true
                    List<EmisOrganVo> subOrganizations = emisOrganService
                        .getVosByParentId(parentOrganization.getId());
                    if (subOrganizations != null && subOrganizations.stream()
                        .map(i -> i.getId().equals(current.getId())).findAny().isPresent()) {
                        emisOrganService.updateIsLeaf(parentOrganization.getId(), true);
                    }
                }
                if (parentOrganization.getLeaf()) {
                    emisOrganService.updateIsLeaf(parentOrganization.getId(), false);
                }
                //修改基本信息
                current.setName(threeParty.getName());
                current.setPriority(
                    threeParty.getPriority() == null ? null : threeParty.getPriority().intValue());
                updateOrganizations.add(current);
                //记录日志
                addRecord(eventTime, identitySource, String.valueOf(current.getId()),
                    current.getName(), records, SyncStatus.SUCCESS,
                    IdentitySourceObjectType.ORGANIZATION, IdentitySourceActionType.UPDATE, "修改成功");
            } else {
                addRecord(eventTime, identitySource, String.valueOf(current.getId()),
                    current.getName(), records, SyncStatus.SKIP,
                    IdentitySourceObjectType.ORGANIZATION, IdentitySourceActionType.UPDATE,
                    "部门[" + threeParty.getName() + "]不存在");
            }
        }));
        //批量保存部门信息
        updateOrganizations.forEach(emisOrganService::saveByVo);
        //保存事件记录
        identitySourceEventRecordService.batchSave(records);
    }

    /**
     * 删除部门
     *
     * @param organIds {@link List}
     */
    void removeOrganizations(List<String> organIds, LocalDateTime eventTime,
                             IdentitySource identitySource) {
        List<IdentitySourceEventRecord> records = new ArrayList<>();
        List<EmisOrganVo> list = emisOrganService.getVosByForeignIds(organIds,
            identitySource.getId());
        if (list == null) {
            return;
        }
        list.forEach(organization -> {
            log.info("处理上游部门删除事件:[{}]开始", organization.getId());
            IdentitySourceEventRecord record = new IdentitySourceEventRecord();
            record.setObjectId(String.valueOf(organization.getId()));
            record.setIdsourceId(identitySource.getId());
            record.setObjectName(organization.getName());
            record.setObjectType(IdentitySourceObjectType.ORGANIZATION.getKey());
            record.setActionType(IdentitySourceActionType.DELETE.getKey());
            record.setEventTime(Date.from(eventTime.atZone(ZoneId.systemDefault()).toInstant()));
            record.setStatus(SyncStatus.SUCCESS.getKey());
            //额外信息
            record.setId(UUID.randomUUID().toString());
            record.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
            record.setCreateTime(new Date());
            records.add(record);
            log.info("处理上游部门删除事件:[{}]结束", organization.getId());
        });
        //批量删除
        emisOrganService.deleteAllById(
            list.stream().map(i -> String.valueOf(i.getId())).toArray(String[]::new));
        //保存事件记录
        identitySourceEventRecordService.batchSave(records);
    }

    private List<EmisOrganVo> processSubOrganizations(EmisOrganVo parent) {
        List<EmisOrganVo> list = new ArrayList<>();
        //递归组装子级数据
        List<EmisOrganVo> subOrganizations = emisOrganService.getVosByParentId(parent.getId());
        if (subOrganizations == null) {
            return list;
        }
        subOrganizations.forEach(sub -> {
            sub.setParentId(parent.getId());
            sub.setPath(parent.getPath() + PATH_SEPARATOR + sub.getId());
            sub.setDisplayPath(parent.getDisplayPath() + PATH_SEPARATOR + sub.getName());
            list.add(sub);
            list.addAll(processSubOrganizations(sub));
        });
        return list;
    }

    private static void addRecord(LocalDateTime eventTime, IdentitySource identitySource, String id,
                                  String name, List<IdentitySourceEventRecord> records,
                                  SyncStatus status, IdentitySourceObjectType objectType,
                                  IdentitySourceActionType actionType, String desc) {
        //记录日志
        IdentitySourceEventRecord record = new IdentitySourceEventRecord();
        record.setObjectId(id);
        record.setIdsourceId(identitySource.getId());
        record.setObjectName(name);
        record.setObjectType(objectType.getKey());
        record.setActionType(actionType.getKey());
        record.setEventTime(Date.from(eventTime.atZone(ZoneId.systemDefault()).toInstant()));
        record.setStatus(status.getKey());
        //额外信息
        record.setId(UUID.randomUUID().toString());
        record.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
        record.setCreateTime(new Date());
        record.setDescription(desc);
        records.add(record);
    }

    protected List<UserInfo> getMobiles(List<String> mobileList) {
        return userInfoService.findAllByMobileIn(mobileList.toArray(new String[0]));
    }

    protected List<UserInfo> getEmails(List<String> emailList) {
        return userInfoService.findAllByEmailIn(emailList.toArray(new String[0]));
    }

    protected List<UserInfo> getByUsernames(List<String> nameList) {
        return userInfoService.findAllByUsernameIn(nameList.toArray(new String[0]));
    }

    /**
     * UserService
     */
    private final UserInfoService                  userInfoService;

    /**
     * EmisOrganService
     */
    private final EmisOrganService                 emisOrganService;

    /**
     * UserOrganRelationService
     */
    private final UserOrganRelationService         userOrganRelationService;

    /**
     * IdentitySourceEventRecordService
     */
    private final IdentitySourceEventRecordService identitySourceEventRecordService;

    /**
     * UserExtService
     */
    private final UserExtService                   userExtService;

    /**
     * UserGroupRelationService
     */
    private final UserGroupRelationService         userGroupRelationService;

    public DefaultIdentitySourceEventPostProcessor(MailMsgEventPublish mailMsgEventPublish,
                                                   PasswordEncoder passwordEncoder,
                                                   PasswordGenerator passwordGenerator,
                                                   TransactionDefinition transactionDefinition,
                                                   PlatformTransactionManager platformTransactionManager,
                                                   IdentitySourceService identitySourceService,
                                                   IdentitySourceSyncHistoryService identitySourceSyncHistoryService,
                                                   IdentitySourceSyncRecordService identitySourceSyncRecordService,
                                                   UserInfoService userInfoService,
                                                   EmisOrganService emisOrganService,
                                                   UserOrganRelationService userOrganRelationService,
                                                   IdentitySourceEventRecordService identitySourceEventRecordService,
                                                   Storage storage, UserExtService userExtService,
                                                   UserGroupRelationService userGroupRelationService) {
        super(mailMsgEventPublish, passwordEncoder, passwordGenerator, transactionDefinition,
            platformTransactionManager, identitySourceService, identitySourceSyncHistoryService,
            identitySourceSyncRecordService, storage);
        this.userInfoService = userInfoService;
        this.emisOrganService = emisOrganService;
        this.userOrganRelationService = userOrganRelationService;
        this.identitySourceEventRecordService = identitySourceEventRecordService;
        this.userExtService = userExtService;
        this.userGroupRelationService = userGroupRelationService;
    }
}
