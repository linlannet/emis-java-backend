/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.service.account.userdetail;

import org.springframework.stereotype.Service;

import net.linlan.emis.console.service.admin.AdministratorService;
import net.linlan.emis.support.security.session.RefreshCurrentSessionPrincipalService;
import net.linlan.emis.support.userdetails.UserDetails;

/**
 * 刷新当前用户会话 Principal 实现
 *
 * @author Linlan
 * Created on  2024/3/2 14:06
 */
@Service
public class RefreshCurrentSessionPrincipalServiceImpl implements
                                                       RefreshCurrentSessionPrincipalService {
    /**
     * 获取 Principal
     *
     * @param userId {@link String}
     * @return {@link UserDetails}
     */
    @Override
    public UserDetails getPrincipal(String userId) {
        return administratorService.getUserDetails(userId);
    }

    private final AdministratorService administratorService;

    public RefreshCurrentSessionPrincipalServiceImpl(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }
}
