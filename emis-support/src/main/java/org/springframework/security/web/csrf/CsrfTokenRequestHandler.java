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
package org.springframework.security.web.csrf;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;

/**
 * A callback interface that is used to make the {@link CsrfToken} created by the
 * {@link CsrfTokenRepository} available as a request attribute. Implementations of this
 * interface may choose to perform additional tasks or customize how the token is made
 * available to the application through request attributes.
 *
 * @author Steve Riesenberg
 * @since 5.8
 */
@FunctionalInterface
public interface CsrfTokenRequestHandler extends CsrfTokenRequestResolver {

    /**
     * Handles a request using a {@link CsrfToken}.
     * @param request the {@code HttpServletRequest} being handled
     * @param response the {@code HttpServletResponse} being handled
     * @param csrfToken the {@link CsrfToken} created by the {@link CsrfTokenRepository}
     */
    void handle(HttpServletRequest request, HttpServletResponse response,
                Supplier<CsrfToken> csrfToken);

    @Override
    default String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(csrfToken, "csrfToken cannot be null");
        String actualToken = request.getHeader(csrfToken.getHeaderName());
        if (actualToken == null) {
            actualToken = request.getParameter(csrfToken.getParameterName());
        }
        return actualToken;
    }

}
