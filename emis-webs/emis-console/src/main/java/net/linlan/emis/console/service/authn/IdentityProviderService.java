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
package net.linlan.emis.console.service.authn;

import java.util.List;

import net.linlan.emis.console.pojo.query.authn.IdentityProviderListQuery;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderCreateResult;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderListResult;
import net.linlan.emis.console.pojo.result.authn.IdentityProviderResult;
import net.linlan.emis.console.pojo.save.authn.IdentityProviderCreateParam;
import net.linlan.emis.console.pojo.update.authn.IdentityProviderUpdateParam;
import net.linlan.emis.provider.entity.IdentityProvider;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;

/**
 * <p>
 * 身份认证源配置 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-16
 */
public interface IdentityProviderService {
    /**
     * 平台是否启用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean identityProviderIsEnable(String id);

    /**
     * 通过平台类型获取
     *
     * @param provider {@link String}
     * @return {@link IdentityProvider}
     */
    List<IdentityProvider> getByIdentityProvider(String provider);

    /**
     * 认证源列表
     *
     * @param pageModel {@link PageModel }
     * @param query     {@link IdentityProviderListQuery }
     * @return {@link List}
     */
    Page<IdentityProviderListResult> getIdentityProviderList(PageModel pageModel,
                                                             IdentityProviderListQuery query);

    /**
     * 认证源详情
     *
     * @param id {@link String}
     * @return {@link IdentityProviderResult}
     */
    IdentityProviderResult getIdentityProvider(String id);

    /**
     * 保存认证源
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link IdentityProviderCreateResult}
     */
    IdentityProviderCreateResult createIdp(IdentityProviderCreateParam param);

    /**
     * 更改认证源状态
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    Boolean updateIdentityProviderStatus(String id, Boolean enabled);

    /**
     * 更新身份源
     *
     * @param param {@link IdentityProviderUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updateIdentityProvider(IdentityProviderUpdateParam param);

    /**
     * 删除认证源
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    Boolean deleteIdentityProvider(String id);
}
