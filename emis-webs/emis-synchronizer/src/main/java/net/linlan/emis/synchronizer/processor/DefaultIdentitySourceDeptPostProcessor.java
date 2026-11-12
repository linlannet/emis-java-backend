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
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.RandomUtils;
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
import net.linlan.emis.manage.processor.IdentitySourceSyncDeptPostProcessor;
import net.linlan.emis.manage.vo.VerifyDept;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.organ.service.EmisOrganService;
import net.linlan.emis.support.security.password.PasswordGenerator;
import net.linlan.emis.support.userdetails.OrganizationType;
import static java.util.stream.Collectors.toList;

import static net.linlan.emis.support.constant.EmisConstants.*;
import static net.linlan.emis.user.constant.AccountConstants.ROOT_DEPT_NAME;

/**
 * 身份源数据 pull post 处理器
 *
 * @author Linlan
 * Created on 2022/3/1 22:04
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
@Component
public class DefaultIdentitySourceDeptPostProcessor extends AbstractIdentitySourcePostProcessor
                                                    implements IdentitySourceSyncDeptPostProcessor {
    private static final String PATH_SEPARATOR = "/";

    @Override
    public void process(String batch, String identitySourceId, List<VerifyDept> deptList,
                        Date startTime, TriggerType triggerType) {
        //保存同步历史
        IdentitySourceSyncHistory history = new IdentitySourceSyncHistory();
        history.setBatch(batch);
        history.setIdsourceId(identitySourceId);
        history.setStartTime(startTime);
        history.setObjectType(IdentitySourceObjectType.ORGANIZATION.getKey());
        history.setTriggerType(triggerType.getKey());
        history.setStatus(SyncStatus.PENDING.getKey());
        identitySourceSyncHistoryService.save(history);
        //手动开启事务
        TransactionStatus transactionStatus = platformTransactionManager
            .getTransaction(transactionDefinition);
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            log.info("处理组织机构数据开始");
            IdentitySource identitySource = getIdentitySource(identitySourceId);
            StrategyConfig strategyConfig = getStrategyConfig(identitySourceId);
            //过滤掉身份源根目录
            deptList = deptList.stream().filter(
                item -> !getRootForeignId(identitySource.getProvider()).equals(item.getDeptId()))
                .collect(toList());
            Statistics statistics = new Statistics();
            //处理组织父节点
            rootOrganizationHandler(identitySource, deptList, strategyConfig, statistics);
            //处理组织子节点
            childrenOrganizationHandler(identitySource, deptList, strategyConfig, statistics);
            stopWatch.stop();
            log.info("处理组织机构数据结束, 执行时长: {} ms", stopWatch.getTotalTimeMillis());
            //更新同步历史
            history.setUpdatedCount((long) statistics.getUpdateOrganizations().size());
            history.setCreatedCount((long) statistics.getCreateOrganizations().size());
            history.setSkippedCount((long) statistics.getSkipOrganizations().size());
            history.setDeletedCount((long) statistics.getDeleteOrganizations().size());
            history.setEndTime(new Date());
            history.setStatus(SyncStatus.SUCCESS.getKey());
            identitySourceSyncHistoryService.save(history);
            //保存同步记录
            saveSyncHistoryRecord(history.getId(), statistics);
            //提交事务
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.error("处理组织机构数据发生异常", e);
            platformTransactionManager.rollback(transactionStatus);
            //更新同步历史
            history.setEndTime(new Date());
            history.setStatus(SyncStatus.FAIL.getKey());
            identitySourceSyncHistoryService.save(history);
        }
    }

    /**
     * 处理根组织
     *
     * @param identitySource {@link IdentitySource}
     * @param deptList {@link Dept}
     * @param strategyConfig {@link StrategyConfig}
     * @param statistics {@link Statistics}
     */
    private void rootOrganizationHandler(IdentitySource identitySource, List<VerifyDept> deptList,
                                         StrategyConfig strategyConfig, Statistics statistics) {

        // 身份源没有选择现有组织
        if (Objects.isNull(strategyConfig.getOrganization())
            || Objects.isNull(strategyConfig.getOrganization().getTargetId())) {
            //创建根组织
            Long targetId = RandomUtils.randomLid();
            EmisOrganVo entity = createRootOrganization(targetId, identitySource,
                CollectionUtils.isEmpty(deptList));
            statistics.getCreateOrganizations().add(entity);
            //更新身份源根组织配置
            StrategyConfig.Organization organization = new StrategyConfig.Organization();
            organization.setTargetId(targetId);
            strategyConfig.setOrganization(organization);
            identitySourceService.updateStrategyConfig(identitySource.getId(), strategyConfig);
            return;
        }
        // 查询身份源配置的根组织，如果不存在，创建根组织
        Long targetId = strategyConfig.getOrganization().getTargetId();
        Optional<EmisOrganVo> optional = Optional.ofNullable(emisOrganService.getVoById(targetId));
        if (!optional.isPresent()) {
            EmisOrganVo entity = createRootOrganization(targetId, identitySource,
                CollectionUtils.isEmpty(deptList));
            statistics.getCreateOrganizations().add(entity);
            return;
        }
        //存在根组织，部分字段不一致进行更新操作
        EmisOrganVo entity = optional.get();
        //TODO 如果存在多个身份源选择一个父组织的时候，扩展ID是否更新呢？不更新就会造成用户关联问题
        if (BooleanUtils.compare(entity.getLeaf(), CollectionUtils.isEmpty(deptList)) != 0
            || !StringUtils.equals(entity.getForeignId(),
                getRootForeignId(identitySource.getProvider()))) {
            entity.setLeaf(CollectionUtils.isEmpty(deptList));
            entity.setForeignId(getRootForeignId(identitySource.getProvider()));
            statistics.getUpdateOrganizations().add(entity);
            emisOrganService.saveByVo(entity);
        }
    }

    /**
     * 处理子节点
     * 1、上游不存在，本地不存在-新增
     * 2、上游存在，本地存在-修改
     * 3、上游不存在，本地存在-删除
     *
     * @param identitySource {@link IdentitySource}
     * @param deptList {@link Dept}
     * @param strategyConfig {@link StrategyConfig}
     * @param statistics {@link Statistics}
     */
    private void childrenOrganizationHandler(IdentitySource identitySource,
                                             List<VerifyDept> deptList,
                                             StrategyConfig strategyConfig, Statistics statistics) {
        //@formatter:off
        Set<EmisOrganVo> createSet = Sets.newLinkedHashSet(), updateSet = Sets.newLinkedHashSet();
        Set<SkipOrganization> skipSet=Sets.newLinkedHashSet();
        //查询身份源下所有数据
        List<EmisOrganVo> list = emisOrganService.getVosByIdentitySourceId(identitySource.getId());
        if (list == null) {
            list = Lists.newArrayList();
        }
        Optional<EmisOrganVo> optional = Optional.ofNullable(
            emisOrganService.getVoById(strategyConfig.getOrganization().getTargetId()));
        if (!optional.isPresent()) {
            log.error("处理子组织数据失败，父组织不存在");
            throw new RuntimeException("处理子组织数据失败，父组织不存在");
        }
        //构建新关联关系
        Set<Modal> modals = buildModal(deptList, list);
        //构建更新&修改对象
        buildEntity(optional.get(), identitySource.getId(), modals, createSet, updateSet,skipSet);

        //保存
        if (!CollectionUtils.isEmpty(createSet)){
            createSet.forEach(emisOrganService::saveByVo);
            statistics.getCreateOrganizations().addAll(createSet);
        }
        //修改
        if (!CollectionUtils.isEmpty(updateSet)){
            updateSet.forEach(emisOrganService::saveByVo);
            statistics.getUpdateOrganizations().addAll(updateSet);
        }
        //所有当前部门三方ID列表
        //所有上游部门ID列表
        List<String> thirdPartyDeptIds = getDeptIds(deptList);
        //过滤要删除的部门
        Set<EmisOrganVo> deleteOrganizations = list.stream().filter(item -> !thirdPartyDeptIds.contains(item.getForeignId())).collect(Collectors.toSet());
        deleteOrganizations = deleteOrganizations.stream().filter(i-> !i.getForeignId().equals(getRootForeignId(identitySource.getProvider()))).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(deleteOrganizations)){
            emisOrganService.deleteAllById(deleteOrganizations.stream().map(i -> String.valueOf(i.getId())).toArray(String[]::new));
            statistics.getDeleteOrganizations().addAll(deleteOrganizations);
        }
        //跳过
        statistics.getSkipOrganizations().addAll(skipSet);
        //@formatter:on
    }

    private List<String> getDeptIds(List<VerifyDept> deptList) {
        List<String> ids = deptList.stream().map(VerifyDept::getDeptId).distinct()
            .collect(toList());
        deptList.forEach(item -> {
            if (!CollectionUtils.isEmpty(item.getChildren())) {
                List<String> deptIds = getDeptIds(item.getChildren());
                ids.addAll(deptIds);
            }
        });
        return ids;
    }

    /**
     * 根据新模型，构建新增，修改数据集
     *
     * @param modals {@link Modal} 新模型数据
     *
     */
    private void buildEntity(EmisOrganVo parent, String identitySourceId, Set<Modal> modals,
                             Set<EmisOrganVo> createSet, Set<EmisOrganVo> updateSet,
                             Set<SkipOrganization> skipSet) {
        for (Modal modal : modals) {
            VerifyDept thirdParty = modal.getThirdParty();
            Set<Modal> children = modal.getChildren();
            IdentitySource identitySource = getIdentitySource(identitySourceId);
            //构建新增数据
            if (modal.isNew()) {
                EmisOrganVo entity = new EmisOrganVo();
                entity.setId(RandomUtils.randomLid());
                entity.setName(thirdParty.getName());
                entity.setCode(RandomStringUtils.randomAlphanumeric(7));
                entity.setType(OrganizationType.DEPARTMENT);
                entity.setParentId(parent.getId());
                entity.setPath(parent.getPath() + PATH_SEPARATOR + entity.getId());
                entity.setDisplayPath(parent.getDisplayPath() + PATH_SEPARATOR + entity.getName());
                entity.setForeignId(thirdParty.getDeptId());
                entity.setDataOrigin(getDataOrigin(identitySource.getProvider()).getType());
                entity.setIdentitySourceId(identitySourceId);
                entity.setLeaf(CollectionUtils.isEmpty(children));
                entity.setEnabled(Boolean.TRUE);
                entity.setPriority(
                    thirdParty.getPriority() == null ? null : thirdParty.getPriority().intValue());
                entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
                entity.setCreateTime(new Date());
                createSet.add(entity);
                //处理子节点
                if (!CollectionUtils.isEmpty(children)) {
                    buildEntity(entity, identitySourceId, children, createSet, updateSet, skipSet);
                }
            }
            //构建修改数据
            else {
                EmisOrganVo current = modal.getCurrent();
                //是否需要修改
                if (!equalsOrganization(modal, parent)) {
                    current.setName(thirdParty.getName());
                    current.setPriority(thirdParty.getPriority() == null ? null
                        : thirdParty.getPriority().intValue());
                    current.setParentId(parent.getId());
                    current.setPath(parent.getPath() + PATH_SEPARATOR + current.getId());
                    current.setDisplayPath(
                        parent.getDisplayPath() + PATH_SEPARATOR + thirdParty.getName());
                    current.setForeignId(thirdParty.getDeptId());
                    current.setLeaf(CollectionUtils.isEmpty(children));
                    updateSet.add(current);
                } else {
                    skipSet.add(SkipOrganization.builder().organization(current)
                        .actionType(IdentitySourceActionType.UPDATE).reason("组织信息一致").build());
                }
                //处理子节点
                if (!CollectionUtils.isEmpty(children)) {
                    buildEntity(current, identitySourceId, children, createSet, updateSet, skipSet);
                }
            }
        }
    }

    /**
     * 是否需要更新组织
     *
     * @param modal {@link EmisOrganVo} 模型
     * @param parent {@link EmisOrganVo} 父组织
     * @return {@link Boolean} 是否需要修改
     */
    private Boolean equalsOrganization(Modal modal, EmisOrganVo parent) {
        VerifyDept thirdParty = modal.getThirdParty();
        EmisOrganVo current = modal.getCurrent();
        //@formatter:off
        return
                //名称
                StringUtils.equals(thirdParty.getName(),current.getName()) &&
                //上级部门ID
                Objects.equals(parent.getId(),current.getParentId()) &&
                //部门PATH
                StringUtils.equals(parent.getPath() + PATH_SEPARATOR + current.getId(),current.getPath()) &&
                //部门显示PATH
                StringUtils.equals(parent.getDisplayPath() + PATH_SEPARATOR + thirdParty.getName(),current.getDisplayPath()) &&
                //是否叶子节点
                Objects.equals(CollectionUtils.isEmpty(modal.getChildren()),current.getLeaf()) &&
                //次序
                Objects.equals(thirdParty.getPriority(),current.getPriority());
        //@formatter:on
    }

    /**
     * 根据同步数据构建模型
     *
     * @param deptList {@link Dept} 同步来源数据
     * @param list {@link EmisOrganVo} 数据库现有数据
     */
    private Set<Modal> buildModal(List<VerifyDept> deptList, List<EmisOrganVo> list) {
        if (Objects.isNull(deptList)) {
            return Collections.emptySet();
        }
        Set<Modal> modals = Sets.newLinkedHashSet();
        //KEY：外部ID，VALUE: 部门实体
        Map<String, EmisOrganVo> map = new HashMap<>(16);
        list.forEach(dept -> map.put(dept.getForeignId(), dept));
        for (VerifyDept dept : deptList) {
            if (map.containsKey(dept.getDeptId())) {
                Modal modal = new Modal();
                modal.setCurrent(map.get(dept.getDeptId()));
                modal.setThirdParty(dept);
                modal.setChildren(buildModal(dept.getChildren(), list));
                modals.add(modal);
                continue;
            }
            Modal modal = new Modal();
            modal.setThirdParty(dept);
            modal.setChildren(buildModal(dept.getChildren(), list));
            modals.add(modal);
        }
        return modals;
    }

    /**
     * 创建根组织
     *
     * @param identitySource {@link String} 身份提供商
     * @param isLeaf {@link Boolean} 是否叶子节点
     */
    private EmisOrganVo createRootOrganization(Long id, IdentitySource identitySource,
                                               Boolean isLeaf) {
        //@formatter:off
        //添加节点
        EmisOrganVo entity = new EmisOrganVo();
        entity.setId(id);
        entity.setCode(RandomStringUtils.randomAlphanumeric(7));
        entity.setName(identitySource.getName());
        entity.setParentId(0L);
        entity.setLeaf(isLeaf);
        entity.setForeignId(getRootForeignId(identitySource.getProvider()));
        entity.setDataOrigin(getDataOrigin(identitySource.getProvider()).getType());
        entity.setType(OrganizationType.DEPARTMENT);
        entity.setEnabled(Boolean.TRUE);
        entity.setPath(PATH_SEPARATOR + ROOT_DEPT_NAME + PATH_SEPARATOR + id);
        entity.setDisplayPath(PATH_SEPARATOR + ROOT_DEPT_NAME + PATH_SEPARATOR + identitySource.getName());
        entity.setIdentitySourceId(identitySource.getId());
        entity.setCreatorId(SYSTEM_DEFAULT_USER_NAME);
        entity.setCreateTime(new Date());
        emisOrganService.saveByVo(entity);
        //@formatter:on
        return emisOrganService.getVoById(id);
    }

    /**
     * 新增同步记录
     *
     * @param historyId  {@link String}
     * @param statistics {@link Statistics}
     */
    private void saveSyncHistoryRecord(String historyId, Statistics statistics) {
        //创建组织
        saveCreateOrganizationsRecord(historyId, statistics.getCreateOrganizations());
        //更新组织记录
        saveUpdateOrganizationsRecord(historyId, statistics.getUpdateOrganizations());
        //删除组织记录
        saveDeleteOrganizationsRecord(historyId, statistics.getDeleteOrganizations());
        //跳过组织记录
        saveSkipOrganizationsRecord(historyId, statistics.getSkipOrganizations());
    }

    /**
     * 保存创建组织记录
     *
     * @param historyId {@link String}
     * @param createOrganizations {@link Set}
     */
    private void saveCreateOrganizationsRecord(String historyId,
                                               Set<EmisOrganVo> createOrganizations) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        createOrganizations.forEach(user -> {
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(SyncStatus.SUCCESS.getKey());
            entity.setObjectType(IdentitySourceObjectType.ORGANIZATION.getKey());
            entity.setObjectId(String.valueOf(user.getId()));
            entity.setObjectName(user.getName());
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
     * 保存更新组织记录
     *
     * @param historyId {@link String}
     * @param updateOrganizations {@link Set}
     */
    private void saveUpdateOrganizationsRecord(String historyId,
                                               Set<EmisOrganVo> updateOrganizations) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        updateOrganizations.forEach(user -> {
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(SyncStatus.SUCCESS.getKey());
            entity.setObjectType(IdentitySourceObjectType.ORGANIZATION.getKey());
            entity.setObjectId(String.valueOf(user.getId()));
            entity.setObjectName(user.getName());
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
     * 保存删除组织记录
     *
     * @param historyId {@link String}
     * @param deleteOrganizations {@link Set}
     */
    private void saveDeleteOrganizationsRecord(String historyId,
                                               Set<EmisOrganVo> deleteOrganizations) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        deleteOrganizations.forEach(user -> {
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(SyncStatus.SUCCESS.getKey());
            entity.setObjectType(IdentitySourceObjectType.ORGANIZATION.getKey());
            entity.setObjectId(String.valueOf(user.getId()));
            entity.setObjectName(user.getName());
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
     * 保存跳过组织记录
     *
     * @param historyId {@link String}
     * @param skipOrganizations {@link Set}
     */
    private void saveSkipOrganizationsRecord(String historyId,
                                             Set<SkipOrganization> skipOrganizations) {
        List<IdentitySourceSyncRecord> list = Lists.newArrayList();
        skipOrganizations.forEach(skipUser -> {
            EmisOrganVo organization = skipUser.getOrganization();
            IdentitySourceSyncRecord entity = new IdentitySourceSyncRecord();
            entity.setId(UUID.randomUUID().toString());
            entity.setSyncHistoryId(historyId);
            entity.setStatus(SyncStatus.SKIP.getKey());
            entity.setObjectType(IdentitySourceObjectType.ORGANIZATION.getKey());
            entity.setObjectId(String.valueOf(organization.getId()));
            entity.setObjectName(organization.getName());
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

    @Data
    public static class Modal implements Serializable {
        /**
         * 主数据 (如果为空，代表新数据)
         */
        private EmisOrganVo current;
        /**
         * 第三方数据
         */
        private VerifyDept  thirdParty;
        /**
         * 子集
         */
        private Set<Modal>  children;

        public Boolean isNew() {
            return Objects.isNull(current);
        }
    }

    @Data
    public static class Statistics implements Serializable {

        //@Serial
        private static final long     serialVersionUID = 7025078516116020630L;

        /**
         * 创建
         */
        private Set<EmisOrganVo>      createOrganizations;

        /**
         * 修改
         */
        private Set<EmisOrganVo>      updateOrganizations;

        /**
         * 删除
         */
        private Set<EmisOrganVo>      deleteOrganizations;

        /**
         * 跳过
         */
        private Set<SkipOrganization> skipOrganizations;

        public Statistics() {
            this.setUpdateOrganizations(Sets.newLinkedHashSet());
            this.setSkipOrganizations(Sets.newLinkedHashSet());
            this.setCreateOrganizations(Sets.newLinkedHashSet());
            this.setDeleteOrganizations(Sets.newLinkedHashSet());
        }
    }

    @Data
    @Builder
    private static class SkipOrganization {

        private EmisOrganVo              organization;
        private IdentitySourceActionType actionType;
        private String                   reason;
    }

    /**
     * EmisOrganService
     */
    private final EmisOrganService emisOrganService;

    public DefaultIdentitySourceDeptPostProcessor(MailMsgEventPublish mailMsgEventPublish,
                                                  PasswordEncoder passwordEncoder,
                                                  PasswordGenerator passwordGenerator,
                                                  TransactionDefinition transactionDefinition,
                                                  PlatformTransactionManager platformTransactionManager,
                                                  IdentitySourceService identitySourceService,
                                                  EmisOrganService emisOrganService,
                                                  IdentitySourceSyncHistoryService identitySourceSyncHistoryService,
                                                  IdentitySourceSyncRecordService identitySourceSyncRecordService,
                                                  Storage storage) {
        super(mailMsgEventPublish, passwordEncoder, passwordGenerator, transactionDefinition,
            platformTransactionManager, identitySourceService, identitySourceSyncHistoryService,
            identitySourceSyncRecordService, storage);
        this.emisOrganService = emisOrganService;
    }
}
