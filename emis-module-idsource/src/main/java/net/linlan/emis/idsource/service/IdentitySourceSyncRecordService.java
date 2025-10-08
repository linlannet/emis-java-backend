/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.idsource.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.emis.idsource.dao.IdentitySourceSyncRecordDao;
import net.linlan.emis.idsource.dto.IdentitySourceSyncRecordDto;
import net.linlan.emis.idsource.entity.IdentitySourceSyncRecord;
import net.linlan.emis.idsource.param.IdentitySourceSyncRecordParam;

/**
 *
 * IdentitySourceSyncRecord数据域:身份源同步记录服务类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:09:58
 *
 */
@Service
public class IdentitySourceSyncRecordService {

    @Resource
    private IdentitySourceSyncRecordDao dao;

    /** get the list of entity IdentitySourceSyncRecord
     * 列表方法，返回{@link IdentitySourceSyncRecord} 列表
     * @param map the input select conditions
     * @return 列表的身份源同步记录数据 {@link List} 对象，包含 {@link IdentitySourceSyncRecord} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<IdentitySourceSyncRecord> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link IdentitySourceSyncRecord}
     * @param id 主键ID|1
     * @return {@link IdentitySourceSyncRecord}
     */
    public IdentitySourceSyncRecord findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param identitySourceSyncRecord the input identitySourceSyncRecord
     */
    //@Transactional
    public void save(IdentitySourceSyncRecord identitySourceSyncRecord) {
        identitySourceSyncRecord.init();
        dao.save(identitySourceSyncRecord);
    }

    /** batch save the entity with input list
     * 保存对象身份源同步记录，批量保存list
     * @param list {@link IdentitySourceSyncRecord}
     */
    public void batchSave(List<IdentitySourceSyncRecord> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param identitySourceSyncRecord the input identitySourceSyncRecord
     */
    //@Transactional
    public void update(IdentitySourceSyncRecord identitySourceSyncRecord) {
        dao.update(identitySourceSyncRecord);
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

    /** get the page of dto IdentitySourceSyncRecordDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的身份源同步记录数据 {@link Page} 对象，包含 {@link IdentitySourceSyncRecord} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<IdentitySourceSyncRecordDto> getPageDto(IdentitySourceSyncRecordParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link IdentitySourceSyncRecordDto}
     * @param id 主键ID|1
     * @return {@link IdentitySourceSyncRecordDto}
     */
    public IdentitySourceSyncRecordDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

}
