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

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.setting.PasswordPolicyConfigResult;
import net.linlan.emis.console.pojo.result.setting.WeakPasswordLibListResult;
import net.linlan.emis.console.pojo.save.setting.PasswordPolicySaveParam;
import net.linlan.emis.console.service.setting.PasswordPolicyService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import static net.linlan.emis.core.constant.SettingConstants.SECURITY_PATH;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;

/**
 * 密码策略配置 Controller
 *
 * @author Linlan
 * Created on  2023/05/05 21:18
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "密码策略配置")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = SECURITY_PATH
                        + "/password_policy", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityPasswordPolicyController {

    /**
     * 密码策略配置
     *
     * @return {@link List}
     */
    @GetMapping(value = "/config")
    @Operation(summary = "获取密码策略")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<PasswordPolicyConfigResult> config() {
        PasswordPolicyConfigResult result = passwordPolicyService.getPasswordPolicyConfig();
        return ResponseResult.ok(result);
    }

    /**
     * 保存密码策略
     *
     * @return {@link List}
     */
    @Lock
    @Preview
    @PostMapping(value = "/save")
    @Audit(type = EventType.SAVE_PASSWORD_POLICY)
    @Operation(summary = "保存密码策略")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> save(@Validated @RequestBody PasswordPolicySaveParam param) {
        boolean result = passwordPolicyService.savePasswordPolicyConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 系统弱密码库
     *
     * @return {@link List}
     */
    @GetMapping(value = "/weak_password_lib")
    @Operation(summary = "获取系统弱密码库")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<WeakPasswordLibListResult>> getWeakPasswordLibList() {
        List<WeakPasswordLibListResult> result = passwordPolicyService.getWeakPasswordLibList();
        return ResponseResult.ok(result);
    }

    /**
     * 密码策略实现类
     */
    private final PasswordPolicyService passwordPolicyService;
}
