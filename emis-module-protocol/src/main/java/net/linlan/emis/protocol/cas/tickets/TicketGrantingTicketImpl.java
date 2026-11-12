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

import java.util.Objects;

import net.linlan.emis.protocol.cas.util.TicketUtils;
import net.linlan.emis.support.userdetails.UserDetails;
import static net.linlan.emis.protocol.cas.constant.CasProtocolConstants.PREFIX_ST;

/**
 * @author Linlan
 * Created on  2022/12/29 16:25
 */
public class TicketGrantingTicketImpl implements TicketGrantingTicket {
    private final String      id;
    private final long        createTime;

    private final UserDetails userDetails;

    TicketGrantingTicketImpl(final String id, final UserDetails userDetails) {
        this.id = id;
        this.userDetails = userDetails;
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    public UserDetails getUserDetails() {
        return this.userDetails;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public synchronized ServiceTicket grantServiceTicket(final String service) {
        return new ServiceTicketImpl(TicketUtils.generateTicket(PREFIX_ST), this, service);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) obj;
        return Objects.equals(this.id, ticket.getId())
               && Objects.equals(this.createTime, ticket.getCreateTime());
    }

    @Override
    public int hashCode() {
        return id.hashCode() + Long.hashCode(createTime);
    }
}
