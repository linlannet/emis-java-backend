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
package net.linlan.emis.console.controller.account;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.pojo.result.account.*;
import net.linlan.emis.console.pojo.save.account.OrganizationCreateParam;
import net.linlan.emis.console.pojo.update.account.OrganizationUpdateParam;
import net.linlan.emis.console.service.account.OrganizationService;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.user.constant.AccountConstants;
import static net.linlan.emis.user.constant.AccountConstants.ACCOUNT_GROUP_NAME;

/**
 * 系统账户-组织机构
 *
 * @author Linlan
 * Created on  2020/7/11 21:18
 */
@Validated
@Tag(name = ACCOUNT_GROUP_NAME + "-" + "组织机构")
@RestController
@RequestMapping(value = AccountConstants.ORGAN_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> createOrganization(@RequestBody @Validated OrganizationCreateParam param) {
        boolean result = organizationService.createOrg(param);
        return ResponseResult.ok(result);
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateOrganization(@RequestBody @Validated OrganizationUpdateParam param) {
        boolean result = organizationService.updateOrg(param);
        return ResponseResult.ok(result);
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteOrganization(@PathVariable(value = "id") String id) {
        boolean result = organizationService.deleteOrg(id);
        return ResponseResult.ok(result);
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enableOrganization(@PathVariable(value = "id") String id) {
        boolean result = organizationService.updateStatus(id, Boolean.TRUE);
        return ResponseResult.ok(result);
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableOrganization(@PathVariable(value = "id") String id) {
        boolean result = organizationService.updateStatus(id, Boolean.FALSE);
        return ResponseResult.ok(result);
    }

    /**
     * 移动组织机构
     *
     * @param id       {@link String}
     * @param parentId {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "移动组织")
    @Audit(type = EventType.MOVE_ORGANIZATION)
    @PutMapping(value = "/move")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> moveOrganization(@RequestParam(value = "id") String id,
                                                    @RequestParam(value = "parentId") String parentId) {
        boolean result = organizationService.moveOrganization(id, parentId);
        return ResponseResult.ok(result);
    }

    /**
     * 查询根组织
     *
     * @return {@link List}
     */
    @GetMapping(value = "/get_root")
    @Operation(summary = "获取根组织")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<OrganizationRootResult> getRootOrganization() {
        OrganizationRootResult result = organizationService.getRootOrganization();
        return ResponseResult.ok(result);
    }

    /**
     * 查询子组织
     *
     * @return {@link List}
     */
    @GetMapping(value = "/get_child")
    @Operation(summary = "获取下级组织")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<OrganizationChildResult>> getChildOrganization(@RequestParam(value = "parentId") String parentId) {
        List<OrganizationChildResult> result = organizationService.getChildOrganization(parentId);
        return ResponseResult.ok(result);
    }

    /**
     * 搜索组织
     *
     * @return {@link List}
     */
    @GetMapping(value = "/search")
    @Operation(summary = "搜索组织")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<SearchOrganizationResult>> searchOrganization(@RequestParam(value = "keyWord") String keyWord) {
        List<SearchOrganizationResult> result = organizationService.searchOrganization(keyWord);
        return ResponseResult.ok(result);
    }

    /**
     * 搜索组织树结构
     *
     * @return {@link List}
     */
    @GetMapping(value = "/search/tree")
    @Operation(summary = "搜索组织-树结构")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<SearchOrganizationTreeResult>> filterOrganizationTree(@RequestParam(value = "keyWord") String keyWord) {
        List<SearchOrganizationTreeResult> result = organizationService
            .searchOrganizationTree(keyWord);
        return ResponseResult.ok(result);
    }

    /**
     * 根据ID查询组织
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取组织信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<OrganizationResult> getOrganization(@PathVariable(value = "id") String id) {
        OrganizationResult result = organizationService.getOrganization(id);
        return ResponseResult.ok(result);
    }

    /**
     * 批量获取组织信息
     *
     * @param ids {@link List}
     * @return {@link BatchOrganizationResult}
     */
    @Validated
    @Operation(summary = "批量获取组织信息")
    @GetMapping(value = "/batch_get")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<List<BatchOrganizationResult>> batchGetOrganization(@RequestParam(value = "ids", required = false) @NotNull(message = "组织ID不能为空") List<String> ids) {
        List<BatchOrganizationResult> result = organizationService.batchGetOrganization(ids);
        return ResponseResult.ok(result);
    }

    /**
     * 组织机构服务实现类
     */
    private final OrganizationService organizationService;

    /**
     * 构造
     *
     * @param organizationService {@link OrganizationService}
     */
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
}
