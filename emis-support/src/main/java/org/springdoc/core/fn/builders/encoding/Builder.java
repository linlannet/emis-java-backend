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
package org.springdoc.core.fn.builders.encoding;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Encoding;

/**
 * The type Encoding builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * The name of this encoding object instance.
     * This property is a key in encoding map of MediaType object and
     * MUST exist in a schema as a property.
     *
     */
    private String      name        = "";

    /**
     * The Content-Type for encoding a specific property.
     *
     */
    private String      contentType = "";

    /**
     * Describes how a specific property value will be serialized depending on its type
     *
     */
    private String      style       = "";

    /**
     * When this is true, property values of type array or object generate separate parameters for each value of the array,
     * or key-value-pair of the map.
     *
     */
    private boolean     explode;

    /**
     * Determines whether the parameter value SHOULD allow reserved characters,
     * as defined by RFC3986 to be included without percent-encoding.
     *
     */
    private boolean     allowReserved;

    /**
     * An array of header objects
     *
     */
    private Header[]    headers     = {};

    /**
     * The list of optional extensions
     *
     */
    private Extension[] extensions  = {};

    /**
     * Instantiates a new Encoding builder.
     */
    private Builder() {
    }

    /**
     * Builder encoding builder.
     *
     * @return the encoding builder
     */
    public static Builder encodingBuilder() {
        return new Builder();
    }

    /**
     * Name encoding builder.
     *
     * @param name the name
     * @return the encoding builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Content type encoding builder.
     *
     * @param contentType the content type
     * @return the encoding builder
     */
    public Builder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Style encoding builder.
     *
     * @param style the style
     * @return the encoding builder
     */
    public Builder style(String style) {
        this.style = style;
        return this;
    }

    /**
     * Explode encoding builder.
     *
     * @param explode the explode
     * @return the encoding builder
     */
    public Builder explode(boolean explode) {
        this.explode = explode;
        return this;
    }

    /**
     * Allow reserved encoding builder.
     *
     * @param allowReserved the allow reserved
     * @return the encoding builder
     */
    public Builder allowReserved(boolean allowReserved) {
        this.allowReserved = allowReserved;
        return this;
    }

    /**
     * Headers encoding builder.
     *
     * @param headerBuilder the header builder
     * @return the encoding builder
     */
    public Builder headers(org.springdoc.core.fn.builders.header.Builder headerBuilder) {
        this.headers = ArrayUtils.add(this.headers, headerBuilder.build());
        return this;
    }

    /**
     * Extension encoding builder.
     *
     * @param extensionBuilder the extension builder
     * @return the encoding builder
     */
    public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
        this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
        return this;
    }

    /**
     * Build encoding.
     *
     * @return the encoding
     */
    public Encoding build() {
        return new Encoding() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String contentType() {
                return contentType;
            }

            @Override
            public String style() {
                return style;
            }

            @Override
            public boolean explode() {
                return explode;
            }

            @Override
            public boolean allowReserved() {
                return allowReserved;
            }

            @Override
            public Header[] headers() {
                return headers;
            }

            @Override
            public Extension[] extensions() {
                return extensions;
            }
        };
    }
}
