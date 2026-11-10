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
package net.linlan.emis.manage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.linlan.emis.core.constant.TriggerType;
import net.linlan.emis.support.exception.SupportException;

/**
 * 身份验证源接口
 *
 * @author Linlan
 *
 */
public interface IdentityVerifySource<T extends IdentitySourceConfig> {
    /**
     * 获取身份源ID
     *
     * @return {@link String}
     */
    String getId();

    /**
     * 获取身份源名称
     *
     * @return {@link String}
     */
    String getName();

    /**
     * 获取身份源配置
     *
     * @return {@link String}
     */
    T getConfig();

    /**
     * 同步
     *
     * @param triggerType {@link TriggerType} 执行方式
     */
    void sync(TriggerType triggerType);

    /**
     * 回调
     *
     * @param request {@link HttpServletRequest}
     * @param body {@link String}
     * @return {@link Map}
     */
    default Object event(HttpServletRequest request, String body) {
        throw new SupportException("暂未实现");
    }

}
