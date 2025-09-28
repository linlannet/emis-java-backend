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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSON;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.uext.SettingsContextHelper;
import net.linlan.emis.uext.config.SmsConfig;
import net.linlan.emis.uext.constant.SmsType;
import net.linlan.emis.uext.entity.MessageCategory;
import net.linlan.emis.uext.entity.MsgVariable;

/**
 * 短信消息发送
 *
 * @author Linlan
 */
@Component
@Slf4j
@AllArgsConstructor
public class SmsMsgEventPublish {
    public static final String TEMPLATE_CODE = "template_code";

    public static final String CONTENT       = "content";

    public static final String MOBILE        = "mobile";

    public static final String USERNAME      = "username";
    public static final String PASSWORD      = "password";

    /**
     * 发布验证代码
     *
     * @param mobile      {@link MessageCategory} 手机号
     * @param type     {@link SmsType} 消息类型
     * @param code 验证码
     */
    public void publishVerifyCode(String mobile, SmsType type, String code) {
        // publish event
        LinkedHashMap<String, String> parameter = new LinkedHashMap<>(16);
        parameter.put(MsgVariable.VERIFY_CODE, code);
        parameter.put(MsgVariable.EXPIRE_TIME_KEY,
            String.valueOf(SettingsContextHelper.getCodeValidTime()));
        publish(type, mobile, parameter);
    }

    /**
     * 发布 通知事件
     *
     * @param type    {@link SmsType} 短信类型
     * @param mobile {@link String} 接收人手机号
     * @param parameter {@link Map} 参数
     */
    @SneakyThrows
    public void publish(SmsType type, String mobile, LinkedHashMap<String, String> parameter) {
        if (StringUtils.isBlank(mobile)) {
            log.warn("发送短信通知失败, 接受者为空, type: {}", type);
            return;
        }
        parameter.put(MOBILE, mobile);
        // 根据模板类型查询code
        SmsConfig smsConfig = SettingsContextHelper.getSmsProviderConfig();
        List<SmsConfig.TemplateConfig> templates = smsConfig.getTemplates();
        if (CollectionUtils.isEmpty(templates)) {
            throw new SupportException("未配置[" + type.getValue() + "]短信模板");
        }
        Optional<SmsConfig.TemplateConfig> template = templates.stream()
            .filter(item -> type == item.getType()).findFirst();
        SmsConfig.TemplateConfig templateConfig = template
            .orElseThrow(() -> new SupportException("未配置[" + type.getValue() + "]短信模板"));
        parameter.put(TEMPLATE_CODE, templateConfig.getCode());
        parameter.put(CONTENT, JSON.toJSONString(parameter));
        // publish event
        applicationEventPublisher.publishEvent(new SmsMsgEvent(type, parameter));
    }

    /**
     * ApplicationEventPublisher
     */
    private final ApplicationEventPublisher applicationEventPublisher;
}
