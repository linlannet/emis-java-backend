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
package net.linlan.emis.console.service.setting;

import java.util.List;

import net.linlan.emis.core.frame.EmisSettingVo;

/**
 * @author Linlan
 * Created on  2021/11/9 22:30
 */
public interface SettingService {
    /**
     * 保存设置
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link Boolean}
     */
    Boolean saveSetting(EmisSettingVo entity);

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link EmisSettingVo}
     */
    EmisSettingVo getSetting(String name);

    /**
     * 根据KEY模糊获取配置
     *
     * @param name {@link String}
     * @return {@link List<EmisSettingVo>}
     */
    List<EmisSettingVo> findByNameLike(String name);

    /**
     * 移除配置
     *
     * @param name {@link String}
     * @return {@link Boolean}
     */
    Boolean removeSetting(String name);
}
