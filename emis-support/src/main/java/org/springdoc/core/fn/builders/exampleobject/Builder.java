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
package org.springdoc.core.fn.builders.exampleobject;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.ExampleObject;

/**
 * The type Example object builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * A unique name to identify this particular example
     */
    private String      name          = "";

    /**
     * A brief summary of the purpose or context of the example
     */
    private String      summary       = "";

    /**
     * A string representation of the example.  This is mutually exclusive with the externalValue property, and ignored if the externalValue property is specified.  If the media type associated with the example allows parsing into an object, it may be converted from a string
     */
    private String      value         = "";

    /**
     * A URL to point to an external document to be used as an example.  This is mutually exclusive with the value property.
     */
    private String      externalValue = "";

    /**
     * The list of optional extensions
     */
    private Extension[] extensions    = {};

    /**
     * A reference to a example defined in components examples.
     */
    private String      ref           = "";

    /**
     * A description of the purpose or context of the example
     */
    private String      description   = "";

    /**
     * Instantiates a new Example object builder.
     */
    private Builder() {
    }

    /**
     * An example object example object builder.
     *
     * @return the example object builder
     */
    public static Builder exampleOjectBuilder() {
        return new Builder();
    }

    /**
     * Name example object builder.
     *
     * @param name the name
     * @return the example object builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Summary example object builder.
     *
     * @param summary the summary
     * @return the example object builder
     */
    public Builder summary(String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * Value example object builder.
     *
     * @param value the value
     * @return the example object builder
     */
    public Builder value(String value) {
        this.value = value;
        return this;
    }

    /**
     * External value example object builder.
     *
     * @param externalValue the external value
     * @return the example object builder
     */
    public Builder externalValue(String externalValue) {
        this.externalValue = externalValue;
        return this;
    }

    /**
     * Extensions example object builder.
     *
     * @param extensionBuilder the extensions
     * @return the example object builder
     */
    public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
        this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
        return this;
    }

    /**
     * Ref example object builder.
     *
     * @param ref the ref
     * @return the example object builder
     */
    public Builder ref(String ref) {
        this.ref = ref;
        return this;
    }

    /**
     * Description example object builder.
     *
     * @param description the description
     * @return the example object builder
     */
    public Builder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Build example object.
     *
     * @return the example object
     */
    public ExampleObject build() {
        return new ExampleObject() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String summary() {
                return summary;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public String externalValue() {
                return externalValue;
            }

            @Override
            public Extension[] extensions() {
                return extensions;
            }

            @Override
            public String ref() {
                return ref;
            }

            @Override
            public String description() {
                return description;
            }
        };
    }
}
