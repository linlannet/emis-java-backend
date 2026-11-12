/*
 * emis-openapi - enterprise multiple identity system
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
package net.linlan.emis.openapi.service;

import java.util.List;

import net.linlan.emis.openapi.pojo.result.OrganizationChildResult;
import net.linlan.emis.openapi.pojo.result.OrganizationResult;
import net.linlan.emis.openapi.pojo.save.OrganizationCreateParam;
import net.linlan.emis.openapi.pojo.update.OrganizationUpdateParam;

/**
 * <p>
 * 组织机构 服务类
 * </p>
 *
 * @author Linlan
 * Created on 2020-08-09
 */
public interface OrganizationService {

    /**
     * 创建组织机构
     *
     * @param param {@link OrganizationCreateParam}
     */
    void createOrgan(OrganizationCreateParam param);

    /**
     * 修改组织机构
     *
     * @param param {@link OrganizationUpdateParam}
     */
    void updateOrgan(OrganizationUpdateParam param);

    /**
     * 启用/禁用
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    void updateStatus(String id, boolean enabled);

    /**
     * 删除组织机构
     *
     * @param id {@link List}
     * @return {@link Boolean}
     */
    void deleteOrgan(String id);

    /**
     * 根据ID查询组织机构
     *
     * @param id {@link String}
     * @return {@link OrganizationResult}
     */
    OrganizationResult getOrganizationById(String id);

    /**
     * 查询子组织
     *
     * @param parentId {@link String}
     * @return {@link OrganizationChildResult}
     */
    List<OrganizationChildResult> getChildOrganization(String parentId);

    /**
     * 根据外部ID获取组织ID
     *
     * @param foreignId {@link String}
     * @return {@link String}
     */
    String getOrganizationIdByForeignId(String foreignId);
}
