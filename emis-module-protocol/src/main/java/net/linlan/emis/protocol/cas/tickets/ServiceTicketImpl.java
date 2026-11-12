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
package net.linlan.emis.protocol.cas.tickets;

/**
 * @author Linlan
 * Created on  2022/12/29 16:25
 */
public class ServiceTicketImpl extends TicketGrantingTicketImpl implements ServiceTicket {
    private TicketGrantingTicket ticketGrantingTicket;
    private String               service;

    ServiceTicketImpl(final String id, final TicketGrantingTicket ticket, final String service) {
        super(id, ticket.getUserDetails());
        this.ticketGrantingTicket = ticket;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public String getService() {
        return this.service;
    }

    @Override
    public TicketGrantingTicket getTicketGrantingTicket() {
        return ticketGrantingTicket;
    }
}
