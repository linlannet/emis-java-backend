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
package net.linlan.emis.verify.dingtalk;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import net.linlan.emis.core.encrypt.JsonPropertyEncrypt;
import net.linlan.emis.manage.IdentitySourceConfig;

/**
 * 钉钉配置
 *
 * @author Linlan
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class DingTalkConfig extends IdentitySourceConfig {

    private static final long serialVersionUID = 8537679521873685897L;
    /**
     * 企业 ID
     */
    private String            corpId;
    /**
     * 应用App key
     */
    @NotEmpty(message = "AppKey不能为空")
    private String            appKey;

    /**
     * 应用AppSecret
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "AppSecret不能为空")
    private String            appSecret;

    /**
     * 加密 aes key。实时同步，此参数必填。
     */
    @JsonPropertyEncrypt
    private String            aesKey;

    /**
     * 签名token
     */
    @JsonPropertyEncrypt
    private String            token;

}
