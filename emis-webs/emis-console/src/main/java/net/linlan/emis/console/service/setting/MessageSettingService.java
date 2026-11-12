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
package net.linlan.emis.console.service.setting;

import net.linlan.emis.console.pojo.result.setting.EmailProviderConfigResult;
import net.linlan.emis.console.pojo.result.setting.SmsProviderConfigResult;
import net.linlan.emis.console.pojo.save.setting.MailProviderSaveParam;
import net.linlan.emis.console.pojo.save.setting.SmsProviderSaveParam;

/**
 * 消息设置接口
 *
 * @author Linlan
 * Created on  2021/10/1 21:19
 */
public interface MessageSettingService extends SettingService {
    /**
     * 保存配置
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveMailProviderConfig(MailProviderSaveParam param);

    /**
     * 保存邮件验证配置
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveSmsProviderConfig(SmsProviderSaveParam param);

    /**
     * 禁用短信验证服务
     *
     * @return {@link Boolean}
     */
    Boolean disableSmsProvider();

    /**
     * 禁用邮件提供商
     *
     * @return {@link Boolean}
     */
    Boolean disableMailProvider();

    /**
     * 获取邮件提供商配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    EmailProviderConfigResult getMailProviderConfig();

    /**
     * 获取短信验证服务配置
     *
     * @return {@link SmsProviderConfigResult}
     */
    SmsProviderConfigResult getSmsProviderConfig();
}
