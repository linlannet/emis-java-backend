/*
 * emis-module-user - user账户模块
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
package net.linlan.emis.uext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapesecurity.salvation2.Directives.SourceExpressionDirective;
import com.shapesecurity.salvation2.FetchDirectiveKind;
import com.shapesecurity.salvation2.Policy;
import com.shapesecurity.salvation2.Values.Host;

import lombok.extern.slf4j.Slf4j;

import net.linlan.emis.core.constant.ConfigBeanNameConstants;
import net.linlan.emis.core.constant.SettingConstants;
import net.linlan.emis.core.context.ContentSecurityPolicyUtils;
import net.linlan.emis.core.encrypt.EncryptContextHelper;
import net.linlan.emis.core.encrypt.EncryptionModule;
import net.linlan.emis.core.frame.EmisSettingService;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.core.security.captcha.CaptchaProviderConfig;
import net.linlan.emis.core.security.constant.SecuritySettingConstants;
import net.linlan.emis.manage.mail.MailProviderConfig;
import net.linlan.emis.manage.sms.aliyun.AliyunSmsProviderConfig;
import net.linlan.emis.manage.sms.qiniu.QiNiuSmsProviderConfig;
import net.linlan.emis.manage.sms.tencent.TencentSmsProviderConfig;
import net.linlan.emis.support.event.ConfigRefreshListenerRunner;
import net.linlan.emis.support.exception.SupportException;
import net.linlan.emis.uext.config.SmsConfig;
import net.linlan.emis.uext.constant.MessageSettingConstants;
import net.linlan.emis.uext.constant.MfaFactor;
import net.linlan.emis.uext.constant.MfaMode;
import net.linlan.emis.uext.constant.MfaSettingConstants;
import net.linlan.emis.uext.constant.SmsProvider;
import net.linlan.utils.SpringContextUtils;

/**
 * ServerContextHelp
 *
 * @author Linlan
 * Created on  2020/10/13 20:26
 */
@Slf4j
public final class SettingsContextHelper {

