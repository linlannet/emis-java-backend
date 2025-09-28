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
package net.linlan.emis.uext.entity;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * TemplateInfo数据域:模板信息配置实体类
 * @author Linlan
 *
 * CreateTime 2025-09-07 18:33:53
 *
 */
@Data
public class TemplateInfo extends BaseEntity {

    /**
     * 模板ID
     */
    private String  id;

    /**
     * 应用ID
     */
    private String  appId;

    /**
     * 机构ID
     */
    private String  organId;

    /**
     * 外键ID
     */
    private String  foreignId;

    /**
     * 模板类型，邮件模板
     */
    private String  type;

    /**
     * 模板子类型
     */
    private String  subType;

    /**
     * 模板名称，标题名称
     */
    private String  name;

    /**
     * 签名，发送人名称
     */
    private String  signature;

    /**
     * 内容JSON，发布版本的JSON配置
     */
    private String  content;

    /**
     * 排序号
     */
    private Integer priority;

    /**
     * 状态0未生效1正常2受限3锁定
     */
    private Integer status;

    /**
     * 删除标记0正常1已删除2应用内受限3回收站
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
