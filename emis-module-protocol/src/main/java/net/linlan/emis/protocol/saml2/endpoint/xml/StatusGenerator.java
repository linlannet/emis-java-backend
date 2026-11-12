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
package net.linlan.emis.protocol.saml2.endpoint.xml;

import org.opensaml.saml.saml2.core.Status;
import org.opensaml.saml.saml2.core.StatusCode;
import org.opensaml.saml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml.saml2.core.impl.StatusCodeBuilder;

/**
 * 状态生成器
 *
 * @author Linlan
 * Created on  2022/6/1 22:39
 */
public class StatusGenerator {

    public Status generateStatus(String value) {
        Status status = builderStatus();

        StatusCode statusCode = builderStatusCode(value);

        status.setStatusCode(statusCode);

        return status;
    }

    public Status builderStatus() {
        return new StatusBuilder().buildObject();
    }

    public StatusCode builderStatusCode(String value) {
        StatusCode statusCode = new StatusCodeBuilder().buildObject();
        statusCode.setValue(value);

        return statusCode;
    }

}
