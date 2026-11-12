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
package net.linlan.emis.protocol.cas.model;

// import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.emis.app.constant.AuthorizationType;
import net.linlan.emis.app.constant.InitLoginType;

/**
 * @author Linlan
 * Created on  2023/1/2 22:27
 */
@Data
public class AppCasStandardSaveConfigParam implements Serializable {
    //@Serial
    private static final long serialVersionUID = 1881187724713984421L;

    /**
     * 应用ID
     */
    @Schema(description = "授权类型")
    private AuthorizationType authorizationType;

    /**
     * SSO 发起登录类型
     */
    @Schema(description = "SSO 发起登录类型")
    private InitLoginType     initLoginType;

    /**
     * SSO 发起登录URL
     */
    @Schema(description = "SSO 发起登录URL")
    private String            initLoginUrl;

    /**
     * 单点登录 SP 回调地址
     */
    @Parameter(name = "单点登录 sp Callback Url")
    private String            spCallbackUrl;
}
