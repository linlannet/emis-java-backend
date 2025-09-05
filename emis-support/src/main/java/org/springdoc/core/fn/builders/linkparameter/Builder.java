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
package org.springdoc.core.fn.builders.linkparameter;

import java.lang.annotation.Annotation;

import io.swagger.v3.oas.annotations.links.LinkParameter;

/**
 * The type Link parameter builder.
 * @author bnasslahsen
 */
public class Builder {
    /**
     * The name of this link parameter.
     *
     */
    private String name       = "";

    /**
     * A constant or an expression to be evaluated and passed to the linked operation.
     *
     */
    private String expression = "";

    /**
     * Instantiates a new Link parameter builder.
     */
    private Builder() {
    }

    /**
     * Builder link parameter builder.
     *
     * @return the link parameter builder
     */
    public static Builder linkParameterBuilder() {
        return new Builder();
    }

    /**
     * Name link parameter builder.
     *
     * @param name the name
     * @return the link parameter builder
     */
    public Builder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Expression link parameter builder.
     *
     * @param expression the expression
     * @return the link parameter builder
     */
    public Builder expression(String expression) {
        this.expression = expression;
        return this;
    }

    /**
     * Build link parameter.
     *
     * @return the link parameter
     */
    public LinkParameter build() {
        return new LinkParameter() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String expression() {
                return expression;
            }
        };
    }
}
