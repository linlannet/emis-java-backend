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
package net.linlan.emis.manage.event;

/**
     * 身份源配置事件类型
     *
     * @author Linlan
     */
public enum IdentitySourceEventType {
                                     /**
                                      * 注册
                                      */
                                     REGISTER,
                                     /**
                                      * 销毁
                                      */
                                     DESTROY,

                                     /**
                                      * 同步
                                      */
                                     SYNC
}