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
package net.linlan.emis.openapi.endpoint;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.openapi.pojo.result.OrganizationChildResult;
import net.linlan.emis.openapi.pojo.result.OrganizationResult;
import net.linlan.emis.openapi.pojo.save.OrganizationCreateParam;
import net.linlan.emis.openapi.pojo.update.OrganizationUpdateParam;
import net.linlan.emis.openapi.service.OrganizationService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import static net.linlan.emis.openapi.constant.OpenApiConstants.ORGAN_PATH;

/**
 * 系统账户-组织机构
 *
 * @author Linlan
 * Created on 2020/7/11 21:18
 */
@Validated
@Tag(name = "组织机构管理")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ORGAN_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    /**
     * 创建组织机构
     *
     * @param param {@link OrganizationCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @PostMapping(value = "/create")
    @Audit(type = EventType.CREATE_ORG)
    @Operation(summary = "创建组织")
    public ResponseResult<Boolean> createOrganization(@RequestBody @Validated OrganizationCreateParam param) {
        organizationService.createOrgan(param);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 修改组织机构
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改组织")
    @Audit(type = EventType.UPDATE_ORG)
    @PutMapping(value = "/update")
    public ResponseResult<Boolean> updateOrganization(@RequestBody @Validated OrganizationUpdateParam param) {
        organizationService.updateOrgan(param);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 删除组织机构
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除组织")
    @Audit(type = EventType.DELETE_ORG)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseResult<Boolean> deleteOrganization(@PathVariable(value = "id") String id) {
        organizationService.deleteOrgan(id);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 启用组织
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用组织")
    @PutMapping(value = "/enable/{id}")
    public ResponseResult<Boolean> enableOrganization(@PathVariable(value = "id") String id) {
        organizationService.updateStatus(id, Boolean.TRUE);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 禁用组织
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用组织")
    @PutMapping(value = "/disable/{id}")
    public ResponseResult<Boolean> disableOrganization(@PathVariable(value = "id") String id) {
        organizationService.updateStatus(id, Boolean.FALSE);
        return ResponseResult.ok(Boolean.TRUE);
    }

    /**
     * 根据父ID查询子组织树
     *
     * @return {@link List}
     */
    @GetMapping(value = "/{parentId}/lst")
    @Operation(summary = "根据父ID查询子组织列表")
    public ResponseResult<List<OrganizationChildResult>> getChildOrganization(@PathVariable(value = "parentId") String parentId) {
        List<OrganizationChildResult> list = organizationService.getChildOrganization(parentId);
        return ResponseResult.ok(list);
    }

    /**
     * 根据ID查询组织
     *
     * @param id {@link String}
     * @return {@link OrganizationResult}
     */
    @Operation(summary = "根据组织ID查询组织")
    @GetMapping(value = "/{id}")
    public ResponseResult<OrganizationResult> getOrganization(@PathVariable(value = "id") String id) {
        OrganizationResult result = organizationService.getOrganizationById(id);
        return ResponseResult.ok(result);
    }

    /**
     * 根据外部ID获取组织ID
     *
     * @param foreignId {@link String}
     * @return {@link String}
     */
    @Operation(summary = "根据外部ID获取组织ID")
    @GetMapping(value = "/{foreignId}/id")
    public ResponseResult<String> getOrganizationIdByForeignId(@PathVariable(value = "foreignId") String foreignId) {
        return ResponseResult.ok(organizationService.getOrganizationIdByForeignId(foreignId));
    }

    /**
     * 组织机构服务实现类
     */
    private final OrganizationService organizationService;
}
