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
package org.springdoc.core.fn.builders.discriminatormapping;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;

/**
 * The type Discriminator mapping builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * The property value that will be mapped to a Schema
     *
     */
    private String      value      = "";

    /**
     * The schema that is being mapped to a property value
     *
     */
    private Class<?>    schema     = Void.class;

    /**
     * The Extensions.
     */
    private Extension[] extensions = {};

    /**
     * Instantiates a new Discriminator mapping builder.
     */
    private Builder() {
    }

    /**
     * A discriminator mapping builde discriminator mapping builder.
     *
     * @return the discriminator mapping builder
     */
    public static Builder discriminatorMappingBuilder() {
        return new Builder();
    }

    /**
     * Value discriminator mapping builder.
     *
     * @param value the value
     * @return the discriminator mapping builder
     */
    public Builder value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Schema discriminator mapping builder.
     *
     * @param schema the schema
     * @return the discriminator mapping builder
     */
    public Builder schema(Class<?> schema) {
        this.schema = schema;
        return this;
    }

    /**
     * Build discriminator mapping.
     *
     * @return the discriminator mapping
     */
    public DiscriminatorMapping build() {
        return new DiscriminatorMapping() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String value() {
                return value;
            }

            @Override
            public Class<?> schema() {
                return schema;
            }

            @Override
            public Extension[] extensions() {
                return extensions;
            }
        };
    }

}
