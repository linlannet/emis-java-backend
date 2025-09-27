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
package net.linlan.emis.uext.param;

import java.util.Date;

import lombok.Data;

import net.linlan.utils.param.BaseParam;

/**
 *
 * MailSendRecord数据域:邮件发送记录查询参数实体类
 * <br>
 * @author Linlan
 * CreateTime 2025-09-07 18:33:53<br>
 * @version 1.0<br>
 * @since 1.0<br>
 *
 */
@Data
public class MailSendRecordParam extends BaseParam {

    /**
     * 主键ID
     */
    private Long    id;
    /**
     * 外键ID
     */
    private String  foreignId;
    /**
     * 发送信息者名称，后台用户则管理员，会员则会员名称，企业会员则号名称
     */
    private String  senderName;
    /**
     * 接收人ID，邮箱地址
     */
    private String  receiverEmail;
    /**
     * 接收人
     */
    private String  receiverName;
    /**
     * 业务类型
     */
    private String  bizType;
    /**
     * 信息标题，主题
     */
    private String  title;
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
     * 阅读时间
     */
    private Date    readTime;
    /**
     * 信息状态(0未读1已读2全组已读)
     */
    private String  readStatus;
    /**
     * 创建者ID
     */
    private String  creatorId;
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

}
