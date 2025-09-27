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
package net.linlan.emis.manage.password.manager;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.linlan.commons.script.json.StringMap;
import net.linlan.emis.core.frame.EmisSettingVo;
import net.linlan.emis.manage.password.PasswordPolicyManager;
import net.linlan.emis.support.security.password.PasswordValidator;
import net.linlan.emis.support.security.password.enums.PasswordComplexityRule;
import net.linlan.emis.support.security.password.validator.*;
import net.linlan.emis.support.security.password.weak.PasswordWeakLib;
import net.linlan.emis.uext.SettingsContextHelper;
import net.linlan.emis.uext.constant.PasswordPolicySettingConstants;
import net.linlan.emis.user.entity.UserHistoryPassword;
import net.linlan.emis.user.entity.UserInfo;
import net.linlan.emis.user.service.UserHistoryPasswordService;
import net.linlan.emis.user.service.UserInfoService;

/**
 * 密码策略管理器
 *
 * @author Linlan
 */
public class DefaultPasswordPolicyManager implements PasswordPolicyManager<UserInfo> {

    public DefaultPasswordPolicyManager(UserInfoService userInfoService,
                                        UserHistoryPasswordService userHistoryPasswordService,
                                        PasswordWeakLib passwordWeakLib,
                                        PasswordEncoder passwordEncoder) {
        this.userHistoryPasswordService = userHistoryPasswordService;
        this.userInfoService = userInfoService;
        this.passwordWeakLib = passwordWeakLib;
        this.passwordEncoder = passwordEncoder;
        //@formatter:off
        providers = Collections.unmodifiableList(Arrays.asList(
                getPasswordIllegalSequenceValidator(),
                getPasswordContinuousSameCharValidator(),
                getPasswordLengthValidator(),
                getPasswordComplexityRuleValidator(),
                getWeakPasswordValidator()));
        //@formatter:on
    }

    /**
     * 校验密码
     *
     * @param user   {@link  UserInfo} 用户ID
     * @param password {@link  String} 密码
     */
    @Override
    public void validate(UserInfo user, String password) {
        List<PasswordValidator> validators = new ArrayList<>(providers);
        if (user != null && ObjectUtils.isNotEmpty(user.getId())) {
            //@formatter:off
            validators.add(getPasswordIncludeUserInfoValidator(user.getId()));
            validators.add(getHistoryPasswordValidator(user.getId()));
            //@formatter:on
        }
        validators.forEach(passwordValidator -> passwordValidator.validate(password));
    }

