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

import java.io.IOException;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import net.linlan.commons.core.StringUtils;
import net.linlan.emis.core.constant.ConfigBeanNameConstants;
import net.linlan.emis.core.encrypt.EncryptionModule;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.core.geo.GeoLocationProviderConfig;
import net.linlan.emis.core.geo.NoneGeoLocationParserImpl;
import net.linlan.emis.core.geo.constant.GeoIpProviderConstants;
import net.linlan.emis.core.geo.ip2region.Ip2RegionGeoLocationParserImpl;
import net.linlan.emis.core.geo.maxmind.MaxmindGeoLocationParserImpl;
import net.linlan.emis.core.geo.maxmind.MaxmindProviderConfig;
import net.linlan.emis.support.userdetails.geo.GeoLocationParser;
import static net.linlan.emis.support.userdetails.geo.GeoLocationProvider.IP2REGION;
import static net.linlan.emis.support.userdetails.geo.GeoLocationProvider.MAXMIND;

/**
 * 地理位置库
 *
 * @author Linlan
 */
@Configuration
@Slf4j
public class EmisGeoLocationConfiguration {

    @Resource
    private EmisSettingService baseConfigWholeService;

    @RefreshScope
    @Bean(value = ConfigBeanNameConstants.GEO_LOCATION)
    public GeoLocationParser geoLocation(RestTemplate restTemplate) {
        try {
            ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
            // 指定序列化输入的类型
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            // 查询数据库是否开启地理位置服务
            EmisSettingVo setting = baseConfigWholeService
                .getByKey(GeoIpProviderConstants.IPADDRESS_SETTING_NAME);
            if (!Objects.isNull(setting) && StringUtils.isNotBlank(setting.getContent())) {
                GeoLocationProviderConfig provider = objectMapper.readValue(setting.getContent(),
                    GeoLocationProviderConfig.class);
                // maxmind
                if (MAXMIND.equals(provider.getProvider())) {
                    return new MaxmindGeoLocationParserImpl(
                        (MaxmindProviderConfig) provider.getConfig(), restTemplate);
                }
                // ip2region
                if (IP2REGION.equals(provider.getProvider())) {
                    return new Ip2RegionGeoLocationParserImpl();
                }
            }
            //没有数据默认使用 ip2region
            return new Ip2RegionGeoLocationParserImpl();
        } catch (IOException e) {
            log.error("Create geo location Exception: {}", e.getMessage(), e);
        }
        return new NoneGeoLocationParserImpl();
    }
}
