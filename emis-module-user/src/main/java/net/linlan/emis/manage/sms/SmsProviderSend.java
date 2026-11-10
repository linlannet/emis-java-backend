/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.manage.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.linlan.emis.uext.constant.SmsProvider;

/**
 * 短信发送
 *
 * @author Linlan
 */
public interface SmsProviderSend {

    Logger log = LoggerFactory.getLogger(SmsProviderSend.class);

    /**
     * 发送短信验证码
     * 如果是腾讯云，params要用LinkedHashMap，保证顺序
     *
     * @param request {@link SendSmsRequest}
     * @return {@link SmsResponse}
     */
    SmsResponse send(SendSmsRequest request);

    /**
     * 服务商类型
     *
     * @return {@link SmsProvider}
     */
    SmsProvider getProvider();
}
