/*
 * emis-core - core核心模块
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
package net.linlan.emis.core.security.savedredirect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

/**
     * 跨站重定向参数
     *
     * @author Linlan
 *
     */
@Data
public class SavedRedirect implements Serializable {

    private static final long serialVersionUID = -8631010419770301773L;

    /**
     * Action
     */
    private String            action;

    /**
     * Method
     */
    private String            method           = HttpMethod.GET.toString();

    /**
     * Parameters
     */
    private List<Parameter>   parameters       = new ArrayList<>();

    @Data
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public static class Parameter implements Serializable {

        private static final long serialVersionUID = -8631010419770301774L;
        /**
         * key
         */
        private String            key;
        /**
         * value
         */
        private String            value;
    }
}
