/*
 * emis-module-audit - audit安全审计
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
package net.linlan.emis.audit.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.audit.dao.AuditInfoDao;
import net.linlan.emis.audit.dto.AuditInfoDto;
import net.linlan.emis.audit.dto.DictResult;
import net.linlan.emis.audit.entity.AuditInfo;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.audit.param.AuditInfoParam;
import net.linlan.emis.support.exception.BadParamsException;
import net.linlan.emis.support.security.util.SecurityUtils;
import net.linlan.frame.comm.constant.AdminType;

/**
 *
 * AuditInfo数据域:行为审计服务类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-06 15:55:04<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Service
public class AuditInfoService {

    @Resource
    private AuditInfoDao dao;

    /** get the list of entity AuditInfo
     * 列表方法，返回{@link AuditInfo} 列表
     * @param map the input select conditions
     * @return 列表的行为审计数据 {@link List} 对象，包含 {@link AuditInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AuditInfo> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AuditInfo}
     * @param id 主键ID|1
     * @return {@link AuditInfo}
     */
    public AuditInfo findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param auditInfo the input auditInfo
     */
    //@Transactional
    public void save(AuditInfo auditInfo) {
        auditInfo.init();
        dao.save(auditInfo);
    }

    /** batch save the entity with input list
     * 保存对象行为审计，批量保存list
     * @param list {@link AuditInfo}
     */
    public void batchSave(List<AuditInfo> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param auditInfo the input auditInfo
     */
    //@Transactional
    public void update(AuditInfo auditInfo) {
        dao.update(auditInfo);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id 主键ID|1
     */
    public void deleteById(String id) {
        if (StringUtils.isBlank(id)) {
            return;
        }
        dao.deleteById(id);
    }

    /** batch delete the entity by input ids
     * 批量删除方法，通过ids删除对象
     * @param ids the input ids
     */
    //@Transactional
    public void deleteByIds(String[] ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return;
        }
        dao.deleteByIds(ids);
    }

    /** query the total count by input select conditions
     * 通过输入的条件查询记录总数
     * @param map the input select conditions
     * @return total count
     */
    public int getCount(Map<String, Object> map) {
        return dao.getCount(map);
    }

    /** get the page of dto AuditInfoDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的行为审计数据 {@link Page} 对象，包含 {@link AuditInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AuditInfoDto> getPageDto(AuditInfoParam param) {
        if (AdminType.USER.getType().equals(SecurityUtils.getCurrentUserType())
            && !AdminType.USER.getType().equals(param.getUserType())) {
            throw new BadParamsException("用户类型错误");
        }
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AuditInfoDto}
     * @param id 主键ID|1
     * @return {@link AuditInfoDto}
     */
    public AuditInfoDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 获取字典类型
     *
     * @param userType {@link String}
     * @return {@link List}
     */
    public List<DictResult> getAuditDict(String userType) {
        List<EventType> types = Arrays.asList(EventType.values());
        //获取分组
        List<DictResult> list = types.stream().map(EventType::getResource)
            .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList()).stream()
            .map(resource -> {
                DictResult group = new DictResult();
                group.setName(resource.getName());
                group.setCode(resource.getCode());
                return group;
            }).collect(Collectors.toList());
        //处理每个分组的审计类型
        list.forEach(dict -> {
            Set<DictResult.AuditType> auditTypes = new LinkedHashSet<>();
            types.stream()
                .filter(auditType -> auditType.getResource().getCode().equals(dict.getCode()))
                .forEach(auditType -> {
                    if (auditType.getUserTypes().stream().map(AdminType::getType)
                        .collect(Collectors.toList()).contains(userType)) {
                        DictResult.AuditType type = new DictResult.AuditType();
                        type.setName(auditType.getDesc());
                        type.setCode(auditType.getCode());
                        auditTypes.add(type);
                    }
                });
            dict.setTypes(auditTypes);
        });
        list = list.stream().filter(i -> !CollectionUtils.isEmpty(i.getTypes()))
            .collect(Collectors.toList());
        return list;
    }

}
