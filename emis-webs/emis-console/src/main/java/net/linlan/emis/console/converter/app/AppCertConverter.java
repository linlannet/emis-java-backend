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
package net.linlan.emis.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import net.linlan.emis.app.entity.AppCertification;
import net.linlan.emis.console.pojo.result.app.AppCertListResult;

/**
 * 应用证书Converter
 *
 * @author Linlan
 * Created on  2022/6/4 23:52
 */
@Mapper(componentModel = "spring")
public interface AppCertConverter {

    /**
     * 实体转换为应用程序证书列表结果
     *
     * @param list {@link List}
     * @return {@link List}
     */
    default List<AppCertListResult> entityConvertToAppCertListResult(List<AppCertification> list) {
        List<AppCertListResult> results = new ArrayList<>();
        for (AppCertification cert : list) {
            results.add(entityConvertToAppCertListResult(cert));
        }
        return results;
    }

    /**
     * 实体转换为应用程序证书列表结果
     *
     * @param list {@link List}
     * @return {@link List}
     */
    AppCertListResult entityConvertToAppCertListResult(AppCertification list);
}
