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
import net.linlan.emis.app.dao.AppInfoDao;
import net.linlan.emis.app.dto.AppInfoDto;
import net.linlan.emis.app.dto.AppRelationDto;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.param.AppInfoParam;
import net.linlan.emis.app.param.GetAppListParam;

/**
 *
 * AppInfo数据域:应用协议服务类
 * @author Linlan
 *
 *
 */
@Service
public class AppInfoService {

    @Resource
    private AppInfoDao dao;

    /** get the list of entity AppInfo
     * 列表方法，返回{@link AppInfo} 列表
     * @param map the input select conditions
     * @return 列表的应用协议数据 {@link List} 对象，包含 {@link AppInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<AppInfo> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link AppInfo}
     * @param id 主键ID|1
     * @return {@link AppInfo}
     */
    public AppInfo findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param appInfo the input appInfo
     */
    //@Transactional
    public void save(AppInfo appInfo) {
        appInfo.init();
        dao.save(appInfo);
    }

    /** batch save the entity with input list
     * 保存对象应用协议，批量保存list
     * @param list {@link AppInfo}
     */
    public void batchSave(List<AppInfo> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param appInfo the input appInfo
     */
    //@Transactional
    public void update(AppInfo appInfo) {
        dao.update(appInfo);
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

    /** get the page of dto AppInfoDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的应用协议数据 {@link Page} 对象，包含 {@link AppInfo} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<AppInfoDto> getPageDto(AppInfoParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link AppInfoDto}
     * @param id 主键ID|1
     * @return {@link AppInfoDto}
     */
    public AppInfoDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 更新应用状态
     *
     * @param id {@link  String}
     * @param enabled {@link  Boolean}
     * @return {@link  Boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer updateStatus(@Param(value = "id") String id,
                                @Param(value = "enabled") Boolean enabled) {
        AppInfo accessPolicy = findById(id);
        if (ObjectUtils.isNotEmpty(accessPolicy)) {
            accessPolicy.setIsEnabled(enabled);
            update(accessPolicy);
        }
        return 0;
    }

    /**
     * 根据clientId获取配置
     *
     * @param clientId {@link String}
     * @return {@link AppInfo}
     */
    public AppInfo findByClientId(String clientId) {
        List<AppInfo> list = getList(new StringMap().put("clientId", clientId).map());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     *  findByCode
     *
     * @param appCode {@link String}
     * @return {@link AppInfo}
     */
    public AppInfo findByCode(String appCode) {
        List<AppInfo> list = getList(new StringMap().put("appCode", appCode).map());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     *  findByIdIn
     *
     * @param appIds 应用IDs
     * @return 应用列表
     */
    public List<AppInfo> findByIdIn(String[] appIds) {
        List<AppInfo> list = getList(new StringMap().put("appIds", appIds).map());
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * 根据主体ID获取应用列表
     *
     * @param subjectIds 授权主体Ids
     * @return  分页信息
     */
    public List<AppRelationDto> getAppList(String[] subjectIds) {
        return dao.getAppList(subjectIds);
    }

    /**
     * 根据主体ID，查询参数、分页条件获取应用列表
     *
     * @param subjectIds 授权主体Ids
     * @param param 查询条件
     * @return  分页信息
     */
    public Page<AppInfo> getAppList(List<String> subjectIds, GetAppListParam param) {
        return dao.getAppList(subjectIds, param);
    }

    /**
     * 获取用户应用数量
     *
     * @param subjectIds 授权主体Ids
     * @return    结果
     */
    public int getAppCount(String[] subjectIds) {
        return dao.getAppCount(subjectIds);
    }
}
