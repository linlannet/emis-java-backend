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
package org.springdoc.core.fn.builders.externaldocumentation;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.extensions.Extension;

/**
 * The type External documentation builder.
 * @author bnasslahsen
 */
public class Builder {

    /**
     * A short description of the target documentation.
     *
     */
    private String      description = "";

    /**
     * The URL for the target documentation. Value must be in the format of a URL.
     *
     */
    private String      url         = "";

    /**
     * The list of optional extensions
     *
     */
    private Extension[] extensions  = {};

    /**
     * Instantiates a new External documentation builder.
     */
    private Builder() {
    }

    /**
     * An external documentation external documentation builder.
     *
     * @return the external documentation builder
     */
    public static Builder externalDocumentationBuilder() {
        return new Builder();
    }

    /**
     * Description external documentation builder.
     *
     * @param description the description
     * @return the external documentation builder
     */
    public Builder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Url external documentation builder.
     *
     * @param url the url
     * @return the external documentation builder
     */
    public Builder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Extensions external documentation builder.
     *
     * @param extensionBuilder the extensions
     * @return the external documentation builder
     */
    public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
        this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
        return this;
    }

    /**
     * Build external documentation.
     *
     * @return the external documentation
     */
    public ExternalDocumentation build() {
        return new ExternalDocumentation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public String url() {
                return url;
            }

            @Override
            public Extension[] extensions() {
                return extensions;
            }
        };
    }
}
