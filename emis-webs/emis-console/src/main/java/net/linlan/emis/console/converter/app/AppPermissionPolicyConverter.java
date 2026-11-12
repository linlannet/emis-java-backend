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
package net.linlan.emis.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import net.linlan.emis.app.dto.AppPermissionPolicyDto;
import net.linlan.emis.app.entity.AppPermissionPolicy;
import net.linlan.emis.app.param.AppPermissionPolicyParam;
import net.linlan.emis.console.pojo.result.app.AppPermissionPolicyListResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionPolicyCreateParam;
import net.linlan.emis.console.pojo.update.app.AppPermissionPolicyUpdateParam;
import net.linlan.emis.support.repository.domain.Page;

/**
 * 策略映射
 *
 * @author Linlan
 * Created on  2020/8/14 22:45
 */
@Mapper(componentModel = "spring", uses = AppPermissionActionConverter.class)
public interface AppPermissionPolicyConverter {

    /**
     * 资源创建参数转实体类
     *
     * @param param {@link AppPermissionPolicyCreateParam}
     * @return {@link AppPermissionPolicy}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppPermissionPolicy policyCreateParamConvertToEntity(AppPermissionPolicyCreateParam param);

    /**
     * 资源修改参数转实体类
     *
     * @param param {@link AppPermissionPolicyCreateParam}
     * @return {@link AppPermissionPolicy}
     */
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppPermissionPolicy policyUpdateParamConvertToEntity(AppPermissionPolicyUpdateParam param);

    /**
     * 资源转换为资源列表结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<AppPermissionPolicyListResult> entityConvertToPolicyListResult(com.github.pagehelper.Page<AppPermissionPolicyDto> page) {
        Page<AppPermissionPolicyListResult> result = new Page<>();
        List<AppPermissionPolicyDto> pageList = page;
        if (!CollectionUtils.isEmpty(pageList)) {
            //@formatter:off
            result.setPagination(Page.Pagination.builder()
                    .total(page.getTotal())
                    .totalPages(page.getPages())
                    .current(page.getPageNum())
                    .build());
            //@formatter:on
            List<AppPermissionPolicyListResult> list = new ArrayList<>();
            for (AppPermissionPolicyDto po : pageList) {
                list.add(entityConvertToPolicyListResult(po));
            }
            result.setList(list);
        }
        return result;
    }

    /**
     * entityConvertToPolicyListResult
     *
     * @param entity {@link AppPermissionPolicyListResult}
     * @return {@link AppPermissionPolicyParam}
     */
    AppPermissionPolicyListResult entityConvertToPolicyListResult(AppPermissionPolicyDto entity);
}
