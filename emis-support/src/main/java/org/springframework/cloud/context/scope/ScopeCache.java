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

import java.util.Collection;

/**
 * A special-purpose cache interface specifically for the {@link GenericScope} to use to
 * manage cached bean instances. Implementations generally fall into two categories: those
 * that store values "globally" (i.e. one instance per key), and those that store
 * potentially multiple instances per key based on context (e.g. via a thread local). All
 * implementations should be thread safe.
 *
 * @author Dave Syer
 *
 */
public interface ScopeCache {

    /**
     * Removes the object with this name from the cache.
     * @param name The object name.
     * @return The object removed, or null if there was none.
     */
    Object remove(String name);

    /**
     * Clears the cache and returns all objects in an unmodifiable collection.
     * @return All objects stored in the cache.
     */
    Collection<Object> clear();

    /**
     * Gets the named object from the cache.
     * @param name The name of the object.
     * @return The object with that name, or null if there is none.
     */
    Object get(String name);

    /**
     * Put a value in the cache if the key is not already used. If one is already present
     * with the name provided, it is not replaced, but is returned to the caller.
     * @param name The key.
     * @param value The new candidate value.
     * @return The value that is in the cache at the end of the operation.
     */
    Object put(String name, Object value);

}
