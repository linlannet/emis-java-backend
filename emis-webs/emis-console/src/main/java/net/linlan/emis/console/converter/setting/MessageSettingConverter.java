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

import org.mapstruct.Mapper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.linlan.emis.console.pojo.result.setting.EmailProviderConfigResult;
import net.linlan.emis.console.pojo.result.setting.SmsProviderConfigResult;
import net.linlan.emis.console.pojo.save.setting.MailProviderSaveParam;
import net.linlan.emis.console.pojo.save.setting.SmsProviderSaveParam;
import net.linlan.emis.core.encrypt.EncryptContextHelper;
import net.linlan.emis.core.encrypt.EncryptionModule;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.manage.mail.MailProviderConfig;
import net.linlan.emis.manage.sms.SmsProviderConfig;
import net.linlan.emis.manage.sms.aliyun.AliyunSmsProviderConfig;
import net.linlan.emis.manage.sms.qiniu.QiNiuSmsProviderConfig;
import net.linlan.emis.manage.sms.tencent.TencentSmsProviderConfig;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.emis.uext.config.SmsConfig;
import net.linlan.emis.uext.constant.MailProvider;
import net.linlan.emis.uext.constant.MailSafetyType;
import net.linlan.emis.uext.constant.SmsProvider;
import net.linlan.emis.uext.entity.MessageNoticeChannel;
import static net.linlan.emis.uext.SettingsContextHelper.getSmsProviderConfig;
import static net.linlan.emis.uext.constant.MessageSettingConstants.MESSAGE_PROVIDER_EMAIL;
import static net.linlan.emis.uext.constant.MessageSettingConstants.MESSAGE_SMS_PROVIDER;

/**
 * 消息设置转换器
 *
 * @author Linlan
 * Created on  2021/10/1 23:18
 */
@Mapper(componentModel = "spring")
public interface MessageSettingConverter {
    /**
     * 邮件提供商配置转实体类
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link EmisSettingVo}
     */
    default EmisSettingVo mailProviderConfigToEntity(MailProviderSaveParam param) {
        EmisSettingVo entity = new EmisSettingVo();
        entity.setName(MESSAGE_PROVIDER_EMAIL);
        String desc = MessageNoticeChannel.MAIL.getValue();
        //@formatter:off
        //根据提供商封装参数
        desc = desc + param.getProvider().getName();
        MailProviderConfig mailProviderConfig = buildMailProviderConfig(param);
        // 验证
        ValidationUtils.ValidationResult<?> validationResult = ValidationUtils.validateEntity(mailProviderConfig);
        if (Objects.requireNonNull(validationResult).isHasErrors()) {
            throw new ValidationException(validationResult.getMessage());
        }
        try {
            ObjectMapper objectMapper = EncryptionModule.deserializerEncrypt();
            objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
            String value = objectMapper.writeValueAsString(mailProviderConfig);
            entity.setContent(value);
        }catch (JsonProcessingException e){
            throw new SupportException("配置转换异常",e.getMessage());
        }
        entity.setDescription(desc);
        //@formatter:no
        return entity;
    }

    /**
     * 构建邮件提供商信息
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link MailProviderConfig}
     */
    default MailProviderConfig buildMailProviderConfig(MailProviderSaveParam param) {
        MailProviderConfig mailProviderConfig = new MailProviderConfig();
        mailProviderConfig.setUsername(param.getUsername());
        mailProviderConfig.setSecret(EncryptContextHelper.encrypt(param.getSecret()));
        // 封装提供商信息
        mailProviderConfig.setProvider(param.getProvider());
        if (MailProvider.CUSTOMIZE == param.getProvider()) {
            mailProviderConfig.setSmtpUrl(param.getSmtpUrl());
            mailProviderConfig.setPort(param.getPort());
            mailProviderConfig.setSafetyType(param.getSafetyType());
        }
        else {
            mailProviderConfig.setSmtpUrl(param.getProvider().getSmtpUrl());
            mailProviderConfig.setPort(param.getProvider().getSslPort());
            mailProviderConfig.setSafetyType(MailSafetyType.SSL);
        }
        return mailProviderConfig;
    }

