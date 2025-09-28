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
package net.linlan.emis.ubind.dto;

import java.util.Date;

import lombok.Data;

/**
 *
 * UserIdpBind数据域:用户身份提供商绑定Dto对象
 * <br>
 * @author Linlan
 * CreateTime 2025-09-06 15:46:35<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Data
public class UserIdpBindDto {
    /**
     * id
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 个人邮箱
     */
    private String email;

    /**
     * 手机号对应的国家号
     */
    private String stateCode;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * OpenId
     */
    private String openId;

    /**
     * unionId
     */
    public String  unionId;

    /**
     * 身份提供商 ID
     */
    private String idpId;

    /**
     * 身份认证提供商名称
     */
    private String idpName;

    /**
     * 身份提供商 类型
     */
    private String idpType;

    /**
     * 绑定时间
     */
    private Date   bindTime;
}
