/*
 * emis-module-provider - provider身份提供商
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
package net.linlan.emis.authn.gitee;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.manage.client.IdentityProviderConfig;

/**
 * Gitee 登录
 *
 * @author Linlan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GiteeIdentityProviderOAuth2Config extends IdentityProviderConfig {
    private static final long serialVersionUID = -8631010419770301783L;
    /**
     * 客户端id
     */
    @NotBlank(message = "应用clientId不能为空")
    private String            clientId;

    /**
     * 客户端Secret
     */
    @NotBlank(message = "应用clientSecret不能为空")
    private String            clientSecret;
}
