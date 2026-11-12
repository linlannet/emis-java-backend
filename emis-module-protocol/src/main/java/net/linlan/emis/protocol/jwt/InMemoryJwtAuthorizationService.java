/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.jwt;

import net.linlan.emis.protocol.base.ApplicationServiceLoader;

/**
 * 内存
 *
 * @author Linlan
 * Created on 2022/10/23
 */
public class InMemoryJwtAuthorizationService extends AbstractJwtAuthorizationService {

    public InMemoryJwtAuthorizationService(ApplicationServiceLoader applicationServiceLoader) {
        super(applicationServiceLoader);
    }

    @Override
    public void save(JwtAuthentication token) {

    }

    @Override
    public void remove(JwtAuthentication authorization) {

    }

    @Override
    public JwtAuthentication findById(String id) {
        return null;
    }

    @Override
    public JwtAuthentication findByToken(String token) {
        return null;
    }

}
