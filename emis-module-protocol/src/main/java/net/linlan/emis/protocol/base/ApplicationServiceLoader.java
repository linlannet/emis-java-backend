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
package net.linlan.emis.protocol.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Configuration;

import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.app.service.AppInfoService;
import net.linlan.emis.protocol.base.exception.AppNotExistException;
import net.linlan.emis.protocol.base.exception.AppTemplateNotExistException;

/**
 * 应用服务加载器
 *
 * @author Linlan
 */
@Configuration
public class ApplicationServiceLoader implements ApplicationContextAware {

    private final Logger                          logger                = LoggerFactory
        .getLogger(ApplicationServiceLoader.class);

    /**
     * 用于保存接口实现类名及对应的类
     */
    private Map<String, ApplicationService>       loadMap               = new HashMap<>(16);

    private ApplicationContext                    applicationContext;

    /**
     * key: code，value：templateImpl
     */
    private final Map<String, ApplicationService> applicationServiceMap = new HashMap<>(16);

    /**
     * Set the ApplicationContext that this object runs in.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        loadMap = applicationContext.getBeansOfType(ApplicationService.class);
    }

    /**
     * 获取应用列表
     *
     * @return {@link List}
     */
    public List<ApplicationService> getApplicationServiceList() {
        return loadMap.values().stream().collect(Collectors.toList());
    }

    /**
     * 根据CODE获取应用
     *
     * @param code {@link String}
     * @return {@link ApplicationService}
     */
    public ApplicationService getApplicationService(String code) {
        ApplicationService impl = applicationServiceMap.get(code);
        if (!Objects.isNull(impl)) {
            return impl;
        }
        for (ApplicationService service : getApplicationServiceList()) {
            if (code.equals(service.getCode())) {
                applicationServiceMap.put(code, service);
                return service;
            }
        }
        throw new AppTemplateNotExistException();
    }

    /**
     * 根据应用编码获取应用服务
     *
     * @param appId {@link String}
     * @return {@link ApplicationService}
     */
    public ApplicationService getApplicationServiceByAppId(String appId) {
        AppInfoService appInfoService = applicationContext.getBean(AppInfoService.class);
        AppInfo app = appInfoService.findById(appId);
        if (Objects.isNull(app)) {
            throw new AppNotExistException();
        }
        return getApplicationService(app.getTemplate());
    }

    /**
     * 根据应用编码获取应用服务
     *
     * @param appCode {@link String}
     * @return {@link ApplicationService}
     */
    public ApplicationService getApplicationServiceByAppCode(String appCode) {
        AppInfoService appInfoService = applicationContext.getBean(AppInfoService.class);
        AppInfo app = appInfoService.findByCode(appCode);
        if (Objects.isNull(app)) {
            throw new AppNotExistException();
        }
        return getApplicationService(app.getTemplate());
    }

    public void addApplicationService(List<String> beanNameList) {
        Map<String, ApplicationService> applicationServiceMap = new HashMap<>(16);
        for (String beanName : beanNameList) {
            applicationServiceMap.put(beanName,
                applicationContext.getBean(beanName, ApplicationService.class));
        }
        loadMap.putAll(applicationServiceMap);
    }
}
