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

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.google.common.collect.Lists;

import net.linlan.emis.app.dto.AppGroupDto;
import net.linlan.emis.app.entity.AppAccount;
import net.linlan.emis.app.entity.AppGroup;
import net.linlan.emis.app.param.AppGroupParam;
import net.linlan.emis.console.pojo.query.app.AppGroupListQuery;
import net.linlan.emis.console.pojo.result.app.AppGroupGetResult;
import net.linlan.emis.console.pojo.result.app.AppGroupListResult;
import net.linlan.emis.console.pojo.save.app.AppAccountCreateParam;
import net.linlan.emis.console.pojo.save.app.AppGroupCreateParam;
import net.linlan.emis.console.pojo.update.app.AppGroupUpdateParam;
import net.linlan.emis.support.repository.domain.Page;

/**
 * 分组映射
 *
 * @author Linlan
 * Created on  2023/8/31 15:45
 */
@Mapper(componentModel = "spring")
public interface AppGroupConverter {

    /**
     * 实体转换为分组列表结果
     *
     * @param appGroupPoPage {@link List}
     * @return {@link List}
     */
    default Page<AppGroupListResult> entityConvertToAppGroupListResult(com.github.pagehelper.Page<AppGroupDto> appGroupPoPage) {
        Page<AppGroupListResult> page = new Page<>();
        List<AppGroupListResult> list = Lists.newArrayList();
        for (AppGroupDto po : appGroupPoPage) {
            AppGroupListResult result = entityConvertToAppGroupListResult(po);
            list.add(result);
        }
        page.setList(list);
        //@formatter:off
        page.setPagination(Page.Pagination.builder()
                .total(appGroupPoPage.getTotal())
                .totalPages(appGroupPoPage.getPages())
                .current(appGroupPoPage.getPageNum())
                .build());
        //@formatter:on
        return page;
    }

    /**
     * 实体转分组管理列表
     *
     * @param appGroupPo {@link AppGroupDto}
     * @return {@link AppGroupListResult}
     */
    AppGroupListResult entityConvertToAppGroupListResult(AppGroupDto appGroupPo);

    /**
     * 实体转分组返回
     *
     * @param entity {@link AppGroup}
     * @return {@link AppGroupGetResult}
     */
    AppGroupGetResult entityConvertToAppGroupResult(AppGroup entity);

    /**
     * 将分组修改对象转换为entity
     *
     * @param param {@link AppGroupUpdateParam}
     * @return {@link AppGroup}
     */
    @Mapping(target = "type", ignore = true)

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppGroup appGroupUpdateParamConverterToEntity(AppGroupUpdateParam param);

    /**
     * 分组新增参数转换分组实体
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link AppAccount}
     */
    @Mapping(target = "type", ignore = true)

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updatorId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    AppGroup appGroupCreateParamConvertToEntity(AppGroupCreateParam param);

    /**
     * 转查询参数
     *
     * @param query {@link AppGroupListQuery}
     * @return {@link AppGroupQueryParam}
     */
    AppGroupParam appGroupQueryToQueryParam(AppGroupListQuery query);
}