    /**
     * 获取验证码提供商配置
     *
     * @return {@link SmsConfig}
     */
    public static SmsConfig getSmsProviderConfig() {
        EmisSettingVo setting = getEmisSettingService()
            .getByKey(MessageSettingConstants.MESSAGE_SMS_PROVIDER);
        if (!Objects.isNull(setting) && StringUtils.isNotBlank(setting.getContent())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                SmsConfig config = objectMapper.readValue(setting.getContent(), SmsConfig.class);
                SmsProvider provider = config.getProvider();
                //阿里
                if (SmsProvider.ALIYUN.equals(provider)) {
                    AliyunSmsProviderConfig smsConfig = (AliyunSmsProviderConfig) config
                        .getConfig();
                    smsConfig.setAccessKeySecret(
                        EncryptContextHelper.decrypt(smsConfig.getAccessKeySecret()));
                    return config;
                }
                //腾讯
                else if (SmsProvider.TENCENT.equals(provider)) {
                    TencentSmsProviderConfig smsConfig = (TencentSmsProviderConfig) config
                        .getConfig();
                    smsConfig.setSecretKey(EncryptContextHelper.decrypt(smsConfig.getSecretKey()));
                    return config;
                }
                //七牛
                else if (SmsProvider.QINIU.equals(provider)) {
                    QiNiuSmsProviderConfig smsConfig = (QiNiuSmsProviderConfig) config.getConfig();
                    smsConfig.setSecretKey(EncryptContextHelper.decrypt(smsConfig.getSecretKey()));
                    return config;
                }
                throw new SupportException("暂未支持此短信 [" + provider + "] 提供商配置获取");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new SmsConfig();
    }

    /**
     * 获取邮箱提供商配置
     *
     * @return {@link MailProviderConfig}
     */
    public static MailProviderConfig getMailProviderConfig() {
        try {
            EmisSettingVo setting = getEmisSettingService()
                .getByKey(MessageSettingConstants.MESSAGE_PROVIDER_EMAIL);
            if (!Objects.isNull(setting) && StringUtils.isNotBlank(setting.getContent())) {
                String value = setting.getContent();
                ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                // 根据提供商序列化
                return objectMapper.readValue(value, MailProviderConfig.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }
        return null;
    }

    /**
     * 获取登录失败持续时间
     *
     * @return {@link Integer}
     */
    public static Integer getLoginFailureDuration() {
        EmisSettingVo setting = getEmisSettingService()
            .getByKey(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION));
        }
        return Integer.valueOf(setting.getContent());
    }

    /**
     * 获取连续登录失败次数
     *
     * @return {@link Integer}
     */
    public static Integer getLoginFailureCount() {
        EmisSettingVo setting = getEmisSettingService()
            .getByKey(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_FAILURE_COUNT);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_FAILURE_COUNT));
        }
        return Integer.valueOf(setting.getContent());
    }

    /**
     * 获取验证码有效时间
     *
     * @return {@link Integer} 秒
     */
    public static Integer getCodeValidTime() {
        EmisSettingVo setting = getEmisSettingService()
            .getByKey(SecuritySettingConstants.VERIFY_CODE_VALID_TIME);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SecuritySettingConstants.SECURITY_BASIC_DEFAULT_SETTINGS
                .get(SecuritySettingConstants.VERIFY_CODE_VALID_TIME));
        }
        return Integer.valueOf(setting.getContent());
    }

    /**
     * 获取自动解锁时间
     *
     * @return {@link Integer} 秒
     */
    public static Integer getAutoUnlockTime() {
        EmisSettingVo setting = getEmisSettingService()
            .getByKey(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME));
        }
        return Integer.valueOf(setting.getContent());
    }

    /**
     * 添加 img-src host 内容安全策略
     *
     * @param value {@link String}
     */
    public static void addImgSrcHostContentSecurityPolicy(String value) {
        EmisSettingVo settingEntity = getEmisSettingService()
            .getByKey(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);
        String contentSecurityPolicy;
        if (Objects.isNull(settingEntity)) {
            settingEntity = new EmisSettingVo();
            settingEntity
                .setName(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);
            contentSecurityPolicy = SecuritySettingConstants.SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);
        } else {
            contentSecurityPolicy = settingEntity.getContent();
        }
        Policy parse = ContentSecurityPolicyUtils.parse(contentSecurityPolicy);
        SourceExpressionDirective directive = parse.getFetchDirective(FetchDirectiveKind.ImgSrc)
            .orElseThrow(() -> new RuntimeException());
        Host host = Host.parseHost(value).orElseThrow(() -> new RuntimeException());
        directive.removeHost(host);
        directive.addHost(host, (severity, message) -> {
            log.error("添加操作内容安全策略 img-src host 异常：{}", message);
            throw new SupportException(message);
        });
        settingEntity.setContent(parse.toString());
        getEmisSettingService().saveSetting(settingEntity);
        refresh(ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN);
    }

    /**
     * refresh
     * @param beanName {@link  String}
     * 利用redis订阅分发，完成集群参数刷新加载
     */
    public static void refresh(String beanName) {
        RedissonClient redisson = SpringContextUtils.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(ConfigRefreshListenerRunner.CONFIG_REFRESH);
        topic.publish(beanName);
    }

    public static EmisSettingService getEmisSettingService() {
        return SpringContextUtils.getBean(EmisSettingService.class);
    }

    /**
     * 已启用MFA认证
     *
     * @return  {@link Boolean}
     */
    public static Boolean isMfaEnabled() {
        EmisSettingVo setting = getEmisSettingService().getByKey(MfaSettingConstants.MFA_MODE);
        if (Objects.isNull(setting)) {
            return !MfaSettingConstants.MFA_SETTING_DEFAULT_SETTINGS
                .get(MfaSettingConstants.MFA_MODE).equals(MfaMode.NONE.getKey());
        }
        return !MfaMode.NONE.getKey().equals(setting.getContent());
    }

    /**
     * 获取 mfa Factors
     *
     * @return  {@link Boolean}
     */
    public static List<MfaFactor> getMfaFactors() {
        List<MfaFactor> factors = new ArrayList<>();
        EmisSettingVo setting = getEmisSettingService().getByKey(MfaSettingConstants.MFA_FACTOR);
        if (Objects.isNull(setting)) {
            return factors;
        }
        List<String> strings = Arrays.stream(setting.getContent().split(","))
            .collect(Collectors.toList());
        for (String id : strings) {
            MfaFactor type = MfaFactor.getType(id);
            if (!Objects.isNull(type)) {
                factors.add(type);
            }
        }
        return factors;
    }

    /**
     * 获取验证码提供商配置
     *
     * @return  {@link Boolean}
     */
    public static CaptchaProviderConfig getCaptchaProviderConfig() {
        EmisSettingVo entity = getEmisSettingService()
            .getByKey(SecuritySettingConstants.CAPTCHA_SETTING_NAME);
        if (!Objects.isNull(entity) && !SettingConstants.NOT_CONFIG.equals(entity.getContent())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                return objectMapper.readValue(entity.getContent(), CaptchaProviderConfig.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
