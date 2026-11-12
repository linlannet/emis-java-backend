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
package net.linlan.emis.console.service.app.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import net.linlan.emis.app.constant.AppType;
import net.linlan.emis.console.pojo.result.app.AppTemplateResult;
import net.linlan.emis.console.service.app.AppTemplateService;
import net.linlan.emis.protocol.base.ApplicationService;
import net.linlan.emis.protocol.base.ApplicationServiceLoader;

/**
 * ApplicationTemplateServiceImpl
 *
 * @author Linlan
 * Created on  2020/11/29 22:23
 */
@Service
@AllArgsConstructor
public class AppTemplateServiceImpl implements AppTemplateService {

    /**
     * List
     *
     * @param type {@link AppType}
     * @return {@link List}
     */
    @Override
    public List<AppTemplateResult> getAppTemplateList(AppType type, String name) {
        List<AppTemplateResult> results = new ArrayList<>();
        List<ApplicationService> list = applicationServiceLoader.getApplicationServiceList();
        if (StringUtils.isNotBlank(name)) {
            list = list.stream()
                .filter(applicationService -> applicationService.getName().contains(name))
                .collect(Collectors.toList());
        }
        for (ApplicationService protocol : list) {
            if (protocol.getType().equals(type)) {
                AppTemplateResult result = new AppTemplateResult();
                result.setProtocol(protocol.getProtocol());
                result.setCode(protocol.getCode());
                result.setDesc(protocol.getDescription());
                result.setIcon(protocol.getBase64Icon());
                result.setName(protocol.getName());
                result.setType(protocol.getType());
                results.add(result);
            }
        }
        return results;
    }

    /**
     * List
     *
     * @param code {@link Map}
     * @return {@link List}
     */
    @Override
    public Object getAppTemplateFormSchema(String code) {
        ApplicationService applicationService = applicationServiceLoader
            .getApplicationService(code);
        if (!Objects.isNull(applicationService)) {
            return applicationService.getFormSchema();
        }
        return new ArrayList<>();
    }

    private final ApplicationServiceLoader applicationServiceLoader;

}
