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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
 * <br>
 * @author Linlan
 * CreateTime 2025-09-22 00:29:01<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Service
public class EmisOrganService {

    @Resource
    private EmisOrganDao     dao;
    @Resource
    private AdminDeptService adminDeptService;

    /**
     * 根据名称查询数量
     *
     * @param name {@link String}
     * @return {@link Long}
     */
    public int countByName(String name) {
        return adminDeptService.getCount(new StringMap().put("name", name).map());
    }

    /**
     * 根据名称查询组织机构
     *
     * @param name {@link String}
     * @return {@link EmisOrganVo}
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
     * @return {@link EmisOrganVo}
     */
    public List<AdminDeptDto> findByParentId(Long parentId) {
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
     * @return {@link EmisOrganVo}
     */
    public List<EmisOrganVo> findByParentIdOrderByPriorityAsc(Long parentId) {
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
     * findById
     *
     * @param id {@link Long}
     * @return {@link Optional}
     */
    public EmisOrganVo findById(Long id) {
        AdminDeptDto dto = adminDeptService.getDtoById(id);
        return (EmisOrganVo) EmisOrganVo.DTO.apply(dto);
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
            List<AdminDeptDto> oldChildList = findByParentId(oldParentId);
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
            List<AdminDeptDto> childList = findByParentId(id);
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
     * @param id     {@link String}
     * @param isLeaf {@link Boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    void updateIsLeaf(@Param(value = "id") Long id, @Param(value = "isLeaf") Boolean isLeaf) {
        AdminDept dept = adminDeptService.findById(id);
        if (ObjectUtils.isNotEmpty(dept)) {
            dept.setIsLeaf(isLeaf);
            adminDeptService.update(dept);
        }
    }

    /**
     * 更新启用/禁用
     *
     * @param id     {@link Serializable}
     * @param status {@link Boolean}
     * @return {@link  Integer}
     */
    Integer updateStatus(@Param(value = "id") Long id, @Param(value = "status") Boolean status) {
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
     * @return {@link EmisOrganVo}
     */
    //    @Query(value = "FROM EmisOrganVo WHERE name LIKE :keyWord OR code LIKE :keyWord")
    List<EmisOrganVo> findByNameLikeOrCodeLike(@Param(value = "keyWord") String keyWord) {
        return null;
    }

    /**
     * 查询指定id返回组织机构
     *
     * @param id {@link Collection}
     * @return {@link EmisOrganVo}
     */
    List<EmisOrganVo> findByIdInOrderByCreateTimeDesc(Collection<String> id) {
        return null;
    }

    /**
     * 根据外部用id查询组织
     *
     * @param identitySourceId         {@link String}
     * @param deptIdList         {@link List}
     * @return {@link EmisOrganVo}
     */
    List<EmisOrganVo> findByIdentitySourceIdAndForeignIdIn(String identitySourceId,
                                                           List<String> deptIdList) {
        return null;
    }

    /**
     * 根据外部用id查询组织
     *
     * @param identitySourceId         {@link String}
     * @param organIdList         {@link List}
     * @return {@link EmisOrganVo}
     */
    List<EmisOrganVo> findByIdentitySourceIdAndIdIn(String identitySourceId,
                                                    List<String> organIdList) {
        return null;
    }

    /**
     * 根据外部用id查询组织
     *
     * @param deptId         {@link String}
     * @return {@link EmisOrganVo}
     */
    EmisOrganVo findByForeignId(String deptId) {
        return null;
    }

    /**
     * 根据外部用id查询组织
     *
     * @param foreignId       {@link String}
     * @param identitySourceId {@link String}
     * @return {@link EmisOrganVo}
     */
    EmisOrganVo findByForeignIdAndIdentitySourceId(String foreignId, String identitySourceId) {
        return null;
    }

    /**
     * 根据外部用id查询组织
     *
     * @param foreignId       {@link String}
     * @param identitySourceId {@link String}
     * @return {@link List}
     */
    List<EmisOrganVo> findByForeignIdInAndIdentitySourceId(List<String> foreignId,
                                                           String identitySourceId) {
        return null;
    }

    /**
     * 查询子组织
     *
     * @param parentId         {@link String}
     * @param dataOrigin       {@link String}
     * @param identitySourceId {@link String}
     * @return {@link EmisOrganVo}
     */
    List<EmisOrganVo> findByParentIdAndDataOriginAndIdentitySourceId(String parentId,
                                                                     String dataOrigin,
                                                                     String identitySourceId) {
        return null;
    }

    /**
     * 根据身份源ID获取所有数据
     *
     * @param identitySourceId {@link String}
     * @return  {@link List}
     */
    List<EmisOrganVo> findByIdentitySourceId(String identitySourceId) {
        return null;
    }

    /**
     *  通过parentId查询
     *
     * @param parentIds {@link List}
     * @return {@link List}
     */
    List<EmisOrganVo> findByIdInOrderByPriorityAsc(Collection<String> parentIds) {
        return null;
    }

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param entity    {@link EmisOrganVo}
     * @return {@link EmisOrganVo}
     */
    public EmisOrganVo save(EmisOrganVo entity) {
        return null;
    }

    /**
     * delete all by id
     *
     * @param ids {@link Iterable}
     */
    public void deleteAllById(String[] ids) {

    }

    /**
     * 查询组织成员数量或id
     *
     * @param organId {@link  String}
     * @return {@link  List}
     */
    //    @Query(value = "SELECT user.id FROM UserInfo user INNER JOIN UserOrganRelation om ON user.id = om.userId INNER JOIN EmisOrganVo organization ON organization.id = om.organId WHERE organization.id = :organId OR LOCATE(:organId, organization.path) > 0 ")
    public List<String> getOrgMemberList(@Param("organId") String organId) {
        return null;
    }

    public List<OrganDeptDto> getListByUserId(String id) {
        return null;
    }
}
