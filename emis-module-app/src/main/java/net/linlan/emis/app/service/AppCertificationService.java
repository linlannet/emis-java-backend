/*
 * emis-module-app - app应用管理
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
package net.linlan.emis.app.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.app.dao.AppCertificationDao;
import net.linlan.emis.app.dto.AppCertificationDto;
import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.app.param.AppCertificationParam;

/**
 *
 * AppCertifacation数据域:应用证书服务类
 * @author Linlan
 *
 *
 */
@Service
public class AppCertificationService {

    @Resource
    private AppCertificationDao dao;

    /** get the list of entity AppCertifacation
     * 列表方法，返回{@link AppCertification} 列表
     * @param map the input select conditions
     * @return 列表的应用证书数据 {@link List} 对象，包含 {@link AppCertification} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AppCertification> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AppCertification}
     * @param id 主键ID|1
     * @return {@link AppCertification}
     */
    public AppCertification findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param appCertifacation the input appCertifacation
     */
    //@Transactional
    public void save(AppCertification appCertifacation) {
        appCertifacation.init();
        dao.save(appCertifacation);
    }

    /** batch save the entity with input list
     * 保存对象应用证书，批量保存list
     * @param list {@link AppCertification}
     */
    public void batchSave(List<AppCertification> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param appCertifacation the input appCertifacation
     */
    //@Transactional
    public void update(AppCertification appCertifacation) {
        dao.update(appCertifacation);
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

    /** get the page of dto AppCertifacationDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用证书数据 {@link Page} 对象，包含 {@link AppCertification} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AppCertificationDto> getPageDto(AppCertificationParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppCertificationDto}
     * @param id 主键ID|1
     * @return {@link AppCertificationDto}
     */
    public AppCertificationDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 根据应用ID查询证书
     *
     * @param appId  应用ID
     * @param usingType 使用类型
     * @return {@link AppCertification}
     */
    public AppCertification findByAppIdAndUsingType(String appId, String usingType) {
        List<AppCertification> list = getList(
            new StringMap().put("appId", appId).put("usingType", usingType).map());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据appid删除所有的数据
     *
     * @param appId 应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByAppId(@Param("appId") String appId) {
        dao.deleteAllByAppId(appId);
    }

}
