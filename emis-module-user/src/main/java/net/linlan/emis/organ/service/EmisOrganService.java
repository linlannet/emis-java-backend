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
package net.linlan.emis.organ.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.organ.dao.EmisOrganDao;
import net.linlan.emis.organ.dto.OrganDeptDto;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.frame.admin.dto.AdminDeptDto;
import net.linlan.frame.admin.entity.AdminDept;
import net.linlan.frame.admin.param.AdminDeptParam;
import net.linlan.frame.admin.service.AdminDeptService;

/**
 *
 * UserOrganRelation数据域:用户机构关系服务类
 * @author Linlan
 *
 * CreateTime 2025-09-22 00:29:01
 *
 */
@Service
public class EmisOrganService {

    @Resource
    private EmisOrganDao     dao;
    @Resource
    private AdminDeptService adminDeptService;

    /**
     * findById
     *
     * @param id {@link Long}
     * @return {@link EmisOrganVo}
     */
    public EmisOrganVo getVoById(Long id) {
        if (id == null) {
            return null;
        }
        AdminDeptDto dto = adminDeptService.getDtoById(id);
        if (dto == null) {
            return null;
        }
        return (EmisOrganVo) EmisOrganVo.DTO.apply(dto);
    }

    /**
     * findById
     *
     * @param organId {@link String}
     * @return {@link EmisOrganVo}
     */
    public EmisOrganVo getVoByOrganId(String organId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setOrganId(organId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (EmisOrganVo) EmisOrganVo.DTO.apply(list.get(0));
    }

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param entity    {@link EmisOrganVo}
     * @return 机构Vo对象
     */
    @Transactional(rollbackFor = Exception.class)
    public EmisOrganVo saveByVo(EmisOrganVo entity) {
        if (entity == null) {
            return null;
        }
        AdminDept dept = adminDeptService.findById(entity.getId());
        boolean isNew = dept == null;
        if (isNew) {
            dept = new AdminDept();
            dept.setId(entity.getId());
        }
        dept.setOrganId(entity.getOrganId());
        dept.setParentId(entity.getParentId());
        dept.setAppId(entity.getAppId());
        dept.setForeignId(entity.getForeignId());
        dept.setSpare1(entity.getIdentitySourceId());
        if (StringUtils.isNotBlank(entity.getDataOrigin())
            && org.apache.commons.lang3.StringUtils.isNumeric(entity.getDataOrigin())) {
            dept.setCreateType(Integer.valueOf(entity.getDataOrigin()));
        }
        if (entity.getType() != null) {
            dept.setType(entity.getType().getType());
        }
        dept.setName(entity.getDisplayPath());
        dept.setSname(entity.getName());
        dept.setIsLeaf(entity.getLeaf());
        dept.setIsDisabled(!Boolean.TRUE.equals(entity.getEnabled()));
        dept.setSearchCode(entity.getPath());
        dept.setImagePath(entity.getImagePath());
        dept.setPriority(entity.getPriority());
        dept.setDescription(entity.getRemark());
        if (isNew) {
            dept.setCreatorId(entity.getCreatorId());
            dept.setCreateTime(entity.getCreateTime());
            adminDeptService.save(dept);
        } else {
            adminDeptService.update(dept);
        }
        entity.setId(dept.getId());
        return entity;
    }

    /** batch save the entity with input list
     * 保存对象应用协议，批量保存list
     * @param list {@link AdminDept}
     */
    public void batchSave(List<AdminDept> list) {
        dao.batchSave(list);
    }

    /**
     * 根据名称查询数量
     *
     * @param name {@link String}
     * @return    结果
     */
    public int countByName(String name) {
        return adminDeptService.getCount(new StringMap().put("name", name).map());
    }

    /**
     * 根据名称查询组织机构
     *
     * @param name {@link String}
     * @return 机构Vo对象
     */
    public EmisOrganVo getByName(String name) {
        AdminDeptDto dto = adminDeptService.getByName(name);
        //                List<EmisOrganVo> vos = Lists.transform(list, EmisOrganVo.DTO);

        return (EmisOrganVo) EmisOrganVo.DTO.apply(dto);
    }

    /**
     * 查询子组织
     *
     * @param parentId {@link String}
     * @return 机构Vo对象
     */
    public List<AdminDeptDto> getByParentId(Long parentId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setParentId(parentId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * 查询子组织根据sort排序
     *
     * @param parentId {@link String}
     * @return 机构Vo对象
     */
    public List<EmisOrganVo> getVosByParentId(Long parentId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setParentId(parentId);
        param.setOrderBy(5);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (list != null && list.size() > 0) {
            List<EmisOrganVo> vos = Lists.transform(list, EmisOrganVo.DTO);
            return vos;
        }
        return null;
    }

    /**
     * deleteById
     *
     * @param id {@link Long}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        adminDeptService.deleteById(id);
    }

    /**
     * 移动组织机构
     *
     * @param id       {@link String}
     * @param parentId {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public void moveOrganization(@Param(value = "id") Long id,
                                 @Param(value = "parentId") Long parentId) {
        if (id.equals(parentId)) {
            throw new RuntimeException("当前机构与所选机构相同，不可移动");
        }
        AdminDeptDto entity = adminDeptService.getDtoById(id);
        if (ObjectUtils.isNotEmpty(entity)) {
            Long oldParentId = entity.getParentId();
            AdminDeptDto newParent = adminDeptService.getDtoById(parentId);
            if (ObjectUtils.isNotEmpty(newParent)) {
                if (newParent.getIsLeaf()) {
                    newParent.setIsLeaf(false);
                    adminDeptService.save(newParent);
                    Target.builder().id(parentId.toString()).name(newParent.getName())
                        .type(TargetType.ORGANIZATION).build();
                }
                entity.setParentId(parentId);
                //父级路径
                entity
                    .setSearchCode(newParent.getSearchCode() + StringUtils.SLASH + entity.getId());
                //父级展示路径
                entity.setName(newParent.getName() + StringUtils.SLASH + entity.getName());
            }
            adminDeptService.save(entity);
            // 判断旧的父节点下是否还存在子节点，不存在更改此节点为叶子节点
            List<AdminDeptDto> oldChildList = getByParentId(oldParentId);
            if (CollectionUtils.isEmpty(oldChildList)) {
                AdminDept oldParentOrganization = adminDeptService.findById(oldParentId);
                if (ObjectUtils.isNotEmpty(oldParentOrganization)) {
                    oldParentOrganization.setIsLeaf(true);
                    adminDeptService.save(oldParentOrganization);
                }
            }
            AuditContext.setTarget(Target.builder().id(entity.toString()).name(entity.getName())
                .type(TargetType.ORGANIZATION).build());
            //存在子组织，递归更改子组织 path 和 displayPath
            recursiveUpdateChildNodePathAndDisplayPath(entity.getId());
        }

    }

    /**
     * 递归修改子节点 path 和 displayPath
     *
     * @param id {@link  Long } 当前节点ID
     */
    private void recursiveUpdateChildNodePathAndDisplayPath(Long id) {
        AdminDept parent = adminDeptService.findById(id);
        if (ObjectUtils.isNotEmpty(parent)) {
            List<AdminDeptDto> childList = getByParentId(id);
            for (AdminDeptDto child : childList) {
                child.setSearchCode(parent.getSearchCode() + StringUtils.SLASH + child.getId());
                child.setName(parent.getName() + StringUtils.SLASH + child.getName());
                adminDeptService.save(child);
                //存在下级节点
                if (!child.getIsLeaf()) {
                    //递归处理
                    recursiveUpdateChildNodePathAndDisplayPath(child.getId());
                }
            }
        }
    }

    /**
     * 更新叶子接点
     *
     * @param id     主键ID
     * @param isLeaf {@link Boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateIsLeaf(@Param(value = "id") Long id,
                             @Param(value = "isLeaf") Boolean isLeaf) {
        AdminDept dept = adminDeptService.findById(id);
        if (ObjectUtils.isNotEmpty(dept)) {
            dept.setIsLeaf(isLeaf);
            adminDeptService.update(dept);
        }
    }

    /**
     * 更新启用/禁用
     *
     * @param id   主键ID
     * @param status {@link Boolean}
     * @return 操作结果
     */
    public Integer updateStatus(@Param(value = "id") Long id,
                                @Param(value = "status") Boolean status) {
        AdminDept dept = adminDeptService.findById(id);
        if (ObjectUtils.isNotEmpty(dept)) {
            dept.setIsDisabled(!status);
            adminDeptService.update(dept);
        }
        return 0;
    }

    /**
     * 根据名称或编码查询组织机构
     *
     * @param keyWord {@link String}
     * @return 机构Vo对象
     */
    public List<EmisOrganVo> getByNameCode(@Param(value = "keyWord") String keyWord) {
        return null;
    }

    /**
     * 查询指定id返回组织机构
     *
     * @param ids {@link Collection}
     * @return 机构Vo对象
     */
    public List<EmisOrganVo> getVosByIds(String ids) {
        return null;
    }

    /**
     * 根据外部用id查询组织
     *
     * @param identitySourceId         {@link String}
     * @param deptIds         {@link List}
     * @return 机构Vo对象
     */
    public List<EmisOrganVo> getVosByDeptIds(String identitySourceId, String[] deptIds) {
        return getVosByForeignIds(java.util.Arrays.asList(deptIds), identitySourceId);
    }

    /**
     * 根据外部用id查询组织
     *
     * @param identitySourceId         {@link String}
     * @param organIds         {@link List}
     * @return 机构Vo对象
     */
    public List<EmisOrganVo> getVosByOrganIds(String identitySourceId, String[] organIds) {
        AdminDeptParam param = new AdminDeptParam();
        param.setSpare1(identitySourceId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (CollectionUtils.isEmpty(list) || organIds == null) {
            return Lists.newArrayList();
        }
        List<Long> ids = java.util.Arrays.stream(organIds).map(Long::valueOf)
            .collect(java.util.stream.Collectors.toList());
        return list.stream().filter(dto -> ids.contains(dto.getId()))
            .map(dto -> (EmisOrganVo) EmisOrganVo.DTO.apply(dto))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 根据外部用id查询组织
     *
     * @param deptId         部门ID
     * @return 机构Vo对象
     */
    public EmisOrganVo findByForeignId(String deptId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setForeignId(deptId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (EmisOrganVo) EmisOrganVo.DTO.apply(list.get(0));
    }

    /**
     * 根据外部用id查询组织
     *
     * @param foreignId       外键ID
     * @param identitySourceId 身份源ID
     * @return 机构Vo对象
     */
    public EmisOrganVo getVoByParams(String foreignId, String identitySourceId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setForeignId(foreignId);
        param.setSpare1(identitySourceId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return (EmisOrganVo) EmisOrganVo.DTO.apply(list.get(0));
    }

    /**
     * 根据外部用id查询组织
     *
     * @param foreignIds       外键IDs
     * @param identitySourceId 身份源ID
     * @return    结果列表
     */
    public List<EmisOrganVo> getVosByForeignIds(List<String> foreignIds, String identitySourceId) {
        if (CollectionUtils.isEmpty(foreignIds)) {
            return Lists.newArrayList();
        }
        AdminDeptParam param = new AdminDeptParam();
        param.setSpare1(identitySourceId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return list.stream().filter(dto -> foreignIds.contains(dto.getForeignId()))
            .map(dto -> (EmisOrganVo) EmisOrganVo.DTO.apply(dto))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 查询子组织
     *
     * @param parentId         父节点ID
     * @param dataOrigin       数据来源
     * @param identitySourceId 身份源ID
     * @return 机构Vo对象
     */
    public List<EmisOrganVo> getVosByParams(String parentId, String dataOrigin,
                                            String identitySourceId) {
        return null;
    }

    /**
     * 根据身份源ID获取所有数据
     *
     * @param identitySourceId 身份源ID
     * @return  结果列表
     */
    public List<EmisOrganVo> getVosByIdentitySourceId(String identitySourceId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setSpare1(identitySourceId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        return list.stream().map(dto -> (EmisOrganVo) EmisOrganVo.DTO.apply(dto))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     *  通过parentId查询
     *
     * @param parentIds 父节点IDs
     * @return    结果列表
     */
    public List<EmisOrganVo> getVosByParentIds(String[] parentIds) {
        List<EmisOrganVo> list = Lists.newArrayList();
        if (parentIds == null) {
            return list;
        }
        for (String parentId : parentIds) {
            List<EmisOrganVo> vos = getVosByParentId(Long.valueOf(parentId));
            if (vos != null) {
                list.addAll(vos);
            }
        }
        return list;
    }

    /**
     * delete all by id
     *
     * @param ids {@link Iterable}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllById(String[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        adminDeptService
            .deleteByIds(java.util.Arrays.stream(ids).map(Long::valueOf).toArray(Long[]::new));
    }

    /**
     * 查询组织成员数量或id
     *
     * @param organId 机构ID
     * @return 机构用户IDs
     */
    public List<String> getOrganUserIds(@Param("organId") String organId) {
        return null;
    }

    public List<OrganDeptDto> getOrganDeptByUserId(String id) {
        return null;
    }
}
