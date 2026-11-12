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
import net.linlan.emis.console.pojo.result.setting.SecurityDefensePolicyConfigResult;
import net.linlan.emis.console.pojo.save.setting.SecurityDefensePolicyParam;
import net.linlan.emis.console.service.setting.SecurityDefensePolicyService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_GROUP_NAME;
import static net.linlan.emis.core.constant.SettingConstants.SETTING_PATH;

/**
 * 安全防御策略
 *
 * @author Linlan
 * Created on  2023-03-09
 */
@Validated
@Tag(name = SETTING_GROUP_NAME + "-" + "安全防御策略")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH
                        + "/security/defense_policy", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityDefensePolicyController {

    /**
     * 保存安全策略
     *
     * @param param {@link SecurityDefensePolicyParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存安全防御策略")
    @Audit(type = EventType.SAVE_SECURITY_POLICY_SETTINGS)
    @PostMapping(value = "/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveSecurityDefenseStrategyConfig(@RequestBody @Validated SecurityDefensePolicyParam param) {
        Boolean result = securityDefenseStrategyService.saveSecurityDefensePolicyConfig(param);
        return ResponseResult.ok(result);
    }

    /**
     * 获取安全策略
     *
     * @return {@link SecurityDefensePolicyConfigResult}
     */
    @Operation(summary = "获取安全策略")
    @GetMapping(value = "/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<SecurityDefensePolicyConfigResult> getSecurityPolicyConfig() {
        SecurityDefensePolicyConfigResult result = securityDefenseStrategyService
            .getSecurityPolicyConfig();
        return ResponseResult.ok(result);
    }

    /**
     * SecurityDefenseStrategy
     */
    private final SecurityDefensePolicyService securityDefenseStrategyService;
}
