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
package net.linlan.emis.uext.config;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import net.linlan.emis.core.constant.ConfigBeanNameConstants;
import net.linlan.emis.manage.mail.DefaultMailProviderSendImpl;
import net.linlan.emis.manage.mail.MailProviderConfig;
import net.linlan.emis.manage.mail.MailProviderSend;
import net.linlan.emis.manage.sms.SmsNoneProviderSend;
import net.linlan.emis.manage.sms.SmsProviderSend;
import net.linlan.emis.manage.sms.SmsSendProviderFactory;
import net.linlan.emis.uext.SettingsContextHelper;
import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

/**
 * 消息发送配置
 *
 * @author Linlan
 */
@Configuration
public class EmisMessageSendConfiguration {

    /**
     * 短信发送
     *
     * @return {@link SmsProviderSend}
     */
    @Bean(ConfigBeanNameConstants.SMS_PROVIDER_SEND)
    @RefreshScope
    public SmsProviderSend smsProviderSend() {
        //查询当前启用的短信提供商
        SmsConfig config = SettingsContextHelper.getSmsProviderConfig();
        if (Objects.isNull(config.getConfig())) {
            return new SmsNoneProviderSend();
        }
        return SmsSendProviderFactory.newInstance(config.getConfig());
    }

    /**
     * 邮件发送
     *
     * @param taskExecutor {@link TaskExecutor}
     * @return {@link MailProviderSend}
     */
    @Bean(ConfigBeanNameConstants.MAIL_PROVIDER_SEND)
    @RefreshScope
    public MailProviderSend mailProviderSend(@Qualifier(value = APPLICATION_TASK_EXECUTOR_BEAN_NAME) TaskExecutor taskExecutor) {

        MailProviderConfig config = SettingsContextHelper.getMailProviderConfig();
        if (Objects.isNull(config)) {
            return null;
        }
        return new DefaultMailProviderSendImpl(config, taskExecutor);
    }

}
