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
package net.linlan.emis.console.controller.identitysource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.linlan.commons.core.ResponseResult;
import net.linlan.emis.audit.annotation.Audit;
import net.linlan.emis.audit.event.type.EventType;
import net.linlan.emis.console.converter.identitysource.IdentitySourceConverter;
import net.linlan.emis.console.pojo.other.IdentitySourceConfigValidatorParam;
import net.linlan.emis.console.pojo.query.identity.IdentitySourceListQuery;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceConfigGetResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceGetResult;
import net.linlan.emis.console.pojo.result.identitysource.IdentitySourceListResult;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceConfigSaveParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceCreateParam;
import net.linlan.emis.console.pojo.save.identitysource.IdentitySourceCreateResult;
import net.linlan.emis.console.pojo.update.identity.IdentitySourceUpdateParam;
import net.linlan.emis.console.service.identitysource.IdentitySourceService;
import net.linlan.emis.idsource.entity.IdentitySource;
import net.linlan.emis.manage.event.IdentitySourceEventUtils;
import net.linlan.emis.support.manage.lock.Lock;
import net.linlan.emis.support.manage.preview.Preview;
import net.linlan.emis.support.repository.domain.Page;
import net.linlan.emis.support.repository.domain.PageModel;
import static net.linlan.emis.idsource.constant.IdentityConstants.IDENTITY_GROUP_NAME;
import static net.linlan.emis.idsource.constant.IdentityConstants.IDENTITY_SOURCE_PATH;

/**
 * 身份源管理
 *
 * @author Linlan
 * Created on  2020/7/11 21:18
 */
@Validated
@Tag(name = IDENTITY_GROUP_NAME + "-" + "身份源管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = IDENTITY_SOURCE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentitySourceController {

    /**
     * 获取列表
     *
     * @return {@link IdentitySourceListResult}
     */
    @Operation(summary = "身份源列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Page<IdentitySourceListResult>> getIdentitySourceList(PageModel pageModel,
                                                                                IdentitySourceListQuery query) {
        Page<IdentitySourceListResult> result = identitySourceService.getIdentitySourceList(query,
            pageModel);
        return ResponseResult.ok(result);
    }

    /**
     * 创建身份源
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link IdentitySourceCreateResult}
     */
    @Lock
    @Preview
    @Operation(summary = "创建身份源")
    @Audit(type = EventType.CREATE_IDENTITY_RESOURCE)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<IdentitySourceCreateResult> createIdentitySource(@RequestBody @Validated IdentitySourceCreateParam param) {
        IdentitySourceCreateResult result = identitySourceService.createIdentitySource(param);
        IdentitySourceEventUtils.register(result.getId());
        return ResponseResult.ok(result);
    }

    /**
     * 修改身份源
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改身份源")
    @Audit(type = EventType.UPDATE_IDENTITY_RESOURCE)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> updateIdentitySource(@RequestBody @Validated IdentitySourceUpdateParam param) {
        boolean result = identitySourceService.updateIdentitySource(param);
        //注册
        IdentitySourceEventUtils.register(param.getId());
        return ResponseResult.ok(result);
    }

    /**
     * 保存身份源配置
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存身份源配置")
    @PutMapping(value = "/save/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> saveIdentitySourceConfig(@RequestBody @Validated IdentitySourceConfigSaveParam param) {
        boolean result = identitySourceService.saveIdentitySourceConfig(param);
        //注册
        IdentitySourceEventUtils.register(param.getId());
        return ResponseResult.ok(result);
    }

    /**
     * 获取身份源配置
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Operation(summary = "获取身份源配置")
    @GetMapping(value = "/get/config/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<IdentitySourceConfigGetResult> getIdentitySourceConfig(@PathVariable(value = "id") String id) {
        IdentitySource entity = identitySourceService.getIdentitySource(id);
        IdentitySourceConfigGetResult result = identitySourceConverter
            .entityConverterToIdentitySourceConfigGetResult(entity);
        return ResponseResult.ok(result);
    }

    /**
     * 获取认证源信息
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取认证源")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<IdentitySourceGetResult> getIdentitySource(@PathVariable(value = "id") String id) {
        IdentitySource entity = identitySourceService.getIdentitySource(id);
        IdentitySourceGetResult result = identitySourceConverter
            .entityConverterToIdentitySourceGetResult(entity);
        return ResponseResult.ok(result);
    }

    /**
     * 启用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用身份源")
    @Audit(type = EventType.ENABLE_IDENTITY_RESOURCE)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> enableIdentitySource(@PathVariable(value = "id") String id) {
        boolean result = identitySourceService.enableIdentitySource(id);
        //注册
        IdentitySourceEventUtils.register(id);
        return ResponseResult.ok(result);
    }

    /**
     * 禁用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用身份源")
    @Audit(type = EventType.DISABLE_IDENTITY_RESOURCE)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> disableIdentitySource(@PathVariable(value = "id") String id) {
        boolean result = identitySourceService.disableIdentitySource(id);
        //移除
        IdentitySourceEventUtils.destroy(id);
        return ResponseResult.ok(result);
    }

    /**
     * 删除身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除身份源")
    @Audit(type = EventType.DELETE_IDENTITY_RESOURCE)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> deleteIdentitySource(@PathVariable String id) {
        boolean result = identitySourceService.deleteIdentitySource(id);
        //移除
        IdentitySourceEventUtils.destroy(id);
        return ResponseResult.ok(result);
    }

    /**
     * 身份源配置验证
     *
     * @param param {@link IdentitySourceConfigValidatorParam}
     * @return {@link Boolean}
     */
    @Operation(summary = "身份源配置验证")
    @PostMapping(value = "/config_validator")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(net.linlan.emis.support.userdetails.UserType).ADMIN)")
    public ResponseResult<Boolean> identitySourceConfigValidator(@RequestBody @Validated IdentitySourceConfigValidatorParam param) {
        boolean result = identitySourceService.identitySourceConfigValidator(param);
        return ResponseResult.ok(result);
    }

    /**
     * 身份源服务
     */
    private final IdentitySourceService   identitySourceService;

    private final IdentitySourceConverter identitySourceConverter;
}
