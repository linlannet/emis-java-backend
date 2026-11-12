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

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.protocol.cas.auth.CentralCacheService;

/**
 * @author Linlan
 * Created on  2022/12/29 16:25
 */
@Slf4j
@Service
public class DefaultTicketRegistry implements TicketRegistry {

    CentralCacheService cacheService;

    public DefaultTicketRegistry(CentralCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void addTicket(final Ticket ticket) {
        cacheService.save(ticket);
    }

    @Override
    public Ticket getTicket(final String ticketId) {
        return cacheService.get(ticketId);
    }

    @Override
    public <T extends Ticket> T getTicket(final String id, final Class<T> clazz) {
        Preconditions.checkNotNull(clazz, "clazz cannot be null");
        Ticket ticket = this.getTicket(id);
        if (ticket == null) {
            return null;
        }

        if (!clazz.isAssignableFrom(ticket.getClass())) {
            throw new ClassCastException("Ticket [" + ticket.getId() + " is of type "
                                         + ticket.getClass() + " when we were expecting " + clazz);
        }
        return (T) ticket;
    }

    @Override
    public void updateTicket(final Ticket ticket) {
        addTicket(ticket);
    }

    @Override
    public void deleteTicket(final String id) {
        cacheService.remove(id);
    }

}
