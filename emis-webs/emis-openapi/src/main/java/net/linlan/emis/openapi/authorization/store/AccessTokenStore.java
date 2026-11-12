/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.authorization.store;

import java.io.Serializable;
import java.util.List;

import net.linlan.emis.openapi.authorization.AccessToken;

/**
 * TokenStorage
 *
 * @author Linlan
 * Created on 2020/5/8 21:42
 */
public interface AccessTokenStore extends Serializable {

    /**
     * save
     *
     * @param token {@link AccessToken}
     */
    void save(AccessToken token);

    /**
     * 根据token删除
     *
     * @param token {@link String}
     */
    void deleteByToken(String token);

    /**
     * 根据token查询
     *
     * @param token {@link String}
     * @return {@link AccessToken}
     */
    AccessToken findByToken(String token);

    /**
     * 根据 clientId 查询
     *
     * @param clientId {@link String}
     * @return {@link List}
     */
    AccessToken findByClientId(String clientId);
}
