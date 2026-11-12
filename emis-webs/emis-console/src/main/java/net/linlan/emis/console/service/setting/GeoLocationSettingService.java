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

import net.linlan.emis.console.pojo.result.setting.GeoIpProviderResult;
import net.linlan.emis.console.pojo.save.setting.GeoIpProviderSaveParam;
import net.linlan.emis.support.userdetails.geo.GeoLocation;

/**
 * ip设置接口
 *
 * @author Linlan
 * Created on  2021/10/1 21:19
 */
public interface GeoLocationSettingService extends SettingService {

    /**
     * 保存配置
     *
     * @param param {@link GeoIpProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveGeoIpLibrary(GeoIpProviderSaveParam param);

    /**
     * 获取配置
     *
     * @return {@link GeoIpProviderResult}
     */
    GeoIpProviderResult getGeoIpLibrary();

    /**
     * 查询ip详细信息
     *
     * @param ip {@link  String}
     * @return {@link GeoLocation}
     */
    GeoLocation getGeoLocation(String ip);
}
