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
 * ProtocolCasConfig数据域:协议CAS配置实体类
 * @author Linlan
 * 
 * CreateTime 2025-09-06 19:12:33
 *
 */
@Data
public class ProtocolCasConfig extends BaseEntity {

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
     * 客户端服务URL地址
     */
    private String  spCallbackUrl;

    /**
     * 用户CAS身份标识
     */
    private String  userIdentityType;

    /**
     * 客户端服务URL地址
     */
    private String  clientServiceUrl;

    /**
     * serviceTicket 过期时间（秒）
     */
    private Integer serviceTicketExpireTime;

    /**
     * 自定义CAS校验响应
     */
    private String  customResponseAttribute;

    /**
     * 额外配置
     */
    private String  additionalConfig;

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
