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

import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.setting.EmailProviderConfigResult;
import net.linlan.emis.console.pojo.save.setting.MailProviderSaveParam;
import net.linlan.emis.console.service.setting.MessageSettingService;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.manage.mail.MailMsgEventPublish;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.uext.constant.MailType;
import net.linlan.emis.uext.entity.MsgVariable;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_PATH;

/**
 * 消息设置
 *
 * @author Linlan
 * Created on  2020/7/24 22:36
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "邮件提供商")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH + "/message/mail_provider")
public class MailProviderController {

    /**
     * 保存邮件服务商配置
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "保存邮件服务提供商配置")
    @Audit(type = EventType.SAVE_MAIL_SERVICE)
    @PostMapping("save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveEmailProviderConfig(@RequestBody MailProviderSaveParam param) {
        Boolean result = messageSettingService.saveMailProviderConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 获取邮件服务商配置
     *
     * @return {@link ResponseResult}
     */
    @Operation(summary = "获取邮件服务提供商配置")
    @GetMapping("config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<EmailProviderConfigResult> getEmailProviderConfig() {
        return ResponseResult.ok(messageSettingService.getMailProviderConfig());
    }

    /**
     * 禁用邮件提供商
     *
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "禁用邮件服务提供商")
    @Audit(type = EventType.OFF_MAIL_SERVICE)
    @PutMapping(value = "/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableMailProvider() {
        Boolean result = messageSettingService.disableMailProvider();
        return ResponseResult.ok(result);
    }

    /**
     * 邮件发送测试
     *
     * @param mailType {@link MailType}
     * @param receiver {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "发送测试邮件")
    @GetMapping(value = "/test")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> sendMail(MailType mailType, String receiver) {
        HashMap<String, Object> map = new HashMap<>(16);
        if (mailType == MailType.UPDATE_PASSWORD || mailType == MailType.UPDATE_BIND_MAIL
            || mailType == MailType.RESET_PASSWORD) {
            map.put(MsgVariable.VERIFY_CODE, RandomStringUtils.randomAlphanumeric(6));
        }
        map.put(MsgVariable.TEST, "(TEST)");
        map.put(MsgVariable.EXPIRE_DAYS, "3");
        map.put("verify_link", PropertiesContextHelper.getPortalPublicBaseUrl());
        mailMsgEventPublish.publish(mailType, receiver, map);
        return ResponseResult.ok();
    }

    /**
     * MailMsgEventPublish
     */
    private final MailMsgEventPublish   mailMsgEventPublish;
    /**
     * MessageSettingService
     */
    private final MessageSettingService messageSettingService;
}
