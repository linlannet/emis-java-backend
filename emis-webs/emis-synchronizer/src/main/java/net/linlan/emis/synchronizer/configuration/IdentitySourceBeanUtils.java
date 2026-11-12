/*
 * emis-synchronizer - synchronizer同步执行器
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
package net.linlan.emis.synchronizer.configuration;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *  IdentitySourceBeanUtils
 *
 * @author Linlan
 * Created on 2022/9/21 23:22
 */
public class IdentitySourceBeanUtils {

    /**
     * 获取Bean名称
     *
     * @param id {@link String}
     * @return {@link  String}
     */
    public static String getSourceBeanName(String id) {
        return "identitySourceBean_" + DigestUtils.md5Hex(id);
    }
}
