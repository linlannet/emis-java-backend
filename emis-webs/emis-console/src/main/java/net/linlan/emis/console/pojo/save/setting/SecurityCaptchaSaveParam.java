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
package net.linlan.emis.console.pojo.save.setting;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.emis.core.constant.CaptchaProviderType;

/**
 * 行为验证码保存入参
 *
 * @author Linlan
 * Created on  2020/10/4 19:05
 */
@Data
@Schema(description = "行为验证码保存入参")
public class SecurityCaptchaSaveParam implements Serializable {
    /**
     * 验证码提供商
     */
    @Parameter(description = "验证码提供商")
    @NotNull(message = "验证码提供商不能为空")
    private CaptchaProviderType provider;

    /**
     * 验证码配置
     */
    @Parameter(description = "验证码配置")
    @NotNull(message = "验证码配置不能为空")
    private JSONObject          config;
}
