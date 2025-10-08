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
package net.linlan.emis.idsource.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 身份源提供商
 *
 * @author Linlan
 *
 */
public enum IdentitySourceProvider implements BaseEnumString {
                                                              /**
                                                               * 钉钉身份源
                                                               */
                                                              DINGTALK("dingtalk", "钉钉",
                                                                       "钉钉（Ding Talk）是阿里巴巴集团打造的企业级智能移动办公平台，是数字经济时代的企业组织协同办公和应用开发平台。"),

                                                              /**
                                                               * 企业微信
                                                               */
                                                              WECHAT_WORK("wechat_work", "企业微信",
                                                                          "企业微信是腾讯微信团队打造的企业通讯与办公工具，具有与微信一致的沟通体验，丰富的OA应用，和连接微信生态的能力，可帮助企业连接内部、连接生态伙伴、连接消费者。专业协作、安全管理、人即服务。"),
                                                              /**
                                                               * 飞书
                                                               */
                                                              FEISHU("feishu", "飞书",
                                                                     "飞书是字节跳动于2016年自研的新一代一站式协作平台，是保障字节跳动全球数万人高效协作的办公工具。飞书将即时沟通、日历、云文档、云盘和工作台深度整合，通过开放兼容的平台，让成员在一处即可实现高效的沟通和流畅的协作，全方位提升企业效率。"),

                                                              /**
                                                               * AD
                                                               */
                                                              WINDOWS_AD("windows-ad", "Windows AD",
                                                                         "使用Windows AD身份源同步数据"),

                                                              /**
                                                               * LDAP
                                                               */
                                                              LDAP("ldap", "LDAP",
                                                                   "LDAP 是轻量目录访问协议，你可以理解为一个树型结构，用来存储用户和组织信息的数据库");

    /**
     * 键
     */
    @JsonValue
    private final String key;
    /**
     * 值
     */
    private final String value;
    /**
     * 名称
     */
    private final String name;

    IdentitySourceProvider(String key, String name, String value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    @EnumConvert
    public static IdentitySourceProvider getType(String key) {
        IdentitySourceProvider[] values = values();
        for (IdentitySourceProvider value : values) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
