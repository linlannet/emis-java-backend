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
package net.linlan.emis.synchronizer.endpoint;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.idsource.service.IdentitySourceService;
import net.linlan.emis.manage.IdentitySourceConfig;
import net.linlan.emis.manage.IdentityVerifySource;
import net.linlan.emis.support.manage.trace.Trace;
import net.linlan.emis.synchronizer.configuration.IdentitySourceBeanUtils;
import static net.linlan.emis.core.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;

/**
 * 身份源回调事件端点
 *
 * @author Linlan
 * Created on 2022/9/20 21:24
 */
@Slf4j
@RestController
@RequestMapping(EVENT_RECEIVE_PATH)
@SuppressWarnings("unchecked")
public class IdentitySourceEventReceiveEndpoint {

    /**
     * 事件通知处理
     *
     * @param request {@link  HttpServletRequest}
     * @param code {@link  String}
     * @param body {@link  String}
     * @return {@link  ResponseEntity}
     */
    @Trace
    @RequestMapping(value = "/{code}")
    public ResponseEntity<?> receive(HttpServletRequest request, @PathVariable String code,
                                     @RequestBody(required = false) String body) {
        Optional<IdentitySource> optional = Optional
            .ofNullable(identitySourceService.findByCode(code));
        if (optional.isPresent()) {
            IdentitySource entity = optional.get();
            //已经启用&已经配置
            if (entity.getIsEnabled() && entity.getIsConfigured()) {
                String beanName = IdentitySourceBeanUtils.getSourceBeanName(optional.get().getId());
                IdentityVerifySource<IdentitySourceConfig> identitySource = (IdentityVerifySource<IdentitySourceConfig>) applicationContext
                    .getBean(beanName);
                Object event = identitySource.event(request, body);
                return ResponseEntity.ok(event);
            }
        }
        return ResponseEntity.ok().build();
    }

    private final ApplicationContext    applicationContext;

    private final IdentitySourceService identitySourceService;

    public IdentitySourceEventReceiveEndpoint(ApplicationContext applicationContext,
                                              IdentitySourceService identitySourceService) {
        this.applicationContext = applicationContext;
        this.identitySourceService = identitySourceService;
    }
}
