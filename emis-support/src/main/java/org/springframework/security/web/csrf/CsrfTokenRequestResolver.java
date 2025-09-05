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

import javax.servlet.http.HttpServletRequest;

/**
 * Implementations of this interface are capable of resolving the token value of a
 * {@link CsrfToken} from the provided {@code HttpServletRequest}. Used by the
 * {@link CsrfFilter}.
 *
 * @author Steve Riesenberg
 * @since 5.8
 */
@FunctionalInterface
public interface CsrfTokenRequestResolver {

    /**
     * Returns the token value resolved from the provided {@code HttpServletRequest} and
     * {@link CsrfToken} or {@code null} if not available.
     * @param request the {@code HttpServletRequest} being processed
     * @param csrfToken the {@link CsrfToken} created by the {@link CsrfTokenRepository}
     * @return the token value resolved from the request
     */
    String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken);

}
