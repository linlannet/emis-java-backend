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

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.console.converter.app.AppCertConverter;
import net.linlan.emis.console.pojo.query.app.AppCertQuery;
import net.linlan.emis.console.pojo.result.app.AppCertListResult;
import net.linlan.emis.console.service.app.AppCertService;

/**
 * 应用证书
 *
 * @author Linlan
 * Created on  2022/5/31 21:46
 */
@Service
@AllArgsConstructor
public class AppCertServiceImpl implements AppCertService {

    /**
     * 获取应用证书列表
     *
     * @param query {@link AppCertQuery}
     * @return {@link List}
     */
    @Override
    public List<AppCertListResult> getAppCertListResult(AppCertQuery query) {
        List<AppCertification> list = appCertificationService.getList(
            new net.linlan.commons.script.json.StringMap().put("appId", query.getAppId()).map());
        return appCertConverter.entityConvertToAppCertListResult(list);
    }

    private final net.linlan.emis.app.service.AppCertificationService appCertificationService;

    private final AppCertConverter  appCertConverter;
}
