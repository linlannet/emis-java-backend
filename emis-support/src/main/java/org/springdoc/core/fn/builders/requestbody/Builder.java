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
package org.springdoc.core.fn.builders.requestbody;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

/**
 * The type Request body builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * A brief description of the request body.
     *
     */
    private String      description            = "";

    /**
     * The content of the request body.
     *
     */
    private Content[]   content                = {};

    /**
     * Determines if the request body is required in the request. Defaults to false.
     *
     */
    private boolean     required;

    /**
     * The list of optional extensions
     *
     */
    private Extension[] extensions             = {};

    /**
     * A reference to a RequestBody defined in components RequestBodies.
     *
     * @since swagger -core 2.0.3
     */
    private String      ref                    = "";

    private boolean     useParameterTypeSchema = false;

    /**
     * Instantiates a new Request body builder.
     */
    private Builder() {
    }

    /**
     * Builder request body builder.
     *
     * @return the request body builder
     */
    public static Builder requestBodyBuilder() {
        return new Builder();
    }

    /**
     * Description request body builder.
     *
     * @param description the description
     * @return the request body builder
     */
    public Builder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Content request body builder.
     *
     * @param contentBuilder the content builder
     * @return the request body builder
     */
    public Builder content(org.springdoc.core.fn.builders.content.Builder contentBuilder) {
        this.content = ArrayUtils.add(this.content, contentBuilder.build());
        return this;
    }

    /**
     * Implementation request body builder.
     *
     * @param clazz the clazz
     * @return the request body builder
     */
    public Builder implementation(Class clazz) {
        this.content = ArrayUtils.add(this.content,
            org.springdoc.core.fn.builders.content.Builder.contentBuilder().schema(
                org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().implementation(clazz))
                .build());
        return this;
    }

    /**
     * Required request body builder.
     *
     * @param required the required
     * @return the request body builder
     */
    public Builder required(boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Extension request body builder.
     *
     * @param extensionBuilder the extension builder
     * @return the request body builder
     */
    public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
        this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
        return this;
    }

    /**
     * Ref request body builder.
     *
     * @param ref the ref
     * @return the request body builder
     */
    public Builder ref(String ref) {
        this.ref = ref;
        return this;
    }

    /**
     * Build request body.
     *
     * @return the request body
     */
    public RequestBody build() {
        return new RequestBody() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public Content[] content() {
                return content;
            }

            @Override
            public boolean required() {
                return required;
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
            public boolean useParameterTypeSchema() {
                return useParameterTypeSchema;
            }
        };
    }
}
