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

import net.linlan.commons.db.page.Pagination;
import net.linlan.emis.view.provider.manager.IdentityProviderManager;
import net.linlan.emis.view.provider.vo.IdentityProviderVo;
import net.linlan.frame.mbiz.ApiIntfConfig;
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
* CreateTime 2025-08-31 23:33:33
*
*/
@RestController
@RequestMapping("/api/provider")
public class IdentityProviderApi extends BaseController {
    @Resource
    private IdentityProviderManager identityProviderManager;

    /** Get IdentityProvider page. 获取身份提供商分页, 返回IdentityProviderVo对象.
     * @param type 类型:1,分页page;2,其他特定类型;默认为1|1
     * @param param 参数查询对象
     * @return 获取IdentityProviderVo分页列表:null,error;page,success
     */
    @PlatLog("获取身份提供商分页, 返回IdentityProviderVo的分页对象.")
    @PreAuthorize("@ss.hasRight('api:provider:identity:page')")
    @GetMapping("identity/page/{type}")
    @Encrypt
    public ResponseResult<Pagination<IdentityProviderVo>> getIdentityProviderPage(@PathVariable(value = "type") String type, IdentityProviderParam param) {
        if (ObjectUtils.isEmpty(param)){
            return failure();
        }
        Pagination result = identityProviderManager.getIdentityProviderPage(type, param);
        if (ObjectUtils.isEmpty(result)){
            return empty();
        }
        return success(result);
    }

    /**
     * Get IdentityProvider Vo by id and type. 主键获取身份提供商详情, 返回Vo对象.
     * @param id 主键ID|1
     * @param type 类型:1,库entity;2,事务dto;3,其他other|2
     * @return {@link IdentityProviderVo}:空null,error;分页page,success
     */
    @PlatLog("主键获取身份提供商详情, 返回IdentityProviderVo对象.")
    @PreAuthorize("@ss.hasRight('api:provider:identity')")
    @GetMapping("identity/{id}/{type}")
    @Encrypt
    public ResponseResult<IdentityProviderVo> getIdentityProviderById(@PathVariable(value = "id") String id, @PathVariable(value = "type") String type) {
        if (ObjectUtils.isEmpty(id)){
            return failure();
        }
        IdentityProviderVo result = identityProviderManager.getIdentityProviderVo(id, type);
        if (ObjectUtils.isEmpty(result)){
            return empty();
        }
        return success(result);
    }

    /** IdentityProvider Operation. IdentityProvider数据操作逻辑，根据操作类型，执行新增操作.
     * @param id 主键ID:null;1;1,2,3|1
     * @param input 对象信息，输入Vo对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PlatLog("检查对象IdentityProvider数据操作逻辑，根据操作类型，执行新增操作")
    @PreAuthorize("@ss.hasRight('api:provider:identity:save')")
    @PostMapping("identity/save/{id}")
    @Encrypt
    @LimitScope(name = "identityProviderSave", key = "identityProviderSave")
    public ResponseResult<String> identityProviderAddOp(@PathVariable(value = "id") String id,
                                                        @RequestBody IdentityProviderVo input) {
        if (identityProviderManager.doIdentityProviderOp(ApiIntfConfig.VALUE_AT_ADD, id, input)){
            return success();
        }
        return failure();
    }

    /** IdentityProvider Operation. IdentityProvider数据操作逻辑，根据操作类型，执行修改操作.
     * @param id 主键ID:null;1;1,2,3|1
     * @param input 对象信息，输入Vo对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PlatLog("检查对象IdentityProvider数据操作逻辑，根据操作类型，执行修改操作")
    @PreAuthorize("@ss.hasRight('api:provider:identity:update')")
    @PostMapping("identity/update/{id}")
    @Encrypt
    @LimitScope(name = "identityProviderUpdate", key = "identityProviderUpdate")
    public ResponseResult<String> identityProviderUpdateOp(@PathVariable(value = "id") String id,
                                                           @RequestBody IdentityProviderVo input) {
        if (identityProviderManager.doIdentityProviderOp(ApiIntfConfig.VALUE_AT_UPDATE, id, input)){
            return success();
        }
        return failure();
    }

    /** IdentityProvider Operation. IdentityProvider数据操作逻辑，根据操作类型，执行删除操作.
     * @param id 主键ID:null;1;1,2,3|1
     * @param input 对象信息，输入Vo对象
     * @return {@link String}:操作成功0;操作失败1
     */
    @PlatLog("检查对象IdentityProvider数据操作逻辑，根据操作类型，执行删除操作")
    @PreAuthorize("@ss.hasRight('api:provider:identity:delete')")
    @PostMapping("identity/delete/{id}")
    @Encrypt
    @LimitScope(name = "identityProviderDelete", key = "identityProviderDelete")
    public ResponseResult<String> identityProviderDeleteOp(@PathVariable(value = "id") String id,
                                                           @RequestBody IdentityProviderVo input) {
        if (identityProviderManager.doIdentityProviderOp(ApiIntfConfig.VALUE_AT_DELETE, id, input)){
            return success();
        }
        return failure();
    }

}
