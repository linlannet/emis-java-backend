/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.cas.auth;

import net.linlan.emis.protocol.cas.tickets.ServiceTicket;
import net.linlan.emis.protocol.cas.tickets.Ticket;
import net.linlan.emis.protocol.cas.tickets.TicketGrantingTicket;
import net.linlan.emis.support.userdetails.UserDetails;

/**
 * @author Linlan
 * Created on  2023/1/2 23:43
 */
public interface CentralAuthenticationService {
    TicketGrantingTicket createTicketGrantingTicket(UserDetails userDetails, String sessionId);

    ServiceTicket grantServiceTicket(String tgtId, String service);

    <T extends Ticket> T getTicket(String id, Class<T> clazz);

    ServiceTicket validateServiceTicket(String id, String service);

    void destroyTicketGrantingTicket(String var1);
}
