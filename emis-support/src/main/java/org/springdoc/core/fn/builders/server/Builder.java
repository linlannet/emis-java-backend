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
package org.springdoc.core.fn.builders.server;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.ArrayUtils;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

/**
 * The type Server builder.
 * @author bnasslahsen
 */
public class Builder {
    /**
     * Required. A URL to the target host.
     * This URL supports Server Variables and may be relative, to indicate that the host location is relative to the location where the
     * OpenAPI definition is being served. Variable substitutions will be made when a variable is named in {brackets}.
     *
     */
    private String           url         = "";

    /**
     * An optional string describing the host designated by the URL. CommonMark syntax MAY be used for rich text representation.
     *
     */
    private String           description = "";

    /**
     * An array of variables used for substitution in the server's URL template.
     *
     */
    private ServerVariable[] variables   = {};

    /**
     * The list of optional extensions
     *
     */
    private Extension[]      extensions  = {};

    /**
     * Instantiates a new Server builder.
     */
    private Builder() {
    }

    /**
     * Builder server builder.
     *
     * @return the server builder
     */
    public static Builder serverBuilder() {
        return new Builder();
    }

    /**
     * Url server builder.
     *
     * @param url the url
     * @return the server builder
     */
    public Builder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Description server builder.
     *
     * @param description the description
     * @return the server builder
     */
    public Builder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Variables server builder.
     *
     * @param serverVariableBuilder the server variable builder
     * @return the server builder
     */
    public Builder variables(org.springdoc.core.fn.builders.servervariable.Builder serverVariableBuilder) {
        this.variables = ArrayUtils.add(this.variables, serverVariableBuilder.build());
        return this;
    }

    /**
     * Extension server builder.
     *
     * @param extensionBuilder the extension builder
     * @return the server builder
     */
    public Builder extension(org.springdoc.core.fn.builders.extension.Builder extensionBuilder) {
        this.extensions = ArrayUtils.add(this.extensions, extensionBuilder.build());
        return this;
    }

    /**
     * Build server.
     *
     * @return the server
     */
    public Server build() {
        return new Server() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String url() {
                return url;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public ServerVariable[] variables() {
                return variables;
            }

            @Override
            public Extension[] extensions() {
                return extensions;
            }
        };
    }
}
