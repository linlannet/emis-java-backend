/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.openapi.constant.OpenApiStatus;
import net.linlan.emis.openapi.converter.OrganizationConverter;
import net.linlan.emis.openapi.exception.OpenApiException;
import net.linlan.emis.openapi.pojo.result.OrganizationChildResult;
import net.linlan.emis.openapi.pojo.result.OrganizationResult;
import net.linlan.emis.openapi.pojo.save.OrganizationCreateParam;
import net.linlan.emis.openapi.pojo.update.OrganizationUpdateParam;
import net.linlan.emis.openapi.service.OrganizationService;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.organ.service.UserOrganRelationService;
import net.linlan.emis.support.repository.base.BaseEntity;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.frame.admin.dto.AdminDeptDto;
import net.linlan.frame.admin.entity.AdminDept;
import net.linlan.frame.admin.param.AdminDeptParam;
import net.linlan.frame.admin.service.AdminDeptService;

/**
 * <p>
 * 组织机构 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on 2020-08-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    public static final String SEPARATE = "/";

    /**
     * 创建组织机构
     *
     * @param param {@link OrganizationCreateParam}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrgan(OrganizationCreateParam param) {
        //保存
        AdminDept entity = organizationConverter.orgCreateParamConvertToEntity(param);
        //查询父节点
        AdminDept parent = null;
        if (entity.getParentId() != null) {
            parent = adminDeptService.findById(entity.getParentId());
        }
        adminDeptService.save(entity);
        //展示路径枚举
        if (parent != null) {
            entity.setName(StringUtils.isEmpty(parent.getName()) ? SEPARATE + entity.getSname()
                : parent.getName() + SEPARATE + entity.getSname());
            //设置路径枚举
            entity.setSearchCode(
                StringUtils.isEmpty(parent.getSearchCode()) ? SEPARATE + entity.getId()
                    : parent.getSearchCode() + SEPARATE + entity.getId());
            adminDeptService.update(entity);
            //存在父节点，更改为非叶子节点
            if (Boolean.TRUE.equals(parent.getIsLeaf())) {
                parent.setIsLeaf(Boolean.FALSE);
                adminDeptService.update(parent);
            }
        } else {
            entity.setName(SEPARATE + entity.getSname());
            entity.setSearchCode(SEPARATE + entity.getId());
            adminDeptService.update(entity);
        }
        AuditContext.setTarget(Target.builder().id(String.valueOf(entity.getId()))
            .name(entity.getSname()).type(TargetType.ORGANIZATION).build());
    }

    /**
     * 修改组织机构
     *
     * @param param {@link OrganizationCreateParam}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgan(OrganizationUpdateParam param) {
        Long id = parseLong(param.getId());
        AdminDept entity = id == null ? null : adminDeptService.findById(id);
        if (entity == null) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_NOT_EXIST);
        }
        AdminDept organization = organizationConverter.orgUpdateParamConvertToEntity(param);
        //如果修改了名字，递归修改和该组织有关所有节点信息的展示路径
        if (!StringUtils.equals(entity.getSname(), param.getName())) {
            //修改名称
            organization.setName(getNewDisplayPath(String.valueOf(entity.getId()), param.getName(),
                entity.getSearchCode(), entity.getName()));
            //递归更改下级名称
            if (!Boolean.TRUE.equals(entity.getIsLeaf())) {
                recursiveUpdateDisplayPath(String.valueOf(entity.getId()),
                    String.valueOf(entity.getId()), param.getName());
            }
        }
        //修改
        BeanUtils.merge(organization, entity, BaseEntity.LAST_MODIFIED_BY,
            BaseEntity.LAST_MODIFIED_TIME);
        adminDeptService.update(entity);
        AuditContext.setTarget(Target.builder().id(String.valueOf(entity.getId()))
            .name(entity.getSname()).type(TargetType.ORGANIZATION).build());
    }

    /**
     * 递归修改显示名称
     *
     * @param parentId {@link  String} 上级ID
     * @param id       {@link  String} ID 要更改名称的节点ID
     * @param name     {@link  String} 名称
     */
    protected void recursiveUpdateDisplayPath(String parentId, String id, String name) {
        List<EmisOrganVo> childNodes = getChildVos(parseLong(parentId));
        for (EmisOrganVo org : childNodes) {
            AdminDept child = adminDeptService.findById(org.getId());
            if (child == null) {
                continue;
            }
            child.setName(getNewDisplayPath(id, name, child.getSearchCode(), child.getName()));
            adminDeptService.update(child);
            //存在下级节点
            if (!Boolean.TRUE.equals(child.getIsLeaf())) {
                //递归处理
                recursiveUpdateDisplayPath(String.valueOf(child.getId()), id, name);
            }
        }
    }

    /**
     * 获取新的显示路径
     *
     * @param id          {@link  String} 要更改的ID
     * @param name        {@link  String} 要更改的新名称
     * @param path        {@link  String} 路径
     * @param displayPath {@link  String} 显示路径
     * @return {@link  String} 新显示名称
     */
    private String getNewDisplayPath(String id, String name, String path, String displayPath) {
        // 修改名称有个问题，如果名称一致，使用替换就会出问题，所以使用KEY=VALUE更改
        String[] pathIds = path.split(SEPARATE);
        String[] pathNames = displayPath.split(SEPARATE);
        //路径数据
        Map<String, String> pathData = new LinkedHashMap<>();
        if (pathIds.length == pathNames.length) {
            //i=1起步，以为切割数组，第0位为""
            for (int i = 1; i < pathNames.length; i++) {
                pathData.put(pathIds[i], pathNames[i]);
            }
        }
        pathData.put(id, name);
        //封装 displayPath
        StringBuilder newDisplayPath = new StringBuilder();

        for (Map.Entry<String, String> next : pathData.entrySet()) {
            newDisplayPath.append(SEPARATE).append(next.getValue());
        }
        return newDisplayPath.toString();
    }

    /**
     * 启用/禁用组织机构
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     */
    @Override
    public void updateStatus(String id, boolean enabled) {
        AdminDept entity = adminDeptService.findById(parseLong(id));
        if (entity == null) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_NOT_EXIST);
        }
        entity.setIsDisabled(!enabled);
        adminDeptService.update(entity);
    }

    /**
     * 删除组织机构
     *
     * @param id {@link List}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrgan(String id) {
        AdminDept entity = adminDeptService.findById(parseLong(id));
        if (entity == null) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_NOT_EXIST);
        }
        //是否存在子节点
        List<AdminDeptDto> list = getByParentId(entity.getId());
        if (!CollectionUtils.isEmpty(list)) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_HAS_SUB_DEPARTMENT);
        }
        //查询当前机构和当前机构下子机构下是否存在用户，不存在删除，存在抛出异常
        Integer count = getOrgMemberCount(entity.getOrganId() == null ? id : entity.getOrganId());
        if (count > 0) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_HAS_USER);
        }
        //删除
        adminDeptService.deleteById(entity.getId());
        AuditContext.setTarget(Target.builder().id(id).name(entity.getSname())
            .type(TargetType.ORGANIZATION).build());
        //查询该子节点上级组织机构是否存在子节点，如果不存在，更改 leaf=true
        if (entity.getParentId() != null) {
            List<AdminDeptDto> parentChildren = getByParentId(entity.getParentId());
            //不存在子部门，执行更改 leaf=true 操作
            if (CollectionUtils.isEmpty(parentChildren)) {
                AdminDept parent = adminDeptService.findById(entity.getParentId());
                if (parent != null) {
                    parent.setIsLeaf(Boolean.TRUE);
                    adminDeptService.update(parent);
                }
            }
        }
    }

    /**
     * 组织机构详情
     *
     * @param id {@link String}
     * @return {@link OrganizationResult}
     */
    @Override
    public OrganizationResult getOrganizationById(String id) {
        AdminDeptDto entity = adminDeptService.getDtoById(parseLong(id));
        if (entity == null) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_NOT_EXIST);
        }
        return organizationConverter
            .entityConvertToOrgDetailResult((EmisOrganVo) EmisOrganVo.DTO.apply(entity));
    }

    /**
     * 查询子组织
     *
     * @param parentId {@link String}
     * @return {@link OrganizationResult}
     */
    @Override
    public List<OrganizationChildResult> getChildOrganization(String parentId) {
        Long id = parseLong(parentId);
        if (id == null || adminDeptService.findById(id) == null) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_NOT_EXIST);
        }
        return organizationConverter.entityConvertToChildOrgListResult(getChildVos(id));
    }

    /**
     * 查询组织成员数量
     *
     * @param organId {@link  String}
     * @return {@link  Long}
     */
    public Integer getOrgMemberCount(String organId) {
        return userOrganRelationService.getCount(new StringMap().put("organId", organId).map());
    }

    @Override
    public String getOrganizationIdByForeignId(String foreignId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setForeignId(foreignId);
        List<AdminDeptDto> list = adminDeptService.getListByParam(param);
        if (CollectionUtils.isEmpty(list)) {
            throw new OpenApiException(OpenApiStatus.DEPARTMENT_NOT_EXIST);
        }
        return String.valueOf(list.get(0).getId());
    }

    private List<AdminDeptDto> getByParentId(Long parentId) {
        AdminDeptParam param = new AdminDeptParam();
        param.setParentId(parentId);
        param.setOrderBy(5);
        return adminDeptService.getListByParam(param);
    }

    private List<EmisOrganVo> getChildVos(Long parentId) {
        List<AdminDeptDto> list = getByParentId(parentId);
        List<EmisOrganVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (AdminDeptDto dto : list) {
                vos.add((EmisOrganVo) EmisOrganVo.DTO.apply(dto));
            }
        }
        return vos;
    }

    private static Long parseLong(String value) {
        if (!StringUtils.isNumeric(value)) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 组织机构数据映射器
     */
    private final OrganizationConverter   organizationConverter;

    /**
     * AdminDeptService
     */
    private final AdminDeptService        adminDeptService;

    /**
     * UserOrganRelationService
     */
    private final UserOrganRelationService userOrganRelationService;
}
