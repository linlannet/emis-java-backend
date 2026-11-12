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
package net.linlan.emis.console.service.setting.impl;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;

import net.linlan.emis.console.service.setting.SettingService;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.support.utils.BeanUtils;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static net.linlan.emis.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 设置impl
 *
 * @author Linlan
 * Created on  2021/11/9 22:29
 */
@AllArgsConstructor
public class SettingServiceImpl implements SettingService {
    /**
     * 保存设置
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveSetting(EmisSettingVo entity) {
        EmisSettingVo setting = getSetting(entity.getName());
        EmisSettingVo saveEntity = entity;
        // 当前配置存在，更改操作
        if (!Objects.isNull(setting)) {
            BeanUtils.merge(entity, setting, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
            saveEntity = setting;
        }
        settingsRepository.saveSetting(saveEntity);
        return Boolean.TRUE;
    }

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link EmisSettingVo}
     */
    @Override
    public EmisSettingVo getSetting(String name) {
        return settingsRepository.getByKey(name);
    }

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link EmisSettingVo}
     */
    @Override
    public List<EmisSettingVo> findByNameLike(String name) {
        return settingsRepository.getByNameLike(name);
    }

    /**
     * 移除配置
     *
     * @param name {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean removeSetting(String name) {
        settingsRepository.deleteByKey(name);
        return Boolean.TRUE;
    }

    /**
     * EmisSettingService
     */
    private final EmisSettingService settingsRepository;

}
