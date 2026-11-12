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

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.setting.SecurityBasicConfigResult;
import net.linlan.emis.console.pojo.result.setting.SecurityCaptchaConfigResult;
import net.linlan.emis.console.pojo.result.setting.SecurityMfaConfigResult;
import net.linlan.emis.console.pojo.save.setting.SecurityBasicSaveParam;
import net.linlan.emis.console.pojo.save.setting.SecurityCaptchaSaveParam;
import net.linlan.emis.console.pojo.save.setting.SecurityMfaSaveParam;
import net.linlan.emis.console.service.setting.SecuritySettingService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_PATH;

/**
 * 安全设置
 *
 * @author Linlan
 * Created on  2020/10/4 20:37
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "安全设置")
@RestController
@RequestMapping(value = SETTING_PATH + "/security", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SecurityBasicController {

    /**
     * 获取高级配置
     *
     * @return {@link SecurityBasicConfigResult}
     */
    @Operation(summary = "获取基础配置")
    @GetMapping(value = "/basic/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<SecurityBasicConfigResult> getBasicConfig() {
        SecurityBasicConfigResult result = securitySettingService.getBasicConfig();
        return ResponseResult.ok(result);
    }

    /**
     * 保存高级配置
     *
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存基础配置")
    @Audit(type = EventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)
    @PostMapping(value = "/basic/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> save(@Validated @RequestBody SecurityBasicSaveParam param) {
        boolean result = securitySettingService.saveBasicConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 获取MFA配置
     *
     * @return {@link SecurityMfaConfigResult}
     */
    @Operation(summary = "获取MFA配置")
    @GetMapping(value = "/mfa/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<SecurityMfaConfigResult> getMfaConfig() {
        SecurityMfaConfigResult result = securitySettingService.getMfaConfig();
        return ResponseResult.ok(result);
    }

    /**
     * 保存MFA配置
     *
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存MFA配置")
    @Audit(type = EventType.SAVE_MFA_SETTINGS)
    @PostMapping(value = "/mfa/save")
    @Validated
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveMfaConfig(@RequestBody SecurityMfaSaveParam param) {
        boolean result = securitySettingService.saveMfaConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 获取行为验证码配置
     *
     * @return {@link SecurityCaptchaConfigResult}
     */
    @Operation(summary = "获取行为验证码配置")
    @GetMapping(value = "/captcha/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<SecurityCaptchaConfigResult> getCaptchaProviderConfig() {
        SecurityCaptchaConfigResult result = securitySettingService.getCaptchaProviderConfig();
        return ResponseResult.ok(result);
    }

    /**
     * 保存行为验证码配置
     *
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存行为验证码配置")
    @Audit(type = EventType.SAVE_CAPTCHA_PROVIDER)
    @PostMapping(value = "/captcha/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveCaptchaProviderConfig(@Validated @RequestBody SecurityCaptchaSaveParam param) {
        boolean result = securitySettingService.saveCaptchaProviderConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用行为验证码
     *
     * @return {@link ResponseResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "禁用行为验证码")
    @Audit(type = EventType.OFF_MAIL_SERVICE)
    @PutMapping(value = "/captcha/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableCaptchaProvider() {
        Boolean result = securitySettingService.disableCaptchaProvider();
        return ResponseResult.ok(result);
    }

    /**
     * SecuritySettingService
     */
    private final SecuritySettingService securitySettingService;
}
