/*
 * emis-module-audit - audit安全审计
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
package net.linlan.emis.audit.api;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.Page;

import io.swagger.v3.oas.annotations.Operation;
import net.linlan.annotation.Encrypt;
import net.linlan.annotation.LimitScope;
import net.linlan.commons.core.*;
import net.linlan.commons.core.annotation.PlatLog;
import net.linlan.emis.audit.dto.AuditInfoDto;
import net.linlan.emis.audit.dto.DictResult;
import net.linlan.emis.audit.entity.AuditInfo;
import net.linlan.emis.audit.param.AuditInfoParam;
import net.linlan.emis.audit.service.AuditInfoService;
import net.linlan.frame.api.BaseController;

/**
*
* AuditInfo控制域:行为审计读写控制类
* @author Linlan
* CreateTime 2025-09-06 15:55:04<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@RestController
@RequestMapping("/api/audit")
public class AuditInfoController extends BaseController {
    @Resource
    private AuditInfoService auditInfoService;

    /** Get AuditInfo list. 获取行为审计列表或分页, 返回AuditInfo对象.
     * @param param 参数查询对象
     * @return 列表的行为审计数据 {@link List} 对象，包含 {@link AuditInfo} 列表:null,error;page,success
     */
    @PreAuthorize("@ss.hasPerms('audit:info:list')")
    @PlatLog(value = "查询行为审计分页", category = 0)
    @Operation(description = "查询行为审计分页")
    @GetMapping("/info/list")
    @Encrypt
    public ResponseResult<List<AuditInfo>> list(AuditInfoParam param) {
        Page<AuditInfoDto> result = auditInfoService.getPageDto(param);
        if (result == null) {
            return empty();
        }
        return successPage(result.getResult(), result.getPageSize(), result.getPageNum(),
            result.getTotal());
    }

    /**
     * Get AuditInfo by id. 主键获取行为审计详细信息.
     * @param id 主键ID|1
     * @return {@link AuditInfo}:空null,error;分页page,success
     */
    @PreAuthorize("@ss.hasPerms('audit:info:detail')")
    @PlatLog(value = "主键获取行为审计详细信息", category = 1)
    @GetMapping(value = "/info/{id}")
    @Encrypt
    public ResponseResult<AuditInfo> findById(@PathVariable("id") String id) {
        return success(auditInfoService.findById(id));
    }

    /** AuditInfo Operation. 新增行为审计数据操作逻辑，根据操作类型，执行新增操作.
     * @param input 对象信息，输入AuditInfo对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('audit:info:save')")
    @PlatLog(value = "新增行为审计", category = 10)
    @PostMapping("/info/save")
    @Encrypt
    @LimitScope(name = "auditInfoSave", key = "auditInfoSave")
    public ResponseResult<String> save(@RequestBody AuditInfo input) {
        auditInfoService.save(input);
        return success();
    }

    /** AuditInfo Operation. 修改行为审计数据操作逻辑，根据操作类型，执行修改操作.
     * @param input 对象信息，输入AuditInfo对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('audit:info:update')")
    @PlatLog(value = "修改行为审计", category = 20)
    @PostMapping("/info/update")
    @Encrypt
    @LimitScope(name = "auditInfoUpdate", key = "auditInfoUpdate")
    public ResponseResult<String> update(@RequestBody AuditInfo input) {
        auditInfoService.update(input);
        return success();
    }

    /** AuditInfo Operation. 删除行为审计数据操作逻辑，根据操作类型，执行删除操作.
     * @param ids 主键ID:null;1;1,2,3|1
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('audit:info:delete')")
    @PlatLog(value = "删除行为审计", category = 40)
    @PostMapping("/info/delete/{ids}")
    @Encrypt
    @LimitScope(name = "auditInfoDelete", key = "auditInfoDelete")
    public ResponseResult<String> delete(@PathVariable String[] ids) {
        auditInfoService.deleteByIds(ids);
        return success();
    }

    /**
     * 获取审计字典类型
     *
     * @return {@link ResponseResult}
     */
    @Validated
    @Operation(description = "获取审计类型")
    @GetMapping(value = "/info/types/{user_type}")
    public ResponseResult<List<DictResult>> getAuditDict(@PathVariable(name = "user_type") @NotNull(message = "用户类型不能为空！") String userType) {
        List<DictResult> dict = auditInfoService.getAuditDict(userType);
        return ResponseResult.ok(dict);
    }

}
