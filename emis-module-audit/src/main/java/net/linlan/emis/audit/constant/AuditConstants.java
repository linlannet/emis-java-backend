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
package net.linlan.emis.audit.constant;

import net.linlan.emis.support.constant.EmisConstants;

/**
 * 系统审计常量
 *
 * @author Linlan
 */
public final class AuditConstants {

    /**
     * 组名称
     */
    public static final String  AUDIT_GROUP_NAME   = "行为审计";

    /**
     * 系统审计API路径
     */
    public final static String  AUDIT_PATH         = EmisConstants.V1_API_PATH + "/audit";

    /**
     * 审计es index
     */
    private static final String AUDIT_INDEX_PREFIX = "emis-audit-";

    public static String getAuditIndexPrefix(Boolean demoEnv) {
        if (demoEnv) {
            return AUDIT_INDEX_PREFIX + "demo-";
        }
        return AUDIT_INDEX_PREFIX;
    }
}
