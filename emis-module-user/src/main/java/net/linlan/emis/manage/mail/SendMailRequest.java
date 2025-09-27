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

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送邮件参数
 *
 * @author Linlan
 */
@Data
@Accessors(chain = true)
public class SendMailRequest {
    /**
     * 发送人
     */
    private String sender;
    /**
     * 收件人
     */
    private String receiver;
    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String body;
}
