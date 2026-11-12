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
package net.linlan.emis.console.configuration;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import net.linlan.emis.core.constant.CaptchaProviderType;
import net.linlan.emis.core.constant.ConfigBeanNameConstants;
import net.linlan.emis.core.security.captcha.CaptchaProviderConfig;
import net.linlan.emis.core.security.captcha.CaptchaValidator;
import net.linlan.emis.core.security.captcha.NoneCaptchaProvider;
import net.linlan.emis.core.security.captcha.geetest.GeeTestCaptchaProviderConfig;
import net.linlan.emis.core.security.captcha.geetest.GeeTestCaptchaValidator;
import net.linlan.emis.uext.SettingsContextHelper;

/**
 * 验证码验证器配置
 *
 * @author Linlan
 * Created on  2022/8/15 21:27
 */
//@Configuration
public class EmisCaptchaValidatorConfiguration {
    private final Logger logger = LoggerFactory.getLogger(EmisCaptchaValidatorConfiguration.class);

    /**
     * 验证码验证器
     *
     * @param restTemplate {@link RestTemplate}
     * @return {@link CaptchaValidator}
     */
    @RefreshScope
    @Bean(ConfigBeanNameConstants.CAPTCHA_VALIDATOR)
    public CaptchaValidator captchaValidator(RestTemplate restTemplate) {
        CaptchaProviderConfig providerConfig = SettingsContextHelper.getCaptchaProviderConfig();
        if (!Objects.isNull(providerConfig)) {
            //极速验证
            if (providerConfig.getProvider().equals(CaptchaProviderType.GEE_TEST)) {
                return new GeeTestCaptchaValidator((GeeTestCaptchaProviderConfig) providerConfig,
                    restTemplate);
            }
            logger.warn("暂未支持此 [{}] 验证器配置", providerConfig.getProvider());
            return new NoneCaptchaProvider();
        }
        logger.warn("暂无验证器配置");
        return new NoneCaptchaProvider();
    }

}
