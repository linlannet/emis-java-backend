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
package net.linlan.emis.ubind.entity;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * UserThirdParty数据域:三方用户实体类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-06 15:46:35<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Data
public class UserThirdParty extends BaseEntity {

    /**
     * 主键ID
     */
    private String  id;

    /**
     * 外键ID
     */
    private String  foreignId;

    /**
     * 用户ID
     */
    private String  userId;

    /**
     * 三方全局D
     */
    private String  unionId;

    /**
     * 开放平台ID
     */
    private String  openId;

    /**
     * 身份提供商ID
     */
    private String  providerId;

    /**
     * 身份提供商类型
     */
    private String  providerType;

    /**
     * 邮箱
     */
    private String  email;

    /**
     * 手机号对应的国家号
     */
    private String  stateCode;

    /**
     * 电话号码
     */
    private String  mobile;

    /**
     * 昵称
     */
    private String  nickName;

    /**
     * 头像
     */
    private String  avatarUrl;

    /**
     * 附加信息
     */
    private String  additionInfo;

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
