/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.pojo.update.user;

// import java.io.Serial;
import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.emis.uext.entity.MessageNoticeChannel;

/**
 *准备更改密码入参
 *
 * @author Linlan
 * Created on  2022/11/13 21:15
 */
@Data
@Schema(description = "准备更改密码入参")
public class PrepareChangePasswordRequest implements Serializable {

    //@Serial
    private static final long    serialVersionUID = 5681761697876754485L;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    @Parameter(description = "消息类型")
    private MessageNoticeChannel channel;
}
