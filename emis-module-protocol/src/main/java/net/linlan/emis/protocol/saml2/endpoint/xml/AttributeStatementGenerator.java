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

import java.util.List;

import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.schema.XSString;
import org.opensaml.core.xml.schema.impl.XSStringBuilder;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeValue;
import org.opensaml.saml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml.saml2.core.impl.AttributeStatementBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import net.linlan.emis.protocol.constant.Saml2SsoModel;

/**
 * AttributeStatements 生成
 *
 * @author Linlan
 * Created on  2022/6/1 22:50
 */
@RequiredArgsConstructor
@Getter
public class AttributeStatementGenerator {
    private final static Logger                          logger = LoggerFactory
        .getLogger(AttributeStatementGenerator.class);
    /**
     * 断言属性
     */
    private final List<Saml2SsoModel.AttributeStatement> attributeStatements;

    public org.opensaml.saml.saml2.core.AttributeStatement generateAttributeStatements() {
        org.opensaml.saml.saml2.core.AttributeStatement attributeStatement = new AttributeStatementBuilder()
            .buildObject();
        // XSStringBuilder
        XSStringBuilder stringBuilder = (XSStringBuilder) XMLObjectProviderRegistrySupport
            .getBuilderFactory().getBuilder(XSString.TYPE_NAME);

        for (Saml2SsoModel.AttributeStatement statement : getAttributeStatements()) {
            Attribute attributeName = new AttributeBuilder().buildObject();
            attributeName.setNameFormat(statement.getNameFormat().getValue());
            //value
            XSString value = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,
                XSString.TYPE_NAME);
            value.setValue(statement.getValue());
            attributeName.getAttributeValues().add(value);
            //name
            attributeName.setName(statement.getKey());
            attributeStatement.getAttributes().add(attributeName);
        }
        return attributeStatement;
    }
}
