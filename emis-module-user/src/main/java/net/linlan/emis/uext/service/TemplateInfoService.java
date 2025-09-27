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
package net.linlan.emis.uext.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.uext.dao.TemplateInfoDao;
import net.linlan.emis.uext.dto.TemplateInfoDto;
import net.linlan.emis.uext.entity.TemplateInfo;
import net.linlan.emis.uext.param.TemplateInfoParam;

/**
 *
 * TemplateInfo数据域:模板信息配置服务类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-07 18:33:53<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Service
public class TemplateInfoService {

    @Resource
    private TemplateInfoDao dao;

    /** get the list of entity TemplateInfo
     * 列表方法，返回{@link TemplateInfo} 列表
     * @param map the input select conditions
     * @return 列表的模板信息配置数据 {@link List} 对象，包含 {@link TemplateInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<TemplateInfo> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link TemplateInfo}
     * @param id 模板ID|1
     * @return {@link TemplateInfo}
     */
    public TemplateInfo findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param templateInfo the input templateInfo
     */
    //@Transactional
    public void save(TemplateInfo templateInfo) {
        templateInfo.init();
        dao.save(templateInfo);
    }

    /** batch save the entity with input list
     * 保存对象模板信息配置，批量保存list
     * @param list {@link TemplateInfo}
     */
    public void batchSave(List<TemplateInfo> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param templateInfo the input templateInfo
     */
    //@Transactional
    public void update(TemplateInfo templateInfo) {
        dao.update(templateInfo);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id 模板ID|1
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

    /** get the page of dto TemplateInfoDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的模板信息配置数据 {@link Page} 对象，包含 {@link TemplateInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<TemplateInfoDto> getPageDto(TemplateInfoParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link TemplateInfoDto}
     * @param id 模板ID|1
     * @return {@link TemplateInfoDto}
     */
    public TemplateInfoDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    public TemplateInfo getByType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        List<TemplateInfo> list = getList(new StringMap().put("type", type).map());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public void deleteByType(String type) {
        dao.deleteByType(type);
    }
}
