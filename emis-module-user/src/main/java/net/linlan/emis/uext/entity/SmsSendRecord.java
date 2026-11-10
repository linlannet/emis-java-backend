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

import java.util.Date;

import lombok.Data;

import net.linlan.commons.core.RandomUtils;
import net.linlan.utils.entity.BaseEntity;

/**
 *
 * SmsSendRecord数据域:短信发送记录实体类
 * @author Linlan
 * 
 * CreateTime 2025-09-07 18:33:53
 *
 */
@Data
public class SmsSendRecord extends BaseEntity {

    /**
     * 主键ID
     */
    private Long    id;

    /**
     * 外键ID
     */
    private String  foreignId;

    /**
     * 接收人手机号
     */
    private String  receiverMobile;

    /**
     * 接收人
     */
    private String  receiverName;

    /**
     * 消息类型
     */
    private String  bizType;

    /**
     * 信息子类型
     */
    private String  bizSubtype;

    /**
     * 信息内容
     */
    private String  content;

    /**
     * 邮件提供商
     */
    private String  provider;

    /**
     * 信息链接，链接平台内地址，可点击访问
     */
    private String  msgUrl;

    /**
     * 信息状态
     */
    private String  status;

    /**
     * 发送时间
     */
    private Date    sendTime;

    /**
     * 发送状态(0草稿1已发)
     */
    private String  sendStatus;

    /**
     * 返回时间
     */
    private Date    returnTime;

    /**
     * 返回结果
     */
    private String  returnInfo;

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
            setId(RandomUtils.randomLid());
        }

    }

}
