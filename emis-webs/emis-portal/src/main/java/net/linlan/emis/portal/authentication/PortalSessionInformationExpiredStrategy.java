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

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;

import com.alibaba.fastjson2.JSONObject;

import net.linlan.commons.core.ResponseResult;
import net.linlan.utils.http.HttpResponseUtils;
import static net.linlan.emis.support.exception.enums.ExceptionStatus.EX000203;

/**
 * session 过期
 *
 * @author Linlan
 * Created on  2021/7/25 21:08
 */
public class PortalSessionInformationExpiredStrategy implements
                                                     org.springframework.security.web.session.SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) {
        HttpServletResponse response = event.getResponse();
        ResponseResult<String> result = new ResponseResult(EX000203.getKey(), EX000203.getValue());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        HttpResponseUtils.flushResponse(response, JSONObject.toJSONString(result));
    }
}
