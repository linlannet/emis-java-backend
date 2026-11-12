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
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.setting.StorageProviderConfigResult;
import net.linlan.emis.console.pojo.save.setting.StorageConfigSaveParam;
import net.linlan.emis.console.service.setting.StorageSettingService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;
import static net.linlan.emis.core.storage.StorageConstants.STORAGE_PATH;

/**
 * 存储配置
 *
 * @author Linlan
 * Created on  2021/10/30 21:16
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "存储配置")
@RestController
@RequestMapping(value = STORAGE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class StorageController {

    /**
     * 启用/禁用存储配置服务
     *
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "禁用存储服务")
    @Audit(type = EventType.OFF_STORAGE_SERVICE)
    @PutMapping(value = "/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableStorage() {
        Boolean result = storageSettingsService.disableStorage();
        return ResponseResult.ok(result);
    }

    /**
     * 保存存储配置
     *
     * @param param {@link StorageConfigSaveParam}
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "保存存储服务配置")
    @Audit(type = EventType.SAVE_STORAGE_SERVICE)
    @PostMapping(value = "/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveStorageConfig(@RequestBody StorageConfigSaveParam param) {
        Boolean result = storageSettingsService.saveStorageConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 获取存储配置
     *
     * @return {@link ResponseResult}
     */
    @Operation(summary = "获取存储服务配置")
    @GetMapping(value = "/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<StorageProviderConfigResult> getStorageConfig() {
        StorageProviderConfigResult result = storageSettingsService.getStorageConfig();
        return ResponseResult.ok(result);
    }

    /**
     * StorageSettingsService
     */
    private final StorageSettingService storageSettingsService;
}
