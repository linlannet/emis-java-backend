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
package net.linlan.emis.console.service.setting.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.emis.console.converter.setting.PasswordPolicyConverter;
import net.linlan.emis.console.pojo.result.setting.PasswordPolicyConfigResult;
import net.linlan.emis.console.pojo.result.setting.WeakPasswordLibListResult;
import net.linlan.emis.console.pojo.save.setting.PasswordPolicySaveParam;
import net.linlan.emis.console.service.setting.PasswordPolicyService;
import net.linlan.emis.core.constant.ConfigBeanNameConstants;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import net.linlan.emis.support.security.password.weak.PasswordWeakLib;
import static net.linlan.emis.uext.constant.PasswordPolicySettingConstants.PASSWORD_POLICY_KEYS;

/**
 * <p>
 * 密码策略 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-08-17
 */
@Service
public class PasswordPolicyServiceImpl extends SettingServiceImpl implements PasswordPolicyService {

    /**
     * 获取配置
     *
     * @return {@link PasswordPolicyConfigResult}
     */
    @Override
    public PasswordPolicyConfigResult getPasswordPolicyConfig() {
        List<EmisSettingVo> list = settingService.getByKeys(PASSWORD_POLICY_KEYS);
        return passwordPolicyConverter.entityConvertToPasswordPolicyConfigResult(list);
    }

    /**
     * 保存配置
     *
     * @param param {@link PasswordPolicySaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean savePasswordPolicyConfig(PasswordPolicySaveParam param) {

        //删除密码配置
        PASSWORD_POLICY_KEYS.forEach(settingService::deleteByKey);
        //保存
        List<EmisSettingVo> list = passwordPolicyConverter
            .passwordPolicySaveParamConvertToEntity(param);
        Boolean save = settingService.save(list);
        //refresh
        ConfigRefreshUtils.refresh(ConfigBeanNameConstants.DEFAULT_PASSWORD_POLICY_MANAGER);
        return save;
    }

    /**
     * 获取系统弱密码库
     *
     * @return {@link WeakPasswordLibListResult}
     */
    @Override
    public List<WeakPasswordLibListResult> getWeakPasswordLibList() {
        List<WeakPasswordLibListResult> results = new ArrayList<>();
        List<String> list = passwordWeakLib.getWordList();
        for (String value : list) {
            results.add(new WeakPasswordLibListResult(value));
        }
        return results;
    }

    /**
     * 密码规则转换器
     */
    private final PasswordPolicyConverter passwordPolicyConverter;

    /**
     * SettingCipherTacticsRepository
     */
    private final EmisSettingService       settingService;

    /**
     * PasswordWeakLib
     */
    private final PasswordWeakLib         passwordWeakLib;

    public PasswordPolicyServiceImpl(EmisSettingService settingsRepository,
                                     PasswordPolicyConverter passwordPolicyConverter,
                                     EmisSettingService settingService,
                                     PasswordWeakLib passwordWeakLib) {
        super(settingsRepository);
        this.passwordPolicyConverter = passwordPolicyConverter;
        this.settingService = settingService;
        this.passwordWeakLib = passwordWeakLib;
    }
}
