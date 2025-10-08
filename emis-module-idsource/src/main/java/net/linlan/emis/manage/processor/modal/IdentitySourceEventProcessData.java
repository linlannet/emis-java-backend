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
package net.linlan.emis.manage.processor.modal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import net.linlan.emis.idsource.constant.IdentitySourceProvider;
import net.linlan.emis.manage.constant.IdentitySourceEventReceiveType;

/**
 * 身份源回调事件数据
 *
 * @author Linlan
 */
@Data
@AllArgsConstructor
public class IdentitySourceEventProcessData<T> implements Serializable {

    private String                         id;

    /**
     * 数据
     */
    private List<T>                        data;

    /**
     * 身份提供商
     */
    private IdentitySourceProvider         provider;

    /**
     * 时间事件
     */
    private LocalDateTime                  eventTime;

    /**
     * 事件类型
     */
    private IdentitySourceEventReceiveType eventType;

}
