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
package net.linlan.emis.support.userdetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * UserDetailsService
 *
 * @author Linlan
 */
public interface UserDetailsService extends
                                    org.springframework.security.core.userdetails.UserDetailsService {

    /** 修改用户密码
     * @param username 用户名
     * @param newPassword  新密码
     */
    public void changePassword(String username, String newPassword);

    /** 修改用户密码
     * @param username 用户名
     * @param oldPassword   旧密码
     * @param newPassword   新密码
     * @throws Exception    异常
     */
    public void changePassword(String username, String oldPassword,
                               String newPassword) throws Exception;

    /** 通过用户手机加载用户
     * @param mobile    用户手机号码
     * @return  用户对象
     * @throws UsernameNotFoundException    异常
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;

    /** 通过用户邮箱加载用户
     * @param email 用户邮箱
     * @return     用户对象
     * @throws UsernameNotFoundException    异常
     */
    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;
}
