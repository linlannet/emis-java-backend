/*
 * emis-module-provider - provider身份提供商
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
package net.linlan.emis.provider.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.commons.core.StringUtils;
import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.provider.dao.IdentityProviderDao;
import net.linlan.emis.provider.dto.IdentityProviderDto;
import net.linlan.emis.provider.entity.IdentityProvider;
import net.linlan.emis.provider.param.IdentityProviderParam;

/**
 *
 * IdentityProvider数据域:身份提供商服务类
 * @author Linlan
 *
 * CreateTime 2025-08-31 23:33:33
 *
 */
@Service
public class IdentityProviderService {

    @Resource
    private IdentityProviderDao dao;

    /** get the list of entity IdentityProvider
     * 列表方法，返回{@link IdentityProvider} 列表
     * @param map the input select conditions
     * @return 列表的身份提供商数据 {@link List} 对象，包含 {@link IdentityProvider} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public List<IdentityProvider> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link IdentityProvider}
     * @param id 主键ID|1
     * @return {@link IdentityProvider}
     */
    public IdentityProvider findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param identityProvider the input identityProvider
     */
    //@Transactional
    public void save(IdentityProvider identityProvider) {
        identityProvider.init();
        dao.save(identityProvider);
    }

    /** batch save the entity with input list
     * 保存对象身份提供商，批量保存list
     * @param list {@link IdentityProvider}
     */
    public void batchSave(List<IdentityProvider> list) {
        dao.batchSave(list);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param identityProvider the input identityProvider
     */
    //@Transactional
    public void update(IdentityProvider identityProvider) {
        dao.update(identityProvider);
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

    /** get the page of dto IdentityProviderDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的身份提供商数据 {@link Page} 对象，包含 {@link IdentityProvider} 列表
     */
    //@DataFilter(tableAlias = "B", owner = false)
    public Page<IdentityProviderDto> getPageDto(IdentityProviderParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        return dao.getPageDto(param);
    }

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link IdentityProviderDto}
     * @param id 主键ID|1
     * @return {@link IdentityProviderDto}
     */
    public IdentityProviderDto getDtoById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return dao.getDtoById(id);
    }

    /**
     * 平台是否启用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    public Boolean identityProviderIsEnable(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        IdentityProvider entity = findById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return false;
        }
        return entity.getIsEnabled();
    }

    /**
     * 通过平台类型获取
     *
     * @param providerType {@link String}
     * @return {@link IdentityProvider}
     */
    public List<IdentityProvider> findListByType(String providerType) {
        return getList(new StringMap().put("type", providerType).map());
    }

    /**
     * 更改认证源状态
     *
     * @param id      {@link String}
     * @param enabled 状态
     * @return {@link Boolean}
     */
    public Boolean updateIdentityProviderStatus(String id, Boolean enabled) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        IdentityProvider entity = findById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return false;
        }
        entity.setIsEnabled(enabled);
        update(entity);
        return true;
    }

    /**
     * 根据ID查找，并且为启用
     *
     * @param id {@link String}
     * @return {@link IdentityProvider}
     */
    public IdentityProvider findByIdAndEnabledIsTrue(String id) {
        List<IdentityProvider> list = getList(
            new StringMap().put("id", id).put("isEnabled", true).map());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据code查找，并且为启用
     *
     * @param code 代码编码
     * @return {@link IdentityProvider}
     */
    public IdentityProvider findByCodeAndEnabledIsTrue(String code) {
        List<IdentityProvider> list = getList(
            new StringMap().put("code", code).put("isEnabled", true).map());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
