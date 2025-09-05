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
package org.springdoc.core.fn.builders.securityrequirement;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * The type Security requirement builder.
 * @author bnasslahsen
 */
public class Builder {
    /**
     * This name must correspond to a declared SecurityRequirement.
     *
     */
    private String   name;

    /**
     * If the security scheme is of type "oauth2" or "openIdConnect", then the value is a list of scope names required for the execution.
     * For other security scheme types, the array must be empty.
     *
     */
    private String[] scopes = {};

    /**
     * Instantiates a new Security requirement builder.
     */
    private Builder() {
    }

    /**
     * Builder security requirement builder.
     *
     * @return the security requirement builder
     */
    public static Builder securityRequirementBuilder() {
        return new Builder();
    }

    /**
     * Name security requirement builder.
     *
     * @param name the name
     * @return the security requirement builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Scopes security requirement builder.
     *
     * @param scopes the scopes
     * @return the security requirement builder
     */
    public Builder scopes(String[] scopes) {
        this.scopes = scopes;
        return this;
    }

    /**
     * Build security requirement.
     *
     * @return the security requirement
     */
    public SecurityRequirement build() {
        return new SecurityRequirement() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String[] scopes() {
                return scopes;
            }
        };
    }
}
