/*
 * emis-console - enterprise multiple identity system
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
package net.linlan.emis.console.service.setting;

import java.util.List;

import net.linlan.emis.console.pojo.result.setting.PasswordPolicyConfigResult;
import net.linlan.emis.console.pojo.result.setting.WeakPasswordLibListResult;
import net.linlan.emis.console.pojo.save.setting.PasswordPolicySaveParam;

/**
 * <p>
 * 密码策略 服务类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-17
 */
public interface PasswordPolicyService extends SettingService {

    /**
     * 获取配置
     *
     * @return {@link PasswordPolicyConfigResult}
     */
    PasswordPolicyConfigResult getPasswordPolicyConfig();

    /**
     * 保存配置
     *
     * @param param {@link PasswordPolicySaveParam}
     * @return {@link Boolean}
     */
    Boolean savePasswordPolicyConfig(PasswordPolicySaveParam param);

    /**
     * 获取弱密码库
     *
     * @return {@link List}
     */
    List<WeakPasswordLibListResult> getWeakPasswordLibList();

}
