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

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.useragent.UserAgent;

/**
 * WEB认证详情类
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails
                                            implements Serializable {

    private static final long           serialVersionUID = -8631010419770301010L;

    /**
     * 地址位置相关
     */
    public GeoLocation                  geoLocation;

    /**
     * userAgent
     */
    public UserAgent                    userAgent;

    /**
     * 登录时间
     */
    public LocalDateTime                authenticationTime;

    /**
     * 认证类型
     */
    public CustomAuthenticationProvider authenticationProvider;

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public LocalDateTime getAuthenticationTime() {
        return authenticationTime;
    }

    public CustomAuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public CustomWebAuthenticationDetails() {
        super(new MockHttpServletRequest());
        this.authenticationTime = LocalDateTime.now();
    }

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     * @param request that the authentication request was received from
     */
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.authenticationTime = LocalDateTime.now();
    }

    public CustomWebAuthenticationDetails(HttpServletRequest request, UserAgent userAgent,
                                          GeoLocation geoLocation) {
        super(request);
        this.userAgent = userAgent;
        this.geoLocation = geoLocation;
        this.authenticationTime = LocalDateTime.now();
    }

}
