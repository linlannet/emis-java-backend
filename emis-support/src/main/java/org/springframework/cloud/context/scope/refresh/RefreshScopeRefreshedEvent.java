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
package org.springframework.cloud.context.scope.refresh;

import org.springframework.context.ApplicationEvent;

/**
 * @author Spencer Gibb
 */
@SuppressWarnings("serial")
public class RefreshScopeRefreshedEvent extends ApplicationEvent {

    /**
     * Default name for the refresh scope refreshed event.
     */
    public static final String DEFAULT_NAME = "__refreshAll__";

    private String             name;

    public RefreshScopeRefreshedEvent() {
        this(DEFAULT_NAME);
    }

    public RefreshScopeRefreshedEvent(String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
