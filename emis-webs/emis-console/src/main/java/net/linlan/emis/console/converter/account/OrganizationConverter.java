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
import java.util.Objects;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.console.pojo.result.account.*;
import net.linlan.emis.console.pojo.save.account.OrganizationCreateParam;
import net.linlan.emis.console.pojo.update.account.OrganizationUpdateParam;
import net.linlan.emis.organ.entity.EmisOrganVo;
import net.linlan.emis.support.userdetails.OrganizationType;

/**
 * 组织机构数据映射
 *
 * @author Linlan
 * Created on  2020/8/14 21:25
 */
@Mapper(componentModel = "spring")
public interface OrganizationConverter {

    /**
     * 组织实体转换为List结果
     *
     * @param data {@link List}
     * @return {@link List}
     */
    default List<OrganizationChildResult> entityConvertToChildOrganListResult(List<EmisOrganVo> data) {
        List<OrganizationChildResult> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data)) {
            for (EmisOrganVo entity : data) {
                list.add(entityConvertToChildOrganListResult(entity));
            }
        }
        return list;
    }

    /**
     * 组织实体转换为TreeList结果
     *
     * @param data     {@link List}
     * @param parentId {@link String}
     * @return {@link List}
     */
    default List<SearchOrganizationTreeResult> entityConvertToChildOrgTreeListResult(String parentId,
                                                                                     List<EmisOrganVo> data) {
        List<SearchOrganizationTreeResult> list = new ArrayList<>();
        List<EmisOrganVo> entityList = data.stream()
            .filter(one -> Objects.equals(parentId, String.valueOf(one.getParentId())))
            .collect(Collectors.toList());
        for (EmisOrganVo organizationEntity : entityList) {
            SearchOrganizationTreeResult result = entityConvertToChildOrgTreeListResult(
                organizationEntity);
            result.setChildren(entityConvertToChildOrgTreeListResult(
                String.valueOf(organizationEntity.getId()), data));
            list.add(result);
        }
        return list;
    }

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link EmisOrganVo}
     * @return {@link OrganizationResult}
     */
    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "dataOrigin", source = "dataOrigin")
    OrganizationChildResult entityConvertToChildOrganListResult(EmisOrganVo data);

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link EmisOrganVo}
     * @return {@link OrganizationResult}
     */
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "dataOrigin", source = "dataOrigin")
    SearchOrganizationTreeResult entityConvertToChildOrgTreeListResult(EmisOrganVo data);

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link EmisOrganVo}
     * @return {@link OrganizationResult}
     */
    @Mapping(target = "type", source = "type.name")
    @Mapping(target = "dataOrigin", source = "dataOrigin")
    OrganizationRootResult entityConvertToRootOrgListResult(EmisOrganVo data);

    /**
     * 组织创建参数转换为组织实体
     *
     * @param param {@link OrganizationCreateParam}
     * @return {@link EmisOrganVo}
     */

    @Mapping(target = "identitySourceId", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "displayPath", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataOrigin", expression = "java(net.linlan.emis.support.userdetails.DataOrigin.INPUT.getType())")
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "leaf", expression = "java(Boolean.TRUE)")
    @Mapping(target = "enabled", expression = "java(Boolean.TRUE)")
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "priority", source = "priority", defaultValue = "9999")
    @Mapping(target = "parentId", source = "parentId", defaultExpression = "java(net.linlan.emis.support.constant.EmisConstants.ROOT_NODE)")
    @Mapping(target = "foreignId", source = "foreignId", defaultExpression = "java(new org.springframework.util.JdkIdGenerator().generateId().toString())")
    EmisOrganVo orgCreateParamConvertToEntity(OrganizationCreateParam param);

    /**
     * 组织修改参数转换为组织实体
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link EmisOrganVo}
     */

    @Mapping(target = "identitySourceId", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "displayPath", ignore = true)
    @Mapping(target = "dataOrigin", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "leaf", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "foreignId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    EmisOrganVo orgUpdateParamConvertToEntity(OrganizationUpdateParam param);

    /**
     * 实体转组织详情结果
     *
     * @param organization {@link EmisOrganVo}
     * @return {@link OrganizationResult}
     */
    OrganizationResult entityConvertToGetOrganizationResult(EmisOrganVo organization);

    /**
     * 实体转批量获取组织结果
     *
     * @param organization {@link EmisOrganVo}
     * @return {@link OrganizationResult}
     */
    BatchOrganizationResult entityConvertToBatchGetOrganizationResult(EmisOrganVo organization);

    /**
     * 实体转批量获取组织结果
     *
     * @param list {@link List}
     * @return {@link List}
     */
    default List<BatchOrganizationResult> entityConvertToBatchGetOrganizationResult(List<EmisOrganVo> list) {
        return list.stream().map(this::entityConvertToBatchGetOrganizationResult)
            .collect(Collectors.toList());
    }

    /**
     * 实体转搜索组织结果
     *
     * @param list {@link EmisOrganVo}
     * @return {@link List}
     */
    @Mapping(target = "type", source = "organizationEntity.type", qualifiedByName = "mapOrganizationTypeToString")
    List<SearchOrganizationResult> entityConvertToSearchOrganizationResult(List<EmisOrganVo> list);

    // 新增类型转换方法
    default String mapOrganizationTypeToString(OrganizationType type) {
        return type != null ? type.getType() : null; // 提取 OrganizationType 中的 type 字段
    }
}
