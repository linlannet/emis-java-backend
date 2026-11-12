/*
 * emis-portal - portal门户平台
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
package net.linlan.emis.portal.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.core.context.PropertiesContextHelper;
import net.linlan.emis.support.utils.UrlUtils;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.utils.http.HttpContextUtils.isHtmlRequest;

/**
 * 注销成功
 *
 * @author Linlan
 * Created on  2020/9/2 22:11
 */
public class PortalLogoutSuccessHandler implements
                                        org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        //@formatter:off
        boolean isHtmlRequest = isHtmlRequest(request);
        if (response.isCommitted()) {
            return;
        }
        if (!isHtmlRequest) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                    ResponseResult.ok());
            return;
        }
        response.sendRedirect(UrlUtils.format(PropertiesContextHelper.getPortalLoginUrl()));
        //@formatter:on
    }
}
