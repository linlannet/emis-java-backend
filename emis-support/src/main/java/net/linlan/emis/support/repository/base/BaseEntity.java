/*
 * emis-support - support支撑模块
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
package net.linlan.emis.support.repository.base;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础实体类
 *
 * @author Linlan
 */
@Getter
@Setter
@ToString
public abstract class BaseEntity<PK extends Serializable> extends IdEntity<String> {

    public static final String CREATOR_BY         = "creatorId";
    public static final String CREATE_DATE        = "createTime";
    public static final String LAST_MODIFIED_BY   = "updatorId";
    public static final String LAST_MODIFIED_TIME = "updateTime";
    public static final String IS_DELETED_COLUMN  = "isDeleted";
    private static final long  serialVersionUID   = -8631010419770301107L;
    /**
     * 创建者
     */
    @CreatedBy
    private String             creatorId;
    /**
     * 创建日期
     */
    @CreatedDate
    @JsonFormat
    private LocalDateTime      createTime;

    /**
     * 最后修改者
     */
    @LastModifiedBy
    private String             updatorId;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @JsonFormat
    private LocalDateTime      updateTime;

    /**
     * 备注
     */
    private String             remark;

}
