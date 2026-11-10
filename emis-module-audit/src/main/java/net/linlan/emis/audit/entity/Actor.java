/*
 * emis-module-audit - audit安全审计
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
package net.linlan.emis.audit.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * 行动者Actor
 * @author Linlan
 */
@Data
@Builder
public class Actor implements Serializable {

    public static final String ACTOR_ID         = "actor.id.keyword";
    public static final String ACTOR_TYPE       = "actor.type.keyword";

    public static final String ACTOR_AUTH_TYPE  = "actor.auth_type.keyword";

    private static final long  serialVersionUID = -8631010419770301125L;
    /**
     * 行动者ID
     */
    @NonNull
    private String             id;

    /**
     * 行动者类型
     */
    @NonNull
    private String             type;

    /**
     * 身份验证类型
     */
    private String             authType;
}
