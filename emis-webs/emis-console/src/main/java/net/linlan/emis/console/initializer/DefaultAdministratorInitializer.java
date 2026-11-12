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
package net.linlan.emis.console.initializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.StringUtils;

import net.linlan.emis.console.repository.AdministratorRepository;
import net.linlan.emis.organ.entity.EmisAdminVo;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.service.UserOrganRelationService;
import net.linlan.emis.support.manage.init.AbstractSystemInitializer;
import net.linlan.emis.support.manage.init.InitializationException;
import net.linlan.emis.user.constant.UserStatus;
import static net.linlan.emis.support.constant.EmisConstants.*;

/**
 * DefaultAdministratorInitialize
 *
 * @author Linlan
 * Created on  2022/11/26 21:44
 */
@Component
public class DefaultAdministratorInitializer extends AbstractSystemInitializer {

    private final Logger        logger                      = LoggerFactory
        .getLogger(DefaultAdministratorInitializer.class);
    private static final String DIR_NAME                    = ".linlan";
    private static final String USER_HOME                   = "user.home";
    private static final String INITIAL_PASSWORD_VALUE_NAME = "initial.password.value";
    // generate: 自动生成，setting: 读取[INITIAL_PASSWORD_VALUE]设置的固定值，没有设置使用[INITIAL_PASSWORD_DEFAULT]的默认值
    private static final String INITIAL_PASSWORD_TYPE_NAME  = "initial.password.type";
    private static final String INITIAL_PASSWORD_DEFAULT    = "linlan.net";

    //    @Override
    //    @Transactional(rollbackFor = Exception.class)
    //    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
//        //@formatter:off
//        String traceId = idGenerator.generateId().toString();
//        TraceUtils.put(traceId);
//        RLock lock = redissonClient.getLock(getLockKeyPrefix());
//        boolean tryLock = false;
//        try {
//            SecurityContextHolder.getContext().setAuthentication(EMIS_INIT_AUTHENTICATION);
//            tryLock = lock.tryLock(1, TimeUnit.SECONDS);
//             if (tryLock){
//                 Optional<EmisAdminVo> optional = administratorRepository.findByUsername(DEFAULT_ADMIN_USERNAME);
//                 if (!optional.isPresent()) {
//                     String initPassword;
//                     String initPasswordFileTips  ;
//                     String passwordType = System.getProperty(INITIAL_PASSWORD_TYPE_NAME);
//                     if (StringUtils.hasText(passwordType) && "generate".equals(passwordType)) {
//                         initPassword = idGenerator.generateId().toString().replace("-", "").toLowerCase(Locale.ENGLISH);
//                     }
//                     else {
//                         String passwordInitial = System.getProperty(INITIAL_PASSWORD_VALUE_NAME);
//                         if (StringUtils.hasText(passwordInitial)) {
//                             initPassword = passwordInitial;
//                         }
//                         else {
//                             initPassword = INITIAL_PASSWORD_DEFAULT;
//                         }
//                     }
//                     String initialAdminPasswordFilePath = getInitialAdminPasswordFilePath();createFile(initialAdminPasswordFilePath);
//                     BufferedWriter stream = new BufferedWriter(new FileWriter(initialAdminPasswordFilePath));
//                     initPasswordFileTips = "This may also be found at: " + initialAdminPasswordFilePath;
//                     //清空
//                     stream.write(initPassword);
//                     stream.flush();
//                     stream.close();
//                     logger.info(" EMIS console initial setup is required. An admin user has been created and a initialize password. Please use the following password to proceed to installation: %s %s".format(initPassword, initPasswordFileTips));
//                     //保存管理员
//                     saveInitAdministrator(DEFAULT_ADMIN_USERNAME, initPassword);
//                 }
//             }
//
//        } catch (Exception exception) {
//            int exitCode = SpringApplication.exit(event.getApplicationContext(),
//                    () -> 0);
//            System.exit(exitCode);
//        } finally {
//            if (tryLock && lock.isLocked()) {
//                lock.unlock();
//            }
//            TraceUtils.remove();
//            SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
//        }
//        //@formatter:on
    //    }

