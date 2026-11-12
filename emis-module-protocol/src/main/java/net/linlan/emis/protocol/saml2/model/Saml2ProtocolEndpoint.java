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
package net.linlan.emis.protocol.saml2.model;

// import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
     * 协议端点域
     *
     * @author Linlan
     * Created on  2022/6/4 23:37
     */
@Data
@Schema(description = "协议端点")
public class Saml2ProtocolEndpoint implements Serializable {

    //@Serial
    private static final long serialVersionUID = -2261602995152894964L;

    /**
     * IDP 元数据端点
     */
    @Parameter(description = "IDP 元数据端点")
    private String            idpMetaEndpoint;

    /**
     * IDP EntityId 端点
     */
    @Parameter(description = "IDP EntityId 端点")
    private String            idpEntityIdEndpoint;

    /**
     * IDP SSO 端点
     */
    @Parameter(description = "IDP SSO 端点")
    private String            idpSsoEndpoint;

    /**
     * IDP SLO 端点
     */
    @Parameter(description = "IDP SLO 端点")
    private String            idpSloEndpoint;
}
