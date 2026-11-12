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
package net.linlan.emis.console.initializer;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.linlan.emis.core.encrypt.EncryptionModule;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.core.geo.GeoLocationProviderConfig;
import net.linlan.emis.support.manage.init.AbstractSystemInitializer;
import net.linlan.emis.support.manage.init.InitializationException;
import static net.linlan.emis.core.geo.constant.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;
import static net.linlan.emis.support.userdetails.geo.GeoLocationProvider.IP2REGION;

/**
 * GeoIpProviderInitializer
 *
 * @author Linlan
 * Created on 2024/11/3 18:11
 */
@Component
public class GeoIpProviderInitializer extends AbstractSystemInitializer {

    private final Logger logger = LoggerFactory.getLogger(GeoIpProviderInitializer.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        //@formatter:off
        try {
            EmisSettingVo optional = settingService.getByKey(IPADDRESS_SETTING_NAME);
            if (Objects.isNull(optional)) {
                logger.info("初始化系统默认IP地址提供商");
                EmisSettingVo setting = new EmisSettingVo();
                setting.setName(IPADDRESS_SETTING_NAME);
                ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                setting.setContent(objectMapper.writeValueAsString(new GeoLocationProviderConfig(IP2REGION, null)));
                setting.setDescription("The system initializes the default configuration.");
                settingService.saveSetting(setting);
            }
        } catch (JsonProcessingException e) {
            throw new InitializationException(e);
        }
        //@formatter:on
    }

    @Override
    public int getOrder() {
        return 2;
    }

    /**
     * EmisSettingService
     */
    private final EmisSettingService settingService;

    /**
     *
     * @param settingService {@link EmisSettingService}
     */
    public GeoIpProviderInitializer(EmisSettingService settingService) {
        this.settingService = settingService;
    }
}