    @Override
    public void init() throws InitializationException {
        try {
            Optional<EmisAdminVo> optional = administratorRepository
                .findByUsername(DEFAULT_ADMIN_USERNAME);
            if (!optional.isPresent()) {
                String initPassword;
                String initPasswordFileTips;
                String passwordType = System.getProperty(INITIAL_PASSWORD_TYPE_NAME);
                if (StringUtils.hasText(passwordType) && "generate".equals(passwordType)) {
                    initPassword = idGenerator.generateId().toString().replace("-", "")
                        .toLowerCase(Locale.ENGLISH);
                } else {
                    String passwordInitial = System.getProperty(INITIAL_PASSWORD_VALUE_NAME);
                    if (StringUtils.hasText(passwordInitial)) {
                        initPassword = passwordInitial;
                    } else {
                        initPassword = INITIAL_PASSWORD_DEFAULT;
                    }
                }
                String initialAdminPasswordFilePath = getInitialAdminPasswordFilePath();
                FileUtils.writeStringToFile(new File(initialAdminPasswordFilePath), initPassword,
                    "UTF-8");
                BufferedWriter stream = new BufferedWriter(
                    new FileWriter(initialAdminPasswordFilePath));
                initPasswordFileTips = "This may also be found at: " + initialAdminPasswordFilePath;
                //清空
                stream.write(initPassword);
                stream.flush();
                stream.close();
                logger.info(
                    "EMIS console initial setup is required. An admin administrator has been created and a initialize password. Please use the following password to proceed to installation: %s  %s ",
                    initPassword, initPasswordFileTips);
                //保存管理员
                EmisAdminVo administrator = new EmisAdminVo();
                administrator.setUsername(DEFAULT_ADMIN_USERNAME);
                administrator.setFullName("管理员");
                administrator.setPassword(passwordEncoder.encode(initPassword));
                administrator.setStatus(UserStatus.ENABLED);
                administrator.setNeedChangePassword(true);
                administrator.setRemark(
                    "This administrator is automatically created during system initialization.");
                administratorRepository.save(administrator);
                UserOrganRelation member = new UserOrganRelation();
                member.setOrganId(ROOT_NODE);
                member.setUserId(administrator.getAdminId());
                userOrganRelationService.save(member);
            }
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }

    /**
     * 保存管理员
     *
     * @param username {@link String}
     * @param password {@link String}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveInitAdministrator(String username, String password) {
        EmisAdminVo administrator = new EmisAdminVo();
        administrator.setUsername(username);
        administrator.setPassword(passwordEncoder.encode(password));
        administrator.setStatus(UserStatus.ENABLED);
        administrator.setNeedChangePassword(true);
        administrator.setRemark(
            "This administrator user is automatically created during system initialization.");
        administratorRepository.save(administrator);
    }

    public static String addSeparator(String dir) {
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        return dir;
    }

    /**
     * 获取初始化管理员密码文件路径
     *
     * @return {@link String}
     */
    public static String getInitialAdminPasswordFilePath() {
        String path = addSeparator(System.getProperty(USER_HOME)) + DIR_NAME + File.separator
                      + "secrets" + File.separator;
        return path + "initialAdminPassword";
    }

    private final AlternativeJdkIdGenerator    idGenerator = new AlternativeJdkIdGenerator();

    private final AdministratorRepository      administratorRepository;

    private final UserOrganRelationService     userOrganRelationService;

    private final PasswordEncoder              passwordEncoder;

    private final RedissonClient               redissonClient;

    public DefaultAdministratorInitializer(AdministratorRepository administratorRepository,
                                           UserOrganRelationService userOrganRelationService,
                                           PasswordEncoder passwordEncoder,
                                           RedissonClient redissonClient) {
        this.administratorRepository = administratorRepository;
        this.userOrganRelationService = userOrganRelationService;
        this.passwordEncoder = passwordEncoder;
        this.redissonClient = redissonClient;
    }

    @Override
    public int getOrder() {
        return 3;
    }

}
