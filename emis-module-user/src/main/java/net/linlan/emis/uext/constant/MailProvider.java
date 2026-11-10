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
package net.linlan.emis.uext.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 邮件提供商配置
 *
 * @author Linlan
 */
public enum MailProvider implements BaseEnumString {

                                                    /**
                                                     * 自定义
                                                     */
                                                    CUSTOMIZE("customize", "自定义", null, null, null),
                                                    /**
                                                     * 腾讯企业邮
                                                     */
                                                    TENCENT("tencent", "腾讯企业邮",
                                                            "smtp.exmail.qq.com", 25, 465),
                                                    /**
                                                     * 阿里企业邮
                                                     */
                                                    ALIYUN("aliyun", "阿里企业邮", "smtp.mxhichina.com",
                                                           25, 465),
                                                    /**
                                                     * 网易
                                                     */
                                                    NETEASE("netease", "网易企业邮", "smtp.163.com", 25,
                                                            465);

    /**
     * 键
     */
    @JsonValue
    private final String  key;
    /**
     * 值
     */
    private final String  value;
    /**
     * smtp 地址
     */
    private final String  smtpUrl;
    /**
     * 端口
     */
    private final Integer port;
    /**
     * SSL 端口
     */
    private final Integer sslPort;

    MailProvider(String key, String value, String smtpUrl, Integer port, Integer sslPort) {
        this.key = key;
        this.value = value;
        this.smtpUrl = smtpUrl;
        this.port = port;
        this.sslPort = sslPort;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return null;
    }

    public String getName() {
        return value;
    }

    @EnumConvert
    public static MailProvider getType(String key) {
        MailProvider[] values = values();
        for (MailProvider status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该平台");
    }

    public String getSmtpUrl() {
        return smtpUrl;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getSslPort() {
        return sslPort;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
