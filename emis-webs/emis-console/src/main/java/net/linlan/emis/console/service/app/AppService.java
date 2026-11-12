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
package net.linlan.emis.console.service.app;

import java.util.Map;

import net.linlan.emis.console.pojo.query.app.AppQuery;
import net.linlan.emis.console.pojo.result.app.AppCreateResult;
import net.linlan.emis.console.pojo.result.app.AppGetResult;
import net.linlan.emis.console.pojo.result.app.AppListResult;
import net.linlan.emis.console.pojo.save.app.AppCreateParam;
import net.linlan.emis.console.pojo.update.app.AppSaveConfigParam;
import net.linlan.emis.console.pojo.update.app.AppUpdateParam;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 应用管理 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-07-31
 */
public interface AppService {

    /**
     * 获取应用（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppQuery}
     * @return {@link AppListResult}
     */
    Page<AppListResult> getAppList(PageModel pageModel, AppQuery query);

    /**
     * 创建应用
     *
     * @param param {@link AppCreateParam}
     * @return {@link AppCreateResult}
     */
    AppCreateResult createApp(AppCreateParam param);

    /**
     * 修改应用
     *
     * @param param {@link AppUpdateParam}
     * @return {@link Boolean}
     */
    boolean updateApp(AppUpdateParam param);

    /**
     * 删除应用
     *
     * @param id {@link  Long}
     * @return {@link Boolean}
     */
    boolean deleteApp(String id);

    /**
     * 获取单个应用详情
     *
     * @param id {@link Long}
     * @return {@link AppGetResult}
     */
    AppGetResult getApp(String id);

    /**
     * 启用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableApp(String id);

    /**
     * 禁用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableApp(String id);

    /**
     *  更新应用配置
     *
     * @param param {@link AppSaveConfigParam}
     * @return {@link Boolean}
     */
    Boolean saveAppConfig(AppSaveConfigParam param);

    /**
     * 获取应用配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    Object getAppConfig(String appId);
}
