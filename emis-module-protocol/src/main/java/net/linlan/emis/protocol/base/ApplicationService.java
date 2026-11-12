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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import net.linlan.emis.app.constant.AppProtocol;
import net.linlan.emis.app.constant.AppType;
import net.linlan.emis.app.constant.AuthorizationType;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.entity.AppInfo;

/**
 * 应用接口
 *
 * @author Linlan
 */
public interface ApplicationService {

    /**
     * 获取应用标志
     *
     * @return {@link String}
     */
    String getCode();

    /**
     * 获取应用名称
     *
     * @return {@link String}
     */
    String getName();

    /**
     * 获取应用描述
     *
     * @return {@link String}
     */
    String getDescription();

    /**
     * 获取应用类型
     *
     * @return {@link AppType}
     */
    AppType getType();

    /**
     * 获取应用协议
     *
     * @return {@link AppProtocol}
     */
    AppProtocol getProtocol();

    /**
     * 获取表单Schema
     *
     * @return {@link Object}
     */
    default Object getFormSchema() {
        return null;
    }

    /**
     * 获取base64图标
     *
     * @return {@link String}
     */
    String getBase64Icon();

    /**
     * 创建应用
     *
     * @param name     {@link String} 名称
     * @param icon     {@link String} 图标
     * @param remark   {@link String} 备注
     * @return {@link String} 应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    default String create(String name, String icon, String remark) {
        return create(name, icon, remark, new ArrayList<>());
    }

    /**
     * 创建应用
     *
     * @param name     {@link String} 名称
     * @param icon     {@link String} 图标
     * @param remark   {@link String} 备注
     * @param groups {@link String} 应用分组
     * @return {@link String} 应用ID
     */
    @Transactional(rollbackFor = Exception.class)
    String create(String name, String icon, String remark, List<String> groups);

    /**
     * 删除应用
     *
     * @param appId {@link String} 应用ID
     */
    void delete(String appId);

    /**
     * 更新应用配置
     *
     * @param appId {@link String}
     * @param config {@link Map}
     */
    @Transactional(rollbackFor = Exception.class)
    void saveConfig(String appId, Map<String, Object> config);

    /**
     * 获取配置
     *
     * @param appId {@link String}
     * @return {@link Object}
     */
    Object getConfig(String appId);

    /**
     * 获取默认应用用户信息
     *
     * @param appId {@link String}
     * @param userId {@link String}
     * @return {@link AppAccount}
     */
    AppAccount getDefaultAppAccount(String appId, String userId);

    /**
     * 创建应用
     *
     * @param name {@link String}
     * @param icon  {@link String}
     * @param remark  {@link String}
     * @param authorizationType {@link AuthorizationType}
     * @return {@link AppInfo}
     */
    AppInfo createApp(String name, String icon, String remark, AuthorizationType authorizationType);

    /**
     * 创建应用
     *
     * @param name {@link String}
     * @param icon  {@link String}
     * @param remark  {@link String}
     * @param groups {@link String} 应用分组
     * @param authorizationType {@link AuthorizationType}
     * @return {@link AppInfo}
     */
    AppInfo createApp(String name, String icon, String remark, List<String> groups,
                      AuthorizationType authorizationType);
}
