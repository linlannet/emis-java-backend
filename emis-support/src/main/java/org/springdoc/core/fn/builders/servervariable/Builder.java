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
package org.springdoc.core.fn.builders.servervariable;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

/**
 * The type Server variable builder.
 * @author bnasslahsen
 */
public class Builder {
    /**
     * Required.  The name of this variable.
     *
     */
    private String      name;

    /**
     * An array of allowable values for this variable.  This field map to the enum property in the OAS schema.
     *
     * @return String array of allowableValues
     */
    private String[]    allowableValues = {};

    /**
     * Required.  The default value of this variable.
     *
     */
    private String      defaultValue;

    /**
     * An optional description for the server variable.
     *
     */
    private String      description     = "";

    /**
     * The list of optional extensions
     *
     */
    private Extension[] extensions      = {};

    /**
     * Instantiates a new Server variable builder.
     */
    private Builder() {
    }

    /**
     * Builder server variable builder.
     *
     * @return the server variable builder
     */
    public static Builder serverVariableBuilder() {
        return new Builder();
    }

    /**
     * Name server variable builder.
     *
     * @param name the name
     * @return the server variable builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Allowable values server variable builder.
     *
     * @param allowableValues the allowable values
     * @return the server variable builder
     */
    public Builder allowableValues(String[] allowableValues) {
        this.allowableValues = allowableValues;
        return this;
    }

    /**
     * Default value server variable builder.
     *
     * @param defaultValue the default value
     * @return the server variable builder
     */
    public Builder defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * Description server variable builder.
     *
     * @param description the description
     * @return the server variable builder
     */
    public Builder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Extension server variable builder.
     *
     * @param extensionBuilder the extension builder
     * @return the server variable builder
     */
    public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
        this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
        return this;
    }

    /**
     * Build server variable.
     *
     * @return the server variable
     */
    public ServerVariable build() {
        return new ServerVariable() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String[] allowableValues() {
                return allowableValues;
            }

            @Override
            public String defaultValue() {
                return defaultValue;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public Extension[] extensions() {
                return extensions;
            }
        };
    }
}
