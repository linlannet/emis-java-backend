/*
 * emis-module-protocol - protocol协议配置
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
package net.linlan.emis.protocol.entity;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * ProtocolJwtConfig数据域:应用JWT配置实体类
 * @author Linlan
 *
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolJwtConfig extends BaseEntity {

    /**
     * 主键ID
     */
    private String  id;

    /**
     * 应用ID
     */
    private String  appId;

    /**
     * 外键ID
     */
    private String  foreignId;

    /**
     * 业务系统JWT SSO地址
     * 业务系统中的JWT SSO地址，在单点登录时本系统将向该地址发送id_token信息，参数名为id_token，业务系统通过id_token与Public Key可获取业务系统中的用户信息，如果在业务系统（SP）发起登录，请求SP登录地址时如果携带service参数，系统会检验合法性，成功后会将浏览器重定向到该地址，并携带id_token身份令牌。
     */
    private String  redirectUrl;

    /**
     * 业务系统中在JWT SSO成功后重定向的URL，一般用于跳转到二级菜单等，若设置了该URL，在JWT SSO时会以参数target_link_url优先传递该值，若未设置该值，此时若SSO中有请求参数target_link_url，则会按照请求参数传递该值。此项可选。
     * 若设置了该URL，在JWT SSO时会以参数target_uri优先传递该值，
     * 若未设置该值，此时若SSO中有请求参数target_uri，则会按照请求参数传递该值。此项可选。
     */
    private String  targetLinkUrl;

    /**
     * 跳转方式
     */
    private String  bindingType;

    /**
     * ID_TOKEN SUBJECT 类型
     */
    private String  idTokenSubjectType;

    /**
     * ID_TOKEN 有效期（秒）
     */
    private Long    idTokenTimeToLive;

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
            setId(RandomUtils.UUID32());
        }

    }

}
