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
package org.springdoc.core.customizers;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * Implement and register a bean of type {@link OpenApiCustomiser} to customize
 * Open api on default OpenAPI description but not on groups
 *
 * @author bnasslahsen
 * @see  GlobalOpenApiCustomizer customize default OpenAPI description and      groups
 */
@FunctionalInterface
public interface OpenApiCustomiser {

    /**
     * Customise.
     *
     * @param openApi the open api
     */
    void customise(OpenAPI openApi);

}
