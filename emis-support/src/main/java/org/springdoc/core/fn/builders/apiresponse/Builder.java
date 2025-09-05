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
package org.springdoc.core.fn.builders.apiresponse;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * The type Api response builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * The Use return type schema.
     */
    boolean             useReturnTypeSchema = false;

    /**
     * A short description of the response.
     *
     */
    private String      description         = "";

    /**
     * The HTTP response code, or 'default', for the supplied response. May only have 1 default entry.
     *
     */
    private String      responseCode        = "default";

    /**
     * An array of response headers. Allows additional information to be included with response.
     *
     */
    private Header[]    headers             = {};

    /**
     * An array of operation links that can be followed from the response.
     *
     */
    private Link[]      links               = {};

    /**
     * An array containing descriptions of potential response payloads, for different media types.
     *
     */
    private Content[]   content             = {};

    /**
     * The list of optional extensions
     *
     */
    private Extension[] extensions          = {};

    /**
     * A reference to a response defined in components responses.
     *
     * @since swagger -core 2.0.3
     */
    private String      ref                 = "";

    /**
     * Instantiates a new Api response builder.
     */
    private Builder() {
    }

    /**
     * Builder api response builder.
     *
     * @return the api response builder
     */
    public static Builder responseBuilder() {
        return new Builder();
    }

    /**
     * Description api response builder.
     *
     * @param description the description
     * @return the api response builder
     */
    public Builder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Response code api response builder.
     *
     * @param responseCode the response code
     * @return the api response builder
     */
    public Builder responseCode(String responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    /**
     * Header api response builder.
     *
     * @param headers the headers
     * @return the api response builder
     */
    public Builder header(org.springdoc.core.fn.builders.header.Builder headers) {
        this.headers = ArrayUtils.add(this.headers, headers.build());
        return this;
    }

    /**
     * Link api response builder.
     *
     * @param linkBuilder the link builder
     * @return the api response builder
     */
    public Builder link(org.springdoc.core.fn.builders.link.Builder linkBuilder) {
        this.links = ArrayUtils.add(this.links, linkBuilder.build());
        return this;
    }

    /**
     * Content api response builder.
     *
     * @param contentBuilder the content builder
     * @return the api response builder
     */
    public Builder content(org.springdoc.core.fn.builders.content.Builder contentBuilder) {
        this.content = ArrayUtils.add(this.content, contentBuilder.build());
        return this;
    }

    /**
     * Implementation api response builder.
     *
     * @param clazz the clazz
     * @return the api response builder
     */
    public Builder implementation(Class clazz) {
        this.content = ArrayUtils.add(this.content,
            org.springdoc.core.fn.builders.content.Builder.contentBuilder().schema(
                org.springdoc.core.fn.builders.schema.Builder.schemaBuilder().implementation(clazz))
                .build());
        return this;
    }

    /**
     * Implementation array api response builder.
     *
     * @param clazz the clazz
     * @return the api response builder
     */
    public Builder implementationArray(Class clazz) {
        this.content = ArrayUtils.add(this.content,
            org.springdoc.core.fn.builders.content.Builder.contentBuilder()
                .array(org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder()
                    .schema(org.springdoc.core.fn.builders.schema.Builder.schemaBuilder()
                        .implementation(clazz)))
                .build());
        return this;
    }

    /**
     * Extension api response builder.
     *
     * @param extensionBuilder the extension builder
     * @return the api response builder
     */
    public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
        this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
        return this;
    }

    /**
     * Ref api response builder.
     *
     * @param ref the ref
     * @return the api response builder
     */
    public Builder ref(String ref) {
        this.ref = ref;
        return this;
    }

    /**
     * Build api response.
     *
     * @return the api response
     */
    public ApiResponse build() {
        return new ApiResponse() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public String responseCode() {
                return responseCode;
            }

            @Override
            public Header[] headers() {
                return headers;
            }

            @Override
            public Link[] links() {
                return links;
            }

            @Override
            public Content[] content() {
                return content;
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
            public boolean useReturnTypeSchema() {
                return useReturnTypeSchema;
            }
        };
    }
}
