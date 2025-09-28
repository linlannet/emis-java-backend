/*
 * emis-module-authn - authn认证模块
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
package net.linlan.emis.authn.alipay;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.manage.client.IdentityProviderConfig;

/**
 * 支付宝 登录配置
 *
 * @author Linlan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlipayIdentityProviderOAuth2Config extends IdentityProviderConfig {

    /**
     * 商户ID
     */
    @NotBlank(message = "商户ID不能为空")
    private String appId;

    /**
     * 应用私钥
     */
    @NotBlank(message = "应用私钥")
    private String appPrivateKey;

    /**
     * 支付宝公钥
     */
    @NotBlank(message = "支付宝公钥")
    private String alipayPublicKey;

}
