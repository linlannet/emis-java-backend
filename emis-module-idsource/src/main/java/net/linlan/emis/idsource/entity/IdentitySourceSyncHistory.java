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
package net.linlan.emis.idsource.entity;

import java.util.Date;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * IdentitySourceSyncHistory数据域:身份源同步历史实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:09:58
 *
 */
@Data
public class IdentitySourceSyncHistory extends BaseEntity {

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
     * 批号
     */
    private String  batch;

    /**
     * 创建数量
     */
    private Long    createdCount;

    /**
     * 更新数量
     */
    private Long    updatedCount;

    /**
     * 删除数量
     */
    private Long    deletedCount;

    /**
     * 跳过数量
     */
    private Long    skippedCount;

    /**
     * 开始时间
     */
    private Date    startTime;

    /**
     * 结束时间
     */
    private Date    endTime;

    /**
     * 对象类型（用户、组织）
     */
    private String  objectType;

    /**
     * 触发类型（手动、任务）
     */
    private String  triggerType;

    /**
     * 同步状态
     */
    private String  status;

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

    /**
     * 初始化方法
     */
    public void init() {
        //添加对数据库或实体对象的默认值处理
        if (getId() == null) {
            setId(RandomUtils.randomSid());
        }

    }

}
