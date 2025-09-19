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
package net.linlan.emis.core.config;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.core.encrypt.EncryptionModule;
import net.linlan.emis.core.storage.Storage;
import net.linlan.emis.core.storage.StorageConfig;
import net.linlan.emis.core.storage.StorageFactory;
import net.linlan.emis.core.storage.constant.StorageProviderSettingConstants;
import net.linlan.emis.core.storage.impl.NoneStorage;
import net.linlan.sys.base.entity.BaseConfigWhole;
import net.linlan.sys.base.service.BaseConfigWholeService;

/**
 * 存储配置
 *
 * @author Linlan
 */
@Slf4j
@Configuration
public class EmisStorageConfiguration {

    @Resource
    private BaseConfigWholeService baseConfigWholeService;

    /**
     * 存储配置
     *
     * @return {@link Storage}
     */
    @Bean(name = StorageProviderSettingConstants.STORAGE_BEAN_NAME)
    @RefreshScope
    public Storage storage() {
        BaseConfigWhole setting = baseConfigWholeService
            .getByKey(StorageProviderSettingConstants.STORAGE_PROVIDER_KEY);
        ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
        // 指定序列化输入的类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        try {
            if (!Objects.isNull(setting) && StringUtils.isNotEmpty(setting.getCfgValue())) {
                return StorageFactory
                    .getStorage(objectMapper.readValue(setting.getCfgValue(), StorageConfig.class));
            }
        } catch (Exception e) {
            log.error("Create storage Exception: {}", e.getMessage(), e);
        }
        return new NoneStorage();
    }

}
