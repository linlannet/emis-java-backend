/*
 * emis-module-app - app应用管理
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
package net.linlan.emis.app.entity;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * AppPermissionPolicy数据域:应用权限策略实体类
 * @author Linlan
 * 
 *
 */
@Data
public class AppPermissionPolicy extends BaseEntity {

    /**
     * 主键ID
     */
    private String  id;

    /**
     * 所属应用
     */
    private String  appId;

    /**
     * 外键ID
     */
    private String  foreignId;

    /**
     * 权限主体ID（用户、角色、分组、组织机构）
     */
    private String  subjectId;

    /**
     * 权限主体类型（用户、角色、分组、组织机构）
     */
    private String  subjectType;

    /**
     * 权限客体ID（操作权限、角色）
     */
    private String  objectId;

    /**
     * 权限客体类型（操作权限、角色）
     */
    private String  objectType;

    /**
     * 效果（Allow（允许）、Deny（拒绝））
     */
    private String  effect;

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
