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

import org.springdoc.core.fn.RouterOperation;
import org.springframework.web.method.HandlerMethod;

/**
 * Implement and register a bean of type {@link RouterOperationCustomizer} to customize an router operation
 * based on the handler method input on default OpenAPI descriptions but not groups
 *
 * @author hyeonisism
 */
@FunctionalInterface
public interface RouterOperationCustomizer {

    /**
     * Customize router operation.
     *
     * @param routerOperation input operation
     * @param handlerMethod original handler method
     * @return customized router operation
     */
    RouterOperation customize(RouterOperation routerOperation, HandlerMethod handlerMethod);

}
