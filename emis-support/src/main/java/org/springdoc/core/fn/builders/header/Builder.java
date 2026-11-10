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
package org.springdoc.core.fn.builders.header;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The type Header builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * Required: The name of the header. The name is only used as the key to store this header in a map.
     *
     */
    private String  name;

    /**
     * Additional description data to provide on the purpose of the header
     *
     */
    private String  description = "";

    /**
     * The schema defining the type used for the header. Ignored if the properties content or array are specified.
     *
     */
    private Schema  schema      = org.springdoc.core.fn.builders.schema.Builder.schemaBuilder()
        .build();

    /**
     * Determines whether this header is mandatory. The property may be included and its default value is false.
     *
     */
    private boolean required;

    /**
     * Specifies that a header is deprecated and should be transitioned out of usage.
     *
     */
    private boolean deprecated;

    /**
     * A reference to a header defined in components headers.
     * @since swagger -core 2.0.3
     */
    private String  ref         = "";

    /**
     * Instantiates a new Header builder.
     */
    private Builder() {
    }

    /**
     * Builder header builder.
     *
     * @return the header builder
     */
    public static Builder headerBuilder() {
        return new Builder();
    }

    /**
     * Name header builder.
     *
     * @param name the name
     * @return the header builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Description header builder.
     *
     * @param description the description
     * @return the header builder
     */
    public Builder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Schema header builder.
     *
     * @param schemaBuilder the schema builder
     * @return the header builder
     */
    public Builder schema(org.springdoc.core.fn.builders.schema.Builder schemaBuilder) {
        this.schema = schemaBuilder.build();
        return this;
    }

    /**
     * Required header builder.
     *
     * @param required the required
     * @return the header builder
     */
    public Builder required(boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Deprecated header builder.
     *
     * @param deprecated the deprecated
     * @return the header builder
     */
    public Builder deprecated(boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    /**
     * Ref header builder.
     *
     * @param ref the ref
     * @return the header builder
     */
    public Builder ref(String ref) {
        this.ref = ref;
        return this;
    }

    /**
     * Build header.
     *
     * @return the header
     */
    public Header build() {
        return new Header() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public Schema schema() {
                return schema;
            }

            @Override
            public boolean required() {
                return required;
            }

            @Override
            public boolean deprecated() {
                return deprecated;
            }

            @Override
            public String ref() {
                return ref;
            }
        };
    }
}
