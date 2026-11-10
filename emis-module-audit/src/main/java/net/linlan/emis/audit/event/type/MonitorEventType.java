/*
 * emis-module-audit - audit安全审计
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
package net.linlan.emis.audit.event.type;

import java.util.Arrays;
import java.util.Collections;

import net.linlan.emis.audit.event.ConsoleResource;
import net.linlan.emis.audit.event.Type;
import net.linlan.frame.comm.constant.AdminType;

/**
 * 系统监控
 *
 * @author Linlan
 */
public class MonitorEventType {

    /**
     * 下线会话
     */
    public static Type DOWN_LINE_SESSION       = new Type("emis:event:monitor:down_line_session",
        "下线会话", ConsoleResource.SESSION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
    /**
     * 批量下线会话
     */
    public static Type BATCH_DOWN_LINE_SESSION = new Type(
        "emis:event:other:batch_down_line_session", "批量下线会话", ConsoleResource.SESSION_RESOURCE,
        Collections.unmodifiableList(Arrays.asList(AdminType.ADMIN)));
}
