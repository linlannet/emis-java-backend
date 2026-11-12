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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.console.converter.setting.GeoLocationSettingConverter;
import net.linlan.emis.console.pojo.result.setting.GeoIpProviderResult;
import net.linlan.emis.console.pojo.save.setting.GeoIpProviderSaveParam;
import net.linlan.emis.console.service.setting.GeoLocationSettingService;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.geo.GeoLocationParser;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.GEO_LOCATION;
import static net.linlan.emis.core.geo.constant.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;

/**
 * ip设置接口
 *
 * @author Linlan
 * Created on  2021/11/1 21:43
 */
@Slf4j
@Service
public class GeoLocationSettingServiceImpl extends SettingServiceImpl
                                           implements GeoLocationSettingService {

    /**
     * 保存配置
     *
     * @param param {@link GeoIpProviderSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveGeoIpLibrary(GeoIpProviderSaveParam param) {
        EmisSettingVo settingEntity = geoLocationSettingsConverter
            .geoLocationProviderConfigToEntity(param);
        Boolean success = saveSetting(settingEntity);
        ConfigRefreshUtils.refresh(GEO_LOCATION);
        return success;
    }

    /**
     * 获取配置
     *
     * @return {@link EmisSettingVo}
     */
    @Override
    public GeoIpProviderResult getGeoIpLibrary() {
        return geoLocationSettingsConverter
            .entityToGeoLocationProviderConfig(getSetting(IPADDRESS_SETTING_NAME));
    }

    /**
     * 查询ip详细信息
     *
     * @param ip {@link String}
     * @return {@link GeoLocation}
     */
    @Override
    public GeoLocation getGeoLocation(String ip) {
        return geoLocationParser.getGeoLocation(ip);
    }

    private final GeoLocationParser           geoLocationParser;
    private final GeoLocationSettingConverter geoLocationSettingsConverter;

    public GeoLocationSettingServiceImpl(EmisSettingService settingsRepository,
                                         GeoLocationParser geoLocationParser,
                                         GeoLocationSettingConverter geoLocationSettingsConverter) {
        super(settingsRepository);
        this.geoLocationParser = geoLocationParser;
        this.geoLocationSettingsConverter = geoLocationSettingsConverter;
    }
}
