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
package net.linlan.emis.openapi.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.JdkIdGenerator;
import org.springframework.util.StringUtils;

import net.linlan.emis.openapi.pojo.result.OrganizationChildResult;
import net.linlan.emis.openapi.pojo.result.OrganizationResult;
import net.linlan.emis.openapi.pojo.save.OrganizationCreateParam;
import net.linlan.emis.openapi.pojo.update.OrganizationUpdateParam;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.frame.admin.entity.AdminDept;

/**
 * 组织机构数据映射
 *
 * @author Linlan
 * Created on 2020/8/14 21:25
 */
@Component
public class OrganizationConverter {

    /**
     * 组织实体转换为List结果
     *
     * @param data {@link List}
     * @return {@link List}
     */
    public List<OrganizationChildResult> entityConvertToChildOrgListResult(List<EmisOrganVo> data) {
        List<OrganizationChildResult> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data)) {
            for (EmisOrganVo entity : data) {
                list.add(entityConvertToChildOrgListResult(entity));
            }
        }
        return list;
    }

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link EmisOrganVo}
     * @return {@link OrganizationChildResult}
     */
    public OrganizationChildResult entityConvertToChildOrgListResult(EmisOrganVo data) {
        if (data == null) {
            return null;
        }
        OrganizationChildResult result = new OrganizationChildResult();
        result.setId(data.getId() == null ? null : String.valueOf(data.getId()));
        result.setName(data.getName());
        result.setParentId(data.getParentId() == null ? null : String.valueOf(data.getParentId()));
        result.setDisplayPath(data.getDisplayPath());
        result.setCode(data.getCode());
        result.setForeignId(data.getForeignId());
        result.setType(data.getType() == null ? null : data.getType().getType());
        result.setDataOrigin(data.getDataOrigin());
        result.setPriority(data.getPriority());
        result.setEnabled(data.getEnabled());
        result.setLeaf(data.getLeaf());
        return result;
    }

    /**
     * 组织创建参数转换为组织实体
     *
     * @param param {@link OrganizationCreateParam}
     * @return {@link AdminDept}
     */
    public AdminDept orgCreateParamConvertToEntity(OrganizationCreateParam param) {
        if (param == null) {
            return null;
        }
        AdminDept entity = new AdminDept();
        entity.setSname(param.getName());
        entity.setName(param.getName());
        entity.setType(param.getType() == null ? null : param.getType().getType());
        entity.setParentId(parseLong(param.getParentId()));
        entity.setForeignId(StringUtils.hasText(param.getForeignId()) ? param.getForeignId()
            : new JdkIdGenerator().generateId().toString());
        entity.setAreaCode(param.getArea());
        entity.setPriority(parseInteger(param.getPriority(), 9999));
        entity.setIsDisabled(!Boolean.TRUE.equals(param.getEnabled()));
        entity.setIsLeaf(Boolean.TRUE);
        entity.setCreateType(1);
        entity.setDescription(param.getDesc());
        return entity;
    }

    /**
     * 组织修改参数转换为组织实体
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link AdminDept}
     */
    public AdminDept orgUpdateParamConvertToEntity(OrganizationUpdateParam param) {
        if (param == null) {
            return null;
        }
        AdminDept entity = new AdminDept();
        entity.setId(parseLong(param.getId()));
        entity.setSname(param.getName());
        entity.setType(param.getType() == null ? null : param.getType().getType());
        entity.setPriority(parseInteger(param.getPriority(), null));
        entity.setIsDisabled(!Boolean.TRUE.equals(param.getEnabled()));
        entity.setDescription(param.getDesc());
        return entity;
    }

    /**
     * 实体转组织详情结果
     *
     * @param organization {@link EmisOrganVo}
     * @return {@link OrganizationResult}
     */
    public OrganizationResult entityConvertToOrgDetailResult(EmisOrganVo organization) {
        if (organization == null) {
            return null;
        }
        OrganizationResult result = new OrganizationResult();
        result.setId(organization.getId() == null ? null : String.valueOf(organization.getId()));
        result.setName(organization.getName());
        result.setDisplayPath(organization.getDisplayPath());
        result.setCode(organization.getCode());
        result.setPriority(
            organization.getPriority() == null ? null : String.valueOf(organization.getPriority()));
        result.setType(organization.getType());
        result.setRemark(organization.getRemark());
        return result;
    }

    private static Long parseLong(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInteger(String value, Integer defaultValue) {
        if (!StringUtils.hasText(value)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
