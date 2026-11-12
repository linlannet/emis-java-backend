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
package net.linlan.emis.console.converter.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.Mapper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.linlan.emis.console.pojo.result.setting.SecurityBasicConfigResult;
import net.linlan.emis.console.pojo.result.setting.SecurityCaptchaConfigResult;
import net.linlan.emis.console.pojo.result.setting.SecurityMfaConfigResult;
import net.linlan.emis.console.pojo.save.setting.SecurityBasicSaveParam;
import net.linlan.emis.console.pojo.save.setting.SecurityCaptchaSaveParam;
import net.linlan.emis.console.pojo.save.setting.SecurityMfaSaveParam;
import net.linlan.emis.core.constant.CaptchaProviderType;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.core.security.captcha.CaptchaProviderConfig;
import net.linlan.emis.core.security.captcha.geetest.GeeTestCaptchaProviderConfig;
import net.linlan.emis.support.validation.ValidationUtils;
import net.linlan.emis.uext.constant.MfaFactor;
import net.linlan.emis.uext.constant.MfaMode;
import static net.linlan.emis.core.security.constant.SecuritySettingConstants.*;
import static net.linlan.emis.uext.constant.MfaSettingConstants.*;

/**
 * 安全设置数据转换器
 *
 * @author Linlan
 * Created on  2020/10/4 21:00
 */
@Mapper(componentModel = "spring")
public interface SecuritySettingConverter {

    /**
     * 实体转换为安全性高级配置结果
     *
     * @param list {@link List}
     * @return {@link SecurityBasicConfigResult}
     */
    default SecurityBasicConfigResult entityConvertToSecurityBasicConfigResult(List<EmisSettingVo> list) {
        //@formatter:off
        SecurityBasicConfigResult result = new SecurityBasicConfigResult();
        //转MAP
        Map<String, String> map = list.stream().collect(Collectors.toMap(EmisSettingVo::getName, EmisSettingVo::getContent, (key1, key2) -> key2));
        //会话有效时间
        result.setSessionValidTime(Integer.valueOf(map.containsKey(SECURITY_BASIC_SESSION_VALID_TIME) ? map.get(SECURITY_BASIC_SESSION_VALID_TIME) : SECURITY_BASIC_DEFAULT_SETTINGS.get(SECURITY_BASIC_SESSION_VALID_TIME)));
        //短信验证码有效时间
        result.setVerifyCodeValidTime(Integer.valueOf(map.containsKey(VERIFY_CODE_VALID_TIME) ? map.get(VERIFY_CODE_VALID_TIME) : SECURITY_BASIC_DEFAULT_SETTINGS.get(VERIFY_CODE_VALID_TIME)));
        //记住我有效时间（秒）
        result.setRememberMeValidTime(Integer.valueOf(map.containsKey(SECURITY_BASIC_REMEMBER_ME_VALID_TIME) ? map.get(SECURITY_BASIC_REMEMBER_ME_VALID_TIME) : SECURITY_BASIC_DEFAULT_SETTINGS.get(SECURITY_BASIC_REMEMBER_ME_VALID_TIME)));
        //用户并发数
        result.setSessionMaximum(Integer.valueOf(map.containsKey(SECURITY_SESSION_MAXIMUM) ? map.get(SECURITY_SESSION_MAXIMUM) : SECURITY_BASIC_DEFAULT_SETTINGS.get(SECURITY_SESSION_MAXIMUM)));
        //@formatter:on
        return result;
    }

    /**
     * 安全高级保存参数转换为实体
     *
     * @param param {@link SecurityBasicSaveParam}
     * @return {@link List}
     */
    default List<EmisSettingVo> securityBasicSaveParamConvertToEntity(SecurityBasicSaveParam param) {
        //@formatter:off
        List<EmisSettingVo> list = new ArrayList<>();
        //会话有效时间
        if (ObjectUtils.isNotEmpty(param.getSessionValidTime())) {
            list.add(new EmisSettingVo().setName(SECURITY_BASIC_SESSION_VALID_TIME).setContent(String.valueOf(param.getSessionValidTime())));
        }
        //记住我有效时间（秒）
        if (ObjectUtils.isNotEmpty(param.getRememberMeValidTime())) {
            list.add(new EmisSettingVo().setName(SECURITY_BASIC_REMEMBER_ME_VALID_TIME).setContent(String.valueOf(param.getRememberMeValidTime())));
        }
        //用户并发数
        if (ObjectUtils.isNotEmpty(param.getSessionMaximum())) {
            list.add(new EmisSettingVo().setName(SECURITY_SESSION_MAXIMUM).setContent(String.valueOf(param.getSessionMaximum())));
        }
        //短信验证码有效时间（秒）
        if (ObjectUtils.isNotEmpty(param.getSmsCodeValidTime())) {
            list.add(new EmisSettingVo().setName(VERIFY_CODE_VALID_TIME).setContent(String.valueOf(param.getSmsCodeValidTime())));
        }
        //@formatter:on
        return list;
    }

