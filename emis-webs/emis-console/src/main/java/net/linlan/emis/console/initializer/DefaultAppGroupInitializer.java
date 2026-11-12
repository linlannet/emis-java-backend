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

import java.util.Arrays;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AlternativeJdkIdGenerator;

import net.linlan.emis.app.constant.AppDefaultGroup;
import net.linlan.emis.app.constant.AppGroupType;
import net.linlan.emis.app.entity.AppGroup;
import net.linlan.emis.app.service.AppGroupService;
import net.linlan.emis.support.manage.init.AbstractSystemInitializer;
import net.linlan.emis.support.manage.init.InitializationException;

/**
 * DefaultAppGroupInitialize
 *
 * @author Linlan
 * Created on  2023/9/11 21:44
 */
@Component
public class DefaultAppGroupInitializer extends AbstractSystemInitializer {
    //implements ApplicationListener<ApplicationReadyEvent>,
    //                                        Ordered {

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
//            if (tryLock) {
//                Arrays.stream(AppDefaultGroup.values()).collect(Collectors.toList()).forEach(i -> {
//                    Optional<AppGroup> optional = appGroupRepository.findByCode(i.getCode());
//                    if (!optional.isPresent()) {
//                        AppGroup appGroup = new AppGroup();
//                        appGroup.setCode(i.getCode());
//                        appGroup.setName(i.getDesc());
//                        appGroup.setType(AppGroupType.DEFAULT);
//                        appGroup.setRemark(
//                            "This app group is automatically created during system initialization.");
//                        appGroupRepository.save(appGroup);
//                    }
//                });
//
//            }
//
//        } catch (Exception exception) {
//            int exitCode = SpringApplication.exit(event.getApplicationContext(),
//                () -> 0);
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
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        //@formatter:off
        Arrays.stream(AppDefaultGroup.values()).forEach(i -> {
            AppGroup optional = appGroupService.findByCode(i.getKey());
            if (optional == null) {
                AppGroup appGroup = new AppGroup();
                appGroup.setCode(i.getKey());
                appGroup.setName(i.getValue());
                appGroup.setType(AppGroupType.DEFAULT.getKey());
                appGroup.setDescription(
                        "This app group is automatically created during system initialization.");
                appGroupService.save(appGroup);
            }
        });
    }

    private final AlternativeJdkIdGenerator idGenerator = new AlternativeJdkIdGenerator();

    private final AppGroupService          appGroupService;

    private final RedissonClient            redissonClient;

    public DefaultAppGroupInitializer(AppGroupService appGroupService,
                                      RedissonClient redissonClient) {
        this.appGroupService = appGroupService;
        this.redissonClient = redissonClient;
    }

    @Override
    public int getOrder() {
        return 4;
    }

}
