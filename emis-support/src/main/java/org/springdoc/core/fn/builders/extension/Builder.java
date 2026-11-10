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
package org.springdoc.core.fn.builders.extension;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

/**
 * The type Extension builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * An option name for these extensions.
     *
     */
    private String              name = "";

    /**
     * The extension properties.
     *
     */
    private ExtensionProperty[] properties;

    /**
     * Instantiates a new Extension builder.
     */
    private Builder() {
    }

    /**
     * Builder extension builder.
     *
     * @return the extension builder
     */
    public static Builder extensionBuilder() {
        return new Builder();
    }

    /**
     * Name extension builder.
     *
     * @param name the name
     * @return the extension builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Properties extension builder.
     *
     * @param extensionPropertyBuilder the properties
     * @return the extension builder
     */
    public Builder propertie(org.springdoc.core.fn.builders.extensionproperty.Builder extensionPropertyBuilder) {
        this.properties = ArrayUtils.add(this.properties, extensionPropertyBuilder.build());
        return this;
    }

    /**
     * Build extension.
     *
     * @return the extension
     */
    public Extension build() {
        return new Extension() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public ExtensionProperty[] properties() {
                return properties;
            }
        };
    }
}
