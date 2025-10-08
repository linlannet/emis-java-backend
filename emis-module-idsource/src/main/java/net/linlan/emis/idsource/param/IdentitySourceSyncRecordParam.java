/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.idsource.param;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * IdentitySourceSyncRecord数据域:身份源同步记录查询参数实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:09:58
 *
 */
@Data
public class IdentitySourceSyncRecordParam extends BaseParam {

    /**
     * 主键ID
     */
    private String  id;
    /**
     * 身份源ID
     */
    private String  idsourceId;
    /**
     * 外键ID
     */
    private String  foreignId;
    /**
     * 记录ID
     */
    private String  syncHistoryId;
    /**
     * 动作类型
     */
    private String  actionType;
    /**
     * 对象ID（用户、组织）
     */
    private String  objectId;
    /**
     * 对象名称（用户、组织）
     */
    private String  objectName;
    /**
     * 对象类型（用户、组织）
     */
    private String  objectType;
    /**
     * 状态
     */
    private String  status;
    /**
     * 描述
     */
    private String  content;
    /**
     * 创建人ID
     */
    private String  creatorId;
    /**
     * 删除标记，0未删除1已删除
     */
    private Integer delFlag;
    /**
     * 备注
     */
    private String  description;
    /**
     * 备用1
     */
    private String  spare1;
    /**
     * 备用2
     */
    private String  spare2;

}
