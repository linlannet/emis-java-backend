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
package net.linlan.emis.console.controller.setting;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.setting.GeoIpProviderResult;
import net.linlan.emis.console.pojo.save.setting.GeoIpProviderSaveParam;
import net.linlan.emis.console.service.setting.GeoLocationSettingService;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.GEO_LOCATION;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_PATH;
import static net.linlan.emis.core.geo.constant.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;

/**
 * IP地址库
 *
 * @author Linlan
 * Created on  2021/11/13 22:09
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "IP地址库")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH + "/geo_ip", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoIpLibraryController {
    /**
     * 获取配置
     *
     * @return {@link  GeoIpProviderResult}
     */
    @Operation(summary = "获取地理位置配置")
    @GetMapping("config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<GeoIpProviderResult> getGeoIpLibrary() {
        GeoIpProviderResult result = geoLocationSettingService.getGeoIpLibrary();
        return ResponseResult.ok(result);
    }

    /**
     * 保存配置
     *
     * @param param {@link  GeoIpProviderSaveParam}
     * @return {@link  Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存地理位置配置")
    @Audit(type = EventType.SAVE_GEO_LOCATION_SERVICE)
    @PostMapping("save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveGeoIpLibrary(@RequestBody @Validated GeoIpProviderSaveParam param) {
        boolean result = geoLocationSettingService.saveGeoIpLibrary(param);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用地理位置服务
     *
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用地理位置服务")
    @Audit(type = EventType.OFF_GEO_LOCATION_SERVICE)
    @PutMapping("/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableGeoIpLibrary() {
        Boolean result = geoLocationSettingService.removeSetting(IPADDRESS_SETTING_NAME);
        // refresh
        ConfigRefreshUtils.refresh(GEO_LOCATION);
        return ResponseResult.ok(result);
    }

    /**
     * 查询IP地址位置信息
     *
     * @param ip {@link  String}
     * @return {@link  ResponseResult}
     */
    @Operation(summary = "查询IP地址位置信息")
    @GetMapping(value = "/get_location")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<GeoLocation> getGeoLocation(@Parameter(description = "IP") String ip) {
        GeoLocation result = geoLocationSettingService.getGeoLocation(ip);
        return ResponseResult.ok(result);
    }

    private GeoLocationSettingService geoLocationSettingService;

}
