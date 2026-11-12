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

import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.linlan.emis.console.converter.setting.SecuritySettingConverter;
import net.linlan.emis.console.pojo.result.setting.SecurityBasicConfigResult;
import net.linlan.emis.console.pojo.result.setting.SecurityCaptchaConfigResult;
import net.linlan.emis.console.pojo.result.setting.SecurityMfaConfigResult;
import net.linlan.emis.console.pojo.save.setting.SecurityBasicSaveParam;
import net.linlan.emis.console.pojo.save.setting.SecurityCaptchaSaveParam;
import net.linlan.emis.console.pojo.save.setting.SecurityMfaSaveParam;
import net.linlan.emis.console.service.setting.SecuritySettingService;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.core.security.session.LocalSession;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import net.linlan.emis.uext.constant.MfaMode;
import net.linlan.utils.http.HttpContextUtils;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.CAPTCHA_VALIDATOR;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static net.linlan.emis.core.security.constant.SecuritySettingConstants.CAPTCHA_SETTING_NAME;
import static net.linlan.emis.core.security.constant.SecuritySettingConstants.SECURITY_BASIC_KEY;
import static net.linlan.emis.uext.constant.MfaSettingConstants.MFA_SETTING_KEYS;

/**
 * <p>
 * 安全设置表 服务实现类
 * </p>
 *
 * @author Linlan
 * Created on  2020-10-01
 */
@Service
public class SecuritySettingServiceImpl extends SettingServiceImpl
                                        implements SecuritySettingService {

    /**
     * 获取基础配置
     *
     * @return {@link SecurityBasicConfigResult}
     */
    @Override
    public SecurityBasicConfigResult getBasicConfig() {
        //查询数据库配置
        List<EmisSettingVo> list = settingService.getByKeys(SECURITY_BASIC_KEY);
        return securitySettingConverter.entityConvertToSecurityBasicConfigResult(list);
    }

    /**
     * 保存基础配置
     *
     * @param param {@link SecurityBasicSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBasicConfig(SecurityBasicSaveParam param) {

        //删除密码配置
        SECURITY_BASIC_KEY.forEach(settingService::deleteByKey);
        //保存
        List<EmisSettingVo> list = securitySettingConverter
            .securityBasicSaveParamConvertToEntity(param);
        Boolean save = settingService.save(list);
        String currentSessionId = HttpContextUtils.getSession().getId();
        //异步下线所有用户（排除当前操作用户）
        executor.execute(() -> {
            List<Object> principals = sessionRegistry.getAllPrincipals();
            principals.forEach(i -> {
                if (i instanceof LocalSession) {
                    if (!((LocalSession) i).getSessionId().equals(currentSessionId)) {
                        sessionRegistry.getSessionInformation(((LocalSession) i).getSessionId())
                            .expireNow();
                    }
                }
            });
        });
        // refresh
        ConfigRefreshUtils.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        return save;
    }

    /**
     * 获取验证码配置
     *
     * @return {@link SecurityCaptchaConfigResult}
     */
    @Override
    public SecurityCaptchaConfigResult getCaptchaProviderConfig() {
        //查询数据库配置
        EmisSettingVo entity = settingService.getByKey(CAPTCHA_SETTING_NAME);
        return securitySettingConverter.entityConvertToSecurityCaptchaConfigResult(entity);
    }

    /**
     * 保存行为验证码
     *
     * @param param {@link SecurityCaptchaSaveParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveCaptchaProviderConfig(SecurityCaptchaSaveParam param) {
        //保存
        List<EmisSettingVo> list = securitySettingConverter
            .securityCaptchaSaveParamConvertToEntity(param);
        settingService.save(list);
        //refresh
        ConfigRefreshUtils.refresh(CAPTCHA_VALIDATOR);
        return true;
    }

    /**
     * 禁用行为验证码
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableCaptchaProvider() {
        Boolean setting = removeSetting(CAPTCHA_SETTING_NAME);
        // refresh
        ConfigRefreshUtils.refresh(CAPTCHA_VALIDATOR);
        return setting;
    }

    /**
     * 获取MFA配置
     *
     * @return {@link SecurityMfaConfigResult}
     */
    @Override
    public SecurityMfaConfigResult getMfaConfig() {
        //查询数据库配置
        List<EmisSettingVo> list = settingService.getByKeys(MFA_SETTING_KEYS);
        return securitySettingConverter.entityConvertToSecurityMfaConfigResult(list);
    }

    /**
     * 保存MFA
     *
     * @param param {@link SecurityMfaSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMfaConfig(SecurityMfaSaveParam param) {

        if (param.getMode().equals(MfaMode.NONE)) {
            settingService.deleteByKeys(MFA_SETTING_KEYS);
            return true;
        }
        //保存
        List<EmisSettingVo> list = securitySettingConverter
            .securityMfaSaveParamConvertToEntity(param);
        return settingService.save(list);
    }

    /**
     * SecurityBasicDataConverter
     */
    private final SecuritySettingConverter securitySettingConverter;

    private final EmisSettingService        settingService;

    private final SessionRegistry          sessionRegistry;

    private final Executor                 executor;

    public SecuritySettingServiceImpl(EmisSettingService settingsRepository,
                                      SecuritySettingConverter securitySettingConverter,
                                      EmisSettingService settingService,
                                      SessionRegistry sessionRegistry,
                                      AsyncConfigurer asyncConfigurer) {
        super(settingsRepository);
        this.securitySettingConverter = securitySettingConverter;
        this.settingService = settingService;
        this.sessionRegistry = sessionRegistry;
        this.executor = asyncConfigurer.getAsyncExecutor();
    }
}
