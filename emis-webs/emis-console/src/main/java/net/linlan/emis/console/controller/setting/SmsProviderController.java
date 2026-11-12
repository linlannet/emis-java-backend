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
package net.linlan.emis.console.controller.setting;

import java.util.LinkedHashMap;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.setting.SmsProviderConfigResult;
import net.linlan.emis.console.pojo.save.setting.SmsProviderSaveParam;
import net.linlan.emis.console.service.setting.MessageSettingService;
import net.linlan.emis.manage.sms.SmsMsgEventPublish;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.security.password.PasswordGenerator;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.constant.SmsType;
import net.linlan.emis.uext.entity.MessageCategory;
import net.linlan.emis.uext.entity.MsgVariable;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_PATH;
import static net.linlan.emis.manage.sms.SmsMsgEventPublish.USERNAME;
import static net.linlan.emis.uext.entity.MsgVariable.EXPIRE_DAYS;

/**
 * 消息设置
 *
 * @author Linlan
 * Created on  2020/7/24 22:36
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "短信提供商")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH + "/message/sms_provider")
public class SmsProviderController {
    /**
     * 禁用短信提供商
     *
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "禁用短信提供商")
    @Audit(type = EventType.OFF_SMS_SERVICE)
    @PutMapping(value = "/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableSmsProvider() {
        Boolean result = messageSettingService.disableSmsProvider();
        return ResponseResult.ok(result);
    }

    /**
     * 保存短信提供商配置
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "保存短信提供商配置")
    @Audit(type = EventType.SAVE_SMS_SERVICE)
    @PostMapping("save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveSmsProviderConfig(@RequestBody SmsProviderSaveParam param) {
        Boolean result = messageSettingService.saveSmsProviderConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 获取短信提供商配置
     *
     * @return {@link ResponseResult}
     */
    @Operation(summary = "获取短信提供商配置")
    @GetMapping("/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<SmsProviderConfigResult> getSmsProviderConfig() {
        SmsProviderConfigResult result = messageSettingService.getSmsProviderConfig();
        return ResponseResult.ok(result);
    }

    /**
     * 短信发送测试
     *
     * @param smsType {@link MailType}
     * @param receiver {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "发送测试短信")
    @GetMapping(value = "/test")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> sendSms(SmsType smsType, String receiver) {
        if (MessageCategory.NOTICE.equals(smsType.getCategory())) {
            LinkedHashMap<String, String> parameter = new LinkedHashMap<>(16);
            boolean flag = SmsType.WELCOME_SMS == smsType || SmsType.RESET_PASSWORD == smsType
                           || SmsType.RESET_PASSWORD_SUCCESS == smsType;
            if (flag) {
                parameter.put(USERNAME, "test");
            }
            if (flag) {
                parameter.put(MsgVariable.PASSWORD, passwordGenerator.generatePassword());
            }
            if (SmsType.PASSWORD_SOON_EXPIRED_REMIND == smsType) {
                parameter.put(EXPIRE_DAYS, "3");
            }
            smsMsgEventPublish.publish(smsType, receiver, parameter);
        } else {
            smsMsgEventPublish.publishVerifyCode(receiver, smsType, "123456");
        }
        return ResponseResult.ok();
    }

    /**
     * MessageSettingService
     */
    private final MessageSettingService messageSettingService;

    private final SmsMsgEventPublish    smsMsgEventPublish;

    private final PasswordGenerator     passwordGenerator;
}
