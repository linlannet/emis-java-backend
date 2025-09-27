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

import lombok.Getter;

import net.linlan.emis.support.manage.enums.EnumConvert;
import net.linlan.emis.uext.entity.MessageCategory;
import net.linlan.utils.enums.BaseEnumString;

/**
 * 短信类型
 *
 * @author Linlan
 */
public enum SmsType implements BaseEnumString {
                                               /**
                                                * 绑定手机号
                                                */
                                               BIND_MOBILE("bind_mobile", "绑定手机号",
                                                           MessageCategory.CODE),
                                               /**
                                                 * 绑定，修改手机号成功
                                                 */
                                               BIND_MOBILE_SUCCESS("bind_mobile_success", "绑定手机号成功",
                                                                   MessageCategory.CODE),
                                               /**
                                                * 修改绑定手机号
                                                */
                                               UPDATE_MOBILE("update_mobile", "修改手机号",
                                                             MessageCategory.CODE),
                                               /**
                                                * 忘记密码
                                                */
                                               FORGET_PASSWORD("forget_password", "忘记密码",
                                                               MessageCategory.CODE),
                                               /**
                                                * 修改密码
                                                */
                                               UPDATE_PASSWORD("update_password", "修改密码",
                                                               MessageCategory.CODE),
                                               /**
                                                * 重置密码
                                                */
                                               RESET_PASSWORD("reset_password", "重置密码",
                                                              MessageCategory.NOTICE),
                                               /**
                                                * 重置密码成功
                                                */
                                               RESET_PASSWORD_SUCCESS("reset_password_success",
                                                                      "重置密码成功",
                                                                      MessageCategory.NOTICE),
                                               /**
                                                * 登录验证
                                                */
                                               LOGIN("login", "登录验证", MessageCategory.CODE),

                                               /**
                                                * 欢迎短信
                                                */
                                               WELCOME_SMS("welcome_sms", "欢迎短信",
                                                           MessageCategory.NOTICE),

                                               /**
                                                * 密码过期提醒
                                                */
                                               PASSWORD_SOON_EXPIRED_REMIND("password_soon_expired_remind",
                                                                            "密码过期提醒",
                                                                            MessageCategory.NOTICE),
                                               /**
                                                * 再次验证
                                                */
                                               AGAIN_VERIFY("again_verify", "再次验证提醒",
                                                            MessageCategory.NOTICE),
                                               /**
                                                * 警示提醒短信
                                                */
                                               WARNING("warning", "警示提醒短信", MessageCategory.NOTICE),

    ;

    /**
     * 键
     */
    @JsonValue
    private final String          key;
    /**
     * 值
     */
    private final String          value;
    /**
     * 短信类型
     */
    @Getter
    private final MessageCategory category;

    SmsType(String key, String value, MessageCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    /**
     * 获取类型
     *
     * @param key {@link String}
     * @return {@link SmsType}
     */
    @EnumConvert
    public static SmsType getType(String key) {
        SmsType[] values = values();
        for (SmsType status : values) {
            if (String.valueOf(status.getKey()).equals(key)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.key;
    }

}
