/*
 * emis-module-idsource - idsource身份源
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
package net.linlan.emis.manage.processor;

import java.time.LocalDateTime;
import java.util.List;

import net.linlan.emis.core.constant.TriggerType;
import net.linlan.emis.manage.vo.VerifyUser;

/**
 * 身份源数据 pull post 处理器
 *
 * @author Linlan
 *
 */
public interface IdentitySourceSyncUserPostProcessor {
    /**
     * 处理数据
     *
     * @param batch {@link String}
     * @param identitySourceId 身份源ID
     * @param userList {@link  List}
     * @param startTime {@link  LocalDateTime}
     * @param triggerType {@link  TriggerType}
     */
    void process(String batch, String identitySourceId, List<VerifyUser> userList,
                 LocalDateTime startTime, TriggerType triggerType);

}
