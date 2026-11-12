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
package net.linlan.emis.console.service.identitysource;

import java.util.List;

import net.linlan.emis.console.pojo.other.IdentitySourceConfigValidatorParam;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceListResult;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceConfigSaveParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceCreateParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceCreateResult;
import net.linlan.emis.console.pojo.update.identity.IdentitySourceUpdateParam;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 身份源配置 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-16
 */
public interface IdentitySourceService {

    /**
     * 身份源列表
     *
     * @param query     {@link  IdentitySourceListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link List}
     */
    Page<IdentitySourceListResult> getIdentitySourceList(IdentitySourceListQuery query,
                                                         PageModel pageModel);

    /**
     * 身份源详情
     *
     * @param id {@link String}
     * @return {@link IdentitySource}
     */
    IdentitySource getIdentitySource(String id);

    /**
     * 创建身份源
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link IdentitySourceCreateResult}
     */
    IdentitySourceCreateResult createIdentitySource(IdentitySourceCreateParam param);

    /**
     * 修改身份源
     *
     * @param param {@link IdentitySourceUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updateIdentitySource(IdentitySourceUpdateParam param);

    /**
     * 禁用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableIdentitySource(String id);

    /**
     * 启用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableIdentitySource(String id);

    /**
     * 删除身份源
     *
     * @param id {@link  String}
     * @return {@link  Boolean}
     */
    Boolean deleteIdentitySource(String id);

    /**
     * 保存身份源配置
     *
     * @param param {@link  IdentitySourceConfigSaveParam}
     * @return {@link  Boolean}
     */
    Boolean saveIdentitySourceConfig(IdentitySourceConfigSaveParam param);

    /**
     * 更新身份源策略
     *
     * @param id             {@link Long} 主键
     * @param strategyConfig {@link String} 策略
     */
    void updateStrategyConfig(String id, String strategyConfig);

    /**
     * 身份源配置验证
     *
     * @param param {@link  IdentitySourceConfigValidatorParam}
     * @return {@link  Boolean}
     */
    Boolean identitySourceConfigValidator(IdentitySourceConfigValidatorParam param);
}
