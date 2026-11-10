/*
 * emis-module-link - link连接适配扩展
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
package net.linlan.emis.link.entity;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * LinkAdapter数据域:接入适配器实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:35:13
 *
 */
@Data
public class LinkAdapter extends BaseEntity {

    /**
     * 主键ID
     */
    private String  id;

    /**
     * 外键ID
     */
    private String  foreignId;

    /**
     * 适配器名称
     */
    private String  name;

    /**
     * 适配器协议类型
     */
    private String  protocol;

    /**
     * 适配器入口
     */
    private String  adapter;

    /**
     * 适配器类型
     */
    private String  type;

    /**
     * 适配器图标
     */
    private String  icon;

    /**
     * 排序码
     */
    private Integer priority;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

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
