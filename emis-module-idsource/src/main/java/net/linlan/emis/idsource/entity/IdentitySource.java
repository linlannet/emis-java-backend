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

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.emis.idsource.config.StrategyConfig;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * IdentitySource数据域:身份源配置实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:09:58
 *
 */
@Data
public class IdentitySource extends BaseEntity {

    /**
     * 主键ID
     */
    private String         id;

    /**
     * 外键ID
     */
    private String         foreignId;

    /**
     * 名称
     */
    private String         name;

    /**
     * 编码
     */
    private String         code;

    /**
     * 提供者
     */
    private String         provider;

    /**
     * 基础配置
     */
    private String         basicConfig;

    /**
     * 同步策略配置
     */
    private StrategyConfig strategyConfig;

    /**
     * 作业配置
     */
    private String         jobConfig;

    /**
     * 是否启用，0否1是
     */
    private Boolean        isEnabled;

    /**
     * 是否已配置，0否1是
     */
    private Boolean        isConfigured;

    /**
     * 排序码
     */
    private Integer        priority;

    /**
     * 删除标记，0未删除1已删除
     */
    private Integer        delFlag;

    /**
     * 备注
     */
    private String         description;

    /**
     * 备用1
     */
    private String         spare1;

    /**
     * 备用2
     */
    private String         spare2;

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
