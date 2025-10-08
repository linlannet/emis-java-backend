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
package net.linlan.emis.manage.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户模型
 *
 * @author Linlan
 *
 */
@Data
public class VerifyUser implements Serializable {

    private static final long serialVersionUID = 8567794739261358220L;
    /**
     * 用户ID
     */
    private String            userId;

    /**
     * 头像地址
     */
    private String            avatarUrl;

    /**
     * 国际电话区号 86
     */
    private String            mobileAreaCode;

    /**
     * 手机号
     */
    private String            mobile;

    /**
     * 邮箱
     */
    private String            email;

    /**
     * 公司邮箱
     */
    private String            organEmail;

    /**
     * 所属部门ID列表
     */
    private List<String>      deptIdList;

    /**
     * 状态  true:启用 false:未启用
     */
    private Boolean           active;

    /**
     * 用户详情
     */
    private VerifyUserDetail  userDetail;

}
