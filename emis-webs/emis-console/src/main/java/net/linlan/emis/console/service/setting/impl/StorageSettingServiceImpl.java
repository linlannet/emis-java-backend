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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.emis.console.converter.setting.StorageSettingConverter;
import net.linlan.emis.console.pojo.result.setting.StorageProviderConfigResult;
import net.linlan.emis.console.pojo.save.setting.StorageConfigSaveParam;
import net.linlan.emis.console.service.setting.StorageSettingService;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import static net.linlan.emis.core.storage.constant.StorageProviderSettingConstants.STORAGE_BEAN_NAME;
import static net.linlan.emis.core.storage.constant.StorageProviderSettingConstants.STORAGE_PROVIDER_KEY;

/**
 * 存储设置接口
 *
 * @author Linlan
 * Created on  2021/11/1 21:43
 */
@Service
public class StorageSettingServiceImpl extends SettingServiceImpl implements StorageSettingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 更改存储启用禁用
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableStorage() {
        removeSetting(STORAGE_PROVIDER_KEY);
        // refresh
        ConfigRefreshUtils.refresh(STORAGE_BEAN_NAME);
        return Boolean.TRUE;
    }

    /**
     * 保存存储配置
     *
     * @param param {@link StorageConfigSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveStorageConfig(StorageConfigSaveParam param) {
        EmisSettingVo entity = storageSettingsConverter.storageConfigSaveParamToEntity(param);
        Boolean setting = saveSetting(entity);
        ConfigRefreshUtils.refresh(STORAGE_BEAN_NAME);

        return setting;
    }

    /**
     * 获取存储配置
     *
     * @return {@link StorageProviderConfigResult}
     */
    @Override
    public StorageProviderConfigResult getStorageConfig() {
        EmisSettingVo entity = getSetting(STORAGE_PROVIDER_KEY);
        return storageSettingsConverter.entityToStorageProviderConfig(entity);
    }

    private final StorageSettingConverter storageSettingsConverter;
    private final EmisSettingService       settingService;

    public StorageSettingServiceImpl(EmisSettingService settingsRepository,
                                     StorageSettingConverter storageSettingsConverter,
                                     EmisSettingService settingService) {
        super(settingsRepository);
        this.storageSettingsConverter = storageSettingsConverter;
        this.settingService = settingService;
    }
}
