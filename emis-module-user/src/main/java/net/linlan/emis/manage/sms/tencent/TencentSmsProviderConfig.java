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
package net.linlan.emis.manage.sms.tencent;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.core.encrypt.JsonPropertyEncrypt;
import net.linlan.emis.manage.sms.SmsProviderConfig;

/**
 * 验证码提供商配置
 *
 * @author Linlan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TencentSmsProviderConfig extends SmsProviderConfig {
    public TencentSmsProviderConfig() {
    }

    /**
     * secretId
     */
    @NotEmpty(message = "SecretId不能为空")
    private String secretId;

    /**
     * secretKey
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "SecretKey不能为空")
    private String secretKey;

    /**
     * 短信应用ID
     */
    @NotEmpty(message = "短信应用ID不能为空")
    private String sdkAppId;

    /**
     * 短信签名内容
     */
    @NotEmpty(message = "短信签名内容不能为空")
    private String signName;

    /**
     * Region
     */
    @NotEmpty(message = "Region不能为空")
    private String region;
}
