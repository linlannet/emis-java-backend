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
package org.springdoc.core.fn.builders.extensionproperty;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

/**
 * The type Extension property builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * The name of the property.
     *
     */
    private String  name;

    /**
     * The value of the property.
     *
     */
    private String  value;

    /**
     * If set to true, field `value` will be parsed and serialized as JSON/YAML
     *
     */
    private boolean parseValue;

    /**
     * Instantiates a new Extension property builder.
     */
    private Builder() {
    }

    /**
     * An extension property extension property builder.
     *
     * @return the extension property builder
     */
    public static Builder extensionPropertyBuilder() {
        return new Builder();
    }

    /**
     * Name extension property builder.
     *
     * @param name the name
     * @return the extension property builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Value extension property builder.
     *
     * @param value the value
     * @return the extension property builder
     */
    public Builder value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Parse value extension property builder.
     *
     * @param parseValue the parse value
     * @return the extension property builder
     */
    public Builder parseValue(boolean parseValue) {
        this.parseValue = parseValue;
        return this;
    }

    /**
     * Build extension property.
     *
     * @return the extension property
     */
    public ExtensionProperty build() {
        return new ExtensionProperty() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public boolean parseValue() {
                return parseValue;
            }
        };
    }
}
