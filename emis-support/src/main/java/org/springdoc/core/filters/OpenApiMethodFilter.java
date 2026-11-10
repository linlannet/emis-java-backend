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
package org.springdoc.core.filters;

import java.lang.reflect.Method;

/**
 * Implement and register a bean of type {@link OpenApiMethodFilter} to
 * conditionally include any detected methods in default OpenAPI descriptions
 * but not groups
 *
 * @author michael.clarke
 * @see  GlobalOpenApiMethodFilter filter methods in default OpenAPI      description and groups
 */
@FunctionalInterface
public interface OpenApiMethodFilter {

    /**
     * Whether the given method should be included in the generated OpenApi definitions. Only methods from classes
     * detected by the relevant loader will be passed to this filter; it cannot be used to load methods that are not
     * annotated with `RequestMethod` or similar mechanisms. Methods that are rejected by this filter will not be
     * processed any further, although methods accepted by this filter may still be rejected by other checks, such as
     * package inclusion checks so may still be excluded from the final OpenApi definition.
     *
     * @param method the method to perform checks against
     * @return whether this method should be used for further processing
     */
    boolean isMethodToInclude(Method method);

}
