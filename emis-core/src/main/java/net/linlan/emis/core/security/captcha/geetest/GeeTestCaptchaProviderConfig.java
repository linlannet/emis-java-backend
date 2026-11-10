/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.captcha.geetest;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.core.security.captcha.CaptchaProviderConfig;

/**
 * 极速验证码
 * @author Linlan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GeeTestCaptchaProviderConfig extends CaptchaProviderConfig {

    private static final long serialVersionUID = -8631010419770301772L;
    /**
     * 验证码ID
     */
    @NotEmpty(message = "验证码ID不能为空")
    private String            captchaId;

    /**
     * 验证码KEY
     */
    @NotEmpty(message = "验证码KEY不能为空")
    private String            captchaKey;
}
