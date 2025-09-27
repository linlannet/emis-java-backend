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

import java.util.Date;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.core.exception.uext.SmsMessageSendException;
import net.linlan.emis.uext.entity.MessageCategory;
import net.linlan.emis.uext.entity.SmsSendRecord;
import net.linlan.emis.uext.service.SmsSendRecordService;

/**
 * 短信消息通知事件
 *
 * @author Linlan
 */
@Async
@Slf4j
@Component
public class SmsMsgEventListener implements ApplicationListener<SmsMsgEvent> {

    /**
     * SmsSend
     */
    @Resource
    private SmsProviderSend      smsProviderSend;
    @Resource
    private SmsSendRecordService smsSendRecordService;

    /**
     * 发送事件通知
     *
     * @param event {@link SmsMsgEvent}
     */
    @Override
    public void onApplicationEvent(@NonNull SmsMsgEvent event) {
        SendSmsRequest smsParam = new SendSmsRequest();
        try {
            //@formatter:off
            // 手机号
            smsParam.setMobile(event.getParameter().get(SmsMsgEventPublish.MOBILE));
            // 模版编码
            smsParam.setTemplate(event.getParameter().get(SmsMsgEventPublish.TEMPLATE_CODE));
            // Content 记录参数值
            String content = event.getParameter().get(SmsMsgEventPublish.CONTENT);
            // 移除手机号，模版编码和Content
            event.getParameter().remove(SmsMsgEventPublish.MOBILE);
            event.getParameter().remove(SmsMsgEventPublish.TEMPLATE_CODE);
            event.getParameter().remove(SmsMsgEventPublish.CONTENT);
            // 短信模版参数
            smsParam.setParameters(event.getParameter());

            //@formatter:on
            SmsResponse send = smsProviderSend.send(smsParam);
            // 保存发送记录
            if (!Objects.isNull(send)) {
                //@formatter:off
                SmsSendRecord record = new SmsSendRecord();
                record.setContent(content);
                record.setReturnInfo(send.getMessage());
                record.setSendStatus(send.getSuccess().toString());
                record.setSendTime(new Date());
                record.setProvider(send.getProvider().getKey());
                record.setBizSubtype(MessageCategory.CODE.getKey());
                record.setBizType(event.getType().getKey());
                record.setReceiverMobile(smsParam.getMobile());
                record.setDescription(JSON.toJSONString(send));
                if (!send.getSuccess()) {
                    log.error("发送短信失败: params: {}, response: {}", smsParam, send);
                }
                //@formatter:on
                smsSendRecordService.save(record);
            } else {
                log.error("发送短信失败,返回值为空: params: {}, ", smsParam);
            }
        } catch (Exception e) {
            log.error("发送短信消息异常 params:{}, error: {}", smsParam, e.getMessage());
            throw new SmsMessageSendException(e);
        }
    }

}
