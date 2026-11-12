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
package net.linlan.emis.console.converter.setting;

import java.util.Objects;

import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.linlan.emis.console.pojo.result.setting.EmailProviderConfigResult;
import net.linlan.emis.console.pojo.result.setting.GeoIpProviderResult;
import net.linlan.emis.console.pojo.save.setting.GeoIpProviderSaveParam;
import net.linlan.emis.console.pojo.save.setting.MailProviderSaveParam;
import net.linlan.emis.core.encrypt.EncryptionModule;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.core.geo.GeoLocationProviderConfig;
import net.linlan.emis.core.geo.maxmind.MaxmindProviderConfig;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.utils.SpringContextUtils;
import static net.linlan.emis.core.geo.constant.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;
import static net.linlan.emis.core.geo.maxmind.MaxmindGeoLocationParserImpl.SHA256_URL;
import static net.linlan.emis.support.userdetails.geo.GeoLocationProvider.MAXMIND;

/**
 * 地理位置设置转换器
 *
 * @author Linlan
 * Created on  2021/10/1 23:18
 */
@Mapper(componentModel = "spring")
public interface GeoLocationSettingConverter {

    Logger log = LoggerFactory.getLogger(GeoLocationSettingConverter.class);

    /**
     * 地理位置配置转实体类
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link EmisSettingVo}
     */
    default EmisSettingVo geoLocationProviderConfigToEntity(GeoIpProviderSaveParam param) {
        ObjectMapper objectMapper = EncryptionModule.serializerEncrypt();
        // 指定序列化输入的类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        try {
            EmisSettingVo entity = new EmisSettingVo();
            entity.setName(IPADDRESS_SETTING_NAME);
            String desc = null;
           //@formatter:off
           //根据提供商封装参数
           if (MAXMIND.getName().equals(param.getProvider())) {
               desc = MAXMIND.getName();
               MaxmindProviderConfig maxmindProviderConfig = param.getConfig().to(MaxmindProviderConfig.class);
               ValidationUtils.ValidationResult<?> validationResult = ValidationUtils.validateEntity(maxmindProviderConfig);
               entity.setContent(objectMapper.writeValueAsString(new GeoLocationProviderConfig(MAXMIND, maxmindProviderConfig)));
               // 验证
               if (Objects.requireNonNull(validationResult).isHasErrors()) {
                   throw new ValidationException(validationResult.getMessage());
               }
               try {
                   ResponseEntity<String> checkConnect = SpringContextUtils.getBean(RestTemplate.class).getForEntity(
                           String.format(SHA256_URL,
                                   maxmindProviderConfig.getSessionKey()), String.class);
                   HttpStatus statusCode = checkConnect.getStatusCode();
                   if (statusCode.isError()) {
                       log.error("MAXMIND调用失败:[{}]", checkConnect);
                       throw new SupportException("注册码错误或连接异常");
                   }
               }
               catch (Exception e) {
                   log.error("MAXMIND调用异常: [{}]", e.getMessage());
                   throw new SupportException("注册码错误或连接异常");
               }
           }
           entity.setDescription(desc);
           //@formatter:no
           return entity;
       }catch (JsonProcessingException e){
           throw  new RuntimeException(e);
       }
    }

    /**
     * 实体转地理位置提供商配置
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link EmailProviderConfigResult}
     */
    default GeoIpProviderResult entityToGeoLocationProviderConfig(EmisSettingVo entity) {
        //没有数据，默认未启用
        if (Objects.isNull(entity) || StringUtils.isBlank(entity.getContent())) {
            return null;
        }
        try {
            String value = entity.getContent();
            ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
            // 指定序列化输入的类型
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            // 根据提供商序列化
            GeoLocationProviderConfig setting = objectMapper.readValue(value, GeoLocationProviderConfig.class);
            if (MAXMIND.equals(setting.getProvider())) {
                MaxmindProviderConfig config = (MaxmindProviderConfig) setting.getConfig();
                //@formatter:off
                return GeoIpProviderResult.builder()
                        .provider(setting.getProvider().getName())
                        .config(config)
                        .enabled(true)
                        .build();
            }
            //@formatter:on
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
