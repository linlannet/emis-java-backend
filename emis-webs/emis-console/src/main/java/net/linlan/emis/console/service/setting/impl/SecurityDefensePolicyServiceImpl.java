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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.console.converter.setting.SecurityDefensePolicyConverter;
import net.linlan.emis.console.pojo.result.setting.EmailProviderConfigResult;
import net.linlan.emis.console.pojo.result.setting.SecurityDefensePolicyConfigResult;
import net.linlan.emis.console.pojo.save.setting.SecurityDefensePolicyParam;
import net.linlan.emis.console.service.setting.SecurityDefensePolicyService;
import net.linlan.emis.core.context.ContentSecurityPolicyUtils;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.support.event.ConfigRefreshUtils;
import net.linlan.emis.support.exception.SupportException;
import static net.linlan.emis.core.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static net.linlan.emis.core.security.constant.SecuritySettingConstants.SECURITY_DEFENSE_POLICY_KEY;

/**
 * @author Linlan
 * Created on  2023-03-09
 */
@Slf4j
@Service
public class SecurityDefensePolicyServiceImpl extends SettingServiceImpl
                                              implements SecurityDefensePolicyService {

    /**
     * 创建品牌化
     *
     * @param param {@link SecurityDefensePolicyParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSecurityDefensePolicyConfig(SecurityDefensePolicyParam param) {
        String contentSecurityPolicy = param.getContentSecurityPolicy();
        try {
            ContentSecurityPolicyUtils.parse(contentSecurityPolicy);
        } catch (ContentSecurityPolicyUtils.ContentSecurityPolicyPolicyException e) {
            ContentSecurityPolicyUtils.PolicyError error = e.getError();
            log.error("解析内容安全策略异常：{}", error.toString());
            throw new SupportException("内容安全策略格式错误");
        }
        //删除安全防御策略
        SECURITY_DEFENSE_POLICY_KEY.forEach(settingService::deleteByKey);
        //保存
        List<EmisSettingVo> list = securityDefensePolicyConverter
            .securityDefensePolicyParamToEntity(param);
        Boolean save = settingService.save(list);
        ConfigRefreshUtils.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        return save;
    }

    /**
     * 获取安全防御策略配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    @Override
    public SecurityDefensePolicyConfigResult getSecurityPolicyConfig() {
        //内容安全策略
        List<EmisSettingVo> list = settingService.getByKeys(SECURITY_DEFENSE_POLICY_KEY);
        return securityDefensePolicyConverter.entityToSecurityDefensePolicyConfigResult(list);
    }

    /**
     * SecurityPolicyConverter
     */
    private final SecurityDefensePolicyConverter securityDefensePolicyConverter;
    private final EmisSettingService              settingService;

    /**
     * BrandServiceImpl
     *
     * @param settingService {@link EmisSettingService}
     * @param securityDefensePolicyConverter  {@link SecurityDefensePolicyConverter}
     */
    public SecurityDefensePolicyServiceImpl(EmisSettingService settingService,
                                            SecurityDefensePolicyConverter securityDefensePolicyConverter) {
        super(settingService);
        this.securityDefensePolicyConverter = securityDefensePolicyConverter;
        this.settingService = settingService;
    }
}
