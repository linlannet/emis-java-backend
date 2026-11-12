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
package net.linlan.emis.protocol.cas.configuration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.linlan.emis.protocol.cas.tickets.DefaultTicketFactory;

/**
 * @author Linlan
 * Created on  2022/12/30 01:03
 */
@Configuration
public class CasConfiguration {

    @Bean
    public DefaultTicketFactory factory() {
        final DefaultTicketFactory factory = new DefaultTicketFactory();
        factory.initialize();
        return factory;
    }

    @Bean
    public DocumentBuilder documentBuilder() throws ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

}
