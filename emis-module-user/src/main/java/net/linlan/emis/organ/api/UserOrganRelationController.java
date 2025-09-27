/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.organ.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.Page;

import net.linlan.annotation.Encrypt;
import net.linlan.annotation.LimitScope;
import net.linlan.commons.core.*;
import net.linlan.commons.core.annotation.PlatLog;
import net.linlan.emis.organ.dto.UserOrganRelationDto;
import net.linlan.emis.organ.entity.UserOrganRelation;
import net.linlan.emis.organ.param.UserOrganRelationParam;
import net.linlan.emis.organ.service.UserOrganRelationService;
import net.linlan.frame.api.BaseController;

/**
*
* UserOrganRelation控制域:用户机构关系读写控制类
* @author Linlan
* CreateTime 2025-09-22 00:29:01<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@RestController
@RequestMapping("/api/organ")
public class UserOrganRelationController extends BaseController {
    @Resource
    private UserOrganRelationService userOrganRelationService;

    /** Get UserOrganRelation list. 获取用户机构关系列表或分页, 返回UserOrganRelation对象.
     * @param param 参数查询对象
     * @return 列表的用户机构关系数据 {@link List} 对象，包含 {@link UserOrganRelation} 列表:null,error;page,success
     */
    @PreAuthorize("@ss.hasPerms('organ:userrelation:list')")
    @PlatLog(value = "查询用户机构关系分页", category = 0)
    @GetMapping("/userrelation/list")
    @Encrypt
    public ResponseResult<List<UserOrganRelation>> list(UserOrganRelationParam param) {
        Page<UserOrganRelationDto> result = userOrganRelationService.getPageDto(param);
        if (result == null) {
            return empty();
        }
        return successPage(result.getResult(), result.getPageSize(), result.getPageNum(),
            result.getTotal());
    }

    /**
     * Get UserOrganRelation by id. 主键获取用户机构关系详细信息.
     * @param id 主键ID|1
     * @return {@link UserOrganRelation}:空null,error;分页page,success
     */
    @PreAuthorize("@ss.hasPerms('organ:userrelation:detail')")
    @PlatLog(value = "主键获取用户机构关系详细信息", category = 1)
    @GetMapping(value = "/userrelation/{id}")
    @Encrypt
    public ResponseResult<UserOrganRelation> findById(@PathVariable("id") String id) {
        return success(userOrganRelationService.findById(id));
    }

    /** UserOrganRelation Operation. 新增用户机构关系数据操作逻辑，根据操作类型，执行新增操作.
     * @param input 对象信息，输入UserOrganRelation对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('organ:userrelation:save')")
    @PlatLog(value = "新增用户机构关系", category = 10)
    @PostMapping("/userrelation/save")
    @Encrypt
    @LimitScope(name = "userOrganRelationSave", key = "userOrganRelationSave")
    public ResponseResult<String> save(@RequestBody UserOrganRelation input) {
        userOrganRelationService.save(input);
        return success();
    }

    /** UserOrganRelation Operation. 修改用户机构关系数据操作逻辑，根据操作类型，执行修改操作.
     * @param input 对象信息，输入UserOrganRelation对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('organ:userrelation:update')")
    @PlatLog(value = "修改用户机构关系", category = 20)
    @PostMapping("/userrelation/update")
    @Encrypt
    @LimitScope(name = "userOrganRelationUpdate", key = "userOrganRelationUpdate")
    public ResponseResult<String> update(@RequestBody UserOrganRelation input) {
        userOrganRelationService.update(input);
        return success();
    }

    /** UserOrganRelation Operation. 删除用户机构关系数据操作逻辑，根据操作类型，执行删除操作.
     * @param ids 主键ID:null;1;1,2,3|1
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('organ:userrelation:delete')")
    @PlatLog(value = "删除用户机构关系", category = 40)
    @PostMapping("/userrelation/delete/{ids}")
    @Encrypt
    @LimitScope(name = "userOrganRelationDelete", key = "userOrganRelationDelete")
    public ResponseResult<String> delete(@PathVariable String[] ids) {
        userOrganRelationService.deleteByIds(ids);
        return success();
    }

}
