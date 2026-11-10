/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.security.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import net.linlan.emis.support.userdetails.geo.GeoLocationParser;
import net.linlan.emis.support.userdetails.useragent.UserAgentParser;
import net.linlan.utils.ip.IPUtils;

/**
 * 基于框架底层进行授权自定义扩展
 */
public class CustomWebAuthenticationDetailsSource implements
                                                  AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
    private final GeoLocationParser geoLocationParser;
    private final UserAgentParser   userAgentParser;

    public CustomWebAuthenticationDetailsSource(GeoLocationParser geoLocationParser,
                                                UserAgentParser userAgentParser) {
        this.geoLocationParser = geoLocationParser;
        this.userAgentParser = userAgentParser;
    }

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new CustomWebAuthenticationDetails(request, userAgentParser.getUserAgent(request),
            geoLocationParser.getGeoLocation(IPUtils.getIpAddr(request)));
    }
}
