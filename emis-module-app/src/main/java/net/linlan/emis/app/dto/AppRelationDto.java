/*
 * emis-module-app - app应用管理
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
package net.linlan.emis.app.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.JdkIdGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

import net.linlan.emis.app.constant.AppProtocol;
import net.linlan.emis.app.constant.AppType;
import net.linlan.emis.app.constant.AuthorizationType;
import net.linlan.emis.app.entity.AppInfo;
import net.linlan.emis.support.userdetails.ApplicationGroup;

/**
 * 应用账户po
 *
 * @author Linlan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppRelationDto extends AppInfo {

    /**
     * 应用分组
     */
    private List<ApplicationGroup> group;

    public AppRelationDto(String id, String name, String code, String clientId, String clientSecret,
                          String template, AppProtocol protocol, AppType type, String icon,
                          String initLoginUrl, AuthorizationType authorizationType, Boolean enabled,
                          String groupIds) {
        super.setId(id);
        super.setName(name);
        super.setCode(code);
        super.setClientId(clientId);
        super.setClientSecret(clientSecret);
        super.setTemplate(template);
        super.setProtocol(protocol.getKey());
        super.setType(type.getKey());
        super.setIcon(icon);
        super.setInitLoginUrl(initLoginUrl);
        super.setAuthorizationType(authorizationType.getKey());
        super.setIsEnabled(enabled);
        if (StringUtils.isNotBlank(groupIds)) {
            this.group = new ArrayList<>();
            for (String groupId : groupIds.split(",")) {
                this.group.add(new ApplicationGroup(groupId, id,
                    new JdkIdGenerator().generateId().toString()));
            }
        }
    }
}
