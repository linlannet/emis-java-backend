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
package org.springframework.cloud.context.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A simple cache implementation backed by a concurrent map.
 *
 * @author Dave Syer
 *
 */
public class StandardScopeCache implements ScopeCache {

    private final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

    public Object remove(String name) {
        return this.cache.remove(name);
    }

    public Collection<Object> clear() {
        Collection<Object> values = new ArrayList<Object>(this.cache.values());
        this.cache.clear();
        return values;
    }

    public Object get(String name) {
        return this.cache.get(name);
    }

    public Object put(String name, Object value) {
        Object result = this.cache.putIfAbsent(name, value);
        if (result != null) {
            return result;
        }
        return value;
    }

}
