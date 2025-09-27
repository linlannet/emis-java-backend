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
package net.linlan.emis.authn.github;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.manage.client.IdentityProviderConfig;

/**
 * GITHUB 认证配置
 *
 * @author Linlan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GithubIdentityProviderOAuth2Config extends IdentityProviderConfig {
    private static final long serialVersionUID = -8631010419770301784L;

    /**
     * Client ID
     */
    @NotBlank(message = "Client ID 不能为空")
    private String            clientId;

    /**
     * Client Secret
     */
    @NotBlank(message = "Client Secret 不能为空")
    private String            clientSecret;
}