    /**
     * 密码包含用户信息验证器
     *
     * @return {@link PasswordIncludeUserInfoValidator }
     */
    private PasswordIncludeUserInfoValidator getPasswordIncludeUserInfoValidator(String userId) {
        EmisSettingVo setting = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_ACCOUNT_CHECK);
        boolean enabled = Objects.isNull(setting)
            ? Boolean.parseBoolean(PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_ACCOUNT_CHECK))
            : Boolean.parseBoolean(setting.getContent());
        if (enabled) {
            UserInfo userInfo = userInfoService.findById(userId);
            if (ObjectUtils.isNotEmpty(userInfo)) {
                return new PasswordIncludeUserInfoValidator(userInfo.getFullName(),
                    userInfo.getNickName(), userInfo.getUsername(), userInfo.getMobile(),
                    userInfo.getEmail());
            }
        }
        return new PasswordIncludeUserInfoValidator(false);
    }

    /**
     * 历史密码检查验证器
     *
     * @return {@link  HistoryPasswordValidator}
     */
    private HistoryPasswordValidator getHistoryPasswordValidator(String userId) {
        EmisSettingVo historyCipherCheck = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_HISTORY_PASSWORD_CHECK);
        boolean enabled = Objects.isNull(historyCipherCheck)
            ? Boolean.parseBoolean(PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_HISTORY_PASSWORD_CHECK))
            : Boolean.parseBoolean(historyCipherCheck.getContent());
        if (enabled) {
            EmisSettingVo historyCipherCheckCount = SettingsContextHelper.getEmisSettingService()
                .getByKey(
                    PasswordPolicySettingConstants.PASSWORD_POLICY_HISTORY_PASSWORD_CHECK_COUNT);
            Integer count = Objects.isNull(historyCipherCheckCount) ? Integer
                .valueOf(PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS.get(
                    PasswordPolicySettingConstants.PASSWORD_POLICY_HISTORY_PASSWORD_CHECK_COUNT))
                : Integer.valueOf(historyCipherCheckCount.getContent());
            List<UserHistoryPassword> entities = userHistoryPasswordService
                .getList(new StringMap().put("userId", userId).map());
            //构建历史密码验证器
            new HistoryPasswordValidator(entities.stream().map(UserHistoryPassword::getPassword)
                .collect(Collectors.toList()), passwordEncoder);
        }
        return new HistoryPasswordValidator(false);
    }

    /**
     * 获取密码长度规则验证器
     *
     * @return {@link  PasswordLengthValidator}
     */
    private PasswordLengthValidator getPasswordLengthValidator() {
        //最小长度
        EmisSettingVo leastLengthEntity = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_LEAST_LENGTH);
        Integer leastLength = Objects.isNull(leastLengthEntity)
            ? Integer.valueOf(PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_LEAST_LENGTH))
            : Integer.valueOf(leastLengthEntity.getContent());
        //最大长度
        EmisSettingVo biggestLengthEntity = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_BIGGEST_LENGTH);
        Integer biggestLength = Objects.isNull(biggestLengthEntity)
            ? Integer.valueOf(PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_BIGGEST_LENGTH))
            : Integer.valueOf(biggestLengthEntity.getContent());
        return new PasswordLengthValidator(leastLength, biggestLength);
    }

    /**
     * 获取密码复杂度规则验证器
     *
     * @return {@link  PasswordComplexityRuleValidator}
     */
    private PasswordComplexityRuleValidator getPasswordComplexityRuleValidator() {
        EmisSettingVo setting = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_COMPLEXITY);
        String complexityRule = Objects.isNull(setting)
            ? PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_COMPLEXITY)
            : setting.getContent();
        PasswordComplexityRule rule = PasswordComplexityRule.getType(complexityRule);
        return new PasswordComplexityRuleValidator(rule);
    }

    /**
     * 获取弱密码规则
     *
     * @return {@link  WeakPasswordValidator}
     */
    private WeakPasswordValidator getWeakPasswordValidator() {
        EmisSettingVo setting = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_WEAK_PASSWORD_CHECK);
        String enable = Objects.isNull(setting)
            ? PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_WEAK_PASSWORD_CHECK)
            : setting.getContent();
        if (Boolean.parseBoolean(enable)) {
            ArrayList<String> list = new ArrayList<>();
            //自定义弱密码
            EmisSettingVo customWeakCipher = SettingsContextHelper.getEmisSettingService()
                .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_CUSTOM_WEAK_PASSWORD);
            if (!Objects.isNull(customWeakCipher)) {
                list.addAll(Arrays.asList(customWeakCipher.getContent().split("\n")));
            }
            //系统弱密码
            list.addAll(passwordWeakLib.getWordList());
            //创建 WeakPasswordValidator
            return new WeakPasswordValidator(list);
        }
        return new WeakPasswordValidator(false);
    }

    /**
     * 获取密码连续相同字符验证器
     *
     * @return {@link  PasswordContinuousSameCharValidator}
     */
    private PasswordContinuousSameCharValidator getPasswordContinuousSameCharValidator() {
        EmisSettingVo setting = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_NOT_SAME_CHARS);
        String rule = Objects.isNull(setting)
            ? PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_NOT_SAME_CHARS)
            : setting.getContent();
        return new PasswordContinuousSameCharValidator(Integer.valueOf(rule));
    }

    /**
     * 获取密码非法序列异常
     *
     * @return {@link  PasswordIllegalSequenceValidator}
     */
    private PasswordIllegalSequenceValidator getPasswordIllegalSequenceValidator() {
        EmisSettingVo setting = SettingsContextHelper.getEmisSettingService()
            .getByKey(PasswordPolicySettingConstants.PASSWORD_POLICY_ILLEGAL_SEQUENCE_CHECK);
        String enable = Objects.isNull(setting)
            ? PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS
                .get(PasswordPolicySettingConstants.PASSWORD_POLICY_ILLEGAL_SEQUENCE_CHECK)
            : setting.getContent();
        return new PasswordIllegalSequenceValidator(Boolean.parseBoolean(enable));
    }

    /**
     * 提供商
     */
    private final List<PasswordValidator>    providers;
    /**
     * 用户
     */
    private final UserInfoService            userInfoService;
    /**
     * 历史密码
     */
    private final UserHistoryPasswordService userHistoryPasswordService;

    /**
     * 弱密码库
     */
    private final PasswordWeakLib            passwordWeakLib;
    /**
     * PasswordEncoder
     */
    private final PasswordEncoder            passwordEncoder;
}
