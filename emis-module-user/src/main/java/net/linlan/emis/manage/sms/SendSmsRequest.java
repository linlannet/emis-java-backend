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
package net.linlan.emis.manage.sms;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * 短信发送入参
 *
 * @author Linlan
 */
@Data
public class SendSmsRequest implements Serializable {

    /**
     * 手机号
     */
    private String              mobile;

    /**
     * 模板
     */
    private String              template;

    /**
     * 参数
     */
    private Map<String, String> parameters;
}
