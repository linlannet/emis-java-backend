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

import net.linlan.emis.app.dto.AppPermissionResourceDto;
import net.linlan.emis.app.entity.AppPermissionResource;
import net.linlan.emis.console.pojo.result.app.AppPermissionResourceGetResult;
import net.linlan.emis.console.pojo.result.app.AppPermissionResourceListResult;
import net.linlan.emis.console.pojo.save.app.AppPermissionResourceCreateParam;
import net.linlan.emis.console.pojo.update.app.AppPermissionResourceUpdateParam;
import net.linlan.emis.support.repository.domain.Page;

/**
 * 资源映射
 *
 * @author Linlan
 * Created on  2020/8/14 22:45
 */
@Mapper(componentModel = "spring", uses = AppPermissionActionConverter.class)
public interface AppPermissionResourceConverter {

    /**
     * 资源创建参数转实体类
     *
     * @param param {@link AppPermissionResourceCreateParam}
     * @return {@link AppPermissionResource}
     */
    @Mapping(target = "actions", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppPermissionResource resourceCreateParamConvertToEntity(AppPermissionResourceCreateParam param);

    /**
     * 资源修改参数转实体类
     *
     * @param param {@link AppPermissionResourceCreateParam}
     * @return {@link AppPermissionResource}
     */
    @Mapping(target = "actions", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppPermissionResource resourceUpdateParamConvertToEntity(AppPermissionResourceUpdateParam param);

    /**
     * 资源转换为资源列表结果
     *
     * @param page {@link Page}
     * @return {@link Page}
     */
    default Page<AppPermissionResourceListResult> entityConvertToResourceListResult(com.github.pagehelper.Page<AppPermissionResourceDto> page) {
        Page<AppPermissionResourceListResult> result = new Page<>();
        if (!page.isEmpty()) {
            List<AppPermissionResourceListResult> list = new ArrayList<>();
            for (AppPermissionResource resource : page) {
                list.add(entityConvertToResourceListResult(resource));
            }
            //@formatter:off
            result.setPagination(Page.Pagination.builder()
                    .total(page.getTotal())
                    .totalPages(page.getPages())
                    .current(page.getPageNum())
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 实体转换为资源列表结果
     *
     * @param data {@link AppPermissionResource}
     * @return {@link AppPermissionResourceListResult}
     */
    AppPermissionResourceListResult entityConvertToResourceListResult(AppPermissionResource data);

    /**
     * 实体转获取详情返回
     *
     * @param resource {@link AppPermissionResource}
     * @return {@link AppPermissionResourceGetResult}
     */
    @Mapping(target = "actions", source = "actions")
    AppPermissionResourceGetResult entityConvertToResourceGetResult(AppPermissionResource resource);
}