    /**
     * 短信提供商配置转实体类
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link EmisSettingVo}
     */
    default EmisSettingVo smsProviderConfigToEntity(SmsProviderSaveParam param) {
        ValidationUtils.ValidationResult<?> validationResult = null;
        String desc = MessageNoticeChannel.SMS.getValue();
        SmsProviderConfig providerConfig = new SmsProviderConfig();
        ObjectMapper objectMapper = EncryptionModule.deserializerEncrypt();
        try {
            // 七牛云
            if (SmsProvider.QINIU.equals(param.getProvider())) {
                QiNiuSmsProviderConfig smsConfig = objectMapper.readValue(param.getConfig().toJSONString(), QiNiuSmsProviderConfig.class);
                validationResult = ValidationUtils.validateEntity(smsConfig);
                providerConfig = smsConfig;
                desc = desc + SmsProvider.QINIU.getValue();
            }
            // 阿里云
            else if (SmsProvider.ALIYUN.equals(param.getProvider())) {
                AliyunSmsProviderConfig smsConfig = objectMapper.readValue(param.getConfig().toJSONString(), AliyunSmsProviderConfig.class);
                validationResult = ValidationUtils.validateEntity(smsConfig);
                providerConfig = smsConfig;
                desc = desc + SmsProvider.ALIYUN.getValue();
            }
            // 腾讯云
            else if (SmsProvider.TENCENT.equals(param.getProvider())) {
                TencentSmsProviderConfig smsConfig = objectMapper.readValue(param.getConfig().toJSONString(), TencentSmsProviderConfig.class);
                validationResult = ValidationUtils.validateEntity(smsConfig);
                providerConfig = smsConfig;
                desc = desc + SmsProvider.TENCENT.getValue();
            }
            //判断并处理参数验证异常
            boolean hasErrors = Objects.requireNonNull(validationResult).isHasErrors();
            if (hasErrors) {
                throw new ValidationException(validationResult.getMessage());
            }
            //封装
            SmsConfig smsConfig=new SmsConfig();
            smsConfig.setConfig(providerConfig);
            smsConfig.setProvider(param.getProvider());
            smsConfig.setLanguage(param.getLanguage());
            smsConfig.setTemplates(param.getTemplates());
            //保存
            EmisSettingVo entity = new EmisSettingVo();
            entity.setName(MESSAGE_SMS_PROVIDER);
            // 指定序列化输入的类型（这里不要移动到上面，只有序列化时才使用）
            String value = objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY).writeValueAsString(smsConfig);
            entity.setContent(value);
            entity.setDescription(desc);
            return entity;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实体转邮件提供商配置
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link EmailProviderConfigResult}
     */
    default EmailProviderConfigResult entityToMailProviderConfig(EmisSettingVo entity) {
        //@formatter:off
        try {
           //没有数据，默认未启用
           if (Objects.isNull(entity)) {
               return EmailProviderConfigResult.builder().enabled(false).build();
           }
           String config = entity.getContent();
           // 根据提供商序列化
           ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
           // 指定序列化输入的类型
           objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
           // 根据提供商序列化
           MailProviderConfig setting = objectMapper.readValue(config, MailProviderConfig.class);
           return EmailProviderConfigResult.builder()
                   .provider(setting.getProvider())
                   .port(setting.getPort())
                   .safetyType(setting.getSafetyType())
                   .username(setting.getUsername())
                   .secret(setting.getSecret())
                   .smtpUrl(setting.getSmtpUrl())
                   .enabled(true)
                   .build();
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
        //@formatter:on
    }

    /**
     * 实体转短信提供商配置
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link SmsProviderConfigResult}
     */
    default SmsProviderConfigResult entityToSmsProviderConfig(EmisSettingVo entity) {
        if (Objects.isNull(entity)) {
            return SmsProviderConfigResult.builder().enabled(false).build();
        }
        SmsConfig config = getSmsProviderConfig();
        //@formatter:off
        SmsProviderConfigResult.SmsProviderConfigResultBuilder builder = SmsProviderConfigResult.builder()
                .enabled(true)
                .language(config.getLanguage().getLocale())
                .templates(config.getTemplates())
                .provider(config.getProvider())
                .config(config.getConfig());
        //@formatter:on
        return builder.build();
    }

}
