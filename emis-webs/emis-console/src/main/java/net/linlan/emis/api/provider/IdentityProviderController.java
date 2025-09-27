/*
 * emis-java-backend - 基于JAVA开发的企业级多身份管理系统后端工程，依托EFRAME框架开发，基于Springboot，采用支持 JAVA 1.8最高版本的framework、security，后端数据访问采用Mybatis框架，支持多种数据库一套代码适配，提供脚手架可快速生成Mapper等，在仓库功能上，借鉴主流的SSO和IAM框架，实现统一用户管理。前端提供VUE框架和REACT框架的控制台管理工程、企业个人门户工程和移动端应用扩展。
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
package net.linlan.emis.api.provider;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.Page;

import net.linlan.annotation.Encrypt;
import net.linlan.annotation.LimitScope;
import net.linlan.commons.core.*;
import net.linlan.commons.core.annotation.PlatLog;
import net.linlan.emis.provider.dto.IdentityProviderDto;
import net.linlan.emis.provider.entity.IdentityProvider;
import net.linlan.emis.provider.param.IdentityProviderParam;
import net.linlan.emis.provider.service.IdentityProviderService;
import net.linlan.frame.api.BaseController;

/**
*
* IdentityProvider控制域:身份提供商读写控制类
* @author Linlan
* CreateTime 2025-08-31 23:33:33<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@RestController
@RequestMapping("/api/provider")
public class IdentityProviderController extends BaseController {
    @Resource
    private IdentityProviderService identityProviderService;

    /** Get IdentityProvider list. 获取身份提供商列表或分页, 返回IdentityProvider对象.
     * @param param 参数查询对象
     * @return 列表的身份提供商数据 {@link List} 对象，包含 {@link IdentityProvider} 列表:null,error;page,success
     */
    @PreAuthorize("@ss.hasPerms('provider:identity:list')")
    @PlatLog(value = "查询身份提供商分页", category = 0)
    @GetMapping("/identity/list")
    @Encrypt
    public ResponseResult<List<IdentityProvider>> list(IdentityProviderParam param) {
        Page<IdentityProviderDto> result = identityProviderService.getPageDto(param);
        if (result == null) {
            return empty();
        }
        return successPage(result.getResult(), result.getPageSize(), result.getPageNum(),
            result.getTotal());
    }

    /**
     * Get IdentityProvider by id. 主键获取身份提供商详细信息.
     * @param id 主键ID|1
     * @return {@link IdentityProvider}:空null,error;分页page,success
     */
    @PreAuthorize("@ss.hasPerms('provider:identity:detail')")
    @PlatLog(value = "主键获取身份提供商详细信息", category = 1)
    @GetMapping(value = "/identity/{id}")
    @Encrypt
    public ResponseResult<IdentityProvider> findById(@PathVariable("id") String id) {
        return success(identityProviderService.findById(id));
    }

    /** IdentityProvider Operation. 新增身份提供商数据操作逻辑，根据操作类型，执行新增操作.
     * @param input 对象信息，输入IdentityProvider对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('provider:identity:save')")
    @PlatLog(value = "新增身份提供商", category = 10)
    @PostMapping("/identity/save")
    @Encrypt
    @LimitScope(name = "identityProviderSave", key = "identityProviderSave")
    public ResponseResult<String> save(@RequestBody IdentityProvider input) {
        identityProviderService.save(input);
        return success();
    }

    /** IdentityProvider Operation. 修改身份提供商数据操作逻辑，根据操作类型，执行修改操作.
     * @param input 对象信息，输入IdentityProvider对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('provider:identity:update')")
    @PlatLog(value = "修改身份提供商", category = 20)
    @PostMapping("/identity/update")
    @Encrypt
    @LimitScope(name = "identityProviderUpdate", key = "identityProviderUpdate")
    public ResponseResult<String> update(@RequestBody IdentityProvider input) {
        identityProviderService.update(input);
        return success();
    }

    /** IdentityProvider Operation. 删除身份提供商数据操作逻辑，根据操作类型，执行删除操作.
     * @param ids 主键ID:null;1;1,2,3|1
     * @return {@link String}:操作成功0;操作失败1
     */
    @PreAuthorize("@ss.hasPerms('provider:identity:delete')")
    @PlatLog(value = "删除身份提供商", category = 40)
    @PostMapping("/identity/delete/{ids}")
    @Encrypt
    @LimitScope(name = "identityProviderDelete", key = "identityProviderDelete")
    public ResponseResult<String> delete(@PathVariable String[] ids) {
        identityProviderService.deleteByIds(ids);
        return success();
    }

}
