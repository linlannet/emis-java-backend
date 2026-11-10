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
package net.linlan.emis.authn.dingtalk;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.manage.client.IdentityProviderConfig;

/**
 * 钉钉Oauth配置参数
 *
 * @author Linlan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkIdentityProviderOAuth2Config extends IdentityProviderConfig {
    private static final long serialVersionUID = -8631010419770301781L;

    /**
     * 应用 key
     */
    @NotBlank(message = "应用AppKey不能为空")
    private String            appKey;

    /**
     * 应用 Secret
     */
    @NotBlank(message = "应用AppSecret不能为空")
    private String            appSecret;

    /**
     * 用于指定用户需要选择的组织
     */
    private String            corpId;
}
