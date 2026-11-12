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
package net.linlan.emis.protocol.base;

// import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Linlan
 * Created on 2023/7/13 21:32
 */
@Data
@SuperBuilder
public class AbstractProtocolConfig implements Serializable {

    //@Serial
    private static final long serialVersionUID = 7006674647924828758L;

    /**
     * 应用ID
     */
    @NonNull
    private String            appId;

    /**
     * 应用名称
     */
    @NonNull
    private String            appName;

    /**
     * 客户端ID
     */
    @NonNull
    private String            clientId;

    /**
     * 客户端秘钥
     */
    @NonNull
    private String            clientSecret;

    /**
     * 应用编码
     */
    @NonNull
    private String            appCode;

    /**
     * 应用模版
     */
    @NonNull
    private String            appTemplate;
}
