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
package net.linlan.emis.uext.dao;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import net.linlan.commons.db.mybatis.MybatisBaseDao;
import net.linlan.emis.uext.dto.SmsSendRecordDto;
import net.linlan.emis.uext.entity.SmsSendRecord;
import net.linlan.emis.uext.param.SmsSendRecordParam;

/**
*
* SmsSendRecord数据域:短信发送记录持久层Dao
* <br>
* @author Linlan
* CreateTime 2025-09-07 18:33:53<br>
* @version 1.0<br>
* @since 1.0<br>
*
*/
@Mapper
public interface SmsSendRecordDao extends MybatisBaseDao<SmsSendRecord> {

    /** get the page of dto SmsSendRecordDto
     * 列表分页方法，返回分页DTO对象列表
     * @param param the input param 查询参数
     * @return 分页的短信发送记录数据 {@link Page} 对象，包含 {@link SmsSendRecord} 列表
     */
    Page<SmsSendRecordDto> getPageDto(SmsSendRecordParam param);

    /** get the dto by input id, return dto
     * 对象详情方法，通过id查询对象{@link SmsSendRecordDto}
     * @param id 主键ID|1
     * @return {@link SmsSendRecordDto}
     */
    SmsSendRecordDto getDtoById(Long id);

}
