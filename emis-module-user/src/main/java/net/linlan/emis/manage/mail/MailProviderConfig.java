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
package net.linlan.emis.manage.mail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

import net.linlan.emis.core.encrypt.EncryptContextHelper;
import net.linlan.emis.core.encrypt.JsonPropertyEncrypt;
import net.linlan.emis.uext.constant.MailProvider;
import net.linlan.emis.uext.constant.MailSafetyType;

/**
 * MailProviderConfig
 *
 * @author Linlan
 */
@Data
public class MailProviderConfig {
    public MailProviderConfig() {
    }

    public MailProviderConfig(MailProvider provider, String smtpUrl, Integer port,
                              MailSafetyType safetyType, String username, String secret) {
        this.provider = provider;
        this.smtpUrl = smtpUrl;
        this.port = port;
        this.safetyType = safetyType;
        this.username = username;
        this.secret = secret;
    }

    /**
     * 平台
     */
    @NotNull(message = "平台不能为空")
    private MailProvider   provider;

    /**
     * smtp地址
     */
    @NotEmpty(message = "smtp地址不能为空")
    private String         smtpUrl;

    /**
     * 端口
     */
    @NotNull(message = "端口号不能为空")
    private Integer        port;

    /**
     * 安全验证
     */
    @NotNull(message = "安全验证方式不能为空")
    private MailSafetyType safetyType;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String         username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @JsonPropertyEncrypt
    private String         secret;

    public String getDecryptSecret() {
        return EncryptContextHelper.decrypt(this.secret);
    }
}
