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
package net.linlan.emis.support.security.jackjson;

import java.util.List;

import org.springframework.security.jackson2.SecurityJackson2Modules;

import com.fasterxml.jackson.databind.Module;

/**
 * 通过加载器获取安全的启动模块
 */
public class SupportJackson2Modules {

    public static List<Module> getModules(ClassLoader loader) {
        return SecurityJackson2Modules.getModules(loader);
    }

}
