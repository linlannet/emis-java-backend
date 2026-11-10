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
package net.linlan.emis.support.manage.trace;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.JdkIdGenerator;
import org.springframework.web.filter.OncePerRequestFilter;

import net.linlan.commons.core.RequestIdUtils;
import net.linlan.commons.core.StringUtils;

/**
 * 为所有请求添加trace
 *
 * @author Linlan
 */
@Component
public class TraceFilter extends OncePerRequestFilter implements OrderedFilter {
    /**
     * Request Header
     */
    public static final String   HEADER_NAME_REQUEST_ID = "request-id";

    private final JdkIdGenerator jdkIdGenerator         = new JdkIdGenerator();

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param filterChain {@link FilterChain}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //先从param里取，没有的话从header里取，还没有的话再创建
        String reqId = request.getParameter(HEADER_NAME_REQUEST_ID);
        if (StringUtils.isNotEmpty(reqId)) {
            reqId = request.getHeader(HEADER_NAME_REQUEST_ID);
        }
        if (StringUtils.isBlank(reqId)) {
            //在这里将数据放入线程
            reqId = jdkIdGenerator.generateId().toString().replace("-", "");
        }
        RequestIdUtils.put(reqId);
        response.addHeader(HEADER_NAME_REQUEST_ID, reqId);
        filterChain.doFilter(request, response);
        RequestIdUtils.remove();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
