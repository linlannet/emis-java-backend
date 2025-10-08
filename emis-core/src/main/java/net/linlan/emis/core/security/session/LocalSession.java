/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.session;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

import net.linlan.commons.core.DateUtils;
import net.linlan.emis.support.userdetails.geo.GeoLocation;
import net.linlan.emis.support.userdetails.useragent.UserAgent;

/**
 * Session
 *
 * @author Linlan
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class LocalSession implements Serializable {

    private static final long serialVersionUID = -8631010419770301768L;

    /**
     * 会话ID
     */
    private String            sessionId;

    /**
     * 用户LID
     */
    private final String      userId;

    /**
     * 用户名
     */
    private final String      username;

    /**
     * 地址位置相关
     */
    private GeoLocation       geoLocation;

    /**
     * userAgent
     */
    private UserAgent         userAgent;

    /**
     * 用户类型
     */
    private String            userType;

    /**
     * 认证类型
     */
    private String            authenticationProvider;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = DateUtils.yyyyMMddHHmmss)
    private LocalDateTime     authenticationTime;

    /**
     * 最后请求时间
     */
    @JsonFormat(pattern = DateUtils.yyyyMMddHHmmss)
    private LocalDateTime     lastRequestTime;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = DateUtils.yyyyMMddHHmmss)
    private LocalDateTime     loginTime;

}
