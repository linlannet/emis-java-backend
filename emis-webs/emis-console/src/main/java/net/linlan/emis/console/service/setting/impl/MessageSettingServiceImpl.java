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

import net.linlan.emis.console.converter.setting.MessageSettingConverter;
import net.linlan.emis.console.pojo.result.setting.EmailProviderConfigResult;
import net.linlan.emis.console.pojo.result.setting.SmsProviderConfigResult;
import net.linlan.emis.console.pojo.save.setting.MailProviderSaveParam;
import net.linlan.emis.console.pojo.save.setting.SmsProviderSaveParam;
import net.linlan.emis.console.service.setting.MessageSettingService;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.MAIL_PROVIDER_SEND;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.SMS_PROVIDER_SEND;
import static net.linlan.emis.uext.constant.MessageSettingConstants.MESSAGE_PROVIDER_EMAIL;
import static net.linlan.emis.uext.constant.MessageSettingConstants.MESSAGE_SMS_PROVIDER;

/**
 * 消息设置
 *
 * @author Linlan
 * Created on  2021/10/1 21:19
 */
@Service
public class MessageSettingServiceImpl extends SettingServiceImpl implements MessageSettingService {

    /**
     * 保存配置
     *
     * @param param {@link EmisSettingVo}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveMailProviderConfig(MailProviderSaveParam param) {
        EmisSettingVo entity = messageSettingConverter.mailProviderConfigToEntity(param);
        Boolean setting = saveSetting(entity);
        ConfigRefreshUtils.refresh(MAIL_PROVIDER_SEND);
        return setting;
    }

    /**
     * 保存邮件验证配置
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveSmsProviderConfig(SmsProviderSaveParam param) {
        EmisSettingVo entity = messageSettingConverter.smsProviderConfigToEntity(param);
        Boolean setting = saveSetting(entity);
        ConfigRefreshUtils.refresh(SMS_PROVIDER_SEND);
        return setting;
    }

    /**
     * 禁用短信验证服务
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableSmsProvider() {
        Boolean setting = removeSetting(MESSAGE_SMS_PROVIDER);
        // refresh
        ConfigRefreshUtils.refresh(SMS_PROVIDER_SEND);
        return setting;
    }

    /**
     * 禁用邮件提供商
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableMailProvider() {
        Boolean setting = removeSetting(MESSAGE_PROVIDER_EMAIL);
        // refresh
        ConfigRefreshUtils.refresh(MAIL_PROVIDER_SEND);
        return setting;
    }

    /**
     * 获取邮件提供商配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    @Override
    public EmailProviderConfigResult getMailProviderConfig() {
        EmisSettingVo entity = getSetting(MESSAGE_PROVIDER_EMAIL);
        return messageSettingConverter.entityToMailProviderConfig(entity);
    }

    /**
     * 获取短信验证服务配置
     *
     * @return {@link SmsProviderConfigResult}
     */
    @Override
    public SmsProviderConfigResult getSmsProviderConfig() {
        EmisSettingVo entity = getSetting(MESSAGE_SMS_PROVIDER);
        return messageSettingConverter.entityToSmsProviderConfig(entity);
    }

    private final MessageSettingConverter messageSettingConverter;

    public MessageSettingServiceImpl(EmisSettingService settingsRepository,
                                     MessageSettingConverter messageSettingConverter) {
        super(settingsRepository);
        this.messageSettingConverter = messageSettingConverter;
    }
}
