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
package net.linlan.emis.uext.entity;

import java.io.Serializable;

/**
 * 内置变量
 *
 * @author Linlan
 */
public final class MsgVariable implements Serializable {

    /**
     * 客户名称
     */
    public static final String CLIENT_NAME        = "client_name";
    /**
     * 时间点
     */
    public static final String TIME               = "time";
    /**
     * 客户描述
     */
    public static final String CLIENT_DESCRIPTION = "client_description";
    /**
     * 用户邮箱
     */
    public static final String USER_EMAIL         = "user_email";
    /**
     * 验证码
     */
    public static final String VERIFY_CODE        = "verify_code";

    /**
     * 剩余天数
     */
    public static final String EXPIRE_DAYS        = "expire_days";

    /**
     * 密码
     */
    public static final String PASSWORD           = "password";

    public static final String TEST               = "test";

    /**
     * 失效时间key
     */
    public static final String EXPIRE_TIME_KEY    = "expire_time";
    /**
     * 发送间隔时间
     */
    public static final int    TIME_TO_LIVE       = 1;
}
