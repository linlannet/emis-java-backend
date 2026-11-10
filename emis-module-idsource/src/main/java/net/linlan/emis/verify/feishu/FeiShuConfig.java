/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.verify.feishu;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

import net.linlan.emis.core.encrypt.JsonPropertyEncrypt;
import net.linlan.emis.manage.IdentitySourceConfig;

/**
 * 飞书配置
 *
 * @author Linlan
 *
 */
@Getter
@Setter
public class FeiShuConfig extends IdentitySourceConfig {

    private static final long serialVersionUID = -834514351515253275L;
    /**
     * 应用App key
     */
    @NotEmpty(message = "AppId不能为空")
    private String            appId;

    /**
     * 应用AppSecret
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "AppSecret不能为空")
    private String            appSecret;

    /**
     * encryptKey
     */
    @JsonPropertyEncrypt
    private String            encryptKey;

    /**
     * verificationToken
     */
    @JsonPropertyEncrypt
    private String            verificationToken;

    public FeiShuConfig() {
    }
}