    /**
     * Security Mfa Save Param 转换为实体
     *
     * @param param {@link SecurityMfaSaveParam}
     * @return {@link List}
     */
    default List<EmisSettingVo> securityMfaSaveParamConvertToEntity(SecurityMfaSaveParam param) {
        List<EmisSettingVo> list = new ArrayList<>();
        list.add(new EmisSettingVo().setName(MFA_MODE).setContent(param.getMode().getKey()));
        if (!param.getFactors().isEmpty()) {
            list.add(new EmisSettingVo().setName(MFA_FACTOR).setContent(param.getFactors().stream()
                .map(MfaFactor::getKey).collect(Collectors.joining(","))));
        }
        return list;
    }

    /**
     * 实体转换为安全 Mfa 配置结果
     *
     * @param list {@link List}
     * @return {@link SecurityMfaConfigResult}
     */
    default SecurityMfaConfigResult entityConvertToSecurityMfaConfigResult(List<EmisSettingVo> list) {
        SecurityMfaConfigResult result = new SecurityMfaConfigResult();
        //转MAP
        Map<String, String> map = list.stream().collect(Collectors.toMap(EmisSettingVo::getName,
            EmisSettingVo::getContent, (key1, key2) -> key2));
        String manner = map.containsKey(MFA_FACTOR) ? map.get(MFA_FACTOR)
            : MFA_SETTING_DEFAULT_SETTINGS.get(MFA_FACTOR);
        List<MfaFactor> manners = new ArrayList<>();
        for (String s : manner.split(",")) {
            MfaFactor type = MfaFactor.getType(s);
            if (!Objects.isNull(type)) {
                manners.add(type);
            }
        }
        if (!manners.isEmpty()) {
            result.setFactors(manners);
        }
        result.setMode(MfaMode.getType(map.containsKey(MFA_MODE) ? map.get(MFA_MODE)
            : MFA_SETTING_DEFAULT_SETTINGS.get(MFA_MODE)));
        return result;
    }

    /**
     * 安全验证码保存参数转换为实体
     *
     * @param param {@link SecurityCaptchaSaveParam}
     * @return {@link List}
     */
    default List<EmisSettingVo> securityCaptchaSaveParamConvertToEntity(SecurityCaptchaSaveParam param) {
        List<EmisSettingVo> list = new ArrayList<>();
        try {
            //极速验证
            if (CaptchaProviderType.GEE_TEST.equals(param.getProvider())) {
                ObjectMapper objectMapper = new ObjectMapper();
                GeeTestCaptchaProviderConfig config = objectMapper.readValue(
                    param.getConfig().toJSONString(), GeeTestCaptchaProviderConfig.class);
                ValidationUtils.ValidationResult<GeeTestCaptchaProviderConfig> validationResult = ValidationUtils
                    .validateEntity(config);
                if (validationResult.isHasErrors()) {
                    throw new ValidationException(validationResult.getMessage());
                }
                EmisSettingVo entity = new EmisSettingVo();
                entity.setName(CAPTCHA_SETTING_NAME);
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                entity.setContent(objectMapper.writeValueAsString(config));
                entity.setDescription("极速验证码配置");
                list.add(entity);
            }
        }
        //JSON异常
        catch (JsonProcessingException e) {
            throw new ValidationException(e);
        }
        return list;
    }

    /**
     * 实体转换为安全验证码配置结果
     *
     * @param entity {@link EmisSettingVo}
     * @return {@link SecurityCaptchaConfigResult}
     */
    default SecurityCaptchaConfigResult entityConvertToSecurityCaptchaConfigResult(EmisSettingVo entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        SecurityCaptchaConfigResult result = new SecurityCaptchaConfigResult();
        ObjectMapper objectMapper = new ObjectMapper();
        // 指定序列化输入的类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        try {
            CaptchaProviderConfig config = objectMapper.readValue(entity.getContent(),
                CaptchaProviderConfig.class);
            result.setConfig(config);
            result.setEnabled(true);
            result.setProvider(config.getProvider());
            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
