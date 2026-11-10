/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.savedredirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 重定向缓存类
 * @author Linlan
 */
public interface RedirectCache {
    enum RedirectType {
                       /**
                        * parameter
                        */
                       PARAMETER,
                       /**
                        * request
                        */
                       REQUEST
    }

    /**
     * EMIS_SECURITY_SAVED_REDIRECT
     */
    String EMIS_SECURITY_SAVED_REDIRECT = "EMIS_SECURITY_SAVED_REDIRECT";

    /**
     * Save Redirect
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param type {@link RedirectType}
     */
    void saveRedirect(HttpServletRequest request, HttpServletResponse response, RedirectType type);

    /**
     * Get Redirect
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {@link SavedRedirect}
     */
    SavedRedirect getRedirect(HttpServletRequest request, HttpServletResponse response);

    /**
     * Remove Redirect
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    void removeRedirect(HttpServletRequest request, HttpServletResponse response);

}
