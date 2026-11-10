/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import net.linlan.commons.core.ObjectUtils;
import net.linlan.emis.support.repository.base.BaseEntity;
import net.linlan.emis.support.utils.BeanUtils;
import net.linlan.sys.base.entity.BaseConfigWhole;
import net.linlan.sys.base.service.BaseConfigWholeService;

/**
 * 设置impl
 *
 * @author Linlan
 */
@NoArgsConstructor
@AllArgsConstructor
public class EmisSettingServiceImpl implements EmisSettingService {
    @Resource
    private BaseConfigWholeService baseConfigWholeService;

    /**
     * 保存设置
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link Boolean}
     */
    public Boolean saveSetting(EmisSettingVo entity) {
        EmisSettingVo setting = getByKey(entity.getName());
        EmisSettingVo saveEntity = entity;
        // 当前配置存在，更改操作
        if (!Objects.isNull(setting)) {
            BeanUtils.merge(entity, setting);
            saveEntity = setting;
        }
        baseConfigWholeService.save(EmisSettingVo.transTo(saveEntity));
        return Boolean.TRUE;
    }

    /**
     * 保存批量配置
     *
     * @param list {@link List}
     * @return {@link Boolean}
     */
    public Boolean save(List<EmisSettingVo> list) {
        for (EmisSettingVo setting : list) {
            EmisSettingVo type = getByKey(setting.getName());
            if (Objects.isNull(type)) {
                saveSetting(setting);
                continue;
            }
            BeanUtils.merge(setting, type, BaseEntity.LAST_MODIFIED_BY,
                BaseEntity.LAST_MODIFIED_TIME);
            saveSetting(type);
        }
        return true;
    }

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link EmisSettingVo}
     */
    @Cacheable(key = "#p0", unless = "#result==null")
    public EmisSettingVo getByKey(String name) {
        BaseConfigWhole entity = baseConfigWholeService.getByKey(name);
        return (EmisSettingVo) EmisSettingVo.TO.apply(entity);
    }

    /**
     * 根据类型查询安全配置
     *
     * @param names {@link List}
     * @return {@link EmisSettingVo}
     */
    public List<EmisSettingVo> getByKeys(List<String> names) {
        List<BaseConfigWhole> result = baseConfigWholeService.getByKeys(names);
        if (ObjectUtils.isEmpty(result)) {
            return new ArrayList<>();
        }
        List<EmisSettingVo> vos = Lists.transform(result, EmisSettingVo.TO);
        return vos;
    }

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link EmisSettingVo}
     */
    public List<EmisSettingVo> getByNameLike(String name) {
        List<BaseConfigWhole> result = baseConfigWholeService.getByKeyLike(name);
        if (ObjectUtils.isEmpty(result)) {
            return new ArrayList<>();
        }
        List<EmisSettingVo> vos = Lists.transform(result, EmisSettingVo.TO);
        return vos;
    }

    /**
     * 失效配置
     *
     * @param name {@link String}
     * @return {@link Boolean}
     */
    public Boolean disableByKey(String name) {
        BaseConfigWhole entity = baseConfigWholeService.getByKey(name);
        if (entity != null) {
            entity.setIsDisabled(Boolean.TRUE);
            baseConfigWholeService.update(entity);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 根据名称删除
     *
     * @param name {@link String}
     */
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteByKey(String name) {
        baseConfigWholeService.deleteByKey(name);
    }

    /**
     * 根据名称删除列表
     *
     * @param names {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByKeys(List<String> names) {
        baseConfigWholeService.deleteByKeys(names);
    }

}
