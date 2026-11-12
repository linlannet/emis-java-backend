/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.pojo.result.app;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import net.linlan.emis.app.constant.AppCertUsingType;

/**
 * 获取应用证书列表结果
 *
 * @author Linlan
 * Created on  2022/5/31 23:29
 */
@Data
@AllArgsConstructor
@Schema(description = "获取应用证书列表响应")
public class AppCertListResult {
    /**
     * ID
     */
    @Parameter(description = "证书ID")
    private String           id;

    /**
     * 应用ID
     */
    @Parameter(description = "应用ID")
    private String           appId;

    /**
     * 签名算法
     */
    @Parameter(description = "签名算法")
    private String           signAlgo;

    /**
     * 私钥长度
     */
    @Parameter(description = "私钥长度")
    private Integer          keyLong;

    /**
     * 证书
     */
    @Parameter(description = "证书")
    private String           cert;

    /**
     * 使用类型
     */
    @Parameter(description = "使用类型")
    private AppCertUsingType usingType;
}
