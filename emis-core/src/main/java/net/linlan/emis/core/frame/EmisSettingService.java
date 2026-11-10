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

import java.util.List;

/**
 * @author Linlan
 */
public interface EmisSettingService {
    /**
     * 保存设置
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link Boolean}
     */
    Boolean saveSetting(EmisSettingVo entity);

    /**
     * 保存批量配置
     *
     * @param list {@link List}
     * @return {@link Boolean}
     */
    Boolean save(List<EmisSettingVo> list);

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link EmisSettingVo}
     */
    EmisSettingVo getByKey(String name);

    /**
     * 根据KEY查询
     *
     * @param name {@link String}
     * @return {@link EmisSettingVo}
     */
    List<EmisSettingVo> getByNameLike(String name);

    /**
     * 根据类型查询安全配置
     *
     * @param names {@link List}
     * @return {@link EmisSettingVo}
     */
    List<EmisSettingVo> getByKeys(List<String> names);

    /**
     * 移除配置
     *
     * @param name {@link String}
     * @return {@link Boolean}
     */
    Boolean disableByKey(String name);

    /**
     * 通过KEY删除信息
     * @param name  KEY的名称
     */
    void deleteByKey(String name);

    /**
     * 通过KEYS删除信息
     * @param names     KEYS的名称
     */
    void deleteByKeys(List<String> names);

}
