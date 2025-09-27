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
package net.linlan.emis.manage.mail;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.support.constant.EmisConstants;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.uext.SettingsContextHelper;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.entity.MessageCategory;
import net.linlan.emis.uext.entity.MsgVariable;
import net.linlan.utils.SpringContextUtils;

/**
 * 邮件消息发送
 *
 * @author Linlan
 */
@Component
@Slf4j
@AllArgsConstructor
public class MailMsgEventPublish {

    /**
     * ApplicationEventPublisher
     */
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发布验证代码
     *
     * @param receiver   {@link MessageCategory} 收件人
     * @param type       {@link MailType} 邮件类型
     * @param verifyCode {@link MessageCategory} 验证码
     */
    public void publishVerifyCode(String receiver, MailType type, String verifyCode) {
        // publish event
        HashMap<String, Object> parameter = new HashMap<>(16);
        parameter.put(MsgVariable.VERIFY_CODE, verifyCode);
        parameter.put(MsgVariable.EXPIRE_TIME_KEY, SettingsContextHelper.getCodeValidTime());
        publish(type, receiver, parameter);
    }

    /**
     * 发布 邮件通知事件
     *
     * @param type     {@link MailType} 邮件类型
     * @param receiver {@link String} 接受者
     * @param param     {@link String} 参数
     */
    @SneakyThrows
    public void publish(MailType type, String receiver, Map<String, Object> param) {
        MailProviderConfig config = SettingsContextHelper.getMailProviderConfig();
        if (Objects.isNull(config)) {
            throw new SupportException("未配置邮件服务");
        }

        if (StringUtils.isBlank(receiver)) {
            log.warn("发送邮件通知失败, 接受者为空, type: {}", type);
            return;
        }
        // 时间点
        param.put(MsgVariable.TIME,
            LocalDateTime.now().format(EmisConstants.DEFAULT_DATE_TIME_FORMATTER));
        // 客户端名称
        param.put(MsgVariable.CLIENT_NAME, "EMIS 多身份集成系统");
        // 客户端描述
        param.put(MsgVariable.CLIENT_DESCRIPTION,
            "EMIS 多身份集成系统，简称：EMIS（Enterprise Multiple Identity System）， 用于管理企业内员工账号、权限、身份认证、应用访问，帮助整合部署在本地或云端的内部办公系统、业务系统及三方 SaaS 系统的所有身份，实现一个账号打通所有应用的服务。");
        // 收件人
        param.put(MsgVariable.USER_EMAIL, receiver);
        // publish event
        ObjectMapper objectMapper = SpringContextUtils.getBean(ObjectMapper.class);
        applicationEventPublisher.publishEvent(new MailMsgEvent(type, receiver, param));

    }
}
