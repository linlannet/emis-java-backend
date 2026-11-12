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

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.linlan.emis.audit.constant.TargetType;
import net.linlan.emis.audit.context.AuditContext;
import net.linlan.emis.audit.entity.Target;
import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.console.service.admin.AdministratorService;
import net.linlan.emis.core.exception.user.PasswordValidatedFailException;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.support.userdetails.UserDetails;
import net.linlan.emis.support.userdetails.UserDetailsService;

/**
 * FortressUserDetailsService
 *
 * @author Linlan
 * Created on  2020/10/25 21:41
 */
@Component(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EmisAdminVo> optional = administratorService
            .findByUsernameOrMobileOrEmail(username);
        //不存在该用户
        if (!optional.isPresent()) {
            logger.info("根据用户名、手机号、邮箱未查询该管理员【{}】", username);
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        EmisAdminVo administrator = optional.get();
        //锁定
        if (administrator.isLocked()) {
            logger.info("管理员【{}】被锁定", administrator.getUsername());
        }
        //禁用
        if (administrator.isDisabled()) {
            logger.info("管理员【{}】被禁用", administrator.getUsername());
        }
        return administratorService.getUserDetails(administrator);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        Optional<EmisAdminVo> optional = administratorRepository.findByMobile(mobile);
        //不存在该用户
        if (!optional.isPresent()) {
            logger.info("根据手机号未查询该管理员【{}】", mobile);
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return getUserDetails(optional.get());
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<EmisAdminVo> optional = administratorRepository.findByEmail(email);
        //不存在该用户
        if (!optional.isPresent()) {
            logger.info("根据邮箱未查询该管理员【{}】", email);
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return getUserDetails(optional.get());
    }

    @Override
    public void changePassword(String username, String newPassword) {
        administratorService.forceResetAdministratorPassword(username, newPassword);
    }

    @Override
    public void changePassword(String username, String oldPassword,
                               String newPassword) throws Exception {
        EmisAdminVo administrator = findByUsername(username);
        boolean matches = passwordEncoder.matches(oldPassword, administrator.getPassword());
        if (!matches) {
            logger.error("用户ID: [{}] 用户名: [{}] 旧密码匹配失败", administrator.getAdminId(),
                administrator.getUsername());
            throw new PasswordValidatedFailException();
        }
        forceResetUserPassword(administrator, newPassword);
    }

    private UserDetails getUserDetails(EmisAdminVo administrator) {
        //锁定
        if (administrator.isLocked()) {
            logger.info("用户【{}】被锁定", administrator.getUsername());
        }
        //禁用
        if (administrator.isDisabled()) {
            logger.info("用户【{}】被禁用", administrator.getUsername());
        }
        return administratorService.getUserDetails(administrator);
    }

    public void forceResetUserPassword(EmisAdminVo administrator, String password) {
        boolean matches = passwordEncoder.matches(password, administrator.getPassword());
        if (matches) {
            logger.error("用户ID: [{}] 用户名: [{}] 新密码与旧密码相同", administrator.getAdminId(),
                administrator.getUsername());
            throw new PasswordValidatedFailException("新密码不允许与旧密码相同");
        }
        password = passwordEncoder.encode(password);
        administrator.setPassword(password);
        administrator.setLastUpdatePasswordTime(LocalDateTime.now());
        administrator.setNeedChangePassword(false);
        // 更新密码
        administratorRepository.save(administrator);
        AuditContext.setTarget(Target.builder().id(administrator.getAdminId())
            .name(administrator.getUsername()).type(TargetType.USER).build());
    }

    public EmisAdminVo findByUsername(String username) throws Exception {
        return administratorRepository.findByUsername(username).orElseThrow(() -> {
            AuditContext.setContent("重置密码失败，用户不存在");
            logger.warn(AuditContext.getContent());
            return new SupportException("操作失败");
        });
    }

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    /**
     *  PasswordEncoder
     */
    private final PasswordEncoder         passwordEncoder;

    /**
     * UserService
     */
    private final AdministratorService    administratorService;

    public UserDetailsServiceImpl(AdministratorRepository administratorRepository,
                                  PasswordEncoder passwordEncoder,
                                  AdministratorService administratorService) {
        this.administratorRepository = administratorRepository;
        this.passwordEncoder = passwordEncoder;
        this.administratorService = administratorService;
    }
}
